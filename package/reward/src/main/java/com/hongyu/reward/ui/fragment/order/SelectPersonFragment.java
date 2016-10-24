package com.hongyu.reward.ui.fragment.order;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.fw.zycoder.http.callback.DataCallback;
import com.hongyu.reward.R;
import com.hongyu.reward.appbase.BaseLoadFragment;
import com.hongyu.reward.config.ResultCode;
import com.hongyu.reward.http.ResponesUtil;
import com.hongyu.reward.model.BaseModel;
import com.hongyu.reward.model.OrderInfoModel;
import com.hongyu.reward.model.OrderModel;
import com.hongyu.reward.model.ReceiveInfoModel;
import com.hongyu.reward.model.ReceiveModel;
import com.hongyu.reward.request.GetOrderInfoRequestBuilder;
import com.hongyu.reward.request.GetReceivePersonInfoRequestBuilder;
import com.hongyu.reward.request.UserReceiveRequestBuilder;
import com.hongyu.reward.ui.activity.order.RewardStartActivity;
import com.hongyu.reward.ui.activity.order.SelectPersonActivity;
import com.hongyu.reward.ui.dialog.CommonTwoBtnDialogFragment;
import com.hongyu.reward.utils.T;
import com.hongyu.reward.widget.FiveStarSingle;
import com.hongyu.reward.widget.NetImageView;
import com.hongyu.reward.widget.RoundImageView;

/**
 * Created by zhangyang131 on 16/10/4.
 */
public class SelectPersonFragment extends BaseLoadFragment implements View.OnClickListener {
  private String order_id;
  private String shop_name;
  private String shop_img;
  private int isuse = 0;

  private NetImageView mIvShop;
  private TextView mTvShopName;
  private TextView address;
  private RoundImageView mIvHeader;
  private TextView mTvName;
  private TextView mTvOrderNum;
  private TextView mTvGcr;
  private FiveStarSingle mScoreView;
  private TextView mTvTableWait;
  private TextView mTvTablePre;
  private View mBtnSelect;
  private View mBtnNoSelect;


