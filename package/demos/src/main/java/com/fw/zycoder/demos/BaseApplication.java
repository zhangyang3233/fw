package com.fw.zycoder.demos;

import android.app.Application;

import com.fw.zycoder.errorpage.CustomActivityOnCrash;


/**
 * Created by zhangyang131 on 16/6/12.
 */
public class BaseApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        initErrorPage();
    }

    private void initErrorPage() {
        CustomActivityOnCrash.install(this);
        // 程序在后台崩溃是否显示错误页面
        CustomActivityOnCrash.setLaunchErrorActivityWhenInBackground(false);
        // 是否显示错误详细信息
        CustomActivityOnCrash.setShowErrorDetails(BuildConfig.DEBUG);
    }
}
