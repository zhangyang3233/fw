package com.hongyu.reward.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.hongyu.reward.R;
import com.hongyu.reward.model.AdModel;
import com.hongyu.reward.ui.adapter.BannerPagerAdapter;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;

/**
 * Created by zhangyang131 on 16/9/10.
 */
public class BannerView extends FrameLayout implements ViewPager.OnPageChangeListener {
  BannerPagerAdapter adapter;

  public BannerView(Context context) {
    super(context);
    mContext = context;
    initView();
  }

  public BannerView(Context context, AttributeSet attrs) {
    super(context, attrs);
    mContext = context;
    initView();
  }

  private Context mContext;
  private ViewGroup mView;
  private ViewPager mViewPager;
  private CircleIndicator indicator;

  public void initView() {
    initAdapter();
    LayoutInflater inflater = LayoutInflater.from(mContext);
    mView = (ViewGroup) inflater.inflate(R.layout.widget_banner_layout, null);
    mViewPager = (ViewPager) mView.findViewById(R.id.viewPager);
    indicator = (CircleIndicator) mView.findViewById(R.id.indicator);
    indicator.setViewPager(mViewPager);
    mViewPager.setAdapter(adapter);
    addView(mView);
  }


  private void initAdapter() {
    if (adapter == null) {
      adapter = new BannerPagerAdapter(getContext());
      adapter.setOnItemClickListener(adapter.getListener());
    }
  }

  public void setData(ArrayList<AdModel> list) {
    initAdapter();
    adapter.setData(list);
  }

  @Override
  public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

  }

  @Override
  public void onPageSelected(int position) {

  }

  @Override
  public void onPageScrollStateChanged(int state) {
    if (state == 0) {
      int index = mViewPager.getCurrentItem();
      if (index == 0 && adapter.getCount() >= 4) {
        mViewPager.setCurrentItem(adapter.getCount() - 2, false);
      } else if (index == adapter.getCount() - 1) {
        mViewPager.setCurrentItem(1, false);
      }
    }
  }
}
