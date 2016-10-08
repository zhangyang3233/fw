package com.hongyu.reward.ui.city;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.hongyu.reward.appbase.BaseFragment;
import com.hongyu.reward.appbase.BaseSingleFragmentActivity;
import com.hongyu.reward.ui.city.model.City;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * author zaaach on 2016/1/26.
 */
public class CityPickerActivity extends BaseSingleFragmentActivity {
  public static final int REQUEST_CODE_PICK_CITY = 2333;
  public static final String KEY_PICKED_CITY = "picked_city";

  public static void launchForResult(BaseFragment fragment, int requestCode) {
    Intent i = new Intent(fragment.getActivity(), CityPickerActivity.class);
    fragment.startActivityForResult(i, requestCode);
  }

  @Override
  protected String getTitleText() {
    return "选择城市";
  }

  @Override
  protected Class<? extends Fragment> getSingleContentFragmentClass() {
    return CityPickerFragment.class;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    EventBus.getDefault().register(this);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    EventBus.getDefault().unregister(this);
  }

  @Subscribe
  public void onEventMainThread(City city) {
    Intent data = new Intent();
    data.putExtra(KEY_PICKED_CITY, city.getName());
    setResult(RESULT_OK, data);
    finish();
  }

  @Override
  public void finish() {
    super.finish();
  }
}
