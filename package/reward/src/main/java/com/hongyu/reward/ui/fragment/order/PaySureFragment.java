package com.hongyu.reward.ui.fragment.order;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fw.zycoder.utils.MainThreadPostUtils;
import com.hongyu.reward.R;
import com.hongyu.reward.appbase.BaseLoadFragment;
import com.hongyu.reward.config.Constants;
import com.hongyu.reward.manager.AccountManager;
import com.hongyu.reward.model.NoticeEvent;
import com.hongyu.reward.ui.activity.PayResultActivity;
import com.hongyu.reward.ui.activity.order.PaySureActivity;
import com.hongyu.reward.utils.PayEventUtil;
import com.hongyu.reward.utils.T;
import com.umeng.analytics.MobclickAgent;
import com.unionpay.UPPayAssistEx;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import cn.beecloud.BCPay;
import cn.beecloud.BeeCloud;
import cn.beecloud.async.BCCallback;
import cn.beecloud.async.BCResult;
import cn.beecloud.entity.BCPayResult;
import cn.beecloud.entity.BCReqParams;

import static com.hongyu.reward.ui.fragment.order.PaySureFragment.PayType.ALI_APP;
import static com.hongyu.reward.ui.fragment.order.PaySureFragment.PayType.UN_APP;
import static com.hongyu.reward.ui.fragment.order.PaySureFragment.PayType.WX_APP;

/**
 * Created by zhangyang131 on 16/10/3.
 */
