package com.fw.zycoder.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author liuxu34@wanda.cn (Liu Xu)
 */
public class MD5Utils {

  public static final String get(final String src) {
    try {
      // Create MD5 Hash
      MessageDigest digest = MessageDigest.getInstance("MD5");
      digest.update(src.getBytes());
      byte messageDigest[] = digest.digest();

      // Create Hex String
      StringBuffer hexString = new StringBuffer();
      for (int i = 0; i < messageDigest.length; i++) {
        String h = Integer.toHexString(0xFF & messageDigest[i]);
        while (h.length() < 2) {
          h = "0" + h;
        }
        hexString.append(h);
      }
      return hexString.toString();
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
    return "";
  }

  public static boolean check(String content, String md5) {
    return get(content).equals(md5);
  }

  // get md5 signed with key
  public static final String sign(String content, String key) {
    return get(content + key);
  }

  // check md5 signed with key
  public static boolean check(String content, String key, String md5) {
    return get(content + key).equals(md5);
  }

}
