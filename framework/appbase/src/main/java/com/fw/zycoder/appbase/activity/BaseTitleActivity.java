package com.fw.zycoder.appbase.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.LayoutParams;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.fw.zycoder.appbase.R;
import com.fw.zycoder.appbase.activity.title.CommonTitleView;
import com.fw.zycoder.appbase.activity.title.TitleContainer;
import com.readystatesoftware.systembartint.SystemBarTintManager;

/**
 * Created by zhangyang131 on 16/7/14.
 */
public abstract class BaseTitleActivity extends BaseSlideActivity {

  private ActionBar mActionBar;
  protected TitleContainer mCustomTitleView;

  @Override
  protected int getLayoutId() {
    return R.layout.base_title_fragment_activity;
  }

  @Override
  protected void onCreate(Bundle onSaveInstanceState) {
    super.onCreate(onSaveInstanceState);
    initWindow();
    mCustomTitleView = getMyTitleContainer();
    customizeMyTitle(mCustomTitleView);
    initToolbar();
    setCustomTitleView(mCustomTitleView);
    onCreateMenu();
  }

  protected TitleContainer getMyTitleContainer() {
    return CommonTitleView.newInstance(this);
  }

  protected void customizeMyTitle(TitleContainer titleView) {
    // empty implement
  }

  /**
   * 创建title bar的menu,用于替代原有的 onCreateOptionsMenu, onOptionsItemSelected
   * 可参考BaseFilmTitleActivity 的 onCreateMenu 方法
   */
  protected void onCreateMenu() {

  }

  @Override
  public final boolean onCreateOptionsMenu(Menu menu) {
    return false;
  }

  @Override
  public final boolean onOptionsItemSelected(MenuItem item) {
    return false;
  }

  private void initToolbar() {
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    customizeToolbar(toolbar);
    setSupportActionBar(toolbar);
    mActionBar = getSupportActionBar();
    mActionBar.setHomeAsUpIndicator(R.mipmap.base_title_back);
    mActionBar.setDisplayHomeAsUpEnabled(true);
    setTitle(getTitleText());
  }

  protected void customizeToolbar(Toolbar toolbar) {

  }

  public void setLeftTitleView(View v) {
    if (mCustomTitleView != null) {
      mCustomTitleView.setLeftView(v, null);
    }
  }

  public void setRightTitleView(View v) {
    if (mCustomTitleView != null) {
      mCustomTitleView.setRightView(v, null);
    }
  }

  public void setRightTitleView(View v, ViewGroup.LayoutParams layoutParams) {
    if (mCustomTitleView != null) {
      mCustomTitleView.setRightView(v, layoutParams);
    }
  }

  public void setLeftTitleView(View v, ViewGroup.LayoutParams layoutParams) {
    if (mCustomTitleView != null) {
      mCustomTitleView.setLeftView(v, layoutParams);
    }
  }

  @Override
  public void setTitle(CharSequence title) {
    if (mCustomTitleView != null) {
      mCustomTitleView.setTitle(title);
      return;
    }
    super.setTitle(title);
  }

  @Override
  public void setTitle(int titleId) {
    if (mCustomTitleView != null) {
      mCustomTitleView.setTitle(titleId);
    }
    super.setTitle(titleId);
  }

  /**
   * 给出标题
   */
  protected abstract String getTitleText();

  /**
   * 设置标题home图标
   */
  protected void setHomeDrawable(int res) {
    mActionBar.setHomeAsUpIndicator(res);
  }

  /**
   * 设置自定义 view 使用
   *
   * @param view
   * @param <V>
   */
  protected <V extends TitleContainer> void setCustomTitleView(V view) {
    if (view instanceof View) {

      mActionBar.setDisplayHomeAsUpEnabled(false);
      mActionBar.setDisplayShowTitleEnabled(false);
      mActionBar.setDisplayShowHomeEnabled(false);

      mActionBar.setDisplayShowCustomEnabled(true);

      LayoutParams layoutParams =
          new LayoutParams(LayoutParams.MATCH_PARENT,
              LayoutParams.MATCH_PARENT);
      mActionBar.setCustomView((View) view, layoutParams);

      if (((View) view).getParent() instanceof Toolbar) {
        Toolbar parent = (Toolbar) ((View) view).getParent();
        parent.setContentInsetsAbsolute(0, 0);
      }
      mCustomTitleView = view;
    } else if (view == null) {
      mActionBar.setDisplayHomeAsUpEnabled(true);
      mActionBar.setDisplayShowTitleEnabled(true);
      mActionBar.setDisplayShowHomeEnabled(true);
      mActionBar.setDisplayShowCustomEnabled(false);
    }
  }

  @TargetApi(19)
  protected void initWindow() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
      SystemBarTintManager tintManager = new SystemBarTintManager(this);
      tintManager.setStatusBarTintEnabled(true);
      tintManager.setNavigationBarTintEnabled(true);
      tintManager.setTintColor(getResources().getColor(R.color.theme_blue));
    }
  }
}
