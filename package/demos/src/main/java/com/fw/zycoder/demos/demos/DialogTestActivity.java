package com.fw.zycoder.demos.demos;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.fw.zycoder.demos.R;
import com.fw.zycoder.demos.base.DemoActivity;

/**
 * Created by zhangyang131 on 16/6/21.
 */
public class DialogTestActivity extends DemoActivity {
  TextView tv;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_test);
    tv = (TextView) findViewById(R.id.tv);
  }

}
