package debug.xly.com.debugkit.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.Formatter;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import debug.xly.com.debugkit.R;

/**
 * Created by zhangyang131 on 2017/6/5.
 */

public class AppUtil {

  /**
   * 获得app的打包时间
   *
   * @return
   */
  public static String getAppBuildTime(Context context) {
    String result = "";
    try {
      ApplicationInfo ai =
          context.getPackageManager().getApplicationInfo(context.getPackageName(), 0);
      ZipFile zf = new ZipFile(ai.sourceDir);
      ZipEntry ze = zf.getEntry("META-INF/MANIFEST.MF");
      long time = ze.getTime();
      SimpleDateFormat formatter = (SimpleDateFormat) SimpleDateFormat.getInstance();
      formatter.applyPattern("yyyy/MM/dd HH:mm:ss");
      result = formatter.format(new java.util.Date(time));
      zf.close();
    } catch (Exception e) {}

    return result;
  }

  /**
   * 获取版本号
   *
   * @return 当前应用的内部版本号
   */
  public static String getVersionCode(Context context) {
    try {
      PackageManager manager = context.getPackageManager();
      PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
      String version = String.valueOf(info.versionCode);
      return version;
    } catch (Exception e) {
      e.printStackTrace();
      return context.getString(R.string.can_not_find);
    }
  }

  /**
   * 获取版本号
   *
   * @return 当前应用的版本号
   */
  public static String getVersionName(Context context) {
    try {
      PackageManager manager = context.getPackageManager();
      PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
      String version = info.versionName;
      return version;
    } catch (Exception e) {
      e.printStackTrace();
      return context.getString(R.string.can_not_find);
    }
  }

  public static String getMacAddress(Context context) {
    String result = null;
    WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
    result = wifiInfo.getMacAddress();
    return result;
  }

  /**
   * 获取android当前可用内存大小
   *
   * @return
   */
  public static String getAvailMemory(Context context) {// 获取android当前可用内存大小
    ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
    am.getMemoryInfo(mi);
    // mi.availMem; 当前系统的可用内存
    return Formatter.formatFileSize(context, mi.availMem);// 将获取的内存大小规格化
  }

