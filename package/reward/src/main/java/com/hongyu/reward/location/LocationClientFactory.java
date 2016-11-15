package com.hongyu.reward.location;

import android.content.Context;

import com.hongyu.reward.location.client.BaiduClient;
import com.hongyu.reward.location.client.GaodeClient;


/**
 * Created by zhangyang131 on 2016/11/10.
 */

public class LocationClientFactory {

  public static LocationClientInterface create(Context context, LocationType locationType) {
    switch (locationType) {
      case BAIDU:
        return new BaiduClient(context);
      case GAODE:
        return new GaodeClient(context);
      default:
        throw new RuntimeException("locationType not support!");
    }
  }
}
