package com.fw.zycoder.demos.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by zhangyang131 on 16/6/21.
 */
public class DemoActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resetTitle();
    }

    private void resetTitle() {
        String[] s = getTitle().toString().split("/");
        setTitle(s[s.length-1]);
    }
}
