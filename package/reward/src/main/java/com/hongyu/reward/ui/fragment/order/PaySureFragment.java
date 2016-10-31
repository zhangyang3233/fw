package com.hongyu.reward.ui.fragment.order;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.fw.zycoder.http.callback.DataCallback;
import com.hongyu.reward.R;
import com.hongyu.reward.appbase.BaseLoadFragment;
import com.hongyu.reward.config.Constants;
import com.hongyu.reward.http.ResponesUtil;
import com.hongyu.reward.model.SignResultModel;
import com.hongyu.reward.pay.PayAsyncTask;
import com.hongyu.reward.request.PayRequestBuilder;
import com.hongyu.reward.ui.activity.PayResultActivity;
import com.hongyu.reward.ui.activity.order.PaySureActivity;
import com.hongyu.reward.utils.PayEventUtil;
import com.hongyu.reward.utils.T;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by zhangyang131 on 16/10/3.
 */
public class PaySureFragment extends BaseLoadFragment implements OnClickListener {
  private String orderId;
  private String price;
  private Button mBtnPay;
  private TextView mTvPrice;

  private void getData() {
    orderId = getArguments().getString(PaySureActivity.ORDER_ID);
    price = getArguments().getString(PaySureActivity.PRICE);
  }


  @Override
  protected void onStartLoading() {
    refreshData();
  }

  private void refreshData() {
    mTvPrice.setText("￥" + price);
    mBtnPay.setText("确认支付￥" + price);
  }

  @Override
  protected void onInflated(View contentView, Bundle savedInstanceState) {
    initView();
  }

  private void initView() {
    mTvPrice = (TextView) mContentView.findViewById(R.id.price);
    mBtnPay = (Button) mContentView.findViewById(R.id.ok);
    mBtnPay.setOnClickListener(this);
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getData();
  }

  @Override
  protected int getLayoutResId() {
    return R.layout.activity_pay_sure_layout;
  }

  @Override
  public void onClick(View v) {
    showLoadingView();
    PayRequestBuilder builder = new PayRequestBuilder(orderId);
    builder.setDataCallback(new DataCallback<SignResultModel>() {
      @Override
      public void onDataCallback(SignResultModel data) {
        if (!isAdded()) {
          return;
        }
        dismissLoadingView();
        if (ResponesUtil.checkModelCodeOK(data)) { // 请求成功
          pay(data.getData());
        } else {
          T.show(ResponesUtil.getErrorMsg(data));
        }
      }
    });
    builder.build().submit();
  }

  private void pay(final String data) {
    PayAsyncTask payAsyncTask =
        new PayAsyncTask(getActivity(), data, new PayAsyncTask.PayResultCallback() {
          @Override
          public void paySuccess() {
            T.show("支付成功");
            PayEventUtil.paySuccessEvent(getActivity(), price);
            PayResultActivity.launch(getActivity(), true, orderId);
            getActivity().finish();
          }

          @Override
          public void payFailed(String msg) {
            MobclickAgent.onEvent(getActivity(), Constants.APP_EVENT.EVENT_PAY_FAILED);
            PayResultActivity.launch(getActivity(), false, orderId);
          }
        });
    payAsyncTask.execute();
  }
}
