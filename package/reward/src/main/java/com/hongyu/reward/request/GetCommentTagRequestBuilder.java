package com.hongyu.reward.request;

import com.hongyu.reward.config.Constants;
import com.hongyu.reward.http.JSONObjectHttpRequestBuilder;

/**
 * Created by zhangyang131 on 16/10/12.
 */
public class GetCommentTagRequestBuilder extends JSONObjectHttpRequestBuilder {

    @Override
    protected String getApiUrl() {
        return Constants.Server.API_COMMENT_TAG;
    }
}
