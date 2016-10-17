package com.hongyu.reward.request;

import com.hongyu.reward.config.Constants;
import com.hongyu.reward.http.BaseHttpRequestBuilder;
import com.hongyu.reward.model.BillDetailModel;

/**
 * Created by zhangyang131 on 16/10/17.
 */
public class GetBillRequestBuilder extends BaseHttpRequestBuilder<BillDetailModel> {
  public static final String TYPE = "type"; // 账户类型 1:现金账户;2:积分账户;
  public static final String PAGE = "page";
  private String type;
  private String page;

  public GetBillRequestBuilder(String type, String page) {
    this.type = type;
    this.page = page;
  }

  @Override
  protected String getApiUrl() {
    return Constants.Server.API_ACCOUNT_INFO;
  }

  @Override
  protected Class<BillDetailModel> getResponseClass() {
    return BillDetailModel.class;
  }

  @Override
  protected void setParams(Params params) {
    super.setParams(params);
    checkNullAndSet(params, TYPE, type);
    checkNullAndSet(params, PAGE, page);
  }
}
