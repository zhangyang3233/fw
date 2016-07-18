package com.fw.zycoder.demos.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by zhangyang131 on 16/6/21.
 */
public class DemoActivity extends BaseActivity {
  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  protected String getTitleText() {
    String[] s = getTitle().toString().split("/");
    return s[s.length - 1];
  }

  @Override
  public boolean getCanFlingBack() {
    return true;
  }

}
