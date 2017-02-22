package com.hongyu.reward.ui.fragment;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.fw.zycoder.http.callback.DataCallback;
import com.hongyu.reward.R;
import com.hongyu.reward.appbase.BaseTakePhotoFragment;
import com.hongyu.reward.http.ResponesUtil;
import com.hongyu.reward.manager.AccountManager;
import com.hongyu.reward.model.NoticeEvent;
import com.hongyu.reward.model.OrderInfoModel;
import com.hongyu.reward.model.OrderModel;
import com.hongyu.reward.model.ReceiveOrderInfo;
import com.hongyu.reward.request.GetOrderInfoRequestBuilder;
import com.hongyu.reward.ui.activity.OrderDetailActivity;
import com.hongyu.reward.ui.activity.PreViewActivity;
import com.hongyu.reward.ui.activity.order.InputWaitNumActivity;
import com.hongyu.reward.ui.activity.order.ReceiveWaitActivity;
import com.hongyu.reward.ui.dialog.DialogFactory;
import com.hongyu.reward.utils.T;
import com.hongyu.reward.utils.getpic.Consts;
import com.hongyu.reward.widget.FiveStarSingle;
import com.hongyu.reward.widget.NetImageView;
import com.hongyu.reward.widget.RoundImageView;
import com.jph.takephoto.model.TResult;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;

/**
 * 可领取任务详情页
 * Created by zhangyang131 on 16/9/19.
 */
