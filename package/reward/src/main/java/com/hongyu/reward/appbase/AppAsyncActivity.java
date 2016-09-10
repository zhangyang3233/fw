package com.hongyu.reward.appbase;

import android.os.Bundle;

import com.hongyu.reward.widget.AppLoadingView;


/**
 *
 * @author zhangyang
 */
public abstract class AppAsyncActivity extends AppTitleActivity {
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

