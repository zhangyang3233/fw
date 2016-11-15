package com.hongyu.reward.request;

import com.hongyu.reward.config.Constants;
import com.hongyu.reward.http.BaseHttpRequestBuilder;
import com.hongyu.reward.model.BaseModel;

/**
 * Created by zhangyang131 on 2016/11/14.
 */

public class UpdateLocationRequestBuidler extends BaseHttpRequestBuilder<BaseModel> {
    private static final String LOCATION = "location";
    private String location;

    public UpdateLocationRequestBuidler(String location) {
        this.location = location;
    }

    @Override
    protected String getApiUrl() {
        return Constants.Server.API_UPLOAD_LOCATION;
    }

    @Override
    protected Class<BaseModel> getResponseClass() {
        return BaseModel.class;
    }

    @Override
    protected void setParams(Params params) {
        super.setParams(params);
        checkNullAndSet(params, LOCATION, location);
    }
}
