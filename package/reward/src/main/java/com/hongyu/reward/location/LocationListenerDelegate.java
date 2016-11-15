package com.hongyu.reward.location;

import com.hongyu.reward.model.AppLocation;

/**
 * Created by zhangyang131 on 2016/11/10.
 */

public interface LocationListenerDelegate {
    void notifyLocation(AppLocation appLocation);
    void failed(String msg);
    void onLog(String logStr);
}
