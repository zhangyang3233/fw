package com.hongyu.reward.utils;

import android.text.TextUtils;
import android.widget.EditText;

import com.fw.zycoder.utils.PhoneNumberUtils;
import com.hongyu.reward.R;

/**
 * Created by zhangyang131 on 16/9/10.
 */
public class InputUtil {

  /**
   * 检查电话号码格式是否填写正确
   * 
   * @param phoneEditText
   * @return
   */
  public static boolean checkPhoneNum(EditText phoneEditText) {
    String phone = phoneEditText.getText().toString();
    if (TextUtils.isEmpty(phone)) {
      phoneEditText
          .setError(phoneEditText.getContext().getString(R.string.login_phone_must_be_not_empty));
      return false;
    }

    if (!PhoneNumberUtils.isPhoneNum(phone)) {
      phoneEditText.setError(phoneEditText.getContext().getString(R.string.login_phone_not_match));
      return false;
    }
    return true;
  }

  public static boolean checkPwd(EditText pwdEditText){
    String pwd = pwdEditText.getText().toString();
    if (TextUtils.isEmpty(pwd)) {
      pwdEditText
              .setError(pwdEditText.getContext().getString(R.string.login_pwd_must_be_not_empty));
      return false;
    }

    if (pwd.length() < 6) {
      pwdEditText.setError(pwdEditText.getContext().getString(R.string.pwd_too_short));
      return false;
    }

    if (pwd.length() >16) {
      pwdEditText.setError(pwdEditText.getContext().getString(R.string.pwd_too_long));
      return false;
    }

    return true;
  }
}