public class PaySureFragment extends BaseLoadFragment implements OnClickListener {
  private String orderId;
  private String price;
  private Button mBtnPay;
  private TextView mTvPrice;
  private View btn_alipay;
  private View btn_wxpay;
  private View btn_uppay;
  private ImageView alipay_img;
  private ImageView wxpay_img;
  private ImageView up_img;
  PayType payType = ALI_APP;

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
    btn_alipay = mContentView.findViewById(R.id.btn_alipay);
    btn_wxpay = mContentView.findViewById(R.id.btn_wxpay);
    btn_uppay = mContentView.findViewById(R.id.btn_uppay);
    alipay_img = (ImageView) mContentView.findViewById(R.id.alipay_img);
    wxpay_img = (ImageView) mContentView.findViewById(R.id.wxpay_img);
    up_img = (ImageView) mContentView.findViewById(R.id.up_img);
    mBtnPay = (Button) mContentView.findViewById(R.id.ok);
    mBtnPay.setOnClickListener(this);
    btn_alipay.setOnClickListener(this);
    btn_wxpay.setOnClickListener(this);
    btn_uppay.setOnClickListener(this);
    freshLayout();
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getData();
    BeeCloud.setAppIdAndSecret(Constants.BeeCloud.APP_ID, Constants.BeeCloud.APP_SECRET);
    BCPay.initWechatPay(getActivity(), Constants.WX.AppID);
  }

  @Override
  protected int getLayoutResId() {
    return R.layout.activity_pay_sure_layout;
  }


  enum PayType {
    ALI_APP, WX_APP, UN_APP
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.ok:
        showLoadingView();
        int priceInt = (int) (Float.valueOf(price) * 100f);
        pay(priceInt);
        break;
      case R.id.btn_alipay:
        payType = ALI_APP;
        freshLayout();
        break;
      case R.id.btn_wxpay:
        payType = WX_APP;
        freshLayout();
        break;
      case R.id.btn_uppay:
        payType = UN_APP;
        freshLayout();
        break;
    }
    // PayRequestBuilder builder = new PayRequestBuilder(orderId);
    // builder.setDataCallback(new DataCallback<SignResultModel>() {
    // @Override
    // public void onDataCallback(SignResultModel data) {
    // if (!isAdded()) {
    // return;
    // }
    // dismissLoadingView();
    // if (ResponesUtil.checkModelCodeOK(data)) { // 请求成功
    // pay(data.getData());
    // } else {
    // T.show(ResponesUtil.getErrorMsg(data));
    // }
    // }
    // });
    // builder.build().submit();
  }

  private void pay(int priceInt) {
    switch (payType) {
      case WX_APP:
        payWX(priceInt);
        break;
      case ALI_APP:
        payZFB(priceInt);
        break;
      case UN_APP:
        payYL(priceInt);
        break;
    }
  }

  private void freshLayout() {
    switch (payType) {
      case ALI_APP:
        alipay_img.setVisibility(View.VISIBLE);
        wxpay_img.setVisibility(View.INVISIBLE);
        up_img.setVisibility(View.INVISIBLE);
        break;
      case WX_APP:
        alipay_img.setVisibility(View.INVISIBLE);
        wxpay_img.setVisibility(View.VISIBLE);
        up_img.setVisibility(View.INVISIBLE);
        break;
      case UN_APP:
        alipay_img.setVisibility(View.INVISIBLE);
        wxpay_img.setVisibility(View.INVISIBLE);
        up_img.setVisibility(View.VISIBLE);
    }
  }

  // private void pay(final String data) {
  // PayAsyncTask payAsyncTask =
  // new PayAsyncTask(getActivity(), data, new PayAsyncTask.PayResultCallback() {
  // @Override
  // public void paySuccess() {
  // T.show("支付成功");
  // PayEventUtil.paySuccessEvent(getActivity(), price);
  // PayResultActivity.launch(getActivity(), true, orderId);
  // getActivity().finish();
  // }
  //
  // @Override
  // public void payFailed(String msg) {
  // MobclickAgent.onEvent(getActivity(), Constants.APP_EVENT.EVENT_PAY_FAILED);
  // PayResultActivity.launch(getActivity(), false, orderId);
  // }
  // });
  // payAsyncTask.execute();
  // }


  private void payWX(int priceInt) {
    if (BCPay.isWXAppInstalledAndSupported() &&
        BCPay.isWXPaySupported()) {
      Map<String, String> optional = new HashMap<String, String>();
      optional.put("order_id", orderId);
      optional.put("user_id", AccountManager.getInstance().getUser().getUser_id());
      BCPay.getInstance(getActivity()).reqWXPaymentAsync(
          "维依悬赏", // 订单标题
          priceInt, // 订单金额（分）
          genBillNum(), // 订单流水号
          optional, // 扩展参数(可以null)
          bcCallback); // 支付完成后回调入口
    } else {
      Toast.makeText(getActivity(),
          "您尚未安装微信或者安装的微信版本不支持", Toast.LENGTH_LONG).show();
      dismissLoadingView();
    }
  }

  private void payZFB(int priceInt) {
    BCPay.PayParams payParam = new BCPay.PayParams();
    payParam.channelType = BCReqParams.BCChannelTypes.ALI_APP;

    // 商品描述
    payParam.billTitle = "维依悬赏";

    // 支付金额，以分为单位，必须是正整数
    payParam.billTotalFee = priceInt;

    // 商户自定义订单号
    payParam.billNum = genBillNum();
    payParam.optional = new HashMap<>();
    payParam.optional.put("order_id", orderId);
    payParam.optional.put("user_id", AccountManager.getInstance().getUser().getUser_id());

    // 第二个参数实现BCCallback接口，在done方法中查看支付结果
    BCPay.getInstance(getActivity()).reqPaymentAsync(payParam, bcCallback);
  }

  private void payYL(int priceInt) {

    BCPay.PayParams payParam = new BCPay.PayParams();
    payParam.channelType = BCReqParams.BCChannelTypes.UN_APP;

    // 商品描述
    payParam.billTitle = "维依悬赏";

    // 支付金额，以分为单位，必须是正整数
    payParam.billTotalFee = priceInt;

    // 商户自定义订单号
    payParam.billNum = genBillNum();

    // 第二个参数实现BCCallback接口，在done方法中查看支付结果
    BCPay.getInstance(getActivity()).reqPaymentAsync(payParam, bcCallback);
  }

  private String genBillNum() {
    return DateFormat.format("yyyyMMddHHmmss", System.currentTimeMillis()) + orderId;
  }

  BCCallback bcCallback = new BCCallback() {
    @Override
    public void done(final BCResult bcResult) {
      final BCPayResult bcPayResult = (BCPayResult) bcResult;
      // 此处关闭loading界面
      dismissLoadingView();
      Log.i("aaaaaaaaaaa", "bcResult");
      // 根据你自己的需求处理支付结果
      String result = bcPayResult.getResult();

      /*
       * 注意！
       * 所有支付渠道建议以服务端的状态金额为准，此处返回的RESULT_SUCCESS仅仅代表手机端支付成功
       */
      String toastMsg;
      if (result.equals(BCPayResult.RESULT_SUCCESS)) {
        toastMsg = "用户支付成功";
        PayEventUtil.paySuccessEvent(getActivity(), price);
        PayResultActivity.launch(getActivity(), true, orderId);
        EventBus.getDefault().post(new NoticeEvent(NoticeEvent.ORDER_STATUS_CHANGED));
        getActivity().finish();
      } else if (result.equals(BCPayResult.RESULT_CANCEL)) {
        toastMsg = "用户取消支付";
      } else if (result.equals(BCPayResult.RESULT_FAIL)) {
        if (bcPayResult.getErrMsg().equals(BCPayResult.FAIL_PLUGIN_NOT_INSTALLED)) {
          //银联需要重新安装控件
          needToUpdateUPpayplugin();
          return;
        }

        MobclickAgent.onEvent(getActivity(), Constants.APP_EVENT.EVENT_PAY_FAILED);
        PayResultActivity.launch(getActivity(), false, orderId);
        toastMsg = "支付失败, 原因: " + bcPayResult.getErrCode() +
            " # " + bcPayResult.getErrMsg() +
            " # " + bcPayResult.getDetailInfo();

        /**
         * 你发布的项目中不应该出现如下错误，此处由于支付宝政策原因，
         * 不再提供支付宝支付的测试功能，所以给出提示说明
         */
        if (bcPayResult.getErrMsg().equals("PAY_FACTOR_NOT_SET") &&
            bcPayResult.getDetailInfo().startsWith("支付宝参数")) {
          toastMsg = "支付失败：由于支付宝政策原因，故不再提供支付宝支付的测试功能，给您带来的不便，敬请谅解";
        }

        /**
         * 以下是正常流程，请按需处理失败信息
         */
        android.util.Log.e("pay", toastMsg);
      } else if (result.equals(BCPayResult.RESULT_UNKNOWN)) {
        // 可能出现在支付宝8000返回状态
        toastMsg = "订单状态未知";
      } else {
        toastMsg = "invalid return";
      }

      final String finalToastMsg = toastMsg;
      MainThreadPostUtils.post(new Runnable() {
        @Override
        public void run() {
          T.show(finalToastMsg);
        }
      });
    }
  };

  private void needToUpdateUPpayplugin() {
    MainThreadPostUtils.post(new Runnable() {
      @Override
      public void run() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("提示");
        builder.setMessage("完成支付需要安装或者升级银联支付控件，是否安装？");
        builder.setNegativeButton("确定",
                new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialog, int which) {
                    UPPayAssistEx.installUPPayPlugin(getActivity());
                    dialog.dismiss();
                  }
                });

        builder.setPositiveButton("取消",
                new DialogInterface.OnClickListener() {

                  @Override
                  public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                  }
                });
        builder.create().show();
      }
    });
  }
}
