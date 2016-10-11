package com.hongyu.reward.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.fw.zycoder.http.callback.DataCallback;
import com.fw.zycoder.utils.CollectionUtils;
import com.fw.zycoder.utils.GlobalConfig;
import com.fw.zycoder.utils.SPUtil;
import com.hongyu.reward.R;
import com.hongyu.reward.config.Constants;
import com.hongyu.reward.http.ResponesUtil;
import com.hongyu.reward.interfaces.LogoutListener;
import com.hongyu.reward.model.BaseModel;
import com.hongyu.reward.model.LoginModel;
import com.hongyu.reward.request.CommonCallback;
import com.hongyu.reward.request.FindPwdRequestBuilder;
import com.hongyu.reward.request.GetAuthCodeRequestBuilder;
import com.hongyu.reward.request.GetUserInfoRequestBuilder;
import com.hongyu.reward.request.LoginRequestBuilder;
import com.hongyu.reward.request.RegisterRequestBuilder;
import com.hongyu.reward.request.SetPwdRequestBuilder;
import com.hongyu.reward.ui.fragment.WelcomeFragment;
import com.hongyu.reward.utils.T;

import java.util.ArrayList;


/**
 * Created by zhangyang131 on 16/9/8.
 */
public class AccountManager {
  private static AccountManager instance;
  private LoginModel.UserInfo user;
  private ArrayList<LogoutListener> observe;

  public static AccountManager getInstance() {
    if (instance == null) {
      instance = new AccountManager();
    }
    instance.initUser();
    return instance;
  }

  public static AccountManager getInstance(Context context) {
    if (GlobalConfig.getAppContext() == null) {
      GlobalConfig.setAppContext(context);
    }
    return getInstance();
  }

  public void addLogoutListener(LogoutListener l) {
    if (observe == null) {
      observe = new ArrayList<>();
    }
    observe.add(l);
  }

  public void removeLogoutListener(LogoutListener l) {
    if (observe != null) {
      observe.remove(l);
    }
  }

  public boolean isLogin() {
    if (user != null && !TextUtils.isEmpty(user.getUser_id())) {
      return true;
    }
    return false;
  }

  public boolean needToShowWelcome() {
    int v = SPUtil.getInt(Constants.Pref.GUIDE_KEY, 0);
    if (WelcomeFragment.GUIDE_VERSION > v) { // 如果有新的guide页面就应该看看新的
      SPUtil.putInt(Constants.Pref.GUIDE_KEY, WelcomeFragment.GUIDE_VERSION);
      return true;
    }
    return false;
  }

  public void login(String account, String pwd, final LoginRequestBuilder.LoginCallback callback) {
    LoginRequestBuilder builder = new LoginRequestBuilder(account, pwd);
    builder.setDataCallback(new DataCallback<LoginModel>() {
      @Override
      public void onDataCallback(final LoginModel data) {
        if (ResponesUtil.checkModelCodeOK(data)) {
          saveUserInfo(data.getData());
          callback.loginSuccess(data.getData());
        } else {
          callback.loginFailed(ResponesUtil.getErrorMsg(data));
        }
      }
    });
    builder.build().submit();
  }

  public void register(String phone, String repwd, String code,
      final RegisterRequestBuilder.CallBack callBack) {
    final RegisterRequestBuilder builder = new RegisterRequestBuilder(phone, code, repwd);
    builder.setDataCallback(new DataCallback<BaseModel>() {
      @Override
      public void onDataCallback(BaseModel data) {
        if (ResponesUtil.checkModelCodeOK(data)) {
          callBack.success();
        } else {
          callBack.failed(ResponesUtil.getErrorMsg(data));
        }
      }
    });
    builder.build().submit();
  }







  /**
   * 获取手机验证码
   * 
   * @param phone
   * @param callBack
   */
  public void getCode(String phone, final GetAuthCodeRequestBuilder.CallBack callBack) {
    GetAuthCodeRequestBuilder builder = new GetAuthCodeRequestBuilder(phone);
    builder.setDataCallback(new DataCallback<BaseModel>() {
      @Override
      public void onDataCallback(BaseModel data) {
        if (ResponesUtil.checkModelCodeOK(data)) {
          callBack.success();
          T.show(R.string.auth_tip);
        } else {
          callBack.failed(ResponesUtil.getErrorMsg(data));
        }
      }
    });
    builder.build().submit();

  }


