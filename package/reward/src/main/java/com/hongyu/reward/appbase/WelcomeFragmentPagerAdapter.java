package com.hongyu.reward.appbase;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.ArrayList;

/**
 * Created by zhangyang131 on 16/9/8.
 */
public class WelcomeFragmentPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {
    private ArrayList<Fragment> listFragments;

    public WelcomeFragmentPagerAdapter(FragmentManager fm ) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return listFragments.get(position);
    }

    @Override
    public int getCount() {
        return listFragments == null ? 0 : listFragments.size();
    }

    public ArrayList<Fragment> getListFragments() {
        return listFragments;
    }

    public void setListFragments(ArrayList<Fragment> listFragments) {
        this.listFragments = listFragments;
        notifyDataSetChanged();
    }
}
