package com.hongyu.reward.request;

import com.hongyu.reward.config.Constants;
import com.hongyu.reward.http.BaseHttpRequestBuilder;
import com.hongyu.reward.model.OrderCommentModel;

/**
 * Created by zhangyang131 on 16/10/12.
 */
public class GetOrderCommentRequestBuider extends BaseHttpRequestBuilder<OrderCommentModel> {
  private static final String ORDER_ID = "order_id";
  private String orderId;

  public GetOrderCommentRequestBuider(String orderId) {
    this.orderId = orderId;
  }

  @Override
  protected String getApiUrl() {
    return Constants.Server.API_COMMENT_ITEM;
  }

  @Override
  protected Class<OrderCommentModel> getResponseClass() {
    return OrderCommentModel.class;
  }

  @Override
  protected void setParams(Params params) {
    super.setParams(params);
    checkNullAndSet(params, ORDER_ID, orderId);
  }
}
