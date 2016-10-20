package com.hongyu.reward.ui.activity.order;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.hongyu.reward.appbase.BaseSingleFragmentActivity;
import com.hongyu.reward.manager.AccountManager;
import com.hongyu.reward.ui.fragment.order.PublishFinishedCommentFragment;

/**
 * Created by zhangyang131 on 16/10/3.
 */
public class PublishFinishedCommentActivity extends BaseSingleFragmentActivity {
  public static final String ORDER_ID = "order_id";

  public static void launch(Context context, String order_id /**, String nickName, String gcr,
   String good, String score, String headImg**/) {
    Intent i = new Intent(context, PublishFinishedCommentActivity.class);
    i.putExtra(ORDER_ID, order_id);
    AccountManager.launchAfterLogin(context, i);
  }

  @Override
  protected Class<? extends Fragment> getSingleContentFragmentClass() {
    return PublishFinishedCommentFragment.class;
  }

  @Override
  protected String getTitleText() {
    return "给ta评价";
  }
}
