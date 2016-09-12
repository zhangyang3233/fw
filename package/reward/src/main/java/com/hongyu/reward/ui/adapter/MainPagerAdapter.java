package com.hongyu.reward.ui.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;

import com.hongyu.reward.appbase.BaseFragmentPagerAdapter;
import com.hongyu.reward.ui.fragment.EmptyFragment;
import com.hongyu.reward.ui.fragment.FragmentMainTabHome;
import com.hongyu.reward.ui.fragment.FragmentMainTabMy;
import com.hongyu.reward.ui.fragment.FragmentMainTabPublish;
import com.hongyu.reward.ui.fragment.FragmentMainTabReceive;

public class MainPagerAdapter extends BaseFragmentPagerAdapter {
  private Bundle mExtras;

  public MainPagerAdapter(FragmentManager fm) {
    super(fm);
  }

  public void setExtras(Bundle extras) {
    mExtras = extras;
  }

  @Override
  public Fragment getItem(int position) {
    Bundle bundle = mExtras;
    Fragment fragment = null;
    switch (position) {
      case 0:
        fragment = new FragmentMainTabHome();
        break;
      case 1:
        fragment = new FragmentMainTabPublish();
        break;
      case 2:
        fragment = new FragmentMainTabReceive();
        break;
      case 3:
        fragment = new FragmentMainTabMy();
        break;
      default:
        fragment = EmptyFragment.newInstance();
    }
    fragment.setArguments(bundle);
    return fragment;
  }

  @Override
  public int getCount() {
    return 4;
  }


  @Override
  public int getItemPosition(Object object) {
    return PagerAdapter.POSITION_NONE;
  }
}
