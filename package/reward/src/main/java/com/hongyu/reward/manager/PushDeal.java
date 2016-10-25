package com.hongyu.reward.manager;

import android.app.Activity;
import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;

import com.hongyu.reward.appbase.BaseFragment;
import com.hongyu.reward.appbase.BaseSingleFragmentActivity;
import com.hongyu.reward.model.PushModel;
import com.hongyu.reward.ui.activity.OrderDetailActivity;
import com.hongyu.reward.ui.activity.TabHostActivity;
import com.hongyu.reward.ui.activity.order.ReceiveOrderFinishedActivity;
import com.hongyu.reward.ui.activity.order.ReceiveWaitActivity;
import com.hongyu.reward.ui.activity.order.RewardPublishWaitActivity;
import com.hongyu.reward.ui.activity.order.SelectPersonActivity;
import com.hongyu.reward.ui.dialog.SingleBtnDialogFragment;
import com.hongyu.reward.utils.T;

/**
 * Created by zhangyang131 on 16/10/11.
 */
public class PushDeal {

  /**
   * 订单已经被人领取
   * 
   * @param pushInfo
   */
  public static void orderIsReceived(final PushModel.PushInfo pushInfo) {
    final Activity activity = ScreenManager.getScreenManager().currentActivity();
    SingleBtnDialogFragment dialog = new SingleBtnDialogFragment();
    dialog.setContent(pushInfo.getContent());
    dialog.setCancelable(false);
    dialog.setBtn("查看", new SingleBtnDialogFragment.OnClickListener() {
      @Override
      public void onClick(Dialog dialog) {
        dialog.dismiss();
        SelectPersonActivity.launch(activity, pushInfo.getOrder_id(), null, null);
        if (activity instanceof RewardPublishWaitActivity) {
          activity.finish();
        }
      }
    });
    if (activity instanceof TabHostActivity) {
      dialog.show(((TabHostActivity) activity).getSupportFragmentManager(),
          activity.getClass().getSimpleName());
    } else if (activity instanceof AppCompatActivity) {
      dialog.show(((AppCompatActivity) activity).getSupportFragmentManager(),
          activity.getClass().getSimpleName());
    }

  }

  /**
   * 领取的任务被悬赏人拒绝了
   * 
   * @param pushInfo
   */
  public static void orderIsRefuse(final PushModel.PushInfo pushInfo) {
    final Activity activity = ScreenManager.getScreenManager().currentActivity();
    if (activity instanceof OrderDetailActivity) {
      activity.finish();
    }
    T.show(pushInfo.getTitle());
  }

  /**
   * 悬赏人选择了你的订单请求
   * 
   * @param pushInfo
   */
  public static void orderReceiveSuccess(PushModel.PushInfo pushInfo) {
    final Activity activity = ScreenManager.getScreenManager().currentActivity();
    ReceiveWaitActivity.launch(activity, pushInfo.getOrder_id());
    T.show("悬赏人选择了您的订单");
    if (activity instanceof OrderDetailActivity) {
      activity.finish();
    }
  }

  /**
   * 悬赏人取消了任务
   *
   * @param pushInfo
   */
  public static void orderCanceled(PushModel.PushInfo pushInfo) {
    final Activity activity = ScreenManager.getScreenManager().currentActivity();
    SingleBtnDialogFragment dialog = new SingleBtnDialogFragment();
    dialog.setContent(pushInfo.getTitle());
    dialog.setCancelable(false);
    dialog.setBtn("确定", new SingleBtnDialogFragment.OnClickListener() {
      @Override
      public void onClick(Dialog dialog) {
        dialog.dismiss();
        if (activity instanceof ReceiveWaitActivity) {
          activity.finish();
        }
      }
    });
    if (activity instanceof TabHostActivity) {
      dialog.show(((TabHostActivity) activity).getSupportFragmentManager(),
              activity.getClass().getSimpleName());
    } else if (activity instanceof AppCompatActivity) {
      dialog.show(((AppCompatActivity) activity).getSupportFragmentManager(),
              activity.getClass().getSimpleName());
    }

  }

  /**
   * 订单已经完成
   * 
   * @param pushInfo
   */
  public static void orderFinished(PushModel.PushInfo pushInfo) {
    final Activity activity = ScreenManager.getScreenManager().currentActivity();
    // 别人发起的订单
    ReceiveOrderFinishedActivity.launch(activity, pushInfo.getOrder_id());
  }
}
