package com.hongyu.reward.manager;

import android.app.Activity;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;

import com.hongyu.reward.model.PushModel;
import com.hongyu.reward.ui.activity.OrderDetailActivity;
import com.hongyu.reward.ui.activity.TabHostActivity;
import com.hongyu.reward.ui.activity.order.ReceiveOrderFinishedActivity;
import com.hongyu.reward.ui.activity.order.ReceiveWaitActivity;
import com.hongyu.reward.ui.activity.order.RewardPublishWaitActivity;
import com.hongyu.reward.ui.activity.order.SelectPersonActivity;
import com.hongyu.reward.ui.dialog.SingleBtnDialogFragment;

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
    dialog.setContent("您发布的悬赏有人申请领赏了");
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
    showDialog(dialog);
  }

  /**
   * 领取的任务被悬赏人拒绝了
   * 
   * @param pushInfo
   */
  public static void orderIsRefuse(final PushModel.PushInfo pushInfo) {
    final Activity activity = ScreenManager.getScreenManager().currentActivity();
    SingleBtnDialogFragment dialog = new SingleBtnDialogFragment();
    dialog.setContent("抱歉,悬赏人没有选择您的领赏申请");
    dialog.setBtn("好吧", new SingleBtnDialogFragment.OnClickListener(){

      @Override
      public void onClick(Dialog dialog) {
        dialog.dismiss();
        if (activity instanceof OrderDetailActivity) {
          activity.finish();
        }
      }
    });
    showDialog(dialog);
  }

  /**
   * 悬赏人选择了你的订单请求
   * 
   * @param pushInfo
   */
  public static void orderReceiveSuccess(final PushModel.PushInfo pushInfo) {
    final Activity activity = ScreenManager.getScreenManager().currentActivity();

    SingleBtnDialogFragment dialog = new SingleBtnDialogFragment();
    dialog.setContent("恭喜,悬赏人接受了您的领赏申请");
    dialog.setBtn("查看", new SingleBtnDialogFragment.OnClickListener(){

      @Override
      public void onClick(Dialog dialog) {
        dialog.dismiss();
        ReceiveWaitActivity.launch(activity, pushInfo.getOrder_id());
        if (activity instanceof OrderDetailActivity) {
          activity.finish();
        }
      }
    });
    showDialog(dialog);
  }

  /**
   * 悬赏人取消了任务
   *
   * @param pushInfo
   */
  public static void orderCanceled(PushModel.PushInfo pushInfo) {
    final Activity activity = ScreenManager.getScreenManager().currentActivity();
    SingleBtnDialogFragment dialog = new SingleBtnDialogFragment();
    dialog.setContent("抱歉,悬赏人取消了任务");
    dialog.setCancelable(false);
    dialog.setBtn("好吧", new SingleBtnDialogFragment.OnClickListener() {
      @Override
      public void onClick(Dialog dialog) {
        dialog.dismiss();
        if (activity instanceof ReceiveWaitActivity) {
          activity.finish();
        }
      }
    });
    showDialog(dialog);
  }


  /**
   * 订单已经完成
   * 
   * @param pushInfo
   */
  public static void orderFinished(final PushModel.PushInfo pushInfo) {
    final Activity activity = ScreenManager.getScreenManager().currentActivity();
    SingleBtnDialogFragment dialog = new SingleBtnDialogFragment();
    dialog.setContent("悬赏人已支付赏金");
    dialog.setCancelable(false);
    dialog.setBtn("查看", new SingleBtnDialogFragment.OnClickListener() {
      @Override
      public void onClick(Dialog dialog) {
        dialog.dismiss();
        if (!(activity instanceof ReceiveOrderFinishedActivity)) {
          // 别人发起的订单
          ReceiveOrderFinishedActivity.launch(activity, pushInfo.getOrder_id());
        }

        if(activity instanceof ReceiveWaitActivity){
          activity.finish();
        }
      }
    });
    showDialog(dialog);
  }



  private static void showDialog(DialogFragment dialog){
    final Activity activity = ScreenManager.getScreenManager().currentActivity();
    if (activity instanceof TabHostActivity) {
      dialog.show(((TabHostActivity) activity).getSupportFragmentManager(),
              activity.getClass().getSimpleName());
    } else if (activity instanceof AppCompatActivity) {
      dialog.show(((AppCompatActivity) activity).getSupportFragmentManager(),
              activity.getClass().getSimpleName());
    }
  }
}
