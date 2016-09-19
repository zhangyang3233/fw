package com.hongyu.reward.request;

import com.hongyu.reward.config.Constants;
import com.hongyu.reward.http.BaseHttpRequestBuilder;
import com.hongyu.reward.model.AddRewardModel;

/**
 * Created by zhangyang131 on 16/9/19.
 */
public class AddRewardRequestBuilder extends BaseHttpRequestBuilder<AddRewardModel> {
  private static final String ORDER_ID = "order_id";
  private static final String PRICE = "price";
  private String order_id;
  private String price;

  public AddRewardRequestBuilder(String order_id, String price) {
    this.order_id = order_id;
    this.price = price;
  }

  @Override
  protected String getApiUrl() {
    return Constants.Server.API_ORDER_ADD_PRICE;
  }

  @Override
  protected Class<AddRewardModel> getResponseClass() {
    return AddRewardModel.class;
  }

  @Override
  protected void setParams(Params params) {
    super.setParams(params);
    checkNullAndSet(params, ORDER_ID, order_id);
    checkNullAndSet(params, PRICE, price);
  }
}
