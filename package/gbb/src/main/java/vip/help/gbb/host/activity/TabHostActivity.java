package vip.help.gbb.host.activity;

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

import com.readystatesoftware.systembartint.SystemBarTintManager;

import vip.help.gbb.R;
import vip.help.gbb.activity.base.MainPagerAdapter;
import vip.help.gbb.widget.BottomBar;

/**
 * Created by zhangyang131 on 16/8/29.
 */
public class TabHostActivity extends FragmentActivity {
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
    mPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
    mViewPager.setAdapter(mPagerAdapter);
    initDrawerView();
  }

  private void initDrawerView() {
    mDrawerLayout = (DrawerLayout) findViewById(R.id.id_drawer_layout);
    mDrawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
      @Override
      public void onDrawerSlide(View drawerView, float slideOffset) {
      }

      @Override
      public void onDrawerOpened(View drawerView) {
      }

      @Override
      public void onDrawerClosed(View drawerView) {
      }

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
}
