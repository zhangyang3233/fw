package debug.xly.com.debugkit;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;

import debug.xly.com.debugkit.kit.KitActivity;
import debug.xly.com.debugkit.shake.OnShakeInterface;
import debug.xly.com.debugkit.shake.ShakeHelper;
import debug.xly.com.debugkit.util.CurrentActivityUtil;
import debug.xly.com.debugkit.util.L;
import debug.xly.com.debugkit.util.VibratorHelper;

/**
 * Created by zhangyang131 on 2017/5/19.
 */

public class DebugKit {
  private static final String TAG = DebugKit.class.getSimpleName();
  private static ShakeHelper shakeHelper;

  public static void resetShakeHelper() {
    if (shakeHelper != null) {
      shakeHelper.prepareNext();
    }
  }

  public static void init(final Application application) {
    CurrentActivityUtil.init(application);
    initShakeHelper(application);
    L.init(application);
  }

  private static void initShakeHelper(final Application application) {
    shakeHelper = new ShakeHelper(application, new OnShakeInterface() {
      @Override
      public void onShake() {
        Activity activity = CurrentActivityUtil.getCurrentActivity();
        if (activity == null) {
          shakeHelper.prepareNext();
          return;
        }
        VibratorHelper.Vibrate(application, 300);

        showDebugDialog(activity);
      }
    });
    shakeHelper.register();
  }

  private static void showDebugDialog(final Activity activity) {
    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
    builder.setMessage("是否进入调试工程模式？");
    builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
      @Override
      public void onCancel(DialogInterface dialogInterface) {
        shakeHelper.prepareNext();
      }
    });
    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialogInterface, int i) {
        startDebugPage(activity);
        dialogInterface.dismiss();
      }
    });
    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialogInterface, int i) {
        dialogInterface.dismiss();
        shakeHelper.prepareNext();
      }
    });
    builder.create().show();
  }

  private static void startDebugPage(Activity activity) {
    Intent intent = new Intent(activity, KitActivity.class);
    activity.startActivity(intent);
  }

}
