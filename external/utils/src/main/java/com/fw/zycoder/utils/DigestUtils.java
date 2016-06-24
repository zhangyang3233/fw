package com.fw.zycoder.utils;

import android.text.TextUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 
 */
public class DigestUtils {

  /**
   * '0'-'9' and 'A'-'F'
   */
  private static final byte[] HEX_BYTES = {48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 65, 66, 67, 68,
      69, 70};
  private static boolean hasMd5 = true;
  private static MessageDigest messageDigest = null;
  private static char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7',
      '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
  };

  // private static MessageDigest md5 = null;

  static {
    try {
      messageDigest = MessageDigest.getInstance("MD5");
    } catch (NoSuchAlgorithmException e) {
      hasMd5 = false;
      e.printStackTrace();
    }
  }

  /*
   * This method is only used for compute the signature for Apk.
   * Never use it for caculating password!!!!!
   */
  public static synchronized String computeMd5forPkg(String inStr) {
    // convert input String to a char[]
    // convert that char[] to byte[]
    // get the md5 digest as byte[]
    // bit-wise AND that byte[] with 0xff
    // prepend "0" to the output StringBuffer to make sure that we don't end
    // up with
    // something like "e21ff" instead of "e201ff"
    if (inStr == null) {
      return "";
    }

    inStr = inStr.toUpperCase();
    // MessageDigest md5 = null;
    // try {
    // md5 = MessageDigest.getInstance("MD5");
    // } catch (Exception e) {
    // System.out.println(e.toString());
    // e.printStackTrace();
    // return "";
    // }
    char[] charArray = inStr.toCharArray();
    byte[] byteArray = new byte[charArray.length];

    for (int i = 0; i < charArray.length; i++) {
      byteArray[i] = (byte) charArray[i];
    }

    byte[] md5Bytes = messageDigest.digest(byteArray);

    StringBuffer hexValue = new StringBuffer();

    for (int i = 0; i < md5Bytes.length; i++) {
      int val = ((int) md5Bytes[i]) & 0xff;
      if (val < 16) {
        hexValue.append("0");
      }
      hexValue.append(Integer.toHexString(val));
    }

    return hexValue.toString();
  }

  public static synchronized String computeMd5forPkg(byte[] hex) {
    // convert input String to a char[]
    // convert that char[] to byte[]
    // get the md5 digest as byte[]
    // bit-wise AND that byte[] with 0xff
    // prepend "0" to the output StringBuffer to make sure that we don't end
    // up with
    // something like "e21ff" instead of "e201ff"

    if (hex == null) {
      return null;
    }
    int i1;
    int i2;
    byte[] byteBuffers = new byte[2 * hex.length];
    for (int i = 0; i < hex.length; ++i) {
      i1 = (hex[i] & 0xf0) >> 4;
      byteBuffers[2 * i] = HEX_BYTES[i1];
      i2 = hex[i] & 0xf;
      byteBuffers[2 * i + 1] = HEX_BYTES[i2];
    }

    byte[] md5Bytes = messageDigest.digest(byteBuffers);

    StringBuffer hexValue = new StringBuffer();

    for (int i = 0; i < md5Bytes.length; i++) {
      int val = ((int) md5Bytes[i]) & 0xff;
      if (val < 16) {
        hexValue.append("0");
      }
      hexValue.append(Integer.toHexString(val));
    }
    return hexValue.toString();
  }

  public static synchronized String getStringMD5(String source) {
    if (TextUtils.isEmpty(source)) {
      return source;
    } else {
      return bufferToHex(messageDigest.digest(source.getBytes()));
    }
  }

  private static String bufferToHex(byte bytes[]) {
    return bufferToHex(bytes, 0, bytes.length);
  }

  private static String bufferToHex(byte bytes[], int m, int n) {
    StringBuffer stringbuffer = new StringBuffer(2 * n);
    int k = m + n;
    for (int l = m; l < k; l++) {
      appendHexPair(bytes[l], stringbuffer);
    }
    return stringbuffer.toString();
  }

  private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
    char c0 = hexDigits[(bt & 0xf0) >> 4];
    char c1 = hexDigits[bt & 0xf];
    stringbuffer.append(c0);
    stringbuffer.append(c1);
  }

}
