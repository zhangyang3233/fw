package com.hongyu.reward.ui.fragment.order;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fw.zycoder.http.callback.DataCallback;
import com.fw.zycoder.utils.Spanny;
import com.hongyu.reward.R;
import com.hongyu.reward.appbase.BaseLoadFragment;
import com.hongyu.reward.config.Constants;
import com.hongyu.reward.http.ResponesUtil;
import com.hongyu.reward.model.BaseModel;
import com.hongyu.reward.model.NoticeEvent;
import com.hongyu.reward.model.OrderInfoModel;
import com.hongyu.reward.model.OrderModel;
import com.hongyu.reward.model.ReceiveModel;
import com.hongyu.reward.request.CancelOrderRequestBuilder;
import com.hongyu.reward.request.GetOrderInfoRequestBuilder;
import com.hongyu.reward.ui.activity.PhotoViewActivity;
import com.hongyu.reward.ui.activity.order.PaySureActivity;
import com.hongyu.reward.ui.activity.order.RewardStartActivity;
import com.hongyu.reward.ui.dialog.CommonTwoBtnDialogFragment;
import com.hongyu.reward.utils.T;
import com.hongyu.reward.widget.FiveStarSingle;
import com.hongyu.reward.widget.NetImageView;
import com.hongyu.reward.widget.RoundImageView;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

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
  private TextView mAddress;
  private TextView mTvName;
  private TextView mTvOrderNum;
  private TextView mTvGcr;
  private View write_layout;
  private View photo_layout;
  private NetImageView photo_img;
//  private TextView mTaskStatus;
  private RoundImageView mIvHeader;
  private FiveStarSingle mScoreView;
  private View mBtnCall;
  private View option_layout;
  ImageView call_img;

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
    mTvShopName.setText(order.getShop_name());
    mAddress.setText("地址："+order.getShop_address());
    price = order.getPrice();
    mIvShop.loadNetworkImageByUrl(order.getImg());
    if(order.getStatus() == OrderModel.STATUS_PENDING_RECEIVE || order.getStatus() == OrderModel.STATUS_PENDING_PAY){
//      mTaskStatus.setText(R.string.task_continue);
      ((AnimationDrawable)call_img.getDrawable()).start();
    }else if(order.getStatus() == OrderModel.STATUS_CANCEL){
//      mTaskStatus.setText(R.string.task_canceled);
      option_layout.setVisibility(View.GONE);
      call_img.setImageResource(R.mipmap.call_3);
      mBtnCall.setEnabled(false);
      mBtnCall.setAlpha(0.5f);
    }else{
//      mTaskStatus.setText(R.string.task_continue);
      ((AnimationDrawable)call_img.getDrawable()).start();
    }

    final ReceiveModel receive = data.getData().getReceive();
    if(receive != null){
      mobileNum = receive.getMobile();
      mIvHeader.loadNetworkImageByUrl(receive.getImg());
      mTvGcr.setText("好评率:" + (TextUtils.isEmpty(receive.getGcr()) ? "100%" : receive.getGcr()));
      mTvName.setText("领赏人："+receive.getNickname());
      mTvOrderNum.setText("成交:" + receive.getOrder_num() + "单");
      mScoreView.setData(receive.getGcr(), false);

      if(TextUtils.isEmpty(receive.getTicket_img())){
        write_layout.setVisibility(View.VISIBLE);
        photo_layout.setVisibility(View.GONE);
        mTvTableNum.setText(String.valueOf(receive.rank_num));
        mTvTableWait.setText("您前面还有" +String.valueOf(receive.wait_num) + "桌等待");
        mTvTabPre.setText("就餐人数 " + String.valueOf(receive.table_num));
      }else{
        write_layout.setVisibility(View.GONE);
        photo_layout.setVisibility(View.VISIBLE);
        photo_img.loadNetworkImageByUrl(receive.getTicket_img());
        photo_img.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            PhotoViewActivity.launch(getActivity(), receive.getTicket_img());
          }
        });
      }
    }
  }

  @Override
  protected void onInflated(View contentView, Bundle savedInstanceState) {
    initView();
  }

  private void initView() {
    mIvShop = (NetImageView) mContentView.findViewById(R.id.image);
    mTvShopName = (TextView) mContentView.findViewById(R.id.shop_name);
    mAddress = (TextView) mContentView.findViewById(R.id.address);
    mTvName = (TextView) mContentView.findViewById(R.id.name);
    mTvGcr = (TextView) mContentView.findViewById(R.id.gcr);
    mTvOrderNum = (TextView) mContentView.findViewById(R.id.order_num);
    mScoreView = (FiveStarSingle) mContentView.findViewById(R.id.my_score);
    mIvHeader = (RoundImageView) mContentView.findViewById(R.id.header_icon);
//    mTaskStatus = (TextView) mContentView.findViewById(R.id.task_status);
    write_layout =  mContentView.findViewById(R.id.write_layout);
    photo_layout =  mContentView.findViewById(R.id.photo_layout);
    photo_img = (NetImageView) mContentView.findViewById(R.id.photo_img);

    mTvTableNum = (TextView) mContentView.findViewById(R.id.pwh);
    mTvTableWait = (TextView) mContentView.findViewById(R.id.ddzs);
    mTvTabPre = (TextView) mContentView.findViewById(R.id.jcrs);
    order_cancel = (Button) mContentView.findViewById(R.id.order_cancel);
    order_finish = (Button) mContentView.findViewById(R.id.order_finish);
    option_layout = mContentView.findViewById(R.id.option_layout);
    order_cancel.setOnClickListener(this);
    order_finish.setOnClickListener(this);
    mTvTableNum.setText(table_num);
    mTvTableWait.setText("您前面还有"+table_wait+"桌等待");
    mTvTabPre.setText("就餐人数 "+table_pre);
    mBtnCall = mContentView.findViewById(R.id.call);
    call_img = (ImageView) mContentView.findViewById(R.id.call_img);
    mBtnCall.setOnClickListener(this);
    mTvShopName.setText(shop_name);
    mIvShop.loadNetworkImageByUrl(shop_img);
  }


  @Override
  protected int getLayoutResId() {
    return R.layout.activity_reward_start_layout2;
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
        sp.append("大餐近在眼前，确定要取消么？\n\n", new RelativeSizeSpan(0.9f));
        sp.append("＊注意：确认取消前请先与对方电话联系哦", new ForegroundColorSpan(0xfffc4d50), new RelativeSizeSpan(0.7f));
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
        CommonTwoBtnDialogFragment dialogPay = new CommonTwoBtnDialogFragment();
        dialogPay.setContent("请务必与领赏人当面确认哦，支付赏金后，该任务就完成啦！");
        dialogPay.setLeft("支付赏金", new CommonTwoBtnDialogFragment.OnClickListener() {
          @Override
          public void onClick(Dialog dialog) {
            PaySureActivity.launch(getActivity(), order_id, price);
            getActivity().finish();
          }
        });
        dialogPay.setRight("我按错了", new CommonTwoBtnDialogFragment.OnClickListener() {
          @Override
          public void onClick(Dialog dialog) {
            dialog.cancel();
          }
        });
        dialogPay.show(getChildFragmentManager(), this.getClass().getSimpleName());
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
          MobclickAgent.onEvent(getActivity(), Constants.APP_EVENT.EVENT_PUBLISH_CANCEL_BY_RECEIVED);
          T.show("取消订单成功");
          getActivity().finish();
          EventBus.getDefault().post(new NoticeEvent(NoticeEvent.ORDER_STATUS_CHANGED));
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
