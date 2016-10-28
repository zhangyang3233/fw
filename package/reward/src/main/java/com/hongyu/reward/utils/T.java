package com.hongyu.reward.utils;

import android.widget.Toast;

import com.fw.zycoder.utils.GlobalConfig;
import com.fw.zycoder.utils.MainThreadPostUtils;

/**
 * Created by zhangyang131 on 16/9/8.
 */
public class T {
  public static void show(final CharSequence text) {
//    SuperToast.cancelAllSuperToasts();
//    SuperToast st = SuperToast
//        .create(GlobalConfig.getAppContext(), text, SuperToast.Duration.MEDIUM);
//    st.setBackground(SuperToast.Background.ORANGE);
//    st.show();
    MainThreadPostUtils.post(new Runnable() {
      @Override
      public void run() {
        Toast.makeText(GlobalConfig.getAppContext(), text, Toast.LENGTH_SHORT).show();
      }
    });
  }

  public static void show(int textResourceId) {
    show(GlobalConfig.getAppContext().getString(textResourceId));

  }
}
