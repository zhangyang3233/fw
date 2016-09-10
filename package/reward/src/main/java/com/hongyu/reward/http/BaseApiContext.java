package com.hongyu.reward.http;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.fw.zycoder.http.volley.ApiContext;

import java.util.Map;

/**
 * Created by zhangyang131 on 16/9/9.
 */
public class BaseApiContext implements ApiContext {
    private final Context mContext;
    private RequestQueue mRequestQueue;

    public BaseApiContext(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public Context getContext() {
        return this.mContext;
    }

    @Override
    public RequestQueue getRequestQueue() {
        if(mRequestQueue == null){
            mRequestQueue = createRequestQueue();
        }
        return mRequestQueue;
    }

    @Override
    public Map<String, String> getHeaders() {
        return null;
    }

    @Override
    public String getCacheKeyPostfix() {
        return null;
    }

    @Override
    public boolean getNeedFixCacheControl() {
        return false;
    }

    @Override
    public long getCacheTime() {
        return 0;
    }

    private RequestQueue createRequestQueue() {
        return Volley.newRequestQueue(mContext);
    }
}
