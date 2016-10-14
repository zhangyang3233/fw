package com.hongyu.reward.request;

import com.hongyu.reward.config.Constants;
import com.hongyu.reward.http.BaseHttpRequestBuilder;
import com.hongyu.reward.model.BaseModel;

/**
 * Created by zhangyang131 on 16/10/4.
 */
public class CommentRequsetBuilder extends BaseHttpRequestBuilder<BaseModel> {
  private static final String ORDER_ID = "order_id";
  private static final String SCORE = "score";
  private static final String TAG = "tag";
  private static final String CONTENT = "content";
  private String order_id;
  private String score;
  private String tag;
  private String content;

  public CommentRequsetBuilder(String order_id, String score, String tag, String content) {
    this.order_id = order_id;
    this.score = score;
    if(tag == null){
      this.tag = "";
    }else{
      this.tag = tag;
    }
    this.content = content;
  }

  @Override
  protected String getApiUrl() {
    return Constants.Server.API_COMMENT_ORDER;
  }

  @Override
  protected Class<BaseModel> getResponseClass() {
    return BaseModel.class;
  }

  @Override
  protected void setParams(Params params) {
    super.setParams(params);
    checkNullAndSet(params, ORDER_ID, order_id);
    checkNullAndSet(params, SCORE, score);
    params.put(TAG, tag);
    checkNullAndSet(params, CONTENT, content);
  }
}
