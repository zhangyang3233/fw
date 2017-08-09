package debug.xly.com.debugkit;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import debug.xly.com.debugkit.kit.KitActivity;
import debug.xly.com.debugkit.shake.OnShakeInterface;
import debug.xly.com.debugkit.shake.ShakeHelper;
import debug.xly.com.debugkit.util.FloatViewManager;
import debug.xly.com.debugkit.util.KitActivityManager;
import debug.xly.com.debugkit.util.L;
import debug.xly.com.debugkit.util.MainThreadPostUtils;
import debug.xly.com.debugkit.util.VibratorHelper;

import static debug.xly.com.debugkit.util.KitActivityManager.getCurrentActivity;

/**
 * Created by zhangyang131 on 2017/5/19.
 */

public class DebugKit {
  private static final int TYPE_DIALOG = 1;
  private static final int TYPE_FLOAT_VIEW = 2;
  private static final String TAG = DebugKit.class.getSimpleName();
  private static ShakeHelper shakeHelper;
  private static int show_debug_type = TYPE_FLOAT_VIEW;

  public static void resetShakeHelper() {
    if (shakeHelper != null) {
      shakeHelper.prepareNext();
    }
  }

  public static void init(final Application application) {
    KitActivityManager.init(application);
    initShakeHelper(application);
    L.init(application);
  }

  public static void init(final Application application, int show_debug_type) {
    DebugKit.show_debug_type = show_debug_type;
    init(application);
  }

  private static void initShakeHelper(final Application application) {
    shakeHelper = new ShakeHelper(application, new OnShakeInterface() {
      @Override
      public void onShake() {
        final Activity activity = getCurrentActivity();
        if (activity == null) {
          return;
        }
        VibratorHelper.Vibrate(application, 300);
        if (show_debug_type == TYPE_DIALOG) {
          shakeHelper.prepareNext();
          showDebugDialog(activity);
        } else if (show_debug_type == TYPE_FLOAT_VIEW) {
          if (FloatViewManager.isWorked(application)) {
            FloatViewManager.removeFloatView(application);
          } else {
            Toast.makeText(activity, "开启debug悬浮窗", Toast.LENGTH_SHORT).show();
            FloatViewManager.addFloatView(application, R.layout.debug_float_view,
                new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                    Activity activity1 = KitActivityManager.getCurrentActivity();
                    if (activity1 != null) {
                      startDebugPage(activity1);
                    }
                  }
                });
            FloatViewManager.setOnLongClicked(new View.OnLongClickListener() {
              @Override
              public boolean onLongClick(View v) {
                final Activity a1 = getCurrentActivity();
                if (a1 != null) {
                  MainThreadPostUtils.post(new Runnable() {
                    @Override
                    public void run() {
                      Toast.makeText(a1, a1.getClass().getSimpleName(),
                          Toast.LENGTH_LONG).show();
                    }
                  });
                  return true;
                }
                return false;
              }
            });
          }
          shakeHelper.prepareNext();
        }
      }
    });
    shakeHelper.register();
  }

  public static void showDebugDialog(final Activity activity) {
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
