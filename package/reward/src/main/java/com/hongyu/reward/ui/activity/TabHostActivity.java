package com.hongyu.reward.ui.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;

import com.fw.zycoder.utils.Log;
import com.hongyu.reward.R;
import com.hongyu.reward.location.GetLocationListener;
import com.hongyu.reward.location.LocationManager;
import com.hongyu.reward.manager.ScreenManager;
import com.hongyu.reward.model.AppLocation;
import com.hongyu.reward.model.NoticeEvent;
import com.hongyu.reward.ui.adapter.MainPagerAdapter;
import com.hongyu.reward.ui.dialog.SingleBtnDialogFragment;
import com.hongyu.reward.utils.DoubleClickUtil;
import com.hongyu.reward.utils.T;
import com.hongyu.reward.widget.AppLoadingView;
import com.hongyu.reward.widget.BottomBar;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;


/**
 * Created by zhangyang131 on 16/8/29.
 */
public class TabHostActivity extends FragmentActivity {

  private ViewPager mViewPager;
  private MainPagerAdapter mPagerAdapter;
  private BottomBar mBottomBar;
  private View receiveTab;
  private Badge mBadge;
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
//    PgyUpdateManager.register(this);
    ScreenManager.getScreenManager().pushActivity(this);
    EventBus.getDefault().register(this);
    Log.v("activity生命周期", getClass().getSimpleName() + " --> onCreate");
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
    receiveTab = mBottomBar.findViewById(R.id.receive_tab);
    mBadge = new QBadgeView(this).bindTarget(receiveTab);
    mBadge.setGravityOffset(21,0,true);
    mBadge.setBadgeBackgroundColor(getResources().getColor(R.color.red_point_color));
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
  protected void onStart() {
    super.onStart();
    Log.v("activity生命周期", getClass().getSimpleName() + " --> onStart");
  }

  @Override
  protected void onStop() {
    super.onStop();
    Log.v("activity生命周期", getClass().getSimpleName() + " --> onStop");
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    Log.v("activity生命周期", getClass().getSimpleName() + " --> onDestroy");
    ScreenManager.getScreenManager().popActivity(this);
    LocationManager.getInstance().stop();
    EventBus.getDefault().unregister(this);
  }

  // @Override
  // protected void onResume() {
  // super.onResume();
  // // 自定义摇一摇的灵敏度，默认为950，数值越小灵敏度越高。
  // PgyFeedbackShakeManager.setShakingThreshold(1000);
  //
  // // 以对话框的形式弹出
  //// PgyFeedbackShakeManager.register(this);
  //
  // // 以Activity的形式打开，这种情况下必须在AndroidManifest.xml配置FeedbackActivity
  // // 打开沉浸式,默认为false
  // // FeedbackActivity.setBarImmersive(true);
  // PgyFeedbackShakeManager.register(this, false);
  // }
  //
  // @Override
  // protected void onPause() {
  // super.onPause();
  // PgyFeedbackShakeManager.unregister();
  // }

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
    Log.v("activity生命周期", getClass().getSimpleName() + " --> onResume");
    MobclickAgent.onResume(this);
    checkLocationDialog();
  }

  public void onPause() {
    super.onPause();
    Log.v("activity生命周期", getClass().getSimpleName() + " --> onPause");
    MobclickAgent.onPause(this);
  }

  private void checkLocationDialog() {
    if (LocationManager.getInstance().getLocation() == null) {
      final AppLoadingView appLoadingView = new AppLoadingView(this);
      appLoadingView.setLoadingText("正在获取定位");
      LocationManager.getInstance().addLocationListener(new GetLocationListener() {
        @Override
        public void onSuccess(AppLocation locationInfo) {
          appLoadingView.dismiss();
        }

        @Override
        public void onFailed(String msg) {
          appLoadingView.dismiss();
          showGetLocationError();
        }
      });
      appLoadingView.show();
    }
  }

  private void showGetLocationError() {
    SingleBtnDialogFragment dialog = new SingleBtnDialogFragment();
    dialog.setContent("获取定位失败。");
    dialog.setBtn("重新获取", new SingleBtnDialogFragment.OnClickListener() {
      @Override
      public void onClick(Dialog dialog) {
        dialog.dismiss();
        checkLocationDialog();
      }
    });
    dialog.show(getSupportFragmentManager(), getClass().getSimpleName());
  }


  @Subscribe
  public void onEventMainThread(NoticeEvent noticeEvent) {
    if (noticeEvent.getType() == NoticeEvent.NEW_ORDER_CREATE_CLICK) {
      switchPage(2);
      EventBus.getDefault().post(new NoticeEvent(NoticeEvent.TAB2_NEED_FRESH));
    } else if (noticeEvent.getType() == NoticeEvent.NEW_ORDER) {
      receiveCount++;
      // if(receiveCount == 0){
      // mBadge.hide(true);
      // }else{
      // mBadge.hide(false);
      mBadge.setBadgeNumber(receiveCount);
      redPointNotify(receiveCount);
      // }
    } else if (noticeEvent.getType() == NoticeEvent.NEW_ORDER_CLEAR) {
      receiveCount = 0;
      mBadge.setBadgeNumber(receiveCount);
      redPointNotify(receiveCount);
    }
  }

  private void redPointNotify(int newPointCount){
    String data = null;
    if(newPointCount>99){
      data = "99+";
    }else{
      data = String.valueOf(newPointCount);
    }
    EventBus.getDefault().post(new NoticeEvent(NoticeEvent.RED_POINT_NOTIFY, data));
  }

  int receiveCount = 0;
}