  private TextView mTvTableNum;
  private ReceiveModel receive;
  private OrderModel orderModel;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getData();
  }

  private void getData() {
    order_id = getArguments().getString(SelectPersonActivity.ORDER_ID);
    shop_name = getArguments().getString(SelectPersonActivity.SHOP_NAME);
    shop_img = getArguments().getString(SelectPersonActivity.SHOP_IMG);
  }

  @Override
  protected void onStartLoading() {
    GetOrderInfoRequestBuilder b = new GetOrderInfoRequestBuilder(order_id);
    b.setDataCallback(new DataCallback<OrderInfoModel>() {
      @Override
      public void onDataCallback(OrderInfoModel data) {
        if (ResponesUtil.checkModelCodeOK(data)) {
          refreshData(data.getData().getOrder());
        } else if (data.getCode() == ResultCode.code_3005) {
          T.show(ResponesUtil.getErrorMsg(data));
          getActivity().finish();
        } else {
          T.show(ResponesUtil.getErrorMsg(data));
        }
      }
    });
    b.build().submit();

    GetReceivePersonInfoRequestBuilder builder = new GetReceivePersonInfoRequestBuilder(order_id);
    builder.setDataCallback(new DataCallback<ReceiveInfoModel>() {
      @Override
      public void onDataCallback(ReceiveInfoModel data) {
        if (ResponesUtil.checkModelCodeOK(data)) {
          refreshData(data.getData());
        } else if (data.getCode() == ResultCode.code_3005) {
          T.show(ResponesUtil.getErrorMsg(data));
          getActivity().finish();
        } else {
          T.show(ResponesUtil.getErrorMsg(data));
        }
      }
    });
    builder.build().submit();
  }

  private void refreshData(OrderModel orderModel) {
    if (orderModel != null) {
      this.orderModel = orderModel;
      this.shop_name = orderModel.getShop_name();
      this.shop_img = orderModel.getImg();
      mTvShopName.setText(shop_name);
      address.setText(orderModel.getShop_address());
      mIvShop.loadNetworkImageByUrl(shop_img);
    }
  }

  private void refreshData(ReceiveModel receive) {
    if (receive != null) {
      this.receive = receive;
      mTvGcr.setText("好评率:" + (TextUtils.isEmpty(receive.getGcr()) ? "100%" : receive.getGcr()));
      mTvName.setText(receive.getNickname());
      mTvOrderNum.setText("成交:" + receive.getOrder_num() + "单");
      mScoreView.setData(receive.getGcr(), false);
      mIvHeader.loadNetworkImageByUrl(receive.getImg());
      mTvTableNum.setText(String.valueOf(receive.getRank_num()));
      mTvTableWait.setText(String.valueOf(receive.getWait_num()));
      mTvTablePre.setText(String.valueOf(receive.getTable_num()));
    }
  }

  @Override
  protected void onInflated(View contentView, Bundle savedInstanceState) {
    initView();
  }

  private void initView() {
    mIvShop = (NetImageView) mContentView.findViewById(R.id.image);
    mTvShopName = (TextView) mContentView.findViewById(R.id.shop_name);
    address = (TextView) mContentView.findViewById(R.id.address);

    mTvName = (TextView) mContentView.findViewById(R.id.name);
    mTvGcr = (TextView) mContentView.findViewById(R.id.gcr);
    mTvOrderNum = (TextView) mContentView.findViewById(R.id.order_num);
    mScoreView = (FiveStarSingle) mContentView.findViewById(R.id.my_score);
    mIvHeader = (RoundImageView) mContentView.findViewById(R.id.header_icon);

    mTvTableNum = (TextView) mContentView.findViewById(R.id.table_num);
    mTvTableWait = (TextView) mContentView.findViewById(R.id.table_wait);
    mTvTablePre = (TextView) mContentView.findViewById(R.id.table_pre);

    mBtnNoSelect = mContentView.findViewById(R.id.noselect);
    mBtnSelect = mContentView.findViewById(R.id.select);
    mBtnNoSelect.setOnClickListener(this);
    mBtnSelect.setOnClickListener(this);

    mTvShopName.setText(shop_name);
    mIvShop.loadNetworkImageByUrl(shop_img);
  }

  @Override
  protected int getLayoutResId() {
    return R.layout.activity_reward_select_person_layout;
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.noselect: // 不选择该领赏人
        isuse = 0;
        select(false);
        break;
      case R.id.select: // 选择该领赏人
        CommonTwoBtnDialogFragment dialog = new CommonTwoBtnDialogFragment();
        dialog.setContent("确认要选择该领赏人吗？");
        dialog.setLeft("取消", new CommonTwoBtnDialogFragment.OnClickListener() {
          @Override
          public void onClick(Dialog dialog) {
            dialog.dismiss();
          }
        });
        dialog.setRight("选择", new CommonTwoBtnDialogFragment.OnClickListener() {
          @Override
          public void onClick(Dialog dialog) {
            dialog.dismiss();
            select(true);
          }
        });
        dialog.show(getChildFragmentManager(), getClass().getSimpleName());
        break;
    }
  }

  private void select(final boolean select) {
    showLoadingView();
    UserReceiveRequestBuilder builder =
        new UserReceiveRequestBuilder(order_id, receive.getReceive_id(),
            select ? UserReceiveRequestBuilder.IS_USE : UserReceiveRequestBuilder.IS_REPLACE);
    builder.setDataCallback(new DataCallback<BaseModel>() {
      @Override
      public void onDataCallback(BaseModel data) {
        if (!isAdded()) {
          return;
        }
        dismissLoadingView();
        if (ResponesUtil.checkModelCodeOK(data)) {
          if (select) { // 选择成功
            RewardStartActivity.launch(getActivity(), shop_name, shop_img, order_id,
                receive.getTable_num(), receive.getWait_num(), receive.getRank_num());
            getActivity().finish();
          } else { // 不选择成功
            T.show("反馈成功,系统正在给您匹配下个一接单人");
            getActivity().finish();
          }
        } else {
          T.show(ResponesUtil.getErrorMsg(data));
        }

      }
    });
    builder.build().submit();

  }
}
