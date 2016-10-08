package com.hongyu.reward.utils;

import com.fw.zycoder.utils.GlobalConfig;
import com.hongyu.reward.R;
import com.hongyu.reward.model.OrderModel;

/**
 * Created by zhangyang131 on 16/10/4.
 */
public class StatusUtil {

  public static String getMsgByStatus(int status){
        String msg = null;
        if (status == OrderModel.STATUS_PENDING_PAY) {
            msg = "待付款";
        } else if (status == OrderModel.STATUS_FINISHED) {
            msg = "已完成";
        } else if (status == OrderModel.STATUS_PENDING_COMMENT) {
            msg = "待评论";
        } else if (status == OrderModel.STATUS_PENDING_COMPLAINT) {
            msg = "客诉单";
        } else if (status == OrderModel.STATUS_PENDING_RECEIVE) {
            msg = "待领取";
        } else if (status == OrderModel.STATUS_RECEIVED) {
            msg = "已领取";
        } else if (status == OrderModel.STATUS_INVALID) {
            msg = "失效";
        } else if (status == OrderModel.STATUS_CANCEL) {
            msg = "已取消";
        } else if (status == OrderModel.STATUS_APPEND) {
            msg = "追加";
        }
      return msg;
    }

  public static int getColorByStatus(int status) {
    int color = GlobalConfig.getAppContext().getResources().getColor(R.color.text_gray_light);
    if (status == OrderModel.STATUS_PENDING_PAY) {
      color = GlobalConfig.getAppContext().getResources().getColor(R.color.text_orange);
    } else if (status == OrderModel.STATUS_FINISHED) {
      color = GlobalConfig.getAppContext().getResources().getColor(R.color.text_green);
    } else if (status == OrderModel.STATUS_PENDING_COMMENT) {
      color = GlobalConfig.getAppContext().getResources().getColor(R.color.text_orange);
    } else if (status == OrderModel.STATUS_PENDING_COMPLAINT) {
      color = GlobalConfig.getAppContext().getResources().getColor(R.color.text_red);
    } else if (status == OrderModel.STATUS_PENDING_RECEIVE) {
      color = GlobalConfig.getAppContext().getResources().getColor(R.color.text_orange);
    } else if (status == OrderModel.STATUS_RECEIVED) {
      color = GlobalConfig.getAppContext().getResources().getColor(R.color.text_green);
    } else if (status == OrderModel.STATUS_INVALID) {
      color = GlobalConfig.getAppContext().getResources().getColor(R.color.text_gray_light);
    } else if (status == OrderModel.STATUS_CANCEL) {
      color = GlobalConfig.getAppContext().getResources().getColor(R.color.text_gray_light);
    } else if (status == OrderModel.STATUS_APPEND) {
      color = GlobalConfig.getAppContext().getResources().getColor(R.color.text_gray_light);
    }
    return color;
  }
}
