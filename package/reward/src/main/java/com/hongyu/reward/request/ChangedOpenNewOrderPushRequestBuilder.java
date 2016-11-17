package com.hongyu.reward.request;

import com.hongyu.reward.config.Constants;
import com.hongyu.reward.http.BaseHttpRequestBuilder;
import com.hongyu.reward.model.BaseModel;

/**
 * Created by zhangyang131 on 2016/11/17.
 */

public class ChangedOpenNewOrderPushRequestBuilder extends BaseHttpRequestBuilder<BaseModel> {
  private static final String TYPE = "type"; // type=1 打开推送 type=0 关闭推送
  private PushType type;

  public ChangedOpenNewOrderPushRequestBuilder(PushType type) {
    this.type = type;
  }

  @Override
  protected String getApiUrl() {
    return Constants.Server.API_SET_PUSH_ON_OFF;
  }

  @Override
  protected Class<BaseModel> getResponseClass() {
    return BaseModel.class;
  }

  @Override
  protected void setParams(Params params) {
    super.setParams(params);
    checkNullAndSet(params, TYPE, type.type);
  }

  public enum PushType {

    on(1), off(0);

    private int type;

    private PushType(int type) {
      this.type = type;
    }
  }
}
