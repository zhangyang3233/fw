package com.fw.zycoder.http;

import com.fw.zycoder.http.request.GsonRequestBuilder;
import com.fw.zycoder.utils.GsonUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangyang131 on 16/7/14.
 */
public abstract class AppBaseHttpRequestBuilder<T> extends GsonRequestBuilder<T>{

    protected AppBaseHttpRequestBuilder() {
        super();
    }

    private Map<String, Object> mJsonMap;

    /**
     * 使用内建 map 构建 json 字符串参数
     *
     * @param key
     * @param value
     */
    protected void jsonParamsPut(String key, Object value) {
        if (mJsonMap == null) {
            mJsonMap = new HashMap<>();
        }
        mJsonMap.put(key, value);
    }

    /**
     * 取得由内建 map 构建好的 json 字符串参数
     *
     * @return
     */
    protected String getJsonParams() {
        return GsonUtils.getGson().toJson(mJsonMap);
    }

    /**
     * 传入 collection 构建对应的 json 结构结果
     *
     * @param collection
     * @return
     */
    protected String toJson(Object collection) {
        return GsonUtils.getGson().toJson(collection);
    }


    /**
     * for api log use
     */
    public Map<String, Object> getRequestParams() {
        Params params = new Params();
        setParams(params);

        return new HashMap<String, Object>(params.getParamMap());
    }

    /**
     * for api log use
     */
    public String getRequestUrl() {
        return getUrl();
    }
}
