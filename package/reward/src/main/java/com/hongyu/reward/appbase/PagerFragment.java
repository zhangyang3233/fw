//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.hongyu.reward.appbase;

import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;

import com.hongyu.reward.R;


public abstract class PagerFragment extends BaseLoadFragment {
  protected ViewPager mViewPager;
  protected FragmentPagerAdapter mPagerAdapter;
  private int mCurrentFragmentIndex = 0;

  public PagerFragment() {}

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  protected int getLayoutResId() {
    return R.layout.common_pager_layout;
  }


  protected ViewPager getViewPager() {
    return mViewPager;
  }

  protected abstract FragmentPagerAdapter getPagerAdapter();

  protected void onPrepareLoading() {
    super.onPrepareLoading();
  }


  protected void onInflated(View contentView, Bundle savedInstanceState) {
    if (this.getViewPager() != null) {
      this.mViewPager = this.getViewPager();
    } else {
      this.mViewPager = (ViewPager) contentView.findViewById(R.id.common_view_pager);
    }

    this.mPagerAdapter = this.getPagerAdapter();
    this.mViewPager.setAdapter(this.mPagerAdapter);
    this.mViewPager.setCurrentItem(this.mCurrentFragmentIndex);
  }


  public void setOffScreenPageLimit(int limit) {
    this.mViewPager.setOffscreenPageLimit(limit);
  }

  public void setOnPageChangeListener(OnPageChangeListener listener) {
    if (listener != null) {
      this.mViewPager.addOnPageChangeListener(listener);
    }
  }

}
