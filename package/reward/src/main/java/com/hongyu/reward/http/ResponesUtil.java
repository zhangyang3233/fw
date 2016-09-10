package com.hongyu.reward.http;

import android.text.TextUtils;

import com.fw.zycoder.utils.GlobalConfig;
import com.hongyu.reward.R;
import com.hongyu.reward.model.BaseModel;

/**
 * Created by zhangyang131 on 16/9/9.
 */
public class ResponesUtil {

    public static boolean checkModelCodeOK(BaseModel model){
        if(null == model || model.getCode() != 0){
            return false;
        }
        return true;
    }
    public static String getErrorMsg(BaseModel model){
        String msg;
        if(null == model){
            msg = GlobalConfig.getAppContext().getString(R.string.common_net_word_error);
        }else if(TextUtils.isEmpty(model.getMessage())){
            msg = GlobalConfig.getAppContext().getString(R.string.common_error_unknown);
        }else{
            msg = model.getMessage();
        }
        return msg;
    }
}
