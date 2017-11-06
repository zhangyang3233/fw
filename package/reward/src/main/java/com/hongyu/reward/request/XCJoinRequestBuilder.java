package com.hongyu.reward.request;

import com.android.volley.Request;
import com.hongyu.reward.config.Constants;
import com.hongyu.reward.http.BaseHttpRequestBuilder;
import com.hongyu.reward.model.dbmodel.XcModel;

/**
 * Created by zhangyang131 on 2017/8/16.
 */

public class XCJoinRequestBuilder extends BaseHttpRequestBuilder<XcModel> {
    public static final String SHOP_ID = "shop_id";
    public static final String USER_ID = "user_id";
    public static final String PRICE = "price";
    private String user_id;
    private String shop_id;
    private String price;

    public XCJoinRequestBuilder(String user_id, String shop_id, String price) {
        this.user_id = user_id;
        this.shop_id = shop_id;
        this.price = price;
        setMethod(Request.Method.POST);
    }

    @Override
    protected String getApiUrl() {
        return Constants.Server.API_XICHA_JOIN;
    }

    @Override
    protected Class<XcModel> getResponseClass() {
        return XcModel.class;
    }

    @Override
    protected void setParams(Params params) {
        super.setParams(params);
        checkNullAndSet(params, SHOP_ID, shop_id);
        checkNullAndSet(params, USER_ID, user_id);
        checkNullAndSet(params, PRICE, price);
    }
}
