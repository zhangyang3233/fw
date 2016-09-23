package com.hongyu.reward.utils;

import com.fw.zycoder.customtoast.SuperToast;
import com.fw.zycoder.utils.GlobalConfig;

/**
 * Created by zhangyang131 on 16/9/8.
 */
public class T {
  public static void show(CharSequence text) {
    SuperToast.cancelAllSuperToasts();
    SuperToast st = SuperToast
        .create(GlobalConfig.getAppContext(), text, SuperToast.Duration.MEDIUM);
    st.setBackground(SuperToast.Background.ORANGE);
    st.show();
  }

  public static void show(int textResourceId) {
    show(GlobalConfig.getAppContext().getString(textResourceId));

  }
}
