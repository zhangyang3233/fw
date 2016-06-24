package com.fw.zycoder.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 */
public final class NetworkUtil {

  public static final int NETWORK_TYPE_NONE = -1;
  public static final int NETWORK_TYPE_MOBILE = ConnectivityManager.TYPE_MOBILE;
  public static final int NETWORK_TYPE_WIFI = ConnectivityManager.TYPE_WIFI;
  public static final String INTERFACE_WLAN0 = "wlan0";
  private static final String ANDROID_HOTSPOT_IP_ADDRESS = "192.168.43.1";
  private static final String IOS_HOTSPOT_IP_ADDRESS = "172.20.10.1";
  private static boolean reverseProxyOn = false;

  private NetworkUtil() {}

  public static boolean isNetworkConnected(Context context) {
    if (reverseProxyOn) {
      return true;
    }
    ConnectivityManager connManager = (ConnectivityManager) context
        .getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo activeNetworkInfo = null;
    try {
      activeNetworkInfo = connManager.getActiveNetworkInfo();
    } catch (Exception e) {
      // in some roms, here maybe throw a exception(like nullpoint).
      e.printStackTrace();
    }
    return (activeNetworkInfo != null && activeNetworkInfo.isConnected());
  }

  public static NetworkInfo getNetworkInfo(Context context) {
    ConnectivityManager connManager = (ConnectivityManager) context
        .getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo activeNetworkInfo = connManager.getActiveNetworkInfo();
    return activeNetworkInfo;
  }

  public static boolean isMobileNetworkConnected(Context context) {
    ConnectivityManager connManager = (ConnectivityManager) context
        .getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo networkInfo =
        connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
    return networkInfo != null && networkInfo.isConnected();
  }

  public static boolean isWifiConnected(Context context) {
    ConnectivityManager connManager = (ConnectivityManager) context
        .getSystemService(Context.CONNECTIVITY_SERVICE);
    if (connManager == null) {
      return false;
    }
    NetworkInfo networkInfo = null;
    try {
      // maybe throw exception in android framework
      networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
    } catch (Exception e) {
      e.printStackTrace();
    }

    // can not use pingSupplicant (), on cm9 or some other roms it will
    // block whole wifi network!
    return (networkInfo != null && networkInfo.isConnected());
  }

  /**
   * Convert a IPv4 address from an integer to an InetAddress.
   * 
   * @param hostAddress is an Int corresponding to the IPv4 address in network byte order
   * @return the IP address as an {@code InetAddress}, returns null if
   *         unable to convert or if the int is an invalid address.
   */
  private static InetAddress intToInetAddress(int hostAddress) {
    InetAddress inetAddress = null;
    byte[] addressBytes = {(byte) (0xff & hostAddress),
        (byte) (0xff & (hostAddress >> 8)),
        (byte) (0xff & (hostAddress >> 16)),
        (byte) (0xff & (hostAddress >> 24))};

    try {
      inetAddress = InetAddress.getByAddress(addressBytes);
    } catch (UnknownHostException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }

    return inetAddress;
  }

  /**
   * Check wifi is hotSpot or not.
   * 
   * @return whether wifi is hotSpot or not.
   */
  public static boolean checkWifiIsHotSpot(Context context) {
    WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    if (wifiManager == null) {
      return false;
    }
    DhcpInfo dhcpInfo = wifiManager.getDhcpInfo();
    if (dhcpInfo == null) {
      return false;
    }
    InetAddress address = intToInetAddress(dhcpInfo.gateway);
    if (address == null) {
      return false;
    }
    String currentGateway = address.getHostAddress();
    return TextUtils.equals(currentGateway, ANDROID_HOTSPOT_IP_ADDRESS)
        || TextUtils.equals(currentGateway, IOS_HOTSPOT_IP_ADDRESS);
  }

  public static boolean isReverseProxyOn() {
    return reverseProxyOn;
  }

  public static void setUsbReverseProxyState(boolean proxyOn) {
    reverseProxyOn = proxyOn;
  }

