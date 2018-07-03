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

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UnConditionalPollingActivity extends AppCompatActivity {
    @BindView(R.id.webview)
    WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conditional_polling);
        ButterKnife.bind(this);
        setTitle("（无条件）网络请求轮询");
        webview.loadUrl("https://www.jianshu.com/p/11b3ec672812");
        Observable.interval(2, 1, TimeUnit.SECONDS)
                .doOnNext(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Log.d(Constant.TAG, "第 " + aLong + " 次轮询");
                        Retrofit retrofit = new Retrofit.Builder().baseUrl(UrlManager.BASE_URL_JS)
                                .addConverterFactory(GsonConverterFactory.create())
                                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                                .build();
                        RequestApi requestApi = retrofit.create(RequestApi.class);
                        Observable<TranslationBean> observable = requestApi.getCall();
                        observable.subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Observer<TranslationBean>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {

                                    }

                                    @Override
                                    public void onNext(TranslationBean value) {
                                        Log.d(Constant.TAG, value.getContent().getOut());
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Log.e(Constant.TAG, "请求失败");
                                    }

                                    @Override
                                    public void onComplete() {

                                    }
                                });
                    }
                }).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {

            }
        });
    }
}
