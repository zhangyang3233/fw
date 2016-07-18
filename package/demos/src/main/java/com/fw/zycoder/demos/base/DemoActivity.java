package com.fw.zycoder.demos.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.fw.zycoder.demos.R;


/**
 * Created by zhangyang131 on 16/6/21.
 */
public abstract class DemoActivity extends BaseActivity {

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getSingleContentFragmentClass() != null) {
      Fragment f = Fragment.instantiate(this, getSingleContentFragmentClass().getName(), null);
      if (f != null) {
        replaceFragment(f);
      }
    }
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

  @Override
  public void finish() {
    super.finish();
    overridePendingTransition(R.anim.activity_slide_in_from_left,
        R.anim.activity_slide_out_to_right);
  }

  protected abstract Class<? extends Fragment> getSingleContentFragmentClass();
}
