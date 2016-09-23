package com.hongyu.reward.request;

import com.hongyu.reward.config.Constants;
import com.hongyu.reward.http.BaseHttpRequestBuilder;
import com.hongyu.reward.model.OrderListModel;

/**
 * Created by zhangyang131 on 16/9/20.
 */
public class GetOrderListRequestBuilder extends BaseHttpRequestBuilder<OrderListModel> {
  private static final String STATUS = "status";
  private static final String ISME = "isme";
  private static final String PAGE = "page";
  private String status;
  private String isme;
  private String page;

  public GetOrderListRequestBuilder(String status, String isme, String page) {
    this.status = status;
    this.isme = isme;
    this.page = page;
  }

  @Override
  protected String getApiUrl() {
    return Constants.Server.API_ORDER_MY;
  }

  @Override
  protected Class<OrderListModel> getResponseClass() {
    return OrderListModel.class;
  }

  @Override
  protected void setParams(Params params) {
    super.setParams(params);
    checkNullAndSet(params, STATUS, status);
    checkNullAndSet(params, ISME, isme);
    checkNullAndSet(params, PAGE, page);
  }
}
