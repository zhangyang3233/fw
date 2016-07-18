package com.fw.zycoder.demos.demos.guagua;

import android.support.v4.app.Fragment;

import com.fw.zycoder.demos.base.DemoActivity;

/**
 * Created by zhangyang on 16/7/14.
 */
public class ScartchOutActivity extends DemoActivity {


    @Override
    public boolean getCanFlingBack() {
        return false;
    }

    @Override
    protected Class<? extends Fragment> getSingleContentFragmentClass() {
        return ScartchFragment.class;
    }
}