public class OrderDetailFragment extends BaseTakePhotoFragment implements View.OnClickListener {
  private static final String TAG = OrderDetailFragment.class.getSimpleName();
  String order_id;
  boolean is_my_receive;
  private NetImageView mIvShop;
  private TextView mTvShopName;
  private TextView mAddress;
  private TextView mTvNum;
  private TextView mTvPrice;
  private RoundImageView mIvHeader;
  private TextView mTvName;
  private TextView mTvOrderNum;
  private TextView mTvGcr;
  private TextView mTip;
  private FiveStarSingle mScoreView;
  private View mReceiveBtn;
  private String shop_name;
  private String shop_address;
  private String shop_img;
  private String price;
  private String nickname;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getData();
    EventBus.getDefault().register(this);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    EventBus.getDefault().unregister(this);
  }

  private void getData() {
    order_id = getArguments().getString(OrderDetailActivity.ORDER_ID);
    is_my_receive = getArguments().getBoolean(OrderDetailActivity.IS_MY_RECEIVE);
  }

  @Override
  protected void onStartLoading() {
    if(is_my_receive){
      setLoadingViewCancelable(true);
      setLoadingCancelListener(new DialogInterface.OnCancelListener() {
        @Override
        public void onCancel(DialogInterface dialog) {
          getActivity().finish();
        }
      });
      showLoadingView("待悬赏人确认");
    }else{
      showLoadingView();
    }
    GetOrderInfoRequestBuilder builder = new GetOrderInfoRequestBuilder(order_id);
    builder.setDataCallback(new DataCallback<OrderInfoModel>() {
      @Override
      public void onDataCallback(OrderInfoModel data) {
        if (!isAdded()) {
          return;
        }
        if(!is_my_receive){
          dismissLoadingView();
        }
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
    shop_name = order.getShop_name();
    shop_address = order.getShop_address();
    shop_img = order.getImg();
    price = order.getPrice();
    nickname = order.getNickname();

    mTvShopName.setText(shop_name);
    mTvPrice.setText(price);
    mTvName.setText(nickname);
    mIvShop.loadNetworkImageByUrl(shop_img);

    mTvGcr.setText("好评率:" + (TextUtils.isEmpty(order.getGcr()) ? "0%" : order.getGcr()));
    mTvName.setText(order.getNickname());
    mTvOrderNum.setText("成交:" + order.getOrder_num() + "单");
    mIvHeader.loadNetworkImageByUrl(order.getHead_img());
    mIvShop.loadNetworkImageByUrl(order.getImg());
    mAddress.setText("地址：" + order.getShop_address());
    mScoreView.setData(order.getGcr(), false);
    mTvNum.setText(order.getUsernum() + "人");
    if (order.getStatus() != OrderModel.STATUS_PENDING_RECEIVE) { // 该订单已经被人领取
      mReceiveBtn.setEnabled(false);
      if(is_my_receive){
        mTip.setText("*我们为悬赏人保留最长10分钟的考虑时间，请您耐心等待。");
        setLoadingViewCancelable(true);
        setLoadingCancelListener(new DialogInterface.OnCancelListener() {
          @Override
          public void onCancel(DialogInterface dialog) {
            getActivity().finish();
          }
        });
        showLoadingView("待悬赏人确认");
      }else{
        if (order.getStatus() == OrderModel.STATUS_INVALID
                || order.getStatus() == OrderModel.STATUS_CANCEL
                || order.getStatus() == OrderModel.STATUS_APPEND) {
          mTip.setText("*抱歉，该任务已经取消，请返回列表获取最新任务。");
        } else {
          mTip.setText("*抱歉，该任务已被人领取，请返回列表获取最新任务。");
        }
      }
    } else {
      mReceiveBtn.setEnabled(true);
      mTip.setText("*我们为悬赏人保留最长10分钟的考虑时间，请您耐心等待。");

      String refused = order.getRefused_user();
      if (!TextUtils.isEmpty(refused)) {
        String[] arr = refused.split(",");
        if (arr != null && arr.length != 0) {
          String myUserId = AccountManager.getInstance().getUser().getUser_id();
          for (String refuseUser : arr) {
            if (refuseUser.equals(myUserId)) {
              mReceiveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  Toast.makeText(getActivity(), "您已经被悬赏人拒绝", Toast.LENGTH_LONG).show();
                }
              });
              return;
            }
          }
        }
      }
      mReceiveBtn.setOnClickListener(OrderDetailFragment.this);
    }
  }

  @Override
  protected void onInflated(View contentView, Bundle savedInstanceState) {
    initView();
  }

  private void initView() {
    mIvShop = (NetImageView) mContentView.findViewById(R.id.image);
    mTvShopName = (TextView) mContentView.findViewById(R.id.shop_name);
    mTvNum = (TextView) mContentView.findViewById(R.id.num);
    mTvPrice = (TextView) mContentView.findViewById(R.id.price);
    mTvName = (TextView) mContentView.findViewById(R.id.name);
    mAddress = (TextView) mContentView.findViewById(R.id.address);
    mTvGcr = (TextView) mContentView.findViewById(R.id.gcr);
    mTip = (TextView) mContentView.findViewById(R.id.tip);
    mTvOrderNum = (TextView) mContentView.findViewById(R.id.order_num);
    mScoreView = (FiveStarSingle) mContentView.findViewById(R.id.my_score);
    mIvHeader = (RoundImageView) mContentView.findViewById(R.id.header_icon);
    mReceiveBtn = mContentView.findViewById(R.id.receive);
    mReceiveBtn.setOnClickListener(this);

  }

  @Override
  protected int getLayoutResId() {
    return R.layout.activity_reward_detail_layout;
  }


  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.receive:
        // showReceiveDialog();
        showChooseTypeDialog();
        break;
    }
  }

  private void showChooseTypeDialog() {
    DialogFactory.showReceiveTypeView(getActivity(), new DialogFactory.OnWhichListener() {
      @Override
      public void onConfirmClick(int which) {
        switch (which) {
          case 0:// 填写排号单
            // showReceiveDialog();
            // TODO
            InputWaitNumActivity.launch(getActivity(), order_id, shop_img, shop_name, shop_address);
            break;
          case 1:// 拍摄排号单
//            Intent intent = new Intent(
//                MediaStore.ACTION_IMAGE_CAPTURE);
//            intent.putExtra(MediaStore.EXTRA_OUTPUT,
//                Uri.fromFile(new File(Consts.A_mShootPath)));
//            // fake solution for some phone can't call the camera
//            if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
//              startActivityForResult(intent, Consts.REQUEST_CODE_TAKE_A_PICTURE);
//            }
            getTakePhoto().onPickFromCapture(Uri.fromFile(new File(Consts.A_mShootPath)));
            break;
        }
      }
    });

  }



  private void receiveOrder(final String imgPath) {
    PreViewActivity.launch(getActivity(), order_id, shop_name, shop_address, shop_img, null, null,
        null, imgPath);
  }


  /**
   * 悬赏人确认接受该订单的接单请求
   *
   * @param order 订单信息
   */
  @Subscribe
  public void onEventMainThread(ReceiveOrderInfo order) {
    if (!isAdded()) {
      return;
    }
    ReceiveWaitActivity.launch(getActivity(), order.getOrderId());
    getActivity().finish();
  }

  /**
   * 请求悬赏成功
   *
   */
  @Subscribe
  public void onEventMainThread(NoticeEvent event) {
    if (!isAdded()) {
      return;
    }
    if (event.getType() == NoticeEvent.RECEIVE_REQUEST_SUCCESS) {
      setLoadingViewCancelable(true);
      setLoadingCancelListener(new DialogInterface.OnCancelListener() {
        @Override
        public void onCancel(DialogInterface dialog) {
          getActivity().finish();
        }
      });
      showLoadingView("待悬赏人确认");
      mReceiveBtn.setEnabled(false);
    }
  }

  @Override
  public void takeSuccess(TResult result) {
    Log.i(TAG, "takeSuccess：" + result.getImage().getCompressPath());
    receiveOrder(result.getImage().getOriginalPath());
  }

  @Override
  public void takeFail(TResult result, String msg) {
    Log.i(TAG, "takeFail:" + msg);
  }

  @Override
  public void takeCancel() {
    Log.i(TAG, getResources().getString(com.jph.takephoto.R.string.msg_operation_canceled));
  }



}
