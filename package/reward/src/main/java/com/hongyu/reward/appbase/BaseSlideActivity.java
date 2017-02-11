package com.hongyu.reward.appbase;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import com.fw.zycoder.utils.Log;
import com.hongyu.reward.R;
import com.hongyu.reward.interfaces.LogoutListener;
import com.hongyu.reward.manager.ScreenManager;
import com.umeng.analytics.MobclickAgent;

/**
 * @author zhangyang
 */
public class BaseSlideActivity extends AppCompatActivity implements LogoutListener {
  protected BaseFragment mFragment;
  private boolean mIsDestroyed = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ScreenManager.getScreenManager().pushActivity(this);
    Log.v("activity生命周期", this.getClass().getSimpleName()+" --> onCreate");
    setContentView(getLayoutId());
  }

  @Override
  public void onWindowFocusChanged(boolean hasFocus) {
    super.onWindowFocusChanged(hasFocus);
  }

  @Override
  protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    if (this.mFragment != null) {
      this.mFragment.onNewIntent(intent);
    }
  }


  public void onResume() {
    Log.v("activity生命周期", this.getClass().getSimpleName()+" --> onResume");
    super.onResume();
    MobclickAgent.onResume(this);
  }
  public void onPause() {
    Log.v("activity生命周期", this.getClass().getSimpleName()+" --> onPause");
    super.onPause();
    MobclickAgent.onPause(this);
  }

  @Override
  protected void onStart() {
    super.onStart();
    Log.v("activity生命周期", this.getClass().getSimpleName()+" --> onStart");
  }

  @Override
  protected void onStop() {
    Log.v("activity生命周期", this.getClass().getSimpleName()+" --> onStop");
    super.onStop();
  }

  public boolean isDestroyed() {
    try {
      return super.isDestroyed();
    } catch (NoSuchMethodError e) {
      return mIsDestroyed;
    }
  }

  @Override
  protected void onDestroy() {
    Log.v("activity生命周期", this.getClass().getSimpleName()+" --> onDestroy");
    super.onDestroy();
    mIsDestroyed = true;
    ScreenManager.getScreenManager().popActivity(this);
  }

  @Override
  public void finish() {
    super.finish();
  }


  /**
   * get layout id .
   * notice: id : fragment_container , sliding_pane_layout is need in layout
   */
  protected int getLayoutId() {
    return R.layout.baselayout;
  }

  protected void replaceFragment(Fragment newFragment) {
    replaceFragment(newFragment, null, false);
  }

  protected void replaceFragment(Fragment newFragment, Bundle arguments, boolean isAddStack) {
    if (isFinishing()) {
      return;
    }
    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    if (arguments != null) {
      newFragment.setArguments(arguments);
    }
    transaction.replace(R.id.fragment_container, newFragment);
    if (isAddStack) {
      transaction.addToBackStack(null);
    }
    if (!isDestroyed()) {
      transaction.commitAllowingStateLoss();
    }
  }

  public boolean onKeyDown(int keyCode, KeyEvent event) {
    return this.mFragment != null && this.mFragment.onKeyDown(keyCode, event)
        ? true
      : super.onKeyDown(keyCode, event);
  }

  @Override
  public void onLogout() {
  }




}
