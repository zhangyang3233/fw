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
import android.view.WindowManager;

import com.fw.zycoder.utils.Log;
import com.hongyu.reward.R;
import com.hongyu.reward.manager.LocationManager;
import com.hongyu.reward.manager.ScreenManager;
import com.hongyu.reward.ui.adapter.MainPagerAdapter;
import com.hongyu.reward.utils.DoubleClickUtil;
import com.hongyu.reward.utils.T;
import com.hongyu.reward.widget.BottomBar;
import com.pgyersdk.update.PgyUpdateManager;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;


/**
 * Created by zhangyang131 on 16/8/29.
 */
public class TabHostActivity extends FragmentActivity {

  private ViewPager mViewPager;
  private MainPagerAdapter mPagerAdapter;
  private BottomBar mBottomBar;
  // private DrawerLayout mDrawerLayout;

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
    PushAgent.getInstance(this).onAppStart();
    initWindow();
    setContentView(R.layout.activity_classic);
    initView();
    setView();
    PgyUpdateManager.register(this);
    ScreenManager.getScreenManager().pushActivity(this);
  }

  @Override
  protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    if (getParentActivityIntent() != null) {
      Bundle b = getParentActivityIntent().getExtras();
      if (b != null) {
        Log.e("push", "TabHostActivity onNewIntent" + b.getString("aaa"));
      }
    }

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
    // initDrawerView();
  }


  private void setView() {
    mPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
    mViewPager.setAdapter(mPagerAdapter);
  }

  // private void initDrawerView() {
  // mDrawerLayout = (DrawerLayout) findViewById(R.id.id_drawer_layout);
  // mDrawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
  // @Override
  // public void onDrawerSlide(View drawerView, float slideOffset) {}
  //
  // @Override
  // public void onDrawerOpened(View drawerView) {}
  //
  // @Override
  // public void onDrawerClosed(View drawerView) {}
  //
  // @Override
  // public void onDrawerStateChanged(int newState) {
  //
  // }
  // });
  // }

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
  protected void onDestroy() {
    super.onDestroy();
    ScreenManager.getScreenManager().popActivity(this);
    if (LocationManager.getInstance().isStarted()) {
      LocationManager.getInstance().stop();
    }
  }

//  @Override
//  protected void onResume() {
//    super.onResume();
//    // 自定义摇一摇的灵敏度，默认为950，数值越小灵敏度越高。
//    PgyFeedbackShakeManager.setShakingThreshold(1000);
//
//    // 以对话框的形式弹出
////    PgyFeedbackShakeManager.register(this);
//
//    // 以Activity的形式打开，这种情况下必须在AndroidManifest.xml配置FeedbackActivity
//    // 打开沉浸式,默认为false
//    // FeedbackActivity.setBarImmersive(true);
//    PgyFeedbackShakeManager.register(this, false);
//  }
//
//  @Override
//  protected void onPause() {
//    super.onPause();
//    PgyFeedbackShakeManager.unregister();
//  }

  @Override
  public void onBackPressed() {
    if (DoubleClickUtil.isDouble(this.getClass().getSimpleName())) {
      goBackground();
    } else {
      T.show("再次点击关闭程序");
    }
  }

  private void goBackground() {
    Intent intent = new Intent(Intent.ACTION_MAIN);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    intent.addCategory(Intent.CATEGORY_HOME);
    this.startActivity(intent);
  }

  public void onResume() {
    super.onResume();
    MobclickAgent.onResume(this);
  }
  public void onPause() {
    super.onPause();
    MobclickAgent.onPause(this);
  }
}
