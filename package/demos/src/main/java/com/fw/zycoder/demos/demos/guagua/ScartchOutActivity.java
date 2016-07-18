package com.fw.zycoder.demos.demos.guagua;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.fw.zycoder.demos.base.DemoActivity;

/**
 * Created by zhangyang on 16/7/14.
 */
public class ScartchOutActivity extends DemoActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScartchFragment fragment =
                (ScartchFragment) Fragment.instantiate(this, ScartchFragment.class.getName(), null);
        replaceFragment(fragment);
    }

    @Override
    public boolean getCanFlingBack() {
        return false;
    }
}
