package com.hongyu.reward.ui.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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
import com.hongyu.reward.ui.dialog.CommonTwoBtnDialogFragment;
import com.hongyu.reward.widget.BottomBar;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.ArrayList;


/**
 * Created by zhangyang131 on 16/8/29.
 */
public class TabHostActivity extends FragmentActivity
    implements
      LogoutListener,
      GetLocationListener {
  private final int REQUEST_CODE = 127;
  int showDialogCount;
  private ViewPager mViewPager;
  private MainPagerAdapter mPagerAdapter;
  private BottomBar mBottomBar;
  private DrawerLayout mDrawerLayout;
  private Handler handler = new Handler();

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
    AccountManager.getInstance().addLogoutListener(this);
    getPersimmions();
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
    getLocation();
  }

  private void getLocation() {
    if (LocationManager.getInstance().getLocalLocationInfo() != null) { // 已经获取到位置信息
      setView();
    } else { // 没有获取到位置信息
      LocationManager.getInstance().addLocationListener(this);
      LocationManager.getInstance().start();
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
    handler = null;
  }

  @TargetApi(23)
  private void getPersimmions() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      ArrayList<String> permissions = new ArrayList<String>();
      /***
       * 定位权限为必须权限，用户如果禁止，则每次进入都会申请
       */
      // 定位精确位置
      if (checkSelfPermission(
          Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
      }
      if (checkSelfPermission(
          Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
      }
      /*
       * 读写权限和电话状态权限非必要权限(建议授予)只会申请一次，用户同意或者禁止，只会弹一次
       */
      // 读写权限
      addPermission(permissions, Manifest.permission.WRITE_EXTERNAL_STORAGE);
      // 读取电话状态权限
      addPermission(permissions, Manifest.permission.READ_PHONE_STATE);

      if (permissions.size() > 0) {
        requestPermissions(permissions.toArray(new String[permissions.size()]), REQUEST_CODE);
      }
    }
  }

  @TargetApi(23)
  private boolean addPermission(ArrayList<String> permissionsList, String permission) {
    if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) { // 如果应用没有获得对应权限,则添加到列表中,准备批量申请
      if (shouldShowRequestPermissionRationale(permission)) {
        return true;
      } else {
        permissionsList.add(permission);
        return false;
      }

    } else {
      return true;
    }
  }

  @TargetApi(23)
  @Override
  public void onRequestPermissionsResult(int requestCode, String[] permissions,
      int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (requestCode == REQUEST_CODE) {
      int i;
      for (i = 0; i < permissions.length; i++) {
        if (Manifest.permission.ACCESS_FINE_LOCATION.equals(permissions[i])
            && grantResults[i] == PackageManager.PERMISSION_DENIED) { // 用户拒绝
          handler.postDelayed(new Runnable() {
            @Override
            public void run() {
              showPermissionDeniedDialog();
            }
          }, 100);
          return;
        }
      }
    }
  }

  private void showPermissionDeniedDialog() {
    if (showDialogCount > 0) {
      CommonTwoBtnDialogFragment dialog = new CommonTwoBtnDialogFragment();
      dialog.setContent("没有获取到系统位置信息授权,无法定位您当前位置,是否重新获取?");
      dialog.setLeft("取消", new CommonTwoBtnDialogFragment.OnClickListener() {
        @Override
        public void onClick(Dialog dialog) {
          System.exit(0);
        }
      });
      dialog.setRight("重新获取", new CommonTwoBtnDialogFragment.OnClickListener() {
        @Override
        public void onClick(Dialog dialog) {
          dialog.dismiss();
          getPersimmions();
        }
      });
      dialog.show(getSupportFragmentManager(), getClass().getSimpleName());
    } else {
      CommonTwoBtnDialogFragment dialog = new CommonTwoBtnDialogFragment();
      dialog.setContent("是否已禁用获取位置权限?\n请到设置页面允许");
      dialog.setLeft("取消", new CommonTwoBtnDialogFragment.OnClickListener() {
        @Override
        public void onClick(Dialog dialog) {
          System.exit(0);
        }
      });
      dialog.setRight("重新获取", new CommonTwoBtnDialogFragment.OnClickListener() {
        @Override
        public void onClick(Dialog dialog) {
          dialog.dismiss();
          getPersimmions();
        }
      });
      dialog.show(getSupportFragmentManager(), getClass().getSimpleName());
    }
    showDialogCount++;

  }

  @Override
  public void onSuccess(AppLocation locationInfo) {
    LocationManager.getInstance().stop();
    setView();
  }

  @Override
  public void onFailed(String msg) {}


}
