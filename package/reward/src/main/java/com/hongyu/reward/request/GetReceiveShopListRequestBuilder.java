package com.hongyu.reward.request;

import com.hongyu.reward.config.Constants;
import com.hongyu.reward.http.BaseHttpRequestBuilder;
import com.hongyu.reward.model.ShopListMode;

/**
 * Created by zhangyang131 on 16/9/19.
 */
public class GetReceiveShopListRequestBuilder extends BaseHttpRequestBuilder<ShopListMode> {
    private static final String PAGE = "page";
    private static final String LOCATION = "location";
    private static final String KEYWORD = "keyword";
    private String page;
    private String location;
    private String keyword;

    public GetReceiveShopListRequestBuilder(String page, String location, String keyword) {
        this.page = page;
        this.location = location;
        this.keyword = keyword;
    }

    @Override
    protected String getApiUrl() {
        return Constants.Server.API_SHOP_RECE_LIST;
    }

    @Override
    protected Class<ShopListMode> getResponseClass() {
        return ShopListMode.class;
    }

    @Override
    protected void setParams(Params params) {
        super.setParams(params);
        checkNullAndSet(params, PAGE, page);
        checkNullAndSet(params, LOCATION, "36.013974,110.779576");
        checkNullAndSet(params, KEYWORD, keyword);
    }
}
