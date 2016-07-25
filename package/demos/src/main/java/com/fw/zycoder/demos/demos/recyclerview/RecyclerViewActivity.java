package com.fw.zycoder.demos.demos.recyclerview;

import android.support.v4.app.Fragment;

import com.fw.zycoder.demos.base.DemoActivity;

/**
 * Created by zhangyang131 on 16/7/25.
 */
public class RecyclerViewActivity extends DemoActivity {

    @Override
    protected Class<? extends Fragment> getSingleContentFragmentClass() {
        return RecyclerViewDemoFragment.class;
    }
}
