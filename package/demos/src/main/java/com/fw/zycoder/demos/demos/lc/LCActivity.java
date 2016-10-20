package com.fw.zycoder.demos.demos.lc;

import android.support.v4.app.Fragment;

import com.fw.zycoder.demos.base.DemoActivity;

/**
 * Created by zhangyang131 on 2016/10/20.
 */
public class LCActivity extends DemoActivity {

    @Override
    protected Class<? extends Fragment> getSingleContentFragmentClass() {
        return LCFragment.class;
    }
}
