package com.hongyu.reward.ui.welcome;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;

import com.hongyu.reward.appbase.PagerFragment;
import com.hongyu.reward.appbase.WelcomeFragmentPagerAdapter;

import java.util.ArrayList;


/**
 * Created by zhangyang131 on 16/9/8.
 */
public class WelcomeFragment extends PagerFragment {
  WelcomeFragmentPagerAdapter adapter;

  @Override
  protected FragmentPagerAdapter getPagerAdapter() {
    if (adapter == null) {
      adapter = new WelcomeFragmentPagerAdapter(this.getChildFragmentManager());
    }
    ArrayList<Fragment> list = new ArrayList<>();
    list.add(Fragment.instantiate(getActivity(), WelcomeItem1Fragment.class.getName()));
    list.add(Fragment.instantiate(getActivity(), WelcomeItem2Fragment.class.getName()));
    list.add(Fragment.instantiate(getActivity(), WelcomeItem3Fragment.class.getName()));
    list.add(Fragment.instantiate(getActivity(), WelcomeItem4Fragment.class.getName()));
    adapter.setListFragments(list);
    return adapter;
  }

  @Override
  protected void onStartLoading() {}
}
