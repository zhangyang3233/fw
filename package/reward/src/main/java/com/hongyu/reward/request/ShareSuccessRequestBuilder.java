package com.hongyu.reward.request;

import com.hongyu.reward.config.Constants;
import com.hongyu.reward.http.BaseHttpRequestBuilder;
import com.hongyu.reward.model.BaseModel;

/**
 * Created by zhangyang131 on 2016/10/25.
 */
public class ShareSuccessRequestBuilder extends BaseHttpRequestBuilder<BaseModel> {
  private static final String ORDER_ID = "order_id";
  private static final String TYPE = "type";
  private String order_id;
  private String type; // 悬赏者分享 1；领赏者分享 2

  /**
   * 分享后调用增加积分
   * 
   * @param order_id
   * @param type 悬赏者分享 1；领赏者分享 2
   */
  public ShareSuccessRequestBuilder(String order_id, String type) {
    this.order_id = order_id;
    this.type = type;
  }

  @Override
  protected String getApiUrl() {
    return Constants.Server.API_SHARE_POINT;
  }

  @Override
  protected Class<BaseModel> getResponseClass() {
    return BaseModel.class;
  }

  @Override
  protected void setParams(Params params) {
    super.setParams(params);
    checkNullAndSet(params, ORDER_ID, order_id);
    checkNullAndSet(params, TYPE, type);
  }
}
