package com.hongyu.reward.manager;

import android.text.TextUtils;

import com.fw.zycoder.http.callback.DataCallback;
import com.fw.zycoder.utils.MainThreadPostUtils;
import com.fw.zycoder.utils.SPUtil;
import com.hongyu.reward.config.Constants;
import com.hongyu.reward.http.ResponesUtil;
import com.hongyu.reward.model.TokenModel;
import com.hongyu.reward.request.GetTokenRequestBuilder;

import static com.fw.zycoder.utils.SPUtil.getString;

/**
 * pushCode 和 token 是异步申请的，这个类要保证：服务器 pushCode 和 token 是绑定的
 * Created by zhangyang131 on 2016/10/28.
 */

public class PushTokenManager {
    private static PushTokenManager instance;
    private String mToken = "";
    private String mPushCode = "";

    private PushTokenManager() {
        mToken = getLocationToken();
        mPushCode = getLocalPushCode();
    }

    private String getLocalPushCode() {
        return getString(Constants.Pref.PUSHCODE, "");
    }

    private String getLocationToken() {
        return getString(Constants.Pref.TOKEN, "");
    }

    public static PushTokenManager getInstance() {
        if (instance == null) {
            instance = new PushTokenManager();
        }
        return instance;
    }

    public String getToken() {
        return mToken;
    }

    public void setToken(String mToken) {
        if (TextUtils.isEmpty(mToken) || mToken.equals(this.mToken)) {
            return;
        }
        this.mToken = mToken;
        SPUtil.putString(Constants.Pref.TOKEN, mToken);
        checkBind();
    }

    public String getPushCode() {
        return mPushCode;
    }

    public void setPushCode(String mPushCode) {
        if (TextUtils.isEmpty(mPushCode) || mPushCode.equals(this.mPushCode)) {
            return;
        }
        this.mPushCode = mPushCode;
        SPUtil.putString(Constants.Pref.PUSHCODE, mPushCode);
        checkBind();
    }


    private void checkBind() {
        if (TextUtils.isEmpty(mToken) || TextUtils.isEmpty(mPushCode)) {
            return;
        }
        String bind = SPUtil.getString(mToken, "");
        if (bind.equals(mPushCode)) {
            return;
        } else {
            refreshTokenPushCode();
        }
    }

    private void refreshTokenPushCode() {
        GetTokenRequestBuilder builder = new GetTokenRequestBuilder();
        builder.setDataCallback(new DataCallback<TokenModel>() {
            @Override
            public void onDataCallback(TokenModel data) {
                if (!ResponesUtil.checkModelCodeOK(data)) {
                    MainThreadPostUtils.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            refreshTokenPushCode();
                        }
                    }, 500);
                }else{
                    SPUtil.putString(mToken, mPushCode);
                }
            }
        });
        builder.build().submit();
    }
}
