package com.hongyu.reward.ui.login;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hongyu.reward.R;
import com.hongyu.reward.appbase.BaseLoadFragment;
import com.hongyu.reward.manager.AccountManager;
import com.hongyu.reward.model.LoginModel;
import com.hongyu.reward.request.LoginRequestBuilder;
import com.hongyu.reward.ui.main.TabHostActivity;
import com.hongyu.reward.utils.InputUtil;
import com.hongyu.reward.utils.T;


/**
 * Created by zhangyang131 on 16/9/8.
 */
public class LoginFragment extends BaseLoadFragment implements View.OnClickListener {
  private EditText mPhone;
  private EditText mPwd;
  private Button mBtnLogin;
  private TextView mBtnForgetPwd;



  @Override
  protected void onInflated(View contentView, Bundle savedInstanceState) {
    initView();
  }

  private void initView() {
    mPhone = (EditText) mContentView.findViewById(R.id.phone);
    mPwd = (EditText) mContentView.findViewById(R.id.pwd);
    mBtnLogin = (Button) mContentView.findViewById(R.id.login);
    mBtnForgetPwd = (TextView) mContentView.findViewById(R.id.forgetpwd);
    mBtnLogin.setOnClickListener(this);
    mBtnForgetPwd.setOnClickListener(this);
  }

  @Override
  protected int getLayoutResId() {
    return R.layout.activity_app_login_layout;
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.login:
        login();
        break;
      case R.id.forgetpwd:
        ForgetPwdActivity.launch(getActivity());
        break;
    }
  }

  private void login() {
    String phone = mPhone.getText().toString().trim();
    String pwd = mPwd.getText().toString().trim();

    if (!InputUtil.checkPhoneNum(mPhone)) {
      return;
    }

    if (TextUtils.isEmpty(pwd)) {
      T.show(R.string.login_pwd_must_be_not_empty);
      return;
    }


    showLoadingView();
    AccountManager.getInstance().login(phone, pwd, new LoginRequestBuilder.LoginCallback() {
      @Override
      public void loginSuccess(LoginModel.UserInfo userInfo) {
        dismissLoadingView();
        T.show(R.string.login_success);
        gotoMainPage();
        getActivity().finish();
      }

      @Override
      public void loginFailed(String errorMsg) {
        dismissLoadingView();
        T.show(errorMsg);
      }
    });
  }

  private void gotoMainPage() {
    TabHostActivity.launch(getActivity());
    getActivity().finish();
  }

  @Override
  protected void onStartLoading() {

  }
}
