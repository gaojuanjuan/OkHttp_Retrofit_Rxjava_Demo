package com.gjj.frame.rxjava;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.gjj.frame.Constant;
import com.gjj.frame.R;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class TimerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Observable.timer(4, TimeUnit.SECONDS)
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(Constant.TAG,"开始采用subscribe连接");
                    }

                    @Override
                    public void onNext(Long value) {
                        Log.d(Constant.TAG,"接收到的整数是"+value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(Constant.TAG,"对error事件做出响应");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(Constant.TAG,"对Complete事件做出响应");
                    }
                });
    }
}