  public static int getNetworkType(Context context) {
    ConnectivityManager connManager =
        (ConnectivityManager) context.getSystemService(
            Context.CONNECTIVITY_SERVICE);
    NetworkInfo networkInfo;
    try {
      networkInfo = connManager.getActiveNetworkInfo();
    } catch (NullPointerException e) {
      // get some crash that getActiveNetworkInfo() may throw NullPointerException in some ROM...
      // so catch it here
      e.printStackTrace();
      return NETWORK_TYPE_NONE;
    }
    return parseNetworkType(networkInfo);
  }

  public static int parseNetworkType(NetworkInfo networkInfo) {
    if (networkInfo == null || !networkInfo.isConnected()) {
      return NETWORK_TYPE_NONE;
    }
    if (networkInfo.getType() == NETWORK_TYPE_MOBILE) {
      return NETWORK_TYPE_MOBILE;
    } else {
      return NETWORK_TYPE_WIFI;
    }
  }

  /**
   * Get the network type name. If currently connected to a mobile network, the detail mobile
   * network type name will be returned.
   * 
   * @param context
   * @return
   */
  public static String getNetworkTypeName(Context context) {
    ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(
        Context.CONNECTIVITY_SERVICE);
    if (connManager == null) {
      return null;
    }
    try {
      // in some rom and a special time, it maybe throw NullPointer ex,
      // we have to catch it, and return a null value.
      final NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
      return getNetworkTypeName(context, networkInfo);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Get the network type name. If currently connected to a mobile network, the detail mobile
   * network type name will be returned.
   * 
   * @param context
   * @param networkInfo
   * @return the network type name.
   */
  public static String getNetworkTypeName(Context context, NetworkInfo networkInfo) {
    if (networkInfo == null) {
      if (reverseProxyOn) {
        return "PC";
      }
    } else if (networkInfo.getType() == NETWORK_TYPE_MOBILE) {
      TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(
          Context.TELEPHONY_SERVICE);
      return getNetworkTypeName(telephonyManager.getNetworkType());
    } else {
      return "WIFI";
    }
    return null;
  }

  private static String getNetworkTypeName(int type) {
    switch (type) {
      case TelephonyManager.NETWORK_TYPE_GPRS:
        return "GPRS";
      case TelephonyManager.NETWORK_TYPE_EDGE:
        return "EDGE";
      case TelephonyManager.NETWORK_TYPE_UMTS:
        return "UMTS";
      case TelephonyManager.NETWORK_TYPE_HSDPA:
        return "HSDPA";
      case TelephonyManager.NETWORK_TYPE_HSUPA:
        return "HSUPA";
      case TelephonyManager.NETWORK_TYPE_HSPA:
        return "HSPA";
      case TelephonyManager.NETWORK_TYPE_CDMA:
        return "CDMA";
      case TelephonyManager.NETWORK_TYPE_EVDO_0:
        return "CDMA - EvDo rev. 0";
      case TelephonyManager.NETWORK_TYPE_EVDO_A:
        return "CDMA - EvDo rev. A";
      case TelephonyManager.NETWORK_TYPE_EVDO_B:
        return "CDMA - EvDo rev. B";
      case TelephonyManager.NETWORK_TYPE_1xRTT:
        return "CDMA - 1xRTT";
      case TelephonyManager.NETWORK_TYPE_LTE:
        return "LTE";
      case TelephonyManager.NETWORK_TYPE_EHRPD:
        return "CDMA - eHRPD";
      case TelephonyManager.NETWORK_TYPE_IDEN:
        return "iDEN";
      case TelephonyManager.NETWORK_TYPE_HSPAP:
        return "HSPA+";
      default:
        return "UNKNOWN";
    }
  }

  public static String getIPv4Address() {
    return getIPAddress(true);
  }

  public static String getIPv6Address() {
    return getIPAddress(false);
  }

  /**
   * Get IP address from first non-localhost interface
   * 
   * @param useIPv4 true=return ipv4, false=return ipv6
   * @return address or empty string
   */
  public static String getIPAddress(boolean useIPv4) {
    try {
      List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
      for (NetworkInterface intf : interfaces) {
        List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
        for (InetAddress addr : addrs) {
          if (!addr.isLoopbackAddress()) {
            String sAddr = addr.getHostAddress().toUpperCase();
            return sAddr;
          }
        }
      }
    } catch (Exception ex) {} // for now eat exceptions
    return "";
  }

  @SuppressWarnings("deprecation")
  public static String getProxyHost() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
      return System.getProperty("http.proxyHost");
    } else {
      return android.net.Proxy.getDefaultHost();
    }
  }

