package com.gjj.frame.rxjava;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.gjj.frame.Constant;
import com.gjj.frame.R;
import com.jakewharton.rxbinding2.widget.RxTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function3;

public class JointJudgmentActivity extends AppCompatActivity {

    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.age)
    EditText age;
    @BindView(R.id.job)
    EditText job;
    @BindView(R.id.submit)
    Button submit;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joint_judgment);
        ButterKnife.bind(this);

        final Observable<CharSequence> nameObservable = RxTextView.textChanges(name).skip(1);
        Observable<CharSequence> ageObservable = RxTextView.textChanges(age).skip(1);
        Observable<CharSequence> jobObservable = RxTextView.textChanges(job).skip(1);

        Observable.combineLatest(nameObservable, ageObservable, jobObservable, new Function3<CharSequence, CharSequence, CharSequence, Boolean>() {
            @Override
            public Boolean apply(CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3) throws Exception {
                boolean isUserNameValid = !TextUtils.isEmpty(name.getText());
                boolean isUserAgeValid = !TextUtils.isEmpty(age.getText());
                boolean isUserJobValid = !TextUtils.isEmpty(job.getText());
                return isUserNameValid && isUserAgeValid && isUserJobValid;
            }
        }).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean b) throws Exception {
                Log.d(Constant.TAG, "提交按钮是否可点击:" + b);
                submit.setEnabled(b);
            }
        });

    }

    @OnClick(R.id.submit)
    public void onViewClicked() {
    }
}
