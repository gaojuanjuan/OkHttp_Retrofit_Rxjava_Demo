package com.gjj.frame;

import com.gjj.frame.bean.JokesBean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by gaojuanjuan on 2018/2/28.
 */

public interface JokesRequest_interface {

    @Headers("User-Agent:OkHttp Example")
    @GET("content/text.php")
    Call<JokesBean> getJokes(@Query("key") String key,@Query("page") String page,@Query("pagesize") String pageSize);

}
