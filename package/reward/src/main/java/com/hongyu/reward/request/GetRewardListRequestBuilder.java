package com.hongyu.reward.request;

import com.hongyu.reward.config.Constants;
import com.hongyu.reward.http.BaseHttpRequestBuilder;
import com.hongyu.reward.model.RewardListModel;

/**
 * 根据某个商家id获取该商家的悬赏信息列表
 * Created by zhangyang131 on 16/9/19.
 */
public class GetRewardListRequestBuilder extends BaseHttpRequestBuilder<RewardListModel> {
  public static final String SHOP_ID = "shop_id";
  public static final String PAGE = "page";
  private String page;
  private String shop_id;

  public GetRewardListRequestBuilder(String page, String shop_id) {
    this.page = page;
    this.shop_id = shop_id;
  }

  @Override
  protected String getApiUrl() {
    return Constants.Server.API_ORDER_LIST;
  }

  @Override
  protected Class<RewardListModel> getResponseClass() {
    return RewardListModel.class;
  }

  @Override
  protected void setParams(Params params) {
    super.setParams(params);
    checkNullAndSet(params, SHOP_ID, shop_id);
    checkNullAndSet(params, PAGE, page);
  }
}
