package com.gjj.frame.rxjava;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.gjj.frame.Constant;
import com.gjj.frame.R;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class RangeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_range);

    }

    @Override
    protected void onResume() {
        super.onResume();
        //参数说明：
        //参数1：事件序列起始点
        //参数2：事件数量
        Observable.range(3,5)
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(Constant.TAG,"开始采用subscribe连接");
                    }

                    @Override
                    public void onNext(Integer value) {
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
