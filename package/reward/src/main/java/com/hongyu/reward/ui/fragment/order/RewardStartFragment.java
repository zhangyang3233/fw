package com.hongyu.reward.ui.fragment.order;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fw.zycoder.http.callback.DataCallback;
import com.fw.zycoder.utils.Spanny;
import com.hongyu.reward.R;
import com.hongyu.reward.appbase.BaseLoadFragment;
import com.hongyu.reward.http.ResponesUtil;
import com.hongyu.reward.model.BaseModel;
import com.hongyu.reward.model.OrderInfoModel;
import com.hongyu.reward.model.OrderModel;
import com.hongyu.reward.model.ReceiveModel;
import com.hongyu.reward.request.CancelOrderRequestBuilder;
import com.hongyu.reward.request.GetOrderInfoRequestBuilder;
import com.hongyu.reward.ui.activity.order.RewardStartActivity;
import com.hongyu.reward.ui.activity.order.PaySureActivity;
import com.hongyu.reward.ui.dialog.CommonTwoBtnDialogFragment;
import com.hongyu.reward.utils.T;
import com.hongyu.reward.widget.FiveStarSingle;
import com.hongyu.reward.widget.NetImageView;
import com.hongyu.reward.widget.RoundImageView;

/**
 * 发起人看到的界面
 * Created by zhangyang131 on 16/9/19.
 */
public class RewardStartFragment extends BaseLoadFragment implements View.OnClickListener {
  // args
  public String shop_name;
  public String shop_img;
  public String order_id;
  public String table_num;
  public String table_wait;
  public String table_pre;
  // data
  private String mobileNum;
  private String price;
  // ui
  private NetImageView mIvShop;
  private TextView mTvShopName;
  private TextView mTvName;
  private TextView mTvOrderNum;
  private TextView mTvGcr;
  private TextView mTaskStatus;
  private RoundImageView mIvHeader;
  private FiveStarSingle mScoreView;
  private View mBtnCall;

