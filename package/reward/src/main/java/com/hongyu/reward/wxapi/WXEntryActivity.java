package com.hongyu.reward.wxapi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.fw.zycoder.http.callback.DataCallback;
import com.fw.zycoder.utils.GlobalConfig;
import com.fw.zycoder.utils.Log;
import com.hongyu.reward.R;
import com.hongyu.reward.config.Constants;
import com.hongyu.reward.http.ResponesUtil;
import com.hongyu.reward.model.BaseModel;
import com.hongyu.reward.model.NoticeEvent;
import com.hongyu.reward.request.ShareSuccessRequestBuilder;
import com.hongyu.reward.utils.T;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by zhangyang131 on 16/10/13.
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
  public static final String TAG = WXEntryActivity.class.getSimpleName();
  public static final int TIMELINE_SUPPORTED_VERSION = 0x21020001;
  private IWXAPI api;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.i(TAG, "onCreate");
    api = WXAPIFactory.createWXAPI(this, Constants.WX.AppID, false);
    api.handleIntent(getIntent(), this);
  }

  @Override
  protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    Log.i(TAG, "onNewIntent");
    setIntent(intent);
    api.handleIntent(intent, this);
  }


  @Override
  public void onReq(BaseReq baseReq) {
    Log.i(TAG, "onReq");
    this.finish();
  }

  @Override
  public void onResp(BaseResp resp) {
    Log.i(TAG, "onResp");
    int result = 0;
    switch (resp.errCode) {
      case BaseResp.ErrCode.ERR_OK:
        submitShareSuccess(resp.transaction);
        break;
      case BaseResp.ErrCode.ERR_USER_CANCEL:
        result = R.string.errcode_cancel;
        break;
      case BaseResp.ErrCode.ERR_AUTH_DENIED:
        result = R.string.errcode_deny;
        break;
      default:
        result = R.string.errcode_unknown;
        break;
    }
    Log.i(TAG, "resp:" + resp.errCode + "=+==" + resp.errStr + "====" + resp.transaction);
    if (result != 0) {
      T.show(result);
    }
    this.finish();
  }

  private void submitShareSuccess(String transaction) {
    T.show("分享成功");
    try {
      String[] data = transaction.split("&");
      String shareType = data[0];
      String orderId = data[1];
      Log.i(transaction);
      ShareSuccessRequestBuilder builder = new ShareSuccessRequestBuilder(orderId, shareType);
      builder.setDataCallback(new DataCallback<BaseModel>() {
        @Override
        public void onDataCallback(BaseModel data) {
          if (ResponesUtil.checkModelCodeOK(data)) {
            EventBus.getDefault().post(new NoticeEvent(NoticeEvent.USER_POINT_CHANGED));
          }
        }
      });
      builder.build().submit();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    Log.i(TAG, "onDestroy");
    api = null;
  }



  private static String buildTransaction(String shareType, String orderId) {
    return shareType + "&" + orderId;
  }

  public static void receiveShare(IWXAPI api, String shopName, int type, String orderId) {
    WXWebpageObject webpage = new WXWebpageObject();
    webpage.webpageUrl = Constants.WX.share_app + "shop_name=" + shopName;
    WXMediaMessage msg = new WXMediaMessage(webpage);
    msg.title = "请叫我活雷锋！";
    msg.description =
        "把餐厅排队号让给了更需要的人，而我离买保时捷又近了一步！";
    // Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.send_music_thumb);
    // msg.thumbData = Util.bmpToByteArray(thumb, true);
    SendMessageToWX.Req req = new SendMessageToWX.Req();
    req.transaction = buildTransaction("2", orderId);
    req.message = msg;
    if (type == 1) {
      req.scene = SendMessageToWX.Req.WXSceneSession;
    } else if (type == 2) {
      req.scene = SendMessageToWX.Req.WXSceneTimeline;
    }
    api.sendReq(req);
  }

  /**
   *
   * @param order_id
   * @param save_seat
   * @param save_time
   * @param type 1分享微信， 2分享朋友圈
   */
  public static void publishShare(IWXAPI api, String order_id, String save_seat, String save_time,
      int type, String orderId) {
    WXWebpageObject webpage = new WXWebpageObject();
    webpage.webpageUrl = Constants.WX.share_shop + "order_id=" + order_id + "&save_time="
        + save_time + "&save_seat=" + save_seat;
    WXMediaMessage msg = new WXMediaMessage(webpage);
    msg.title = "厉害了，word饭桶们";
    msg.description = "妈妈再也不用担心我为了吃饭而苦逼地排队了！";
    // Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.send_music_thumb);
    // msg.thumbData = Util.bmpToByteArray(thumb, true);
    SendMessageToWX.Req req = new SendMessageToWX.Req();
    req.transaction = buildTransaction("1", orderId);
    req.message = msg;
    if (type == 1) {
      req.scene = SendMessageToWX.Req.WXSceneSession;
    } else if (type == 2) {
      req.scene = SendMessageToWX.Req.WXSceneTimeline;
    }

    api.sendReq(req);
  }

  private static String getAppName() {
    return GlobalConfig.getAppContext().getResources().getString(R.string.app_name_reward);
  }

  public static IWXAPI registWX(Context context) {
    IWXAPI api = WXAPIFactory.createWXAPI(context, Constants.WX.AppID, false);
    return api;
  }
}
