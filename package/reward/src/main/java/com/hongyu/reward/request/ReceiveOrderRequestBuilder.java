package com.hongyu.reward.request;

import com.hongyu.reward.config.Constants;
import com.hongyu.reward.http.BaseHttpRequestBuilder;
import com.hongyu.reward.model.BaseModel;

/**
 *
 * 领取任务
 * Created by zhangyang131 on 16/9/20.
 */
public class ReceiveOrderRequestBuilder extends BaseHttpRequestBuilder<BaseModel> {
  private static final String ORDER_ID = "order_id";
  private static final String RANK_NUM = "rank_num";
  private static final String TABLE_NUM = "table_num";
  private static final String WAIT_NUM = "wait_num";
  private static final String TICKET_IMG = "ticket_img";
  private String order_id;
  private String rank_num;
  private String table_num;
  private String wait_num;
  private String ticket_img;

  /**
   *
   * @param order_id
   * @param wait_num 前面还有几位等待
   * @param rank_num 排位号
   * @param table_num 本桌人数
     */
  public ReceiveOrderRequestBuilder(String order_id, String wait_num, String rank_num,
      String table_num) {
    this.order_id = order_id;
    this.wait_num = wait_num;
    this.rank_num = rank_num;
    this.table_num = table_num;
  }

  public ReceiveOrderRequestBuilder(String order_id, String ticket_img){
    this.order_id = order_id;
    this.ticket_img = ticket_img;
  }

  @Override
  protected String getApiUrl() {
    return Constants.Server.API_ORDER_RECEIVER;
  }

  @Override
  protected Class<BaseModel> getResponseClass() {
    return BaseModel.class;
  }

  @Override
  protected void setParams(Params params) {
    super.setParams(params);
    checkNullAndSet(params, ORDER_ID, order_id);
    checkNullAndSet(params, RANK_NUM, rank_num);
    checkNullAndSet(params, TABLE_NUM, table_num);
    checkNullAndSet(params, WAIT_NUM, wait_num);
    checkNullAndSet(params, TICKET_IMG, ticket_img);
  }
}
