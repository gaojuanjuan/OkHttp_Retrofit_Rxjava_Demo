package com.gjj.frame.rxjava;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;

import com.gjj.frame.Constant;
import com.gjj.frame.R;
import com.gjj.frame.bean.TranslationBean;
import com.gjj.frame.net.RequestApi;
import com.gjj.frame.utils.UrlManager;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.IOException;
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

public class RequestRetryActivity extends AppCompatActivity {

    @BindView(R.id.webview)
    WebView webview;
    //可重试次数
    private int maxConnectCount = 10;
    //当前已重试次数
    private int currentRetryCount = 0;
    //重试等待时间
    private int waitRetryTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_retry);
        ButterKnife.bind(this);
        webview.loadUrl("https://www.jianshu.com/p/508c30aef0c1");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UrlManager.BASE_URL_JS)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestApi request = retrofit.create(RequestApi.class);
        Observable<TranslationBean> observable = request.getCall();
        observable.retryWhen(new Function<Observable<Throwable>, ObservableSource<?>>() {
            @Override
            public ObservableSource<?> apply(Observable<Throwable> throwableObservable) throws Exception {
                return throwableObservable.flatMap(new Function<Throwable, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(Throwable throwable) throws Exception {
                        Log.d(Constant.TAG, "发生异常 = " + throwable.toString());
                        if (throwable instanceof IOException) {
                            Log.d(Constant.TAG, "属于IO异常，需重试");
                            if (currentRetryCount < maxConnectCount) {
                                //记录重试次数
                                currentRetryCount++;
                                Log.d(Constant.TAG, "重试次数 = " + currentRetryCount);
                                /**
                                 * 需求2：实现重试
                                 * 通过返回的Observable发送的事件 = Next事件，从而使得retryWhen重新订阅，最终实现重试功能
                                 *
                                 * 需求3：延迟一段事件再重试
                                 * 采用delay操作符 = 延迟一段时间发送，以实现重试间隔设置
                                 *
                                 * 需求4：遇到的异常越多，时间越长
                                 * 在delay操作符的等待时间内设置 = 每重试一次，增多延迟重试时间1s
                                 */
                                //设置等待时间
                                waitRetryTime = 1000 + currentRetryCount * 1000;
                                Log.d(Constant.TAG, "等待时间 = " + waitRetryTime);
                                return Observable.just(1).delay(waitRetryTime, TimeUnit.MILLISECONDS);
                            } else {
                                //若重试次数 > 设置重试次数，则不重试
                                //通过发送error来停止重试（可在观察者的onError中获取信息）
                                return Observable.error(new Throwable("重试次数已超过设置次数 = " + currentRetryCount));
                            }
                            //若发生的异常不属于I/O异常，则不重试
                            //通过返回的Observable发送的事件 = error事件 实现（可再观察者的onError()中获取信息）
                        } else {
                            return Observable.error(new Throwable("发生了非网络异常（非I/O异常）"));
                        }
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
                    public void onNext(TranslationBean translationBean) {
                        Log.d(Constant.TAG, "接收成功");
                        Log.d(Constant.TAG, translationBean.getContent().getOut());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(Constant.TAG, e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
