package com.hongyu.reward.request;

import com.hongyu.reward.config.Constants;
import com.hongyu.reward.http.BaseHttpRequestBuilder;
import com.hongyu.reward.manager.AccountManager;
import com.hongyu.reward.manager.PushTokenManager;
import com.hongyu.reward.model.BaseModel;
import com.hongyu.reward.utils.MD5;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by zhangyang131 on 2017/5/18.
 */

public class AddPointRequestBuilder extends BaseHttpRequestBuilder {
    private static final String SY = "ef8eaf283b0811e7b054eb16ab187607";
    private static final String TOKEN = "token";
    private static final String USER_ID = "user_id";
    private static final String TIME = "time";
    private static final String SIGN = "sign";
    private String user_id;// 会员id
    private String time;
    private String sign;
    String token = PushTokenManager.getInstance().getToken();

    public AddPointRequestBuilder() {
        this.user_id = AccountManager.getInstance().getUser().getUser_id();
        this.time = String.valueOf(System.currentTimeMillis() / 1000);
        this.sign = getSign();
    }

    private String getSign() {
        String sign;
        TreeMap<String, String> kv = new TreeMap();
        kv.put(TOKEN, token);
        kv.put(USER_ID, user_id);
        kv.put(TIME, time);

        StringBuilder str1 = new StringBuilder();
        Set<String> keySet = kv.keySet();
        Iterator<String> it = keySet.iterator();
        while (it.hasNext()) {
            String key = it.next();
            str1.append(key);
            str1.append(kv.get(key));
        }
        str1.append(SY);
        String str2 = MD5.getMD5(str1.toString()).toLowerCase();
        String str3 = MD5.getMD5(str2).toLowerCase();
        String str4 = str2 + str3;
        sign = str4.substring(0,45);
        return sign;
    }

    @Override
    protected void setParams(Params params) {
        super.setParams(params);
        params.put(USER_ID, user_id);
        params.put(TIME, time);
        params.put(SIGN, sign);
    }

    @Override
    protected String getApiUrl() {
        return Constants.Server.API_ADD_POINT;
    }

    @Override
    protected Class getResponseClass() {
        return BaseModel.class;
    }
}
