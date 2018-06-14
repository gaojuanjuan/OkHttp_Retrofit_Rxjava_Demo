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

public class IntervalRangeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interval_range);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //参数说明：
        //参数1：事件序列起始点；
        //参数2：事件数量
        //参数3：第1次事件延迟发送时间
        //参数4：时间间隔
        //参数5：参数单位
        Observable.intervalRange(3,10,2,1, TimeUnit.SECONDS)
                //从3开始一共发送10个数据
                //第一次延迟2s发送，之后每个1s产生一个数字（从0开始递增1）
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(Constant.TAG,"开始采用subscribe连接");
                    }

                    @Override
                    public void onNext(Long value) {
                        Log.d(Constant.TAG,"接收到了事件"+value);
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
