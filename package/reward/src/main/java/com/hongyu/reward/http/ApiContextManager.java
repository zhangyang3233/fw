//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.hongyu.reward.http;

import com.fw.zycoder.utils.GlobalConfig;

public class ApiContextManager {
  private static ApiContextManager sInstance;
  private BaseApiContext mApiContext;

  private ApiContextManager() {}

  public static synchronized ApiContextManager getInstance() {
    if (sInstance == null) {
      sInstance = new ApiContextManager();
    }

    return sInstance;
  }

  public BaseApiContext getApiContext() {
    if (this.mApiContext == null) {
      this.mApiContext = new BaseApiContext(GlobalConfig.getAppContext());
    }

    return this.mApiContext;
  }

}
