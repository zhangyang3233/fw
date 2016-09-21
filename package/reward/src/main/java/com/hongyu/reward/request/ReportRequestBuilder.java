package com.hongyu.reward.request;

import com.hongyu.reward.config.Constants;
import com.hongyu.reward.http.BaseHttpRequestBuilder;
import com.hongyu.reward.model.BaseModel;

/**
 * Created by zhangyang131 on 16/9/21.
 */
public class ReportRequestBuilder extends BaseHttpRequestBuilder<BaseModel> {
  private static final String REASON = "reason";
  private static final String CONTENT = "content";
  private String reason;
  private String content;

  public ReportRequestBuilder(String reason, String content) {
    this.reason = reason;
    this.content = content;
  }

  @Override
  protected String getApiUrl() {
    return Constants.Server.API_REPORT;
  }

  @Override
  protected Class<BaseModel> getResponseClass() {
    return BaseModel.class;
  }

  @Override
  protected void setParams(Params params) {
    super.setParams(params);
    checkNullAndSet(params, REASON, reason);
    params.put(CONTENT, content);
  }
}
