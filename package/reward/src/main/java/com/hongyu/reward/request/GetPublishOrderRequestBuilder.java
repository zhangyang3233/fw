package com.hongyu.reward.request;

import com.hongyu.reward.config.Constants;
import com.hongyu.reward.http.BaseHttpRequestBuilder;
import com.hongyu.reward.model.OrderIdRequestModel;

/**
 * Created by zhangyang131 on 16/10/5.
 */
public class GetPublishOrderRequestBuilder extends BaseHttpRequestBuilder<OrderIdRequestModel> {

    @Override
    protected String getApiUrl() {
        return Constants.Server.API_ORDER_ACTIVE;
    }

    @Override
    protected Class<OrderIdRequestModel> getResponseClass() {
        return OrderIdRequestModel.class;
    }
}
