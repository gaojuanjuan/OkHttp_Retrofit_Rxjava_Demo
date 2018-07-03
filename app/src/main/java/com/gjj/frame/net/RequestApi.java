package com.gjj.frame.net;

import com.gjj.frame.bean.TranslationBean;
import com.gjj.frame.bean.TranslationBean1;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface RequestApi {

    @GET("ajax.php?a=fy&f=auto&t=auto&w=hi%20world")
    Observable<TranslationBean> getCall();



    // 注册请求
    @GET("ajax.php?a=fy&f=auto&t=auto&w=hi%20register")
    Observable<TranslationBean> register();

    // 登陆请求
    @GET("ajax.php?a=fy&f=auto&t=auto&w=hi%20login")
    Observable<TranslationBean1> login();

}
