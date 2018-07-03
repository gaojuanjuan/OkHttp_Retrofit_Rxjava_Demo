package com.gjj.frame.rxjava;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;

import com.gjj.frame.Constant;
import com.gjj.frame.R;
import com.gjj.frame.bean.TranslationBean;
import com.gjj.frame.bean.TranslationBean1;
import com.gjj.frame.net.RequestApi;
import com.gjj.frame.utils.UrlManager;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NestedRequestActivity extends AppCompatActivity {

    @BindView(R.id.webview)
    WebView webview;
    private Observable<TranslationBean> observable1;
    private Observable<TranslationBean1> observable2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nested_request);
        ButterKnife.bind(this);
        webview.loadUrl("https://www.jianshu.com/p/5f5d61f04f96");


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UrlManager.BASE_URL_JS)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestApi request = retrofit.create(RequestApi.class);
        observable1 = request.register();
        observable2 = request.login();

        observable1
                .subscribeOn(Schedulers.io())               //初始被观察者切换到IO线程进行注册请求
                .observeOn(AndroidSchedulers.mainThread())  //新被观察者切换到主线程处理注册请求的结果
                .doOnNext(new Consumer<TranslationBean>() {
                    @Override
                    public void accept(TranslationBean translationBean) throws Exception {
                        Log.d(Constant.TAG,"第1次网络请求成功");
                        Log.d(Constant.TAG,translationBean.getContent().getOut());
                    }
                })
                .observeOn(Schedulers.io())                 //登陆请求切换到IO线程去发起
                .flatMap(new Function<TranslationBean, ObservableSource<TranslationBean1>>() {//作变换，即作嵌套网络请求
                    @Override
                    public ObservableSource<TranslationBean1> apply(TranslationBean translationBean) throws Exception {
                        //将注册请求转成登陆请求，即发送登陆请求
                        return observable2;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())  //切换到主线程中对登陆请求的结果做处理
                .subscribe(new Consumer<TranslationBean1>() {
                    @Override
                    public void accept(TranslationBean1 translationBean1) throws Exception {

                        Log.d(Constant.TAG,"第2次网络请求成功");
                        Log.d(Constant.TAG,translationBean1.getContent().getOut());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(Constant.TAG,"登陆失败");
                    }
                });


    }
}
