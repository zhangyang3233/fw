package com.hongyu.reward.request;

import com.hongyu.reward.config.Constants;
import com.hongyu.reward.http.BaseHttpRequestBuilder;
import com.hongyu.reward.model.BaseModel;

/**
 * Created by zhangyang131 on 16/9/19.
 */
public class CancelOrderRequestBuilder extends BaseHttpRequestBuilder<BaseModel> {
  private static final String ORDER_ID = "order_id";
  private String order_id;

  public CancelOrderRequestBuilder(String order_id) {
    this.order_id = order_id;
  }

  @Override
  protected String getApiUrl() {
    return Constants.Server.API_ORDER_CANCEL;
  }

  @Override
  protected Class<BaseModel> getResponseClass() {
    return BaseModel.class;
  }

  @Override
  protected void setParams(Params params) {
    super.setParams(params);
    checkNullAndSet(params, ORDER_ID, order_id);
  }
}
