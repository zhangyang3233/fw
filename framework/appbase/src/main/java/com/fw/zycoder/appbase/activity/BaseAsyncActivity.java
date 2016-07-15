package com.fw.zycoder.appbase.activity;

import android.os.Bundle;

import com.fw.zycoder.appbase.view.AppLoadingView;

/**
 *
 * @author zhangyang
 */
public abstract class BaseAsyncActivity extends BaseTitleActivity {

    private AppLoadingView mLoadingView;

    @Override
    protected void onCreate(Bundle onSaveInstanceState) {
        super.onCreate(onSaveInstanceState);
        mLoadingView =
                AppLoadingView.newInstanceOnDecorView((android.view.ViewGroup) getWindow()
                        .getDecorView());
        mLoadingView.setCancelable(true);
    }


    public void setCancelable(boolean cancelable) {
        if (mLoadingView != null) {
            mLoadingView.setCancelable(cancelable);
        }
    }


    public void showLoadingView() {
        if (mLoadingView != null && !mLoadingView.isShown()) {
            mLoadingView.show();
        }
    }

    public void showLoadingView(String content) {
        if (mLoadingView != null && !mLoadingView.isShown()) {
            mLoadingView.show(content);
        }
    }

    public void dissmissLoadingView() {
        if (mLoadingView != null && mLoadingView.isShown()) {
            mLoadingView.dismiss();
        }
    }

}

