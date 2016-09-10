package com.hongyu.reward.ui.login;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fw.zycoder.utils.MainThreadPostUtils;
import com.fw.zycoder.utils.PhoneNumberUtils;
import com.hongyu.reward.R;
import com.hongyu.reward.appbase.BaseLoadFragment;
import com.hongyu.reward.manager.AccountManager;
import com.hongyu.reward.request.GetAuthCodeRequestBuilder;
import com.hongyu.reward.request.RegisterRequestBuilder;
import com.hongyu.reward.utils.T;

/**
 * Created by zhangyang131 on 16/9/10.
 */
public class RegisterFragment extends BaseLoadFragment implements View.OnClickListener {
  public static final int max_second = 60;
  private EditText mPhone;
  private EditText mPwd;
  private EditText mRepwd;
  private EditText mCode;
  private Button mBtnRegister;
  private TextView mBtnGetCode;

  @Override
  protected void onInflated(View contentView, Bundle savedInstanceState) {
    initView();
  }

  private void initView() {
    mPhone = (EditText) mContentView.findViewById(R.id.phone);
    mCode = (EditText) mContentView.findViewById(R.id.code);
    mPwd = (EditText) mContentView.findViewById(R.id.pwd);
    mRepwd = (EditText) mContentView.findViewById(R.id.repwd);
    mBtnGetCode = (TextView) mContentView.findViewById(R.id.get_code);

    mBtnRegister = (Button) mContentView.findViewById(R.id.register);
    mBtnRegister.setOnClickListener(this);
    mBtnGetCode.setOnClickListener(this);
  }

  @Override
  protected int getLayoutResId() {
    return R.layout.activity_app_register_layout;
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.register:
        register();
        break;
      case R.id.get_code:
        sendCode();
        break;
    }
  }

  private void register() {
    String phone = mPhone.getText().toString().trim();
    String code = mCode.getText().toString().trim();
    String pwd = mPwd.getText().toString().trim();
    String repwd = mRepwd.getText().toString().trim();
    // PhoneNumberUtils
    if (TextUtils.isEmpty(phone)) {
      mPhone.setError(getString(R.string.login_phone_must_be_not_empty));
      return;
    }
    if (TextUtils.isEmpty(code)) {
      mCode.setError(getString(R.string.please_input_auth));
      return;
    }
    if (TextUtils.isEmpty(pwd)) {
      mPwd.setError(getString(R.string.login_pwd_must_be_not_empty));
      return;
    }
    if (TextUtils.isEmpty(repwd)) {
      mRepwd.setError(getString(R.string.login_pwd_must_be_not_empty));
      return;
    }

    if (!PhoneNumberUtils.isPhoneNum(phone)) {
      mPhone.setError(getString(R.string.login_phone_not_match));
      return;
    }


    AccountManager.getInstance().register(phone, repwd, code,
        new RegisterRequestBuilder.CallBack() {

          @Override
          public void success() {
            T.show(getString(R.string.register_success));
            getActivity().finish();
          }

          @Override
          public void failed(String error) {
            T.show(error);
          }
        });
  }


  private void sendCode() {
    String phone = mPhone.getText().toString().trim();
    if (TextUtils.isEmpty(phone)) {
      mPhone.setError(getString(R.string.login_phone_must_be_not_empty));
      return;
    }

    if (!PhoneNumberUtils.isPhoneNum(phone)) {
      mPhone.setError(getString(R.string.login_phone_not_match));
      return;
    }

    showLoadingView();
    AccountManager.getInstance().getCode(phone, new GetAuthCodeRequestBuilder.CallBack() {
      @Override
      public void success() {
        dismissLoadingView();
        startTiming();
      }

      @Override
      public void failed(String msg) {
        dismissLoadingView();
        T.show(msg);
      }
    });
  }

  private void startTiming() {
    mBtnGetCode.setText(getString(R.string.login_second, String.valueOf(max_second)));
    mBtnGetCode.setClickable(false);
    new Thread() {
      public void run() {
        int s = max_second;
        while (s >= 0 || isAdded()) {
          resetCodeTextOnMainThread(s);
          try {
            sleep(1000);
            s--;
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      };
    }.start();
  }

  private void resetCodeTextOnMainThread(final int time) {
    if (!isAdded()) {
      return;
    }
    MainThreadPostUtils.post(new Runnable() {
      @Override
      public void run() {
        if (time == 0) {
          mBtnGetCode.setText(R.string.login_get_auth);
          mBtnGetCode.setClickable(true);
        } else {
          mBtnGetCode.setText(getString(R.string.login_second, String.valueOf(time)));
          mBtnGetCode.setClickable(false);
        }
      }
    });
  }

  @Override
  protected void onStartLoading() {

  }
}
