package com.hongyu.reward.pay;

import android.app.Activity;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;

import java.util.Map;

/**
 * Created by zhangyang131 on 16/10/8.
 */
public class PayAsyncTask extends AsyncTask<String, String ,Map<String, String>> {
    private String sign;
    private Activity activity;
    private PayResultCallback callback;

    public PayAsyncTask(Activity activity, String sign, PayResultCallback callback) {
        this.sign = sign;
        this.activity = activity;
        this.callback = callback;
    }

    @Override
    protected Map<String, String> doInBackground(String... params) {
        PayTask alipay = new PayTask(activity);
        Map<String, String> result = alipay.payV2(sign, true);
        return result;
    }

    @Override
    protected void onPostExecute(Map<String, String> stringStringMap) {
        super.onPostExecute(stringStringMap);
        PayResult payResult = new PayResult((Map<String, String>) stringStringMap);
        /**
         对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
         */
        String resultInfo = payResult.getResult();// 同步返回需要验证的信息
        String resultStatus = payResult.getResultStatus();
        // 判断resultStatus 为9000则代表支付成功
        if (TextUtils.equals(resultStatus, "9000")) {
            // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
            if(callback != null){
                callback.paySuccess();
            }
        } else {
            // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
            if(callback != null){
                callback.payFailed("支付失败");
            }
        }
    }


    public interface PayResultCallback{
        void paySuccess();
        void payFailed(String msg);
    }
}
