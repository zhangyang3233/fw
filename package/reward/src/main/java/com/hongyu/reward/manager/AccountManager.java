package com.hongyu.reward.manager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.fw.zycoder.utils.CollectionUtils;
import com.fw.zycoder.utils.GlobalConfig;
import com.hongyu.reward.config.Constants;
import com.hongyu.reward.interfaces.LogoutListener;
import com.hongyu.reward.model.LoginModel;
import com.hongyu.reward.ui.activity.LoginActivity;

import java.util.ArrayList;

/**
 * Created by zhangyang131 on 16/10/14.
 */
public class AccountManager {
  private static AccountManager instance;
  private LoginModel.UserInfo user;
  private ArrayList<LogoutListener> mLogoutListeners;

  public void saveUser(LoginModel.UserInfo user){
    this.user = user;
    this.user.save();
  }

  public static synchronized AccountManager getInstance() {
    if (instance == null) {
      instance = new AccountManager();
      instance.init();
    }
    return instance;
  }

  public void addLogoutListener(LogoutListener logoutListener) {
    if (mLogoutListeners == null) {
      mLogoutListeners = new ArrayList<>();
    }
    mLogoutListeners.add(logoutListener);
  }

  public void removeLogoutListener(LogoutListener logoutListener) {
    if (mLogoutListeners != null) {
      mLogoutListeners.remove(logoutListener);
    }
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
    if (!CollectionUtils.isEmpty(mLogoutListeners)) {
      for (LogoutListener l : mLogoutListeners) {
        l.onLogout();
      }
      mLogoutListeners.clear();
    }
  }


  public LoginModel.UserInfo getUser() {
    return user;
  }

  public boolean isLogin() {
    return (user != null) && !TextUtils.isEmpty(user.getUser_id());
  }

  /**
   * 加载本地 user 信息
   */
  private void init() {
    initUser();
  }


  private void initUser() {
    SharedPreferences pref = GlobalConfig.getAppContext()
        .getSharedPreferences(Constants.Pref.USER_INFO, Context.MODE_PRIVATE);
    String userId = pref.getString(Constants.App.APP_USERID, "");
    if (!TextUtils.isEmpty(userId)) {
      user = new LoginModel.UserInfo();
      user.setUser_id(userId);
      user.setUsername(pref.getString("username", ""));
      user.setNickname(pref.getString("nickname", ""));
      user.setHead_img(pref.getString("head_img", ""));
      user.setAvatar(pref.getString("avatar", ""));
      user.setPhone(pref.getString("phone", ""));
      user.setGender(pref.getInt("gender", 0));
      user.setScore(pref.getFloat("score", 0));
      user.setCash(pref.getFloat("cash", 0));
      user.setLock_cash(pref.getFloat("lock_cash", 0));
    }
  }


  public static void launchAfterLogin(Context context, Intent pendingIntent){
    if(getInstance().isLogin()){
      context.startActivity(pendingIntent);
    }else{
      LoginActivity.launch(context, pendingIntent);
    }
  }
}
