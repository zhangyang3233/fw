package com.hongyu.reward.request;

import com.hongyu.reward.config.Constants;
import com.hongyu.reward.http.BaseHttpRequestBuilder;
import com.hongyu.reward.model.PublishReqeustModel;

/**
 * Created by zhangyang131 on 16/9/13.
 */
public class PublishRequestBuilder extends BaseHttpRequestBuilder<PublishReqeustModel> {
  public static final String SHOP_NAME = "shop_name";
  public static final String USERNUM = "usernum";
  public static final String PRICE = "price";
  public static final String LOCATION = "location";
  public static final String TYPE = "type"; // 0:即时悬赏, 1:预约悬赏
  public static final String MAPUID = "mapuid";
  String shop_name;
  String usernum;
  String price;
  String location;
  int type;
  String mapuid;

  public PublishRequestBuilder(String shop_name, String usernum, String price, String location,
      int type, String mapuid) {
    this.shop_name = shop_name;
    this.usernum = usernum;
    this.price = price;
    this.location = location;
    this.type = type;
    this.mapuid = mapuid;
  }

  @Override
  protected String getApiUrl() {
    return Constants.Server.API_ORDER_PUBLISH;
  }

  @Override
  protected Class<PublishReqeustModel> getResponseClass() {
    return PublishReqeustModel.class;
  }

  @Override
  protected void setParams(Params params) {
    super.setParams(params);
    checkNullAndSet(params, SHOP_NAME, shop_name);
    checkNullAndSet(params, USERNUM, usernum);
    checkNullAndSet(params, PRICE, price);
    checkNullAndSet(params, LOCATION, location);
    checkNullAndSet(params, TYPE, type);
    checkNullAndSet(params, MAPUID, mapuid);
  }
}
