package com.hongyu.reward.ui.activity.order;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.hongyu.reward.appbase.BaseSingleFragmentActivity;
import com.hongyu.reward.ui.fragment.order.CommentFragment;

/**
 * Created by zhangyang131 on 16/10/3.
 */
public class CommentActivity extends BaseSingleFragmentActivity {
  public static final String ORDER_ID = "order_id";
//  public static final String NICKNAME = "nickname";
//  public static final String GCR = "gcr";
//  public static final String GOOD = "good";
//  public static final String SCORE = "score";
//  public static final String HEAD_IMG = "headImg";

  public static void launch(Context context, String order_id /**, String nickName, String gcr,
   String good, String score, String headImg**/) {
    Intent i = new Intent(context, CommentActivity.class);
    i.putExtra(ORDER_ID, order_id);
//    i.putExtra(NICKNAME, nickName);
//    i.putExtra(GCR, gcr);
//    i.putExtra(GOOD, good);
//    i.putExtra(SCORE, score);
//    i.putExtra(HEAD_IMG, headImg);
    context.startActivity(i);
  }

  @Override
  protected Class<? extends Fragment> getSingleContentFragmentClass() {
    return CommentFragment.class;
  }

  @Override
  protected String getTitleText() {
    return "给ta评价";
  }
}
