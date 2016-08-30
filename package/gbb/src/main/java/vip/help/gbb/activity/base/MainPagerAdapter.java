package vip.help.gbb.activity.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;

import vip.help.gbb.host.fragment.Tab1Fragment;
import vip.help.gbb.host.fragment.Tab2Fragment;
import vip.help.gbb.host.fragment.Tab3Fragment;
import vip.help.gbb.host.fragment.Tab4Fragment;

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
        fragment = new Tab1Fragment();
        break;
      case 1:
        fragment = new Tab2Fragment();
        break;
      case 2:
        fragment = new Tab3Fragment();
        break;
      case 3:
        fragment = new Tab4Fragment();
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
