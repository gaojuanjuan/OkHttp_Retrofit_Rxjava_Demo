package com.gjj.frame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.gjj.frame.bean.JokesBean;
import com.gjj.frame.rxjava.ConditionalPollingActivity;
import com.gjj.frame.rxjava.MergeDataSourceActivity;
import com.gjj.frame.rxjava.NestedRequestActivity;
import com.gjj.frame.rxjava.UnConditionalPollingActivity;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.logger_btn)
    Button mLoggerBtn;
    private Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    private void useRetrofit() {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new LoggingInterceptor())
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Urls.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        JokesRequest_interface request = retrofit.create(JokesRequest_interface.class);
        Call<JokesBean> call = request.getJokes(Constant.DOUBIAN_KEY, "1", "1");
        call.enqueue(new Callback<JokesBean>() {
            @Override
            public void onResponse(Call<JokesBean> call, retrofit2.Response<JokesBean> response) {
                Toast.makeText(MainActivity.this, "请求成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<JokesBean> call, Throwable t) {
            }
        });
    }

    private void useOkHttp() {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new LoggingInterceptor())
                .build();
        Request request = new Request.Builder().url(Urls.requestJokesUrl)
                .header("User-Agent", "OkHttp Example")
                .build();
        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {

            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                runOnUiThread(new
                                      Runnable() {
                                          @Override
                                          public void run() {
                                              Toast.makeText(MainActivity.this, "请求成功", Toast.LENGTH_SHORT).show();
                                          }
                                      });
            }
        });
    }

    @OnClick({R.id.rxjava_btn,R.id.logger_btn,R.id.btn_conditional_poll,
            R.id.btn_nested_request,R.id.btn_merge_data_source})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rxjava_btn:
                mIntent = new Intent(MainActivity.this, UnConditionalPollingActivity.class);
                break;
            case R.id.btn_conditional_poll:
                mIntent = new Intent(MainActivity.this, ConditionalPollingActivity.class);
                break;
            case R.id.btn_nested_request:
                mIntent = new Intent(MainActivity.this, NestedRequestActivity.class);
                break;
            case R.id.btn_merge_data_source:
                mIntent = new Intent(MainActivity.this, MergeDataSourceActivity.class);
                break;
            case R.id.logger_btn:
                //测试okhttp的日志拦截器
                //OkHttp的方式
                useOkHttp();
//                useRetrofit();
                break;
        }
        if (mIntent != null){
            startActivity(mIntent);
        }

    }
}
