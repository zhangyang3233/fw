package com.hongyu.reward.ui.fragment.startapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.fw.zycoder.http.callback.DataCallback;
import com.fw.zycoder.utils.PhoneNumberUtils;
import com.hongyu.reward.R;
import com.hongyu.reward.appbase.BaseLoadFragment;
import com.hongyu.reward.http.ResponesUtil;
import com.hongyu.reward.model.BaseModel;
import com.hongyu.reward.request.SetPwdRequestBuilder;
import com.hongyu.reward.ui.activity.RePwdAuthActivity;
import com.hongyu.reward.utils.InputUtil;
import com.hongyu.reward.utils.T;


/**
 * Created by zhangyang131 on 16/9/8.
 */
public class RePwdAuthFragment extends BaseLoadFragment implements View.OnClickListener {
  private String phone;
  private EditText mRePwd;
  private EditText mPwd;
  private Button mBtnCommit;

  @Override
  protected void onInflated(View contentView, Bundle savedInstanceState) {
    getData();
    initView();
  }

  private void getData() {
    phone = getArguments().getString(RePwdAuthActivity.PHONE_NUM);
    if (TextUtils.isEmpty(phone) || !PhoneNumberUtils.isPhoneNum(phone)) {
      T.show(R.string.login_phone_not_match);
    }
  }

  private void initView() {
    mPwd = (EditText) mContentView.findViewById(R.id.pwd);
    mRePwd = (EditText) mContentView.findViewById(R.id.re_pwd);
    mPwd.setHint(R.string.login_pwd_hint);
    mRePwd.setHint(R.string.login_reinput_pwd_hint);
    mBtnCommit = (Button) mContentView.findViewById(R.id.commit);
    mBtnCommit.setOnClickListener(this);
  }

  @Override
  protected int getLayoutResId() {
    return R.layout.activity_repwd_layout;
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.commit:
        commit();
        break;
    }
  }

  private void commit() {
    String pwd = mPwd.getText().toString().trim();
    String repwd = mRePwd.getText().toString().trim();

    if (!InputUtil.checkPwd(mRePwd)) {
      return;
    }
    if (!pwd.equals(repwd)) {
      T.show(R.string.pwd_not_equals);
      return;
    }

    showLoadingView();
    SetPwdRequestBuilder builder = new SetPwdRequestBuilder(phone, repwd);
    builder.setDataCallback(new DataCallback<BaseModel>() {
      @Override
      public void onDataCallback(BaseModel data) {
        if (!isAdded()) {
          return;
        }
        dismissLoadingView();
        if (ResponesUtil.checkModelCodeOK(data)) {
          T.show(R.string.reset_pwd_success);
          getActivity().finish();
        } else {
          T.show(ResponesUtil.getErrorMsg(data));
        }
      }
    });
    builder.build().submit();
  }

  @Override
  protected void onStartLoading() {

  }
}
