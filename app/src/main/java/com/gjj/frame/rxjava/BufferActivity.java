package com.gjj.frame.rxjava;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.gjj.frame.Constant;
import com.gjj.frame.R;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class BufferActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buffer);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Observable.just(1,2,3,4,5)
                .buffer(3,1)
                .subscribe(new Observer<List<Integer>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Integer> value) {
                        Log.d(Constant.TAG,"缓存区里的事件数量 = "+value.size());
                        for (Integer i : value) {
                            Log.d(Constant.TAG,"事件 = "+value);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(Constant.TAG,"对Error事件作出响应");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(Constant.TAG,"对Complete事件作出响应");
                    }
                });
    }
}
