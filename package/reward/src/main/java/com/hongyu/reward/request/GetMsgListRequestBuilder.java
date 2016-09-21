package com.hongyu.reward.request;

import com.hongyu.reward.config.Constants;
import com.hongyu.reward.http.BaseHttpRequestBuilder;
import com.hongyu.reward.model.MsgModel;

/**
 * Created by zhangyang131 on 16/9/21.
 */
public class GetMsgListRequestBuilder extends BaseHttpRequestBuilder<MsgModel> {
  private static final String PAGE = "page";
  private String page;

  public GetMsgListRequestBuilder(String page) {
    this.page = page;
  }

  @Override
  protected String getApiUrl() {
    return Constants.Server.API_MESSAGE_LIST;
  }

  @Override
  protected Class<MsgModel> getResponseClass() {
    return MsgModel.class;
  }

  @Override
  protected void setParams(Params params) {
    super.setParams(params);
    checkNullAndSet(params, PAGE, page);
  }
}
