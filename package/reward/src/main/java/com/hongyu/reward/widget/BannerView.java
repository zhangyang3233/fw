package com.hongyu.reward.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.hongyu.reward.R;
import com.hongyu.reward.ui.adapter.BannerPagerAdapter;

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
    adapter = new BannerPagerAdapter(getContext());
    LayoutInflater inflater = LayoutInflater.from(mContext);
    mView = (ViewGroup) inflater.inflate(R.layout.widget_banner_layout, null);
    mViewPager = (ViewPager) mView.findViewById(R.id.viewPager);
    indicator = (CircleIndicator) mView.findViewById(R.id.indicator);
    indicator.setViewPager(mViewPager);
    mViewPager.setAdapter(adapter);
    mViewPager.addOnPageChangeListener(this);
    addView(mView);
  }

  @Override
  public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

  }

  @Override
  public void onPageSelected(int position) {
    if (position == 0) {
      mViewPager.setCurrentItem(adapter.getCount() + 1, false);
    }
  }

  @Override
  public void onPageScrollStateChanged(int state) {

  }
}
