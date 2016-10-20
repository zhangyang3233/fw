package com.hongyu.reward.http;

import android.text.TextUtils;

import com.fw.zycoder.utils.GlobalConfig;
import com.hongyu.reward.R;
import com.hongyu.reward.config.ResultCode;
import com.hongyu.reward.model.BaseModel;

import org.json.JSONObject;

/**
 * Created by zhangyang131 on 16/9/9.
 */
public class ResponesUtil {

  public static boolean checkModelCodeOK(BaseModel model) {
    if (null == model || model.getCode() != 0) {
      return false;
    }
    return true;
  }

  public static boolean checkModelCodeOK(JSONObject model) {
    if (null == model || model.optInt("code", 1001) != 0) {
      return false;
    }
    return true;
  }

  public static String getErrorMsg(BaseModel model) {
    String msg;
    if (null == model) {
      msg = GlobalConfig.getAppContext().getString(R.string.common_net_word_error);
    } else if (model.getCode() == ResultCode.code_1004) {
      msg = "登录失效, 请重新登录";
    } else if (TextUtils.isEmpty(model.getMessage())) {
      msg = GlobalConfig.getAppContext().getString(R.string.common_error_unknown);
    } else {
      msg = model.getMessage();
    }
    return msg;
  }

  public static String getErrorMsg(JSONObject data) {
    String msg;
    if (null == data) {
      msg = GlobalConfig.getAppContext().getString(R.string.common_net_word_error);
    } else {
      msg = data.optString("message",
          GlobalConfig.getAppContext().getString(R.string.common_error_unknown));
    }
    return msg;
  }
}
