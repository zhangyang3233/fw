package com.hongyu.reward.appbase;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import com.hongyu.reward.R;
import com.hongyu.reward.interfaces.LogoutListener;
import com.hongyu.reward.manager.ScreenManager;
import com.zycoder.sliding.SlidingHelper;
import com.zycoder.sliding.component.SlideActivity;

/**
 * @author zhangyang
 */
public class BaseSlideActivity extends AppCompatActivity implements SlideActivity, LogoutListener {
  protected BaseFragment mFragment;
  private boolean mIsDestroyed = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(getLayoutId());
    SlidingHelper.onCreate(this);
    ScreenManager.getScreenManager().pushActivity(this);
  }

  @Override
  public void onWindowFocusChanged(boolean hasFocus) {
    super.onWindowFocusChanged(hasFocus);
    SlidingHelper.onWindowFocusChanged(this, hasFocus);
  }

  @Override
  protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    SlidingHelper.onNewIntent(this);
    if (this.mFragment != null) {
      this.mFragment.onNewIntent(intent);
    }
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
    super.onDestroy();
    mIsDestroyed = true;
    SlidingHelper.onDestroy(this);
    ScreenManager.getScreenManager().popActivity(this);
  }

  @Override
  public void finish() {
    super.finish();
    SlidingHelper.finish(this);
  }


  /**
   * get layout id .
   * notice: id : fragment_container , sliding_pane_layout is need in layout
   */
  protected int getLayoutId() {
    return R.layout.sample_layout;
  }

  /**
   * @return this activity is need right fling close
   */
  @Override
  public boolean getCanFlingBack() {
    return false;
  }

  /**
   * @return under this activity is need relative move
   */
  @Override
  public boolean getCanRelativeMove() {
    return false;
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
