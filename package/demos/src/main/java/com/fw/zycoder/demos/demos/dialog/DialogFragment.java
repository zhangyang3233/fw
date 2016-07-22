package com.fw.zycoder.demos.demos.dialog;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.fw.zycoder.appbase.fragment.BaseFragment;
import com.fw.zycoder.demos.R;

/**
 * Created by zhangyang131 on 16/7/18.
 */
public class DialogFragment extends BaseFragment implements View.OnClickListener {
  Button btn;

  @Override
  protected void onInflated(View contentView, Bundle savedInstanceState) {
    initViews();
  }

  private void initViews() {
    btn = (Button) mContentView.findViewById(R.id.btn);
    btn.setOnClickListener(this);
  }

  @Override
  protected int getLayoutResId() {
    return R.layout.activity_button;
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.btn:
        showDialogFragment();
        break;
    }
  }

  private void showDialogFragment() {
    MyDialogFragment dialog = new MyDialogFragment();
    dialog.show(getChildFragmentManager(), null);
  }
}
