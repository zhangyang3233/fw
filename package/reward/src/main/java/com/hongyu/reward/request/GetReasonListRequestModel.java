package com.hongyu.reward.request;

import com.hongyu.reward.config.Constants;
import com.hongyu.reward.http.BaseHttpRequestBuilder;
import com.hongyu.reward.model.ReasonModel;

/**
 * Created by zhangyang131 on 16/9/21.
 */
public class GetReasonListRequestModel extends BaseHttpRequestBuilder<ReasonModel> {

  @Override
  protected String getApiUrl() {
    return Constants.Server.API_REPORT_INFO;
  }

  @Override
  protected Class<ReasonModel> getResponseClass() {
    return ReasonModel.class;
  }
}
