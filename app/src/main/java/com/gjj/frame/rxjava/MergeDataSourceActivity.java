package com.gjj.frame.rxjava;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;

import com.gjj.frame.Constant;
import com.gjj.frame.Logger;
import com.gjj.frame.R;
import com.gjj.frame.bean.TranslationBean;
import com.gjj.frame.bean.TranslationBean1;
import com.gjj.frame.net.RequestApi;
import com.gjj.frame.utils.UrlManager;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MergeDataSourceActivity extends AppCompatActivity {

    @BindView(R.id.webview)
    WebView webview;
    private String result = "数据源来自 = ";
    private Observable<TranslationBean> observable1;
    private Observable<TranslationBean1> observable2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merge_data_source);
        ButterKnife.bind(this);
        webview.loadUrl("https://www.jianshu.com/p/fc2e551b907c");

        //使用merge操作符
//        useMerge();

        useZip();
    }

    private void useZip() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UrlManager.BASE_URL_JS)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        RequestApi request = retrofit.create(RequestApi.class);

        observable1 = request.getCall().subscribeOn(Schedulers.io());
        observable2 = request.getCall_2().subscribeOn(Schedulers.io());

        Observable.zip(observable1, observable2,
                new BiFunction<TranslationBean, TranslationBean1, String>() {
            @Override
            public String apply(TranslationBean translationBean, TranslationBean1 translationBean1) throws Exception {
                return translationBean.getContent().getOut() + " & " + translationBean1.getContent().getOut();
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.d(Constant.TAG,"最终接收到的数据是："+s);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Logger.d("获取失败");
                    }
                });
    }

    private void useMerge() {
        Observable<String> network = Observable.just("网络");

        Observable<String> file = Observable.just("本地文件");

        Observable.merge(network, file)
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String value) {
                        Log.d(Constant.TAG, "数据源有：" + value);
                        result += value + "+";
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        Log.d(Constant.TAG, "获取数据完成");
                        Log.d(Constant.TAG, result);
                    }
                });
    }
}
