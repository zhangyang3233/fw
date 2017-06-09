package debug.xly.com.debugkit.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * =======================================================
 * 版权：Copyright LiYing 2015-2016. All rights reserved.
 * 作者：liying - liruoer2008@yeah.net
 * 日期：2016/12/19 19:43
 * 版本：1.0
 * 描述：IP地址工具类
 * 备注：
 * =======================================================
 */
public class IPUtils {


  /**
   * 获取IP
   * 
   * @param context
   * @return
   */
  public static String getIp(final Context context) {
    String ip = null;
    ConnectivityManager conMan = (ConnectivityManager) context
        .getSystemService(Context.CONNECTIVITY_SERVICE);

    // mobile 3G Data Network
    android.net.NetworkInfo.State mobile = conMan.getNetworkInfo(
        ConnectivityManager.TYPE_MOBILE).getState();
    // wifi
    android.net.NetworkInfo.State wifi = conMan.getNetworkInfo(
        ConnectivityManager.TYPE_WIFI).getState();

    // 如果3G网络和wifi网络都未连接，且不是处于正在连接状态 则进入Network Setting界面 由用户配置网络连接
    if (mobile == android.net.NetworkInfo.State.CONNECTED
        || mobile == android.net.NetworkInfo.State.CONNECTING) {
      ip = getLocalIpAddress();
    }
    if (wifi == android.net.NetworkInfo.State.CONNECTED
        || wifi == android.net.NetworkInfo.State.CONNECTING) {
      // 获取wifi服务
      WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
      // 判断wifi是否开启
      if (!wifiManager.isWifiEnabled()) {
        wifiManager.setWifiEnabled(true);
      }
      WifiInfo wifiInfo = wifiManager.getConnectionInfo();
      int ipAddress = wifiInfo.getIpAddress();
      ip = (ipAddress & 0xFF) + "." +
          ((ipAddress >> 8) & 0xFF) + "." +
          ((ipAddress >> 16) & 0xFF) + "." +
          (ipAddress >> 24 & 0xFF);
    }
    return ip;

  }

  /**
   *
   * @return 手机GPRS网络的IP
   */
  private static String getLocalIpAddress() {
    try {
      // Enumeration<NetworkInterface> en=NetworkInterface.getNetworkInterfaces();
      for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en
          .hasMoreElements();) {
        NetworkInterface intf = en.nextElement();
        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr
            .hasMoreElements();) {
          InetAddress inetAddress = enumIpAddr.nextElement();
          if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {// 获取IPv4的IP地址
            return inetAddress.getHostAddress();
          }
        }
      }
    } catch (SocketException e) {
      e.printStackTrace();
    }


    return null;
  }

}
