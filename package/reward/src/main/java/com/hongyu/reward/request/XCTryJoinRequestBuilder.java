package com.hongyu.reward.request;

import com.android.volley.Request;
import com.hongyu.reward.config.Constants;
import com.hongyu.reward.http.BaseHttpRequestBuilder;
import com.hongyu.reward.model.dbmodel.XcModel;

/**
 * Created by zhangyang131 on 2017/8/16.
 */

public class XCTryJoinRequestBuilder extends BaseHttpRequestBuilder<XcModel> {
    private static final String SHOP_ID = "shop_id";
    private static final String USER_ID = "user_id";
    private String user_id;
    private String shop_id;

    public XCTryJoinRequestBuilder(String user_id, String shop_id) {
        this.user_id = user_id;
        this.shop_id = shop_id;
        setMethod(Request.Method.GET);
    }

    @Override
    protected String getApiUrl() {
        return Constants.Server.API_XICHA_TRY_JOIN;
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
    }
}
