package com.hongyu.reward.ui.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.WindowManager;

import com.hongyu.reward.R;
import com.hongyu.reward.interfaces.GetLocationListener;
import com.hongyu.reward.interfaces.LogoutListener;
import com.hongyu.reward.manager.AccountManager;
import com.hongyu.reward.manager.LocationManager;
import com.hongyu.reward.model.AppLocation;
import com.hongyu.reward.ui.adapter.MainPagerAdapter;
import com.hongyu.reward.utils.DoubleClickUtil;
import com.hongyu.reward.utils.T;
import com.hongyu.reward.widget.BottomBar;
import com.readystatesoftware.systembartint.SystemBarTintManager;


/**
 * Created by zhangyang131 on 16/8/29.
 */
public class TabHostActivity extends FragmentActivity
    implements
      LogoutListener,
      GetLocationListener {

  private ViewPager mViewPager;
  private MainPagerAdapter mPagerAdapter;
  private BottomBar mBottomBar;
  private DrawerLayout mDrawerLayout;

  public static void launch(Context context) {
    Intent intent = new Intent(context, TabHostActivity.class);
    if (!(context instanceof Activity)) {
      intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }
    context.startActivity(intent);
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    initWindow();
    setContentView(R.layout.activity_classic);
    initView();
    getLocation();
    AccountManager.getInstance().addLogoutListener(this);
  }

  private void initView() {
    mViewPager = (ViewPager) findViewById(R.id.view_pager);
    mBottomBar = (BottomBar) findViewById(R.id.bottom_bar);
    mViewPager.setOffscreenPageLimit(3);
    mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

      @Override
      public void onPageSelected(int position) {
        mBottomBar.selectTab(position);
      }

      @Override
      public void onPageScrollStateChanged(int state) {

      }
    });

    mBottomBar.selectTab(0);
    mBottomBar.addOnTabChangeListener(new BottomBar.OnTabChangeListener() {
      @Override
      public void onTabSelected(int position) {
        mViewPager.setCurrentItem(position, false);
      }
    });
    initDrawerView();
  }

  private void getLocation() {
    if (!LocationManager.getInstance().isStarted()) {
      LocationManager.getInstance().start();
    }
    if (LocationManager.getInstance().getLocalLocationInfo() != null) { // 已经获取到位置信息
      setView();
    } else { // 没有获取到位置信息
      LocationManager.getInstance().addLocationListener(this);
    }
  }

  private void setView() {
    mPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
    mViewPager.setAdapter(mPagerAdapter);
  }

  private void initDrawerView() {
    mDrawerLayout = (DrawerLayout) findViewById(R.id.id_drawer_layout);
    mDrawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
      @Override
      public void onDrawerSlide(View drawerView, float slideOffset) {}

      @Override
      public void onDrawerOpened(View drawerView) {}

      @Override
      public void onDrawerClosed(View drawerView) {}

      @Override
      public void onDrawerStateChanged(int newState) {

      }
    });
  }

  @TargetApi(19)
  private void initWindow() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
      SystemBarTintManager tintManager = new SystemBarTintManager(this);
      tintManager.setStatusBarTintEnabled(true);
      tintManager.setNavigationBarTintEnabled(true);
      tintManager.setTintColor(getResources().getColor(R.color.colorPrimary));
    }
  }

  public void switchPage(int index) {
    mViewPager.setCurrentItem(index, false);
  }

  @Override
  public void onLogout() {
    LoginActivity.launch(this);
    finish();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    AccountManager.getInstance().removeLogoutListener(this);
    if (LocationManager.getInstance().isStarted()) {
      LocationManager.getInstance().stop();
    }
  }



  @Override
  public void onSuccess(AppLocation locationInfo) {
    LocationManager.getInstance().removeLocationListener(this);
    setView();
  }

  @Override
  public void onFailed(String msg) {}


  @Override
  public void onBackPressed() {
    if (DoubleClickUtil.isDouble(this.getClass().getSimpleName())) {
      super.onBackPressed();
    } else {
      T.show("再次点击关闭程序");
    }
  }
}
