package com.hongyu.reward.request;

import com.hongyu.reward.config.Constants;
import com.hongyu.reward.http.BaseHttpRequestBuilder;
import com.hongyu.reward.model.ReceiveInfoModel;

/**
 * Created by zhangyang131 on 16/10/4.
 */
public class GetReceivePersonInfoRequestBuilder extends BaseHttpRequestBuilder<ReceiveInfoModel> {
  private static final String ORDER_ID = "order_id";
  private String order_id;

  public GetReceivePersonInfoRequestBuilder(String order_id) {
    this.order_id = order_id;
  }

  @Override
  protected String getApiUrl() {
    return Constants.Server.API_ORDER_RECELIST;
  }

  @Override
  protected Class<ReceiveInfoModel> getResponseClass() {
    return ReceiveInfoModel.class;
  }

  @Override
  protected void setParams(Params params) {
    super.setParams(params);
    checkNullAndSet(params, ORDER_ID, order_id);
  }
}
