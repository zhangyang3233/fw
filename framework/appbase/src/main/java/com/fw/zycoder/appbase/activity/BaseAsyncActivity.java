package com.fw.zycoder.appbase.activity;

import android.os.Bundle;

import com.fw.zycoder.appbase.widget.AppLoadingView;

/**
 *
 * @author zhangyang
 */
public abstract class BaseAsyncActivity extends BaseTitleActivity {
    private AppLoadingView appLoadingView;

    @Override
    protected void onCreate(Bundle onSaveInstanceState) {
        super.onCreate(onSaveInstanceState);
        initAppLoadingView();
    }

    private void initAppLoadingView() {
        appLoadingView = new AppLoadingView(this);
    }


    public void setCancelable(boolean cancelable) {
        appLoadingView.setCancelable(cancelable);
    }


    public void showLoadingView() {
        appLoadingView.show();
    }

    public void showLoadingView(String content) {
        appLoadingView.setLoadingText(content);
    }

    public void dissmissLoadingView() {
        appLoadingView.dismiss();
    }

}

