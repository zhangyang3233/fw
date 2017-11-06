package com.hongyu.reward.ui.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.fw.zycoder.http.callback.DataCallback;
import com.fw.zycoder.utils.MainThreadPostUtils;
import com.hongyu.reward.R;
import com.hongyu.reward.appbase.BaseLoadFragment;
import com.hongyu.reward.config.Constants;
import com.hongyu.reward.http.ResponesUtil;
import com.hongyu.reward.manager.AccountManager;
import com.hongyu.reward.model.dbmodel.XcModel;
import com.hongyu.reward.request.XCJoinRequestBuilder;
import com.hongyu.reward.request.XCTryJoinRequestBuilder;
import com.hongyu.reward.ui.adapter.PayChooseAdapter;
import com.hongyu.reward.utils.T;
import com.unionpay.UPPayAssistEx;

import java.util.HashMap;
import java.util.Map;

import cn.beecloud.BCPay;
import cn.beecloud.BeeCloud;
import cn.beecloud.async.BCCallback;
import cn.beecloud.async.BCResult;
import cn.beecloud.entity.BCPayResult;
import cn.beecloud.entity.BCReqParams;


/**
 * Created by zhangyang131 on 2017/8/15.
 */

public class XCHDFragment extends BaseLoadFragment implements View.OnClickListener {
    View info_layout;
    CardView xc_card;
    TextView code_num;
    View pay;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BeeCloud.setAppIdAndSecret(Constants.BeeCloud.APP_ID, Constants.BeeCloud.APP_SECRET);
        BCPay.initWechatPay(getActivity(), Constants.WX.AppID);
    }

    @Override
    protected void onInflated(View contentView, Bundle savedInstanceState) {
        info_layout = contentView.findViewById(R.id.info_layout);
        xc_card = (CardView) contentView.findViewById(R.id.xc_card);
        code_num = (TextView) contentView.findViewById(R.id.code_num);
        pay = contentView.findViewById(R.id.pay);
        pay.setOnClickListener(this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_xchd;
    }


    @Override
    protected void onStartLoading() {
        tryJoin();
    }

    private void hasJoinGetInfo() {
        getCodeNumber();
    }

    private void tryJoin() {
        showLoadingView();
        XCTryJoinRequestBuilder builder = new XCTryJoinRequestBuilder(AccountManager.getInstance().getUser().getUser_id(), "99999999");
        builder.setDataCallback(new DataCallback<XcModel>() {
            @Override
            public void onDataCallback(XcModel data) {
                if (!isAdded()) {
                    return;
                }
                dismissLoadingView();
                if (ResponesUtil.checkModelCodeOK(data)) {
                    // 可以参加活动
                    showPayPage();
                } else if (data.getCode() == 406) { // 该活动当前当日限额已满
                    T.show(ResponesUtil.getErrorMsg(data));
                    hasJoinGetInfo();
                } else if (data.getCode() == 403) { // 当前已经参加过活动
                    showCodePage(data.getData());
                } else {
                    T.show(ResponesUtil.getErrorMsg(data));
                }
            }
        });
        builder.build().submit();
    }


    private void showPayPage() {
        info_layout.setVisibility(View.VISIBLE);
        xc_card.setVisibility(View.GONE);
        pay.setVisibility(View.VISIBLE);
    }

    private void showCodePage(String code) {
        info_layout.setVisibility(View.GONE);
        xc_card.setVisibility(View.VISIBLE);
        pay.setVisibility(View.GONE);
        code_num.setText("您参与的活动号码为：\n" + code);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pay:
                showPayDialog();
                break;
        }
    }

    private void showPayDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String items[] = new String[]{"支付宝", "微信"};
        int[] imgs = new int[]{R.mipmap.alipay, R.mipmap.wxpay};
        builder.setTitle("选择支付方式");//设置标题
        PayChooseAdapter adapter = new PayChooseAdapter(items, imgs, getActivity());
        DialogInterface.OnClickListener listener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                        switch (which) {
                            case 0: // 支付宝
                                payZFB(100);
                                break;
                            case 1: // 微信
                                payWX(100);
                                break;
                        }
                    }
                };
        builder.setAdapter(adapter, listener);
        builder.create().show();
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
        payParam.optional.put("user_id", AccountManager.getInstance().getUser().getUser_id());

        // 第二个参数实现BCCallback接口，在done方法中查看支付结果
        BCPay.getInstance(getActivity()).reqPaymentAsync(payParam, bcCallback);
    }

    private String genBillNum() {
        return DateFormat.format("yyyyMMddHHmmss", System.currentTimeMillis()) + AccountManager.getInstance().getUser().getUser_id();
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
                showProgressDialog();
            } else if (result.equals(BCPayResult.RESULT_CANCEL)) {
                toastMsg = "用户取消支付";
                getCodeNumber();
            } else if (result.equals(BCPayResult.RESULT_FAIL)) {
                if (bcPayResult.getErrMsg().equals(BCPayResult.FAIL_PLUGIN_NOT_INSTALLED)) {
                    //银联需要重新安装控件
                    needToUpdateUPpayplugin();
                    return;
                }

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
    ProgressDialog progressDialog;

    private void showProgressDialog() {
        //this表示该对话框是针对当前Activity的
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        //设置最大值为100
        progressDialog.setMax(100);
        //设置进度条风格STYLE_HORIZONTAL
        progressDialog.setProgressStyle(
                ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("悬赏中");
        progressDialog.show();
        new Thread() {
            public void run() {
                for(int i=0; i<=100; i++) {
                    final   int p = i;
                    MainThreadPostUtils.post(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.setProgress(p);
                        }
                    });
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                MainThreadPostUtils.post(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        getCodeNumber();
                    }
                });
            }
        }.start();
    }

    private void getCodeNumber() {
        XCJoinRequestBuilder builder = new XCJoinRequestBuilder(AccountManager.getInstance().getUser().getUser_id(), "99999999", "100");
        builder.setDataCallback(new DataCallback<XcModel>() {
            @Override
            public void onDataCallback(XcModel data) {
                if (!isAdded()) {
                    return;
                }
                if (ResponesUtil.checkModelCodeOK(data)) {
                } else if (data.getCode() == 406) { // 该活动当前当日限额已满
                    T.show(ResponesUtil.getErrorMsg(data));
                } else if (data.getCode() == 403) { // 当前已经参加过活动
                    showCodePage(data.getData());
                }
            }
        });
        builder.build().submit();
    }

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

    private void payWX(int priceInt) {
        if (BCPay.isWXAppInstalledAndSupported() &&
                BCPay.isWXPaySupported()) {
            Map<String, String> optional = new HashMap<String, String>();
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
}
