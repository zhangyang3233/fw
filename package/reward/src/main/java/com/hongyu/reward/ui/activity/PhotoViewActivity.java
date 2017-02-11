package com.hongyu.reward.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.hongyu.reward.appbase.BaseFragment;
import com.hongyu.reward.appbase.BaseSlideActivity;
import com.hongyu.reward.ui.fragment.PhotoViewFragment;

/**
 * Created by zhangyang131 on 2017/2/10.
 */

public class PhotoViewActivity extends BaseSlideActivity {
  public static final String IMG_URL = "image_url";

  public static void launch(Context context, String img_url) {
    Intent i = new Intent(context, PhotoViewActivity.class);
    i.putExtra(IMG_URL, img_url);
    context.startActivity(i);
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mFragment = (BaseFragment) Fragment.instantiate(this, PhotoViewFragment.class.getName(),
        getIntent().getExtras());
    if (mFragment != null) {
      replaceFragment(mFragment);
    }
  }

}
