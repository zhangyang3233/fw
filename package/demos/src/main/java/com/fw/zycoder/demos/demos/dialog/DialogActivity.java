package com.fw.zycoder.demos.demos.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;

import com.fw.zycoder.demos.R;
import com.fw.zycoder.demos.base.DemoActivity;

/**
 * Created by zhangyang131 on 16/6/27.
 */
public class DialogActivity extends DemoActivity implements View.OnClickListener {
  Button btn;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_button);
    initViews();
  }

  private void initViews() {
    btn = (Button) findViewById(R.id.btn);
    btn.setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.btn:
        showDialogFragment();
        break;
    }
  }

  private void showDialog() {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setMessage("aaa");
    Dialog d = builder.create();
    d.getWindow().setWindowAnimations(R.style.PopupAnimation);
    d.show();
  }

  private void showDialogFragment() {
    MyDialogFragment dialog = new MyDialogFragment();
    dialog.show(getSupportFragmentManager(), null);
  }


}
