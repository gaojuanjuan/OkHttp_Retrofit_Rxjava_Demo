package com.gjj.frame.rxjava;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;

import com.gjj.frame.Constant;
import com.gjj.frame.R;
import com.gjj.frame.bean.TranslationBean;
import com.gjj.frame.net.RequestApi;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConditionalPollingActivity extends AppCompatActivity {

    @BindView(R.id.webview)
    WebView webview;
    private int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conditional_polling);
        ButterKnife.bind(this);
        setTitle("（有条件）网络请求轮询");
        webview.loadUrl("https://www.jianshu.com/p/dbeaaa4afad5");
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://fy.iciba.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        RequestApi requestApi = retrofit.create(RequestApi.class);
        Observable<TranslationBean> observable = requestApi.getCall();
        observable.repeatWhen(new Function<Observable<Object>, ObservableSource<?>>() {
            //在Function函数中，必须对输入的Observable<Objuect>进行处理，此处使用flatMap操作符接收上游的数据
            //此处决定是否重新订阅 & 发送原来的Observable,即轮询
            //此处有2种情况：
            //1.若返回1个Complete()/Error()事件，则不重新订阅&发送原来的Observable,即轮询结束
            //2.若返回其余事件，则重新订阅 & 发送原来的Observable,则继续轮询
            @Override
            public ObservableSource<?> apply(Observable<Object> objectObservable) throws Exception {
                return objectObservable.flatMap(new Function<Object, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(Object o) throws Exception {
                        //如果轮询次数等于5之后，则停止轮询
                        if (i > 3)
                            return Observable.error(new Throwable("轮询结束"));
                        //如果轮询数据小于5，则发送next事件以继续轮询
                        return Observable.just(1).delay(2, TimeUnit.SECONDS);
                    }
                });
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TranslationBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(TranslationBean value) {
                        //接收服务器返回的数据
                        Log.d(Constant.TAG, value.getContent().getOut());
                        i++;

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(Constant.TAG, e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
