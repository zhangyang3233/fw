package com.fw.zycoder.demos;

import android.os.Bundle;
import android.view.View;

import com.fw.zycoder.appbase.fragment.BaseFragment;

/**
 * Created by zhangyang131 on 16/7/22.
 */
public class SplashFragment extends BaseFragment{

    @Override
    protected void onInflated(View contentView, Bundle savedInstanceState) {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_splash_layout;
    }
}