  /**
   * 获取总内存
   *
   * @param context
   * @return
   */
  public static String getTotalMemory(Context context) {
    long mTotal;
    // /proc/meminfo读出的内核信息进行解释
    String path = "/proc/meminfo";
    String content = null;
    BufferedReader br = null;
    try {
      br = new BufferedReader(new FileReader(path), 8);
      String line;
      if ((line = br.readLine()) != null) {
        content = line;
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (br != null) {
        try {
          br.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    // beginIndex
    int begin = content.indexOf(':');
    // endIndex
    int end = content.indexOf('k');
    // 截取字符串信息
    content = content.substring(begin + 1, end).trim();
    mTotal = Integer.parseInt(content);
    DecimalFormat df = new DecimalFormat("0.00");
    double d = mTotal / 1024f / 1024f;
    String db = df.format(d) + "GB";
    return db;
  }

  /**
   * 获得SD卡总大小
   *
   * @return
   */
  public static String getSDTotalSize(Context context) {
    File path = Environment.getExternalStorageDirectory();
    StatFs stat = new StatFs(path.getPath());
    long blockSize = stat.getBlockSize();
    long totalBlocks = stat.getBlockCount();
    return Formatter.formatFileSize(context, blockSize * totalBlocks);
  }

  /**
   * 获得sd卡剩余容量，即可用大小
   *
   * @return
   */
  public static String getSDAvailableSize(Context context) {
    File path = Environment.getExternalStorageDirectory();
    StatFs stat = new StatFs(path.getPath());
    long blockSize = stat.getBlockSize();
    long availableBlocks = stat.getAvailableBlocks();
    return Formatter.formatFileSize(context, blockSize * availableBlocks);
  }

  public static String getNetworkType(Context context) {
    String strNetworkType = "";

    NetworkInfo networkInfo = ((ConnectivityManager) context
        .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
    if (networkInfo != null && networkInfo.isConnected()) {
      if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
        strNetworkType = "WIFI";
      } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
        String _strSubTypeName = networkInfo.getSubtypeName();
        L.e("cocos2d-x", "Network getSubtypeName : " + _strSubTypeName);
        // TD-SCDMA networkType is 17
        int networkType = networkInfo.getSubtype();
        switch (networkType) {
          case TelephonyManager.NETWORK_TYPE_GPRS:
          case TelephonyManager.NETWORK_TYPE_EDGE:
          case TelephonyManager.NETWORK_TYPE_CDMA:
          case TelephonyManager.NETWORK_TYPE_1xRTT:
          case TelephonyManager.NETWORK_TYPE_IDEN: // api<8 : replace by 11
            strNetworkType = "2G";
            break;
          case TelephonyManager.NETWORK_TYPE_UMTS:
          case TelephonyManager.NETWORK_TYPE_EVDO_0:
          case TelephonyManager.NETWORK_TYPE_EVDO_A:
          case TelephonyManager.NETWORK_TYPE_HSDPA:
          case TelephonyManager.NETWORK_TYPE_HSUPA:
          case TelephonyManager.NETWORK_TYPE_HSPA:
          case TelephonyManager.NETWORK_TYPE_EVDO_B: // api<9 : replace by 14
          case TelephonyManager.NETWORK_TYPE_EHRPD: // api<11 : replace by 12
          case TelephonyManager.NETWORK_TYPE_HSPAP: // api<13 : replace by 15
            strNetworkType = "3G";
            break;
          case TelephonyManager.NETWORK_TYPE_LTE: // api<11 : replace by 13
            strNetworkType = "4G";
            break;
          default:
            // http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
            if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA")
                || _strSubTypeName.equalsIgnoreCase("WCDMA")
                || _strSubTypeName.equalsIgnoreCase("CDMA2000")) {
              strNetworkType = "3G";
            } else {
              strNetworkType = _strSubTypeName;
            }

            break;
        }

      }
    }
    return TextUtils.isEmpty(strNetworkType) ? "无网络连接" : strNetworkType;
  }


  /**
   * 获取keyStore签名
   * @param context
   * @param algorithm algorithm可以是 "md5" 或者 "sha1"
   * @return
     */
  public static String getKeyStoreSign(Context context, String algorithm) {
    //获取包管理器
    PackageManager pm = context.getPackageManager();
    //获取当前要获取SHA1值的包名，也可以用其他的包名，但需要注意，
    //在用其他包名的前提是，此方法传递的参数Context应该是对应包的上下文。
    String packageName = context.getPackageName();
    //返回包括在包中的签名信息
    int flags = PackageManager.GET_SIGNATURES;
    PackageInfo packageInfo = null;
    try {
      //获得包的所有内容信息类
      packageInfo = pm.getPackageInfo(packageName, flags);
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }
    //签名信息
    Signature[] signatures = packageInfo.signatures;
    byte[] cert = signatures[0].toByteArray();
    //将签名转换为字节数组流
    InputStream input = new ByteArrayInputStream(cert);
    //证书工厂类，这个类实现了出厂合格证算法的功能
    CertificateFactory cf = null;
    try {
      cf = CertificateFactory.getInstance("X509");
    } catch (CertificateException e) {
      e.printStackTrace();
    }
    //X509证书，X.509是一种非常通用的证书格式
    X509Certificate c = null;
    try {
      c = (X509Certificate) cf.generateCertificate(input);
    } catch (CertificateException e) {
      e.printStackTrace();
    }
    String hexString = null;
    try {
      //加密算法的类，这里的参数可以使MD4,MD5等加密算法
      MessageDigest md = MessageDigest.getInstance(algorithm);
      //获得公钥
      byte[] publicKey = md.digest(c.getEncoded());
      //字节到十六进制的格式转换
      hexString = byte2HexFormatted(publicKey);
    } catch (NoSuchAlgorithmException e1) {
      e1.printStackTrace();
    } catch (CertificateEncodingException e) {
      e.printStackTrace();
    }
    return hexString;
  }
  //这里是将获取到得编码进行16进制转换
  private static String byte2HexFormatted(byte[] arr) {
    StringBuilder str = new StringBuilder(arr.length * 2);
    for (int i = 0; i < arr.length; i++) {
      String h = Integer.toHexString(arr[i]);
      int l = h.length();
      if (l == 1)
        h = "0" + h;
      if (l > 2)
        h = h.substring(l - 2, l);
      str.append(h.toUpperCase());
      if (i < (arr.length - 1))
        str.append(':');
    }
    return str.toString();
  }

  /**
   * 获取当前应用程序的包名
   * @param context 上下文对象
   * @return 返回包名
   */
  public static String getAppProcessName(Context context) {
    //当前应用pid
    int pid = android.os.Process.myPid();
    //任务管理类
    ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    //遍历所有应用
    List<ActivityManager.RunningAppProcessInfo> infos = manager.getRunningAppProcesses();
    for (ActivityManager.RunningAppProcessInfo info : infos) {
      if (info.pid == pid)//得到当前应用
        return info.processName;//返回包名
    }
    return "";
  }


  /**
   * 获取应用详情页面intent
   *
   * @return
   */
  public static Intent getAppDetailSettingIntent(Context context) {
    Intent localIntent = new Intent();
    localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    if (Build.VERSION.SDK_INT >= 9) {
      localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
      localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
    } else if (Build.VERSION.SDK_INT <= 8) {
      localIntent.setAction(Intent.ACTION_VIEW);
      localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
      localIntent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
    }
    return localIntent;
  }
}
