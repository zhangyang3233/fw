package com.hongyu.reward.ui.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.hongyu.reward.appbase.BaseSlideActivity;
import com.hongyu.reward.manager.AccountManager;

/**
 * Created by zhangyang131 on 16/7/22.
 */
public class SplashActivity extends BaseSlideActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        delayedLuanch();
    }

    private void delayedLuanch() {
        new Handler()
                .postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (isFinishing()) {
                            return;
                        }

                        if (isDestroyed()) {
                            return;
                        }
                        jumpToNextActivity();
                        finish();
                    }
                }, getDelayedTime());
    }

    private void jumpToNextActivity() {
        if(AccountManager.getInstance().needToShowWelcome()){
            WelcomeActivity.launch(this);
            finish();
        }else if(AccountManager.getInstance().isLogin()){

        }else{

        }


    }

    @Override
    public boolean getCanFlingBack() {
        return false;
    }

    public long getDelayedTime() {
        SharedPreferences sp = getPreferences(MODE_PRIVATE);
        boolean isFirstLuanch = sp.getBoolean(IS_FIRST_LAUNCH, true);
        if (isFirstLuanch) {
            SharedPreferences.Editor ed = sp.edit();
            ed.putBoolean(IS_FIRST_LAUNCH, false);
            ed.commit();
            return 0;
        }
        return 2000;
    }

    private static final String IS_FIRST_LAUNCH = "is first launch";
}