  @SuppressWarnings("deprecation")
  public static int getProxyPort() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
      return Integer.valueOf(System.getProperty("http.proxyPort"));
    } else {
      return Integer.valueOf(android.net.Proxy.getDefaultHost());
    }
  }

  public static String getMacAddress(Context context) {
    String macAddress = null;
    try {
      List<NetworkInterface> interfaces = Collections
          .list(NetworkInterface.getNetworkInterfaces());
      for (NetworkInterface intf : interfaces) {
        byte[] mac = intf.getHardwareAddress();
        macAddress = convertMacAddress(mac);
        break;
      }
    } catch (Exception ex) {} // for now eat exceptions

    if (TextUtils.isEmpty(macAddress)) {
      return getWifiMacAddress(context);
    }
    return macAddress;
  }

  private static String convertMacAddress(byte[] mac) {
    if (mac == null) {
      return null;
    }
    StringBuilder buf = new StringBuilder();
    for (int idx = 0; idx < mac.length; idx++)
      buf.append(String.format("%02X:", mac[idx]));
    if (buf.length() > 0)
      buf.deleteCharAt(buf.length() - 1);

    // FIXME 为什么有些android获取不到mac地址，或者获取到的mac地址位数不对
    if (buf.length() != 12 && buf.length() != 17) {
      return null;
    }
    return buf.toString();
  }

  private static String getWifiMacAddress(Context ctx) {
    WifiManager manager = null;
    try {
      manager = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
      if (manager == null || manager.getConnectionInfo() == null) {
        return null;
      }
      Enumeration<NetworkInterface> interfaces = null;
      // the WiFi network interface will be one of these.
      interfaces = NetworkInterface.getNetworkInterfaces();

      // We'll use the WiFiManager's ConnectionInfo IP address and compare
      // it with the ips of the enumerated NetworkInterfaces to find the
      // WiFi
      // NetworkInterface.

      // Wifi manager gets a ConnectionInfo object that has the ipAdress
      // as an int It's endianness could be different as the one on
      // java.net.InetAddress
      // maybe this varies from device to device, the android API has no
      // documentation on this method.
      int wifiIP = manager.getConnectionInfo().getIpAddress();

      // so I keep the same IP number with the reverse endianness
      int reverseWifiIP = Integer.reverseBytes(wifiIP);

      while (interfaces.hasMoreElements()) {
        NetworkInterface iface = interfaces.nextElement();

        // since each interface could have many InetAddresses...
        Enumeration<InetAddress> inetAddresses = iface
            .getInetAddresses();
        while (inetAddresses.hasMoreElements()) {
          InetAddress nextElement = inetAddresses.nextElement();
          int byteArrayToInt = byteArrayToInt(nextElement.getAddress(), 0);

          // grab that IP in byte[] form and convert it to int, then
          // compare it to the IP given by the WifiManager's
          // ConnectionInfo. We compare
          // in both endianness to make sure we get it.
          if (byteArrayToInt == wifiIP
              || byteArrayToInt == reverseWifiIP) {
            byte[] mac = iface.getHardwareAddress();
            return convertMacAddress(mac);
          }
        }
      }
    } catch (Exception e) {}

    return null;
  }

  private static final int byteArrayToInt(byte[] arr, int offset) {
    if (arr == null || arr.length - offset < 4)
      return -1;

    int r0 = (arr[offset] & 0xFF) << 24;
    int r1 = (arr[offset + 1] & 0xFF) << 16;
    int r2 = (arr[offset + 2] & 0xFF) << 8;
    int r3 = arr[offset + 3] & 0xFF;
    return r0 + r1 + r2 + r3;
  }

}
