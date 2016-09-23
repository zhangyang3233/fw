package com.hongyu.reward.ui.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fw.zycoder.http.callback.DataCallback;
import com.hongyu.reward.R;
import com.hongyu.reward.appbase.BaseLoadFragment;
import com.hongyu.reward.http.ResponesUtil;
import com.hongyu.reward.model.AddRewardModel;
import com.hongyu.reward.model.BaseModel;
import com.hongyu.reward.model.OrderInfoModel;
import com.hongyu.reward.model.OrderModel;
import com.hongyu.reward.request.AddRewardRequestBuilder;
import com.hongyu.reward.request.CancelOrderRequestBuilder;
import com.hongyu.reward.request.GetOrderInfoRequestBuilder;
import com.hongyu.reward.ui.dialog.AddRewardDialog;
import com.hongyu.reward.ui.dialog.CommonTwoBtnDialogFragment;
import com.hongyu.reward.utils.T;
import com.hongyu.reward.widget.NetImageView;

/**
 * Created by zhangyang131 on 16/9/13.
 */
public class RewardPublishWaitFragment extends BaseLoadFragment implements View.OnClickListener {
  public static final String ORDER_ID = "order_id";
  public static final String SHOP_NAME = "shop_name";
  public static final String SHOP_IMG = "shop_img";
  private String order_id = "0";
  private String shop_name = "";
  private String shop_img = "";
  private float price;

  private NetImageView image;
  private TextView shop_name_tv;
  private TextView order_type;
  private TextView dinner_count_info;
  private TextView reward_money_info;
  private TextView time_count_down;
  private Button cancel;
  private Button add_price;
  private CountDownTimer timer;

  private void initView() {
    image = (NetImageView) mContentView.findViewById(R.id.image);
    shop_name_tv = (TextView) mContentView.findViewById(R.id.shop_name_tv);
    order_type = (TextView) mContentView.findViewById(R.id.order_type);
    dinner_count_info = (TextView) mContentView.findViewById(R.id.dinner_count_info);
    reward_money_info = (TextView) mContentView.findViewById(R.id.reward_money_info);
    time_count_down = (TextView) mContentView.findViewById(R.id.time_count_down);
    cancel = (Button) mContentView.findViewById(R.id.cancel);
    add_price = (Button) mContentView.findViewById(R.id.add_price);
  }

  @Override
  protected void onStartLoading() {
    showLoadingView();
    GetOrderInfoRequestBuilder builder = new GetOrderInfoRequestBuilder(order_id);
    builder.setDataCallback(new DataCallback<OrderInfoModel>() {
      @Override
      public void onDataCallback(OrderInfoModel data) {
        cancel.setOnClickListener(RewardPublishWaitFragment.this);
        add_price.setOnClickListener(RewardPublishWaitFragment.this);
        dismissLoadingView();
        if (ResponesUtil.checkModelCodeOK(data)) {
          refreshData(data.getData().getOrder());
        } else {
          T.show(ResponesUtil.getErrorMsg(data));
        }
      }
    });
    builder.build().submit();
  }

  private void refreshData(OrderModel order) {
    try {
      price = Float.parseFloat(order.getPrice());
    } catch (NumberFormatException e) {
      e.printStackTrace();
    }
    dinner_count_info.setText(getString(R.string.order_user_num, order.getUsernum()));
    reward_money_info.setText(getString(R.string.order_price, price));
    shop_name_tv.setText(order.getShop_name());
    image.loadNetworkImageByUrl(order.getImg());
    if (OrderModel.APPOINTMENT.equals(order.getType())) {
      order_type.setText(R.string.appointment);
    } else {
      order_type.setText(R.string.immediate);
    }
    startCountDownTimer(order.getEnd_time());
  }

  private void startCountDownTimer(String totalTime) {
    long time;
    if (TextUtils.isEmpty(totalTime)) {
      T.show("系统异常");
      return;
    } else {
      try {
        time = Long.parseLong(totalTime) - (System.currentTimeMillis() / 1000);
      } catch (NumberFormatException e) {
        T.show("系统异常");
        return;
      }
    }
    if (time <= 0) {
      setTimeCountDown(0);
      return;
    } else {
      setTimeCountDown((int) time);
    }

    if (timer != null) {
      timer.cancel();
    }
    startTimer(time);
  }

