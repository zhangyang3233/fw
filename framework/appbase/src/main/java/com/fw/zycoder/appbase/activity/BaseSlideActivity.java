package com.fw.zycoder.appbase.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.fw.zycoder.appbase.R;
import com.zycoder.sliding.SlidingHelper;
import com.zycoder.sliding.component.SlideActivity;

/**
 * @author zhangyang
 */
public class BaseSlideActivity extends AppCompatActivity implements SlideActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(getLayoutId());
    SlidingHelper.onCreate(this);
  }

  @Override
  public void onWindowFocusChanged(boolean hasFocus) {
    super.onWindowFocusChanged(hasFocus);
    SlidingHelper.onWindowFocusChanged(this, hasFocus);
  }

  @Override
  protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    SlidingHelper.onNewIntent(this);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    SlidingHelper.onDestroy(this);
  }

  @Override
  public void finish() {
    super.finish();
    SlidingHelper.finish(this);
  }


  /**
   * get layout id .
   * notice: id : fragment_container , sliding_pane_layout is need in layout
   */
  protected int getLayoutId() {
    return R.layout.base_fragment_activity;
  }

  /**
   * @return this activity is need right fling close
   */
  @Override
  public boolean getCanFlingBack() {
    return false;
  }

  /**
   * @return under this activity is need relative move
   */
  @Override
  public boolean getCanRelativeMove() {
    return false;
  }

}
