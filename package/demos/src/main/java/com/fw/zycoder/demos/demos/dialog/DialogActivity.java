package com.fw.zycoder.demos.demos.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.fw.zycoder.demos.base.DemoActivity;

/**
 * Created by zhangyang131 on 16/6/27.
 */
public class DialogActivity extends DemoActivity  {

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    DialogFragment fragment =
            (DialogFragment) Fragment.instantiate(this, DialogFragment.class.getName(), null);
    replaceFragment(fragment);
  }

  @Override
  public boolean getCanFlingBack() {
    return true;
  }
}