  private void startTimer(long time) {
    timer = new CountDownTimer(time * 1000, 1000) {
      @Override
      public void onTick(long millisUntilFinished) {
        setTimeCountDown((int) (millisUntilFinished) / 1000);
      }

      @Override
      public void onFinish() {
        setTimeCountDown(0);
      }
    };
    timer.start();
  }

  private void setTimeCountDown(int seconds) {
    int min = seconds / 60;
    int second = seconds % 60;
    String minStr = min > 9 ? String.valueOf(min) : "0" + min;
    String secondStr = second > 9 ? String.valueOf(second) : "0" + second;
    time_count_down.setText(minStr + ":" + secondStr);
  }

  private void getData() {
    order_id = getArguments().getString(ORDER_ID);
    shop_name = getArguments().getString(SHOP_NAME);
    shop_img = getArguments().getString(SHOP_IMG);
    if (!TextUtils.isEmpty(shop_name)) {
      shop_name_tv.setText(shop_name);
    }
    if (!TextUtils.isEmpty(shop_img)) {
      image.loadNetworkImageByUrl(shop_img);
    }
  }

  @Override
  protected void onInflated(View contentView, Bundle savedInstanceState) {
    initView();
    getData();
  }

  @Override
  protected int getLayoutResId() {
    return R.layout.activity_reward_publish_wait_layout;
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.add_price:
        showAddRewardDialog();
        break;
      case R.id.cancel:
        showCancelDialog();
        break;
    }
  }

  private void showAddRewardDialog() {
    AddRewardDialog dialog = AddRewardDialog.newInstance(price);
    dialog.setAddPriceListener(new AddRewardDialog.AddPriceListener() {
      @Override
      public void addPrice(float addPrice) {
        showLoadingView();
        AddRewardRequestBuilder builder =
            new AddRewardRequestBuilder(order_id, String.valueOf(addPrice));
        builder.setDataCallback(new DataCallback<AddRewardModel>() {
          @Override
          public void onDataCallback(AddRewardModel data) {
            if (ResponesUtil.checkModelCodeOK(data)) {
              T.show(R.string.add_reward_success);
              order_id = data.getData().getOrder_id();
              requestLoad();
            } else {
              dismissLoadingView();
              T.show(ResponesUtil.getErrorMsg(data));
            }
          }
        });
        builder.build().submit();
      }
    });
    dialog.show(getChildFragmentManager(), getClass().getSimpleName());
  }

  private void showCancelDialog() {
    CommonTwoBtnDialogFragment dialog = new CommonTwoBtnDialogFragment();
    dialog.setContent(getString(R.string.cancel_tip_content));
    dialog.setLeft(getString(R.string.dialog_cancel),
        new CommonTwoBtnDialogFragment.OnClickListener() {
          @Override
          public void onClick(Dialog dialog) {
            dialog.dismiss();
          }
        });
    dialog.setRight(getString(R.string.dialog_ok),
        new CommonTwoBtnDialogFragment.OnClickListener() {
          @Override
          public void onClick(Dialog dialog) {
            dialog.dismiss();
            requestCancel();
          }
        });
    dialog.show(getChildFragmentManager(), this.getClass().getSimpleName());
  }

  private void requestCancel() {
    showLoadingView();
    CancelOrderRequestBuilder builder = new CancelOrderRequestBuilder(order_id);
    builder.setDataCallback(new DataCallback<BaseModel>() {
      @Override
      public void onDataCallback(BaseModel data) {
        if (ResponesUtil.checkModelCodeOK(data)) {
          T.show(R.string.cancel_reward_order_success);
          getActivity().finish();
        } else {
          T.show(ResponesUtil.getErrorMsg(data));
        }
      }
    });
    builder.build().submit();
  }
}
