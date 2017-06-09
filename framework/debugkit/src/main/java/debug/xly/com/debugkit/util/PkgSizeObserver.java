package debug.xly.com.debugkit.util;

import android.content.Context;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.os.RemoteException;

import java.lang.reflect.Method;

// aidl文件形成的Bindler机制服务类
public class PkgSizeObserver extends IPackageStatsObserver.Stub {
  static PackageStats pStats = null;

  /***
   * 回调函数，
   *
   * @param pStatus ,返回数据封装在PackageStats对象中
   * @param succeeded 代表回调成功
   */
  @Override
  public void onGetStatsCompleted(PackageStats pStatus, boolean succeeded)
      throws RemoteException {
    // TODO Auto-generated method stub
    // cachesize = pStatus.cacheSize; // 缓存大小
    // datasize = pStatus.codeSize; // 数据大小
    // codesize = pStatus.codeSize; // 应用程序大小
  }

  public static void  getPkgSize(final Context context,
      final OnGetValue l) {
    if (hasValue()) {
      l.newValue(pStats);
      return;
    }
    String pkgName = AppUtil.getAppProcessName(context);
    try {
      // getPackageSizeInfo是PackageManager中的一个private方法，所以需要通过反射的机制来调用
      Method method = PackageManager.class.getMethod("getPackageSizeInfo",
          String.class, IPackageStatsObserver.class);
      // 调用 getPackageSizeInfo 方法，需要两个参数：1、需要检测的应用包名；2、回调
      method.invoke(context.getPackageManager(), pkgName,
          new IPackageStatsObserver.Stub() {
            @Override
            public void onGetStatsCompleted(final PackageStats p, boolean succeeded)
                throws RemoteException {
              MainThreadPostUtils.post(new Runnable() {
                @Override
                public void run() {
                  PkgSizeObserver.pStats = p;
                  l.newValue(p);
                }
              });
            }
          });
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public interface OnGetValue {
    void newValue(PackageStats pStats);
  }



  public static boolean hasValue() {
    return pStats != null;
  }
}
