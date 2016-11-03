package com.fw.zycoder.demos.demos.rxandroid;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fw.zycoder.appbase.fragment.BaseFragment;
import com.fw.zycoder.demos.R;
import com.fw.zycoder.utils.Log;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by zhangyang131 on 2016/11/3.
 */

public class RxAndroidFragment extends BaseFragment implements View.OnClickListener {
    TextView tv;
    Button btn;

    @Override
    protected void onInflated(View contentView, Bundle savedInstanceState) {
        tv = (TextView) mContentView.findViewById(R.id.v1);
        btn = (Button) mContentView.findViewById(R.id.v2);
        btn.setOnClickListener(this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_rxandroid;
    }

    // 创建字符串
    private String[] getStringArr() {
        return new String[]{"A", "B", "C", "D", "E", "F", "G"};
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.v2:
                start();
                break;
        }
    }

    private void start() {
        Observable.from(getStringArr()).map(new Func1<String, String>() {
            @Override
            public String call(String s) {
                return "字母是：" + s;
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.i(s);
                    }
                });
    }

}
