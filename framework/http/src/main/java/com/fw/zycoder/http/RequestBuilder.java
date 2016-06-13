package com.fw.zycoder.http;

import java.util.Map;

/**
 * Created by zhangyang131 on 16/6/13.
 */
public interface RequestBuilder  {
    public String getUrl();
    public Map<String, String> getParams();
    public Method getMethod();
    public void build();

}
