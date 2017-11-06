package com.hongyu.reward.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.hongyu.reward.appbase.BaseSingleFragmentActivity;
import com.hongyu.reward.ui.fragment.XCHDFragment;

/**
 * Created by zhangyang131 on 2017/8/15.
 */

public class XCHDActivity extends BaseSingleFragmentActivity {

    public static void launch(Context context){
        Intent intent = new Intent(context, XCHDActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected Class<? extends Fragment> getSingleContentFragmentClass() {
        return XCHDFragment.class;
    }

    @Override
    protected String getTitleText() {
        return "喜茶活动";
    }
}
