package com.hongyu.reward.ui.fragment.startapp;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.fw.zycoder.utils.Log;
import com.hongyu.reward.R;
import com.hongyu.reward.appbase.PagerFragment;
import com.hongyu.reward.appbase.WelcomeFragmentPagerAdapter;
import com.hongyu.reward.ui.activity.TabHostActivity;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.circlenavigator.CircleNavigator;

import java.util.ArrayList;


/**
 * Created by zhangyang131 on 16/9/8.
 */
public class WelcomeFragment extends PagerFragment {
  WelcomeFragmentPagerAdapter adapter;
  MagicIndicator mMagicIndicator;
  CircleNavigator circleNavigator;
  ViewPager mViewPager;
  int currentItem;

  @Override
  protected FragmentPagerAdapter getPagerAdapter() {
    if (adapter == null) {
      adapter = new WelcomeFragmentPagerAdapter(this.getChildFragmentManager());
    }
    ArrayList<Fragment> list = new ArrayList<>();
    list.add(Fragment.instantiate(getActivity(), WelcomeItem1Fragment.class.getName()));
    list.add(Fragment.instantiate(getActivity(), WelcomeItem2Fragment.class.getName()));
    list.add(Fragment.instantiate(getActivity(), WelcomeItem3Fragment.class.getName()));
    adapter.setListFragments(list);
    return adapter;
  }

  @Override
  protected void onStartLoading() {}

  @Override
  protected void onInflated(View contentView, Bundle savedInstanceState) {
    super.onInflated(contentView, savedInstanceState);
    mMagicIndicator = (MagicIndicator) mContentView.findViewById(R.id.magic_indicator);
    mViewPager = (ViewPager) mContentView.findViewById(R.id.common_view_pager);
    circleNavigator = new CircleNavigator(getContext());
    circleNavigator.setCircleCount(adapter.getCount());
    circleNavigator.setCircleColor(getResources().getColor(R.color.colorPrimaryDark));
    circleNavigator.setCircleClickListener(new CircleNavigator.OnCircleClickListener() {
      @Override
      public void onClick(int index) {
        mViewPager.setCurrentItem(index);
      }
    });
    mMagicIndicator.setNavigator(circleNavigator);
    ViewPagerHelper.bind(mMagicIndicator, mViewPager);
    mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

      @Override
      public void onPageSelected(int position) {
        currentItem = position;
        Log.i("Guide", "监听改变" + position);
      }

      @Override
      public void onPageScrollStateChanged(int state) {}
    });

    mViewPager.setOnTouchListener(new View.OnTouchListener() {
      float startX;
      float startY;
      float endX;
      float endY;

      @Override
      public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
          case MotionEvent.ACTION_DOWN:
            startX = event.getX();
            startY = event.getY();
            break;
          case MotionEvent.ACTION_UP:
            endX = event.getX();
            endY = event.getY();
            WindowManager windowManager = (WindowManager) getActivity().getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);
            // 获取屏幕的宽度
            Point size = new Point();
            windowManager.getDefaultDisplay().getSize(size);
            int width = size.x;
            // 首先要确定的是，是否到了最后一页，然后判断是否向左滑动，并且滑动距离是否符合，我这里的判断距离是屏幕宽度的4分之一（这里可以适当控制）
            if (currentItem == (2) && startX - endX > 0 && startX - endX >= (width / 4)) {
              Log.i("Guide", "进入了触摸");
              TabHostActivity.launch(getActivity());
              getActivity().finish();
//              getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_in);
            }
            break;
        }
        return false;
      }
    });
  }

  @Override
  protected int getLayoutResId() {
    return R.layout.pager_welcome_layout;
  }



}
