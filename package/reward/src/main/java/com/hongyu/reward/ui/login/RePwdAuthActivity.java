package com.hongyu.reward.ui.login;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.hongyu.reward.R;
import com.hongyu.reward.appbase.BaseSingleFragmentActivity;


/**
 * Created by zhangyang131 on 16/9/8.
 */
public class RePwdAuthActivity extends BaseSingleFragmentActivity {
    public static final String PHONE_NUM = "phone_num";

    public static void launch(Context context, String phoneNum) {
        Intent i = new Intent(context, RePwdAuthActivity.class);
        i.putExtra(PHONE_NUM, phoneNum);
        context.startActivity(i);
    }

    @Override
    protected String getTitleText() {
        return getString(R.string.reset_pwd);
    }


    @Override
    protected Class<? extends Fragment> getSingleContentFragmentClass() {
        return RePwdAuthFragment.class;
    }
}
