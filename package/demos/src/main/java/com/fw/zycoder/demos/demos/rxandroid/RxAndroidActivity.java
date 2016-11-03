package com.fw.zycoder.demos.demos.rxandroid;

import android.support.v4.app.Fragment;

import com.fw.zycoder.demos.base.DemoActivity;

/**
 * Created by zhangyang131 on 2016/11/3.
 */

public class RxAndroidActivity extends DemoActivity {

    @Override
    protected Class<? extends Fragment> getSingleContentFragmentClass() {
        return RxAndroidFragment.class;
    }
}
