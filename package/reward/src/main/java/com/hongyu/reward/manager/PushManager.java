package com.hongyu.reward.manager;

import com.fw.zycoder.utils.CollectionUtils;
import com.umeng.message.entity.UMessage;

import java.util.Stack;

/**
 * Created by zhangyang131 on 16/10/12.
 */
public class PushManager {
  private static PushManager instance;
  Stack<UMessage> umsgs = new Stack<>();

  public static synchronized PushManager get() {
    if (instance == null) {
      instance = new PushManager();
    }
    return instance;
  }

  public void addPush(UMessage umsg) {
    umsgs.push(umsg);
  }

  public UMessage popPush() {
    if (CollectionUtils.isEmpty(umsgs)) {
      return null;
    }
    return umsgs.pop();
  }

}
