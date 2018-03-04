package com.gjj.frame.rxjava;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.gjj.frame.Logger;
import com.gjj.frame.R;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class RxJavaActivity extends AppCompatActivity {

    private Disposable mDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_java);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.rxjava_btn1, R.id.rxjava_btn2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rxjava_btn1:
//                useMethod1();
                useMethod2();
                break;
            case R.id.rxjava_btn2:
                break;
        }
    }

    /**
     * RxJava的使用方式1：分步骤实现
     */
    private void useMethod1() {
        //1. 创建被观察者对象
        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            // create() 是 RxJava 最基本的创造事件序列的方法
            // 此处传入了一个 OnSubscribe 对象参数
            // 当 Observable 被订阅时，OnSubscribe 的 call() 方法会自动被调用，即事件序列就会依照设定依次被触发
            // 即观察者会依次调用对应事件的复写方法从而响应事件
            // 从而实现被观察者调用了观察者的回调方法 & 由被观察者向观察者的事件传递，即观察者模式

            // 2. 在复写的subscribe（）里定义需要发送的事件
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                e.onNext(2);
                e.onNext(3);
                e.onComplete();
            }
        });

        //创建观察者
        //方式1：采用Observer接口
        Observer<Integer> observer = new Observer<Integer>() {

            //观察者接收事件前，默认最先调用复写onSubscribe()
            @Override
            public void onSubscribe(Disposable d) {
                Logger.e("开始采用subscribe连接");
                mDisposable = d;
            }

            //当被观察者产生Next事件&观察者接受到时，会调用该复写方法进行相应
            @Override
            public void onNext(Integer value) {
                Logger.e("对Next事件作出响应" + value);
                if (value == 2) {
                    // 设置在接收到第二个事件后切断观察者和被观察者的连接
                    mDisposable.dispose();
                    Logger.e("已经切断了连接：" + mDisposable.isDisposed());
                }
            }

            //当被观察者产生Error事件&观察者接受到时，会调用该复写方法进行相应
            @Override
            public void onError(Throwable e) {
                Logger.e("对Error事件作出响应");
            }

            //当被观察者产生Complete事件&观察者接受到时，会调用该复写方法进行相应
            @Override
            public void onComplete() {
                Logger.e("对Complete事件作出响应");
            }
        };

        observable.subscribe(observer);
    }
    /**
     * RxJava的使用方式2:基于事件流的链式调用
     */
    private void useMethod2(){
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                e.onNext(2);
                e.onNext(3);
                e.onComplete();
            }
        }).subscribe(new Observer<Integer>() {

            //观察者接收事件前，默认最先调用复写onSubscribe()
            @Override
            public void onSubscribe(Disposable d) {
                Logger.e("开始采用subscribe连接");
            }

            //当被观察者产生Next事件&观察者接受到时，会调用该复写方法进行相应
            @Override
            public void onNext(Integer value) {
                Logger.e("对Next事件作出响应" + value);
            }

            //当被观察者产生Error事件&观察者接受到时，会调用该复写方法进行相应
            @Override
            public void onError(Throwable e) {
                Logger.e("对Error事件作出响应");
            }

            //当被观察者产生Complete事件&观察者接受到时，会调用该复写方法进行相应
            @Override
            public void onComplete() {
                Logger.e("对Complete事件作出响应");
            }
        });
    }

}
