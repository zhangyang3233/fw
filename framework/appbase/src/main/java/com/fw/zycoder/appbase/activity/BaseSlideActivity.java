package com.fw.zycoder.appbase.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import com.fw.zycoder.appbase.R;
import com.fw.zycoder.appbase.fragment.BaseFragment;
import com.zycoder.sliding.SlidingHelper;
import com.zycoder.sliding.component.SlideActivity;

/**
 * @author zhangyang
 */
public class BaseSlideActivity extends AppCompatActivity implements SlideActivity {
  private boolean mIsDestroyed = false;
  protected BaseFragment mFragment;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(getLayoutId());
    SlidingHelper.onCreate(this);
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

}
