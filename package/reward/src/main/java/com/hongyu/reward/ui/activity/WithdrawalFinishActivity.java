package com.hongyu.reward.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.hongyu.reward.appbase.BaseSingleFragmentActivity;
import com.hongyu.reward.manager.AccountManager;
import com.hongyu.reward.ui.fragment.WithdrawalFinishFragment;

/**
 * Created by zhangyang131 on 16/10/17.
 */
public class WithdrawalFinishActivity extends BaseSingleFragmentActivity {

    public static void launch(Context context){
        Intent i= new Intent(context, WithdrawalFinishActivity.class);
        AccountManager.launchAfterLogin(context, i);
    }


    @Override
    protected Class<? extends Fragment> getSingleContentFragmentClass() {
        return WithdrawalFinishFragment.class;
    }

    @Override
    protected String getTitleText() {
        return "提现完成";
    }
}
