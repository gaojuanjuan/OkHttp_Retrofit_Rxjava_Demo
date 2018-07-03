package com.gjj.frame.rxjava;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;

import com.gjj.frame.Constant;
import com.gjj.frame.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;

public class DisksAndMemoryActivity extends AppCompatActivity {

    @BindView(R.id.webview)
    WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disks_and_memory);
        ButterKnife.bind(this);
        webview.loadUrl("https://www.jianshu.com/p/6f3b6b934787");
        final String memoryCache = null;
        final String diskCache = "从磁盘缓存中获取数据";

        //检查内存缓存是否有该数据的缓存
        Observable<String> memory = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                if (memoryCache != null) {
                    e.onNext(memoryCache);
                } else {
                    e.onComplete();
                }
            }
        });

        //检查磁盘缓存是否有该数据的缓存
        Observable<String> disk = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                if (diskCache != null) {
                    e.onNext(diskCache);
                } else {
                    e.onComplete();
                }
            }
        });

        //从网络获取缓存
        Observable<String> network = Observable.just("从网络中获取数据");

        //通concat（）合并memory、disk、network 3个被观察者的事件
        //按顺序串连成队列
        Observable.concat(memory, disk, network)
                //通过firstElement()，从串联队列中去除并发送第一个有效事件(Next事件），即依次判断检查memory、disk、network
                .firstElement()
                //firstElement（）取出第1个事件 = memory,即先判断内存缓存中有无数据缓存；由于memoryCache = null,所以发送结束事件（视为无效事件）
                //firstElement()继续取出第2个事件 = disk，即先判断磁盘缓存中有无数据缓存，由于diskCache != null,所以发送next事件（视为有效事件）
                //firstElement()已发出第一个有效事件（disk事件），所以停止判断。
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.d(Constant.TAG, "最终获取的数据来源 = " + s);
                    }
                });

    }
}
