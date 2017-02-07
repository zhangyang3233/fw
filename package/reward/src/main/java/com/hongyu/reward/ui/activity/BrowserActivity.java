package com.hongyu.reward.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.hongyu.reward.appbase.BaseSingleFragmentActivity;
import com.hongyu.reward.ui.fragment.BrowserFragment;

/**
 * Created by zhangyang131 on 16/9/11.
 */
public class BrowserActivity extends BaseSingleFragmentActivity {
  public static final String URL = "url";
  public static final String TITLE = "title";
  private String title;

  public static void launch(Context context, String url) {
    launch(context, url, null);
  }

  public static void launch(Context context, String url, String title) {
    final Intent intent = new Intent(context, BrowserActivity.class);
    if (!(context instanceof Activity)) {
      intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }
    if (!TextUtils.isEmpty(url)) {
      intent.putExtra(URL, url);
    }
    if (!TextUtils.isEmpty(title)) {
      intent.putExtra(TITLE, title);
    }
    context.startActivity(intent);
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.title = getIntent().getStringExtra(TITLE);
  }

  @Override
  public boolean getCanFlingBack() {
    return true;
  }

  @Override
  protected Class<? extends Fragment> getSingleContentFragmentClass() {
    return BrowserFragment.class;
  }

  @Override
  protected String getTitleText() {
    return title;
  }

  @Override
  public void onBackPressed() {
    if (mFragment != null && mFragment.isAdded()) {
      if (!((BrowserFragment) mFragment).onBackPressed()) {
        super.onBackPressed();
      }
    } else {
      super.onBackPressed();
    }
  }
}