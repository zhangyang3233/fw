package com.hongyu.reward.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.hongyu.reward.appbase.BaseSingleFragmentActivity;
import com.hongyu.reward.ui.fragment.PersonInfoSettingFragment;

/**
 * Created by zhangyang131 on 16/10/10.
 */
public class PersonInfoSettingActivity extends BaseSingleFragmentActivity {

    public static void launch(Context context){
        Intent i = new Intent(context, PersonInfoSettingActivity.class);
        context.startActivity(i);
    }


    @Override
    protected Class<? extends Fragment> getSingleContentFragmentClass() {
        return PersonInfoSettingFragment.class;
    }

    @Override
    protected String getTitleText() {
        return "个人资料";
    }
}
