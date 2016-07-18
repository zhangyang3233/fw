package com.zycoder.uicomp.horizontallistview.util.v14;

import android.view.View;

import com.zycoder.uicomp.horizontallistview.util.ViewHelperFactory.ViewHelperDefault;

public class ViewHelper14 extends ViewHelperDefault {

  public ViewHelper14(View view) {
    super(view);
  }

  @Override
  public void setScrollX(int value) {
    view.setScrollX(value);
  }

  @Override
  public boolean isHardwareAccelerated() {
    return view.isHardwareAccelerated();
  }

}
