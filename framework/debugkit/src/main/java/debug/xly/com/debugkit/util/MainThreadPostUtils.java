package debug.xly.com.debugkit.util;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

public class MainThreadPostUtils {

  private static Handler handler;

  private static byte[] handlerLock = new byte[0];

  public static Handler getHandler() {
    synchronized (handlerLock) {
      if (handler == null) {
        handler = new Handler(Looper.getMainLooper());
      }
    }
    return handler;
  }

  public static void post(Runnable run) {
    getHandler().post(run);
  }

  public static void postDelayed(Runnable run, long delayMillis) {
    getHandler().postDelayed(run, delayMillis);
  }




}
