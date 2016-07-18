package com.wanda.uicomp.horizontallistview.util.v16;

import android.view.View;

import com.wanda.uicomp.horizontallistview.util.v14.ViewHelper14;

public class ViewHelper16 extends ViewHelper14 {
  public ViewHelper16(View view) {
    super(view);
  }

  @Override
  public void postOnAnimation(Runnable action) {
    view.postOnAnimation(action);
  }
}
