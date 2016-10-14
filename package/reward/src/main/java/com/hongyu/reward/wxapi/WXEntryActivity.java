package com.hongyu.reward.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

import com.hongyu.reward.R;
import com.hongyu.reward.utils.T;
import com.hongyu.reward.utils.WXUtil;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

/**
 * Created by zhangyang131 on 16/10/13.
 */
public class WXEntryActivity extends AppCompatActivity implements IWXAPIEventHandler {
  private IWXAPI api;

  @Override
  public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
    super.onCreate(savedInstanceState, persistentState);
    api = WXUtil.getApi();
    api.handleIntent(getIntent(), this);
  }

  @Override
  protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    setIntent(intent);
    api.handleIntent(intent, this);
  }

  @Override
  public void onReq(BaseReq baseReq) {
    T.show("asdfasdf");
  }

  @Override
  public void onResp(BaseResp resp) {
    int result = 0;

    switch (resp.errCode) {
      case BaseResp.ErrCode.ERR_OK:
        result = R.string.errcode_success;
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

    T.show(result);
    this.finish();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    api = null;
  }
}
