package com.hongyu.reward.request;

import com.hongyu.reward.config.Constants;
import com.hongyu.reward.http.BaseHttpRequestBuilder;
import com.hongyu.reward.model.HotSearchModel;

/**
 * Created by zhangyang131 on 16/10/13.
 */
public class GetHotSearchRequestBuilder extends BaseHttpRequestBuilder<HotSearchModel> {

    @Override
    protected String getApiUrl() {
        return Constants.Server.API_SHOP_TAG;
    }

    @Override
    protected Class<HotSearchModel> getResponseClass() {
        return HotSearchModel.class;
    }
}
