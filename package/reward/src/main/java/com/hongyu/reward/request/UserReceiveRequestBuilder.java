package com.hongyu.reward.request;

import com.hongyu.reward.config.Constants;
import com.hongyu.reward.http.BaseHttpRequestBuilder;
import com.hongyu.reward.model.BaseModel;

/**
 * 使用/不使用领赏人
 * Created by zhangyang131 on 16/10/4.
 */
public class UserReceiveRequestBuilder extends BaseHttpRequestBuilder<BaseModel> {
  /**
   * 不使用
   */
  public static final String IS_REPLACE = "0";
  /**
   * 使用
   */
  public static final String IS_USE = "1";
  private static final String ORDER_ID = "order_id";
  private static final String RECEIVE_ID = "receive_id";
  private static final String ISUSE = "isuse";

  String order_id;
  String receive_id;
  String isuse; // 0:不使用;1:使用;

  public UserReceiveRequestBuilder(String order_id, String receive_id, String isuse) {
    this.order_id = order_id;
    this.receive_id = receive_id;
    this.isuse = isuse;
  }

  @Override
  protected String getApiUrl() {
    return Constants.Server.API_ORDER_USERECE;
  }

  @Override
  protected Class<BaseModel> getResponseClass() {
    return BaseModel.class;
  }

  @Override
  protected void setParams(Params params) {
    super.setParams(params);
    checkNullAndSet(params, ORDER_ID, order_id);
    checkNullAndSet(params, RECEIVE_ID, receive_id);
    checkNullAndSet(params, ISUSE, isuse);
  }
}
