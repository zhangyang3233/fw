package com.hongyu.reward.request;

import com.hongyu.reward.config.Constants;
import com.hongyu.reward.http.JSONObjectHttpRequestBuilder;

/**
 * Created by zhangyang131 on 16/9/21.
 */
public class GetMyEvaluateRequestBuilder extends JSONObjectHttpRequestBuilder {
  private static final String ISME = "isme";
  private static final String PAGE = "page";
  private int isme; // 0 别人对我的评论, 1是我对别人的评论
  private int page; // 我对别人的评论需要传入此参数,页数

  public GetMyEvaluateRequestBuilder(int isme, int page) {
    this.isme = isme;
    this.page = page;
  }

  @Override
  protected String getApiUrl() {
    return Constants.Server.API_COMMENT_MY;
  }


  @Override
  protected void setParams(Params params) {
    super.setParams(params);
    checkNullAndSet(params, ISME, String.valueOf(isme));
    checkNullAndSet(params, PAGE, String.valueOf(page));
  }
}
