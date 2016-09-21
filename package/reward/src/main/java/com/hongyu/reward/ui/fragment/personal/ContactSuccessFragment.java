package com.hongyu.reward.ui.fragment.personal;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.hongyu.reward.R;
import com.hongyu.reward.appbase.BaseLoadFragment;

/**
 * Created by zhangyang131 on 16/9/21.
 */
public class ContactSuccessFragment extends BaseLoadFragment implements View.OnClickListener {
  Button sure;

  @Override
  protected void onStartLoading() {

  }

  @Override
  protected void onInflated(View contentView, Bundle savedInstanceState) {
    sure = (Button) mContentView.findViewById(R.id.sure);
    sure.setOnClickListener(this);
  }

  @Override
  protected int getLayoutResId() {
    return R.layout.activity_contact_success_layout;
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.sure:
        getActivity().finish();
        break;
    }
  }
}
