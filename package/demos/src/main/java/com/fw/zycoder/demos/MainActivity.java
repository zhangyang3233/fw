package com.fw.zycoder.demos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.fw.zycoder.demos.base.DemoActivity;
import com.fw.zycoder.demos.config.Consts;

public class MainActivity extends DemoActivity {
  private boolean mCanFlingBack;

  public static void luanch(Context context){
    Intent i = new Intent(context, MainActivity.class);
    context.startActivity(i);
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    initFlingBack();
    super.onCreate(savedInstanceState);
  }

  @Override
  protected Class<? extends Fragment> getSingleContentFragmentClass() {
    return MainFragment.class;
  }

  @Override
  public boolean getCanFlingBack() {
    return mCanFlingBack;
  }

  private void initFlingBack() {
    String prefix = getIntent().getStringExtra(Consts.ACTIVITY_INFO);
    if (prefix == null || prefix.equals("")) {
      mCanFlingBack = false;
    } else {
      mCanFlingBack = true;
    }
  }
}
