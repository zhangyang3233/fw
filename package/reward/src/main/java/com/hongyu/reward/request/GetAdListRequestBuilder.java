package com.hongyu.reward.request;

import com.hongyu.reward.config.Constants;
import com.hongyu.reward.http.BaseHttpRequestBuilder;
import com.hongyu.reward.model.AdListModel;

/**
 * Created by zhangyang131 on 16/9/11.
 */
public class GetAdListRequestBuilder extends BaseHttpRequestBuilder<AdListModel> {
    private static final String TYPE = "type";
    private String type;

    public GetAdListRequestBuilder(String type) {
        this.type = type;
    }

    @Override
    protected String getApiUrl() {
        return Constants.Server.API_INDEX_AD;
    }

    @Override
    protected Class<AdListModel> getResponseClass() {
        return AdListModel.class;
    }

    @Override
    protected void setParams(Params params) {
        super.setParams(params);
        checkNullAndSet(params, TYPE, type);
    }
}
