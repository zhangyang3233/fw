package com.fw.zycoder.demos.demos;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.fw.zycoder.demos.R;
import com.fw.zycoder.demos.base.DemoActivity;

/**
 * Created by zhangyang131 on 16/6/22.
 */
public class ToastTestActivity extends DemoActivity {

  Handler mHandler;
  Button showToast1;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_toast);

    mHandler = new Handler();

    showToast1 = (Button) findViewById(R.id.showToast1);

    /**
     * 默认Toast
     */
    showToast1.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
      }
    });
  }

}
