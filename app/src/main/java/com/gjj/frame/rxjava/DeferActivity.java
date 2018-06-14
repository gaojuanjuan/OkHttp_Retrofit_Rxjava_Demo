package com.gjj.frame.rxjava;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.gjj.frame.R;

import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class DeferActivity extends AppCompatActivity {

    private static final String TAG = DeferActivity.class.getSimpleName();
    //第一次对i的赋值
    private int i= 10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_defer);

        //通过defer定义被观察者对象，此时观察者对象孩没创建
        Observable<Integer> observable = Observable.defer(new Callable<ObservableSource<? extends Integer>>() {
            @Override
            public ObservableSource<? extends Integer> call() throws Exception {
                return Observable.just(i);
            }
        });

        //第二次对i的赋值
        i =15;

        //观察者开始订阅，此时才会调用defer()创建被观察者对象（Observable）
        observable.subscribe(new Observer<Integer>() {

            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG,"开始采用subscribe连接");
            }

            @Override
            public void onNext(Integer value) {
                Log.d(TAG,"接收到的整数是"+value);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG,"对error事件做出响应");
            }

            @Override
            public void onComplete() {
                Log.d(TAG,"对Complete事件做出响应");
            }
        });

    }
}