  public void findPwdVerfiy(String phoneNum, String authCode, final CommonCallback callback) {
    FindPwdRequestBuilder builder = new FindPwdRequestBuilder(phoneNum, authCode);
    builder.setDataCallback(new DataCallback<BaseModel>() {
      @Override
      public void onDataCallback(BaseModel data) {
        if (ResponesUtil.checkModelCodeOK(data)) {
          callback.success();
        } else {
          callback.failed(ResponesUtil.getErrorMsg(data));
        }
      }
    });
    builder.build().submit();

  }

  public void setPwd(String phoneNum, String pwd, final CommonCallback callback) {
    SetPwdRequestBuilder builder = new SetPwdRequestBuilder(phoneNum, pwd);
    builder.setDataCallback(new DataCallback<BaseModel>() {
      @Override
      public void onDataCallback(BaseModel data) {
        if (ResponesUtil.checkModelCodeOK(data)) {
          callback.success();
        } else {
          callback.failed(ResponesUtil.getErrorMsg(data));
        }
      }
    });
    builder.build().submit();

  }



  public void saveUserInfo(LoginModel.UserInfo user) {
    this.user = user;
    /**
     * 将个人信息持久化到本地
     */
    SharedPreferences pref = GlobalConfig.getAppContext()
        .getSharedPreferences(Constants.Pref.USER_INFO, Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = pref.edit();
    editor.putString("user_id", user.getUser_id());
    editor.putString("username", user.getUsername());
    editor.putString("nickname", user.getNickname());
    editor.putString("avatar", user.getAvatar());
    editor.putString("phone", user.getPhone());
    editor.putInt("gender", user.getGender());
    editor.putFloat("score", user.getScore());
    editor.putFloat("cash", user.getCash());
    editor.putFloat("lock_cash", user.getLock_cash());
    editor.commit();
  }


  public void requestUserInfo(final GetUserInfoCallback callback) {
    GetUserInfoRequestBuilder builder = new GetUserInfoRequestBuilder();
    builder.setDataCallback(new DataCallback<LoginModel>() {
      @Override
      public void onDataCallback(LoginModel data) {
        if (ResponesUtil.checkModelCodeOK(data)) {
          if (callback != null) {
            callback.getUserInfoSuccess(data.getData());
          }
        } else {
          LoginModel.UserInfo userInfo = getLocalUserInfo();
          if (userInfo == null) {
            if (callback != null) {
              callback.getUserInfoFailed(ResponesUtil.getErrorMsg(data));
            }
          } else {
            if (callback != null) {
              callback.getUserInfoSuccess(userInfo);
            }
          }
        }
      }
    });
    builder.build().submit();
  }

  public LoginModel.UserInfo getLocalUserInfo() {
    initUser();
    return user;
  }

  public void logout() {
    user = null;
    SharedPreferences pref = GlobalConfig.getAppContext()
        .getSharedPreferences(Constants.Pref.USER_INFO, Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = pref.edit();
    editor.clear();
    editor.commit();
    notifyLogout();
  }

  private void notifyLogout() {
    if (!CollectionUtils.isEmpty(observe)) {
      for (LogoutListener l : observe) {
        l.onLogout();
      }
    }
  }

  public void initUser() {
    if (user == null) {
      LoginModel.UserInfo userInfo = new LoginModel.UserInfo();
      SharedPreferences pref = GlobalConfig.getAppContext()
          .getSharedPreferences(Constants.Pref.USER_INFO, Context.MODE_PRIVATE);
      String userId = pref.getString(Constants.App.APP_USERID, "");
      if(!TextUtils.isEmpty(userId)){
        user = userInfo;
        user.setUser_id(userId);
        user.setUsername(pref.getString("username", ""));
        user.setNickname(pref.getString("nickname", ""));
        user.setAvatar(pref.getString("avatar", ""));
        user.setPhone(pref.getString("phone", ""));
        user.setGender(pref.getInt("gender", 0));
        user.setScore(pref.getFloat("score", 0));
        user.setCash(pref.getFloat("cash", 0));
        user.setLock_cash(pref.getFloat("lock_cash", 0));
      }else{
        return;
      }
    }
  }

  public interface GetUserInfoCallback {
    void getUserInfoSuccess(LoginModel.UserInfo userInfo);

    void getUserInfoFailed(String msg);
  }

}
