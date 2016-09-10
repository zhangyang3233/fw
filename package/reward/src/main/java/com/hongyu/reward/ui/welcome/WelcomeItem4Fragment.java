package com.hongyu.reward.ui.welcome;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.hongyu.reward.R;
import com.hongyu.reward.appbase.BaseFragment;
import com.hongyu.reward.ui.login.LoginActivity;


/**
 * Created by zhangyang131 on 16/9/8.
 */
public class WelcomeItem4Fragment extends BaseFragment implements View.OnClickListener{
Button mBtnLogin;
    @Override
    protected void onInflated(View contentView, Bundle savedInstanceState) {
        mBtnLogin = (Button) mContentView.findViewById(R.id.btn_login);
        mBtnLogin.setOnClickListener(this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.welcome_layout_pager4;
    }

    @Override
    public void onClick(View v) {
        LoginActivity.launch(getActivity());
        getActivity().finish();
    }
}
