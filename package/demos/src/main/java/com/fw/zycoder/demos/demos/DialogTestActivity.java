package com.fw.zycoder.demos.demos;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.fw.zycoder.demos.R;
import com.fw.zycoder.demos.base.DemoActivity;

/**
 * Created by zhangyang131 on 16/6/21.
 */
public class DialogTestActivity extends DemoActivity implements View.OnClickListener {
  TextView tv;
  Toast t;
  int aaa=0;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_test);
    tv = (TextView) findViewById(R.id.tv);

    tv.setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {
    if(t != null){
      t.cancel();
    }
    t = Toast.makeText(this,String.valueOf(aaa++),Toast.LENGTH_LONG);
    t.show();


  }
}
