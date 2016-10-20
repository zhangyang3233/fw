package com.hongyu.reward.ui.activity.personal;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.hongyu.reward.R;
import com.hongyu.reward.appbase.BaseSingleFragmentActivity;
import com.hongyu.reward.manager.AccountManager;
import com.hongyu.reward.ui.fragment.personal.MessageListFragment;

/**
 * Created by zhangyang131 on 16/9/21.
 */
public class MessageListActivity extends BaseSingleFragmentActivity {

  public static void launch(Context context) {
    Intent i = new Intent(context, MessageListActivity.class);
    AccountManager.launchAfterLogin(context, i);
  }

  @Override
  protected Class<? extends Fragment> getSingleContentFragmentClass() {
    return MessageListFragment.class;
  }

  @Override
  protected String getTitleText() {
    return getString(R.string.msg_center);
  }

  @Override
  public boolean getCanFlingBack() {
    return true;
  }
}