  private TextView mTvTableNum;
  private TextView mTvTableWait;
  private TextView mTvTabPre;
  private Button order_cancel;
  private Button order_finish;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getData();
  }

  private void getData() {
    Bundle b = getArguments();
    shop_name = b.getString(RewardStartActivity.SHOP_NAME);
    shop_img = b.getString(RewardStartActivity.SHOP_IMG);
    order_id = b.getString(RewardStartActivity.ORDER_ID);
    table_num = b.getString(RewardStartActivity.TABLE_NUM);
    table_wait = b.getString(RewardStartActivity.TABLE_WAIT);
    table_pre = b.getString(RewardStartActivity.TABLE_PRE);
  }

  @Override
  protected void onStartLoading() {
    showLoadingView();
    GetOrderInfoRequestBuilder builder = new GetOrderInfoRequestBuilder(order_id);
    builder.setDataCallback(new DataCallback<OrderInfoModel>() {
      @Override
      public void onDataCallback(OrderInfoModel data) {
        if(!isAdded()){
          return;
        }
        dismissLoadingView();
        refreshUI(data);
      }
    });
    builder.build().submit();
  }

  private void refreshUI(OrderInfoModel data) {
    OrderModel order = data.getData().getOrder();
    mTvGcr.setText("好评率:" + (TextUtils.isEmpty(order.getGcr()) ? "0%" : order.getGcr()));
    mTvName.setText(data.getData().getOrder().getNickname());
    mTvOrderNum.setText("成交:" + order.getOrder_num() + "单");
    mScoreView.setData(order.getGcr(), false);
    price = order.getPrice();

    if(order.getStatus() == OrderModel.STATUS_PENDING_RECEIVE){
      mTaskStatus.setText(R.string.task_continue);
    }

    ReceiveModel receive = data.getData().getReceive();
    if(receive != null){
      mobileNum = receive.getMobile();
      mTvTableNum.setText(String.valueOf(receive.rank_num));
      mTvTableWait.setText(String.valueOf(receive.wait_num));
      mTvTabPre.setText(String.valueOf(receive.table_num));
    }
  }

  @Override
  protected void onInflated(View contentView, Bundle savedInstanceState) {
    initView();
  }

  private void initView() {
    mIvShop = (NetImageView) mContentView.findViewById(R.id.image);
    mTvShopName = (TextView) mContentView.findViewById(R.id.shop_name);
    mTvName = (TextView) mContentView.findViewById(R.id.name);
    mTvGcr = (TextView) mContentView.findViewById(R.id.gcr);
    mTvOrderNum = (TextView) mContentView.findViewById(R.id.order_num);
    mScoreView = (FiveStarSingle) mContentView.findViewById(R.id.my_score);
    mIvHeader = (RoundImageView) mContentView.findViewById(R.id.header_icon);
    mTaskStatus = (TextView) mContentView.findViewById(R.id.task_status);

    mTvTableNum = (TextView) mContentView.findViewById(R.id.table_num);
    mTvTableWait = (TextView) mContentView.findViewById(R.id.table_wait);
    mTvTabPre = (TextView) mContentView.findViewById(R.id.table_per_num);
    order_cancel = (Button) mContentView.findViewById(R.id.order_cancel);
    order_finish = (Button) mContentView.findViewById(R.id.order_finish);
    order_cancel.setOnClickListener(this);
    order_finish.setOnClickListener(this);
    mTvTableNum.setText(table_num);
    mTvTableWait.setText(table_wait);
    mTvTabPre.setText(table_pre);


    mBtnCall = mContentView.findViewById(R.id.call);
    mBtnCall.setOnClickListener(this);

    mTvShopName.setText(shop_name);
    mIvShop.loadNetworkImageByUrl(shop_img);
  }

  @Override
  protected int getLayoutResId() {
    return R.layout.activity_reward_start_layout;
  }


  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.call:
        showCallDialog();
        break;
      case R.id.order_cancel:
        CommonTwoBtnDialogFragment dialogFragment = new CommonTwoBtnDialogFragment();
        Spanny sp = new Spanny();
        sp.append("大餐近在眼前，确定要取消么？\n");
        sp.append("＊注意：确认取消前请先与对方电话联系哦", new ForegroundColorSpan(0xfffc4d50), new RelativeSizeSpan(0.8f));
        dialogFragment.setContent(sp);
        Spanny leftsp = new Spanny("确认取消", new ForegroundColorSpan(0xff1887f9));
        dialogFragment.setLeft(leftsp, new CommonTwoBtnDialogFragment.OnClickListener() {
          @Override
          public void onClick(Dialog dialog) {
            dialog.dismiss();
            cancelOrder();
          }
        });
        Spanny rightsp = new Spanny("我按错了", new ForegroundColorSpan(0xff1887f9));
        dialogFragment.setRight(rightsp, new CommonTwoBtnDialogFragment.OnClickListener() {
          @Override
          public void onClick(Dialog dialog) {
            dialog.dismiss();
          }
        });
        dialogFragment.show(getChildFragmentManager(),this.getClass().getSimpleName());
        break;
      case R.id.order_finish:
        PaySureActivity.launch(getActivity(), order_id, price);
        getActivity().finish();
        break;
    }
  }

  private void cancelOrder() {
    showLoadingView();
    CancelOrderRequestBuilder builder = new CancelOrderRequestBuilder(order_id);
    builder.setDataCallback(new DataCallback<BaseModel>() {
      @Override
      public void onDataCallback(BaseModel data) {
        if(!isAdded()){
          return;
        }
        dismissLoadingView();
        if(ResponesUtil.checkModelCodeOK(data)){
          T.show("取消订单成功");
          getActivity().finish();
        }else{
          T.show(ResponesUtil.getErrorMsg(data));
        }
      }
    });
    builder.build().submit();
  }

  private void showCallDialog() {
    CommonTwoBtnDialogFragment dialog = new CommonTwoBtnDialogFragment();
    dialog.setContent(getString(R.string.call_tip));
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
            Intent intent = new Intent(Intent.ACTION_CALL);
            Uri data = Uri.parse("tel:" + mobileNum);
            intent.setData(data);
            startActivity(intent);
          }
        });
    dialog.show(getChildFragmentManager(), getClass().getSimpleName());
  }
}
