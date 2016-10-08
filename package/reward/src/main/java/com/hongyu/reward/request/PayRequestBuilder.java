package com.hongyu.reward.request;

import com.hongyu.reward.config.Constants;
import com.hongyu.reward.http.BaseHttpRequestBuilder;
import com.hongyu.reward.model.SignResultModel;

/**
 * Created by zhangyang131 on 16/10/8.
 */
public class PayRequestBuilder extends BaseHttpRequestBuilder<SignResultModel> {
    private static final String ORDER_ID = "orderId";
    private String order_id;

    public PayRequestBuilder(String order_id) {
        this.order_id = order_id;
    }

    @Override
    protected String getApiUrl() {
        return Constants.Server.API_PAY_SIGN;
    }

    @Override
    protected Class<SignResultModel> getResponseClass() {
        return SignResultModel.class;
    }

    @Override
    protected void setParams(Params params) {
        super.setParams(params);
        checkNullAndSet(params, ORDER_ID, order_id);
    }
}
