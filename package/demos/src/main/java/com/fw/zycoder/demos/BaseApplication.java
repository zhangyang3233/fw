package com.fw.zycoder.demos;

import android.app.Application;

import com.fw.zycoder.errorpage.CustomActivityOnCrash;
import com.fw.zycoder.utils.GlobalConfig;
import com.fw.zycoder.utils.Log;


/**
 * Created by zhangyang131 on 16/6/12.
 */
public class BaseApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        initErrorPage();
        initGlobalConfig();
        initLog();
    }

    private void initLog() {
        Log.setIsPrintLog(BuildConfig.IS_SHOW_LOG);
    }

    private void initGlobalConfig() {
        GlobalConfig.setAppContext(this);
        GlobalConfig.setDebug(BuildConfig.DEBUG);
    }

    private void initErrorPage() {
        CustomActivityOnCrash.install(this);
        // 程序在后台崩溃是否显示错误页面
        CustomActivityOnCrash.setLaunchErrorActivityWhenInBackground(false);
        // 是否显示错误详细信息
        CustomActivityOnCrash.setShowErrorDetails(BuildConfig.DEBUG);
    }
}
