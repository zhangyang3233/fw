package com.fw.zycoder.demos;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.fw.zycoder.utils.BadgeUtil;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);


    TextView tv = (TextView) findViewById(R.id.tv);

    tv.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        BadgeUtil.setBadgeCount(MainActivity.this, 1);
      }
    });

    BadgeUtil.sendToHuawei(this, 1);
  }
}
