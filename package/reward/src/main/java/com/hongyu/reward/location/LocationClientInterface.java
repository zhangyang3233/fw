package com.hongyu.reward.location;

/**
 * Created by zhangyang131 on 2016/11/10.
 */

public abstract class LocationClientInterface {
    protected LocationListenerDelegate locationListener;
    protected abstract void initConfig();
    protected abstract void start();
    protected abstract void stop();
    protected abstract void setLocationListener(LocationListenerDelegate locationListener);
}
