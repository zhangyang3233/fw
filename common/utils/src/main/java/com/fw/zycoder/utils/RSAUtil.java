package com.fw.zycoder.utils;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

/**
 * RSA 加密 用户密码加密专用
 */
public class RSAUtil {

  /**
   * 公钥加密数据
   * 
   * @param data 需要加密数据
   * @param publicKey 公钥
   * @return 加密数据
   */
  public static final String encrypt(String data, String publicKey) {
    String encryptData = null;
    try {
      final KeyFactory keyFac = KeyFactory.getInstance("RSA");
      final PublicKey pubKey =
          keyFac.generatePublic(new X509EncodedKeySpec(hexString2ByteArr(publicKey)));

      final Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
      cipher.init(Cipher.ENCRYPT_MODE, pubKey);
      final byte[] result = cipher.doFinal(data.getBytes("UTF-8"));
      encryptData = byteArr2HexString(result);
    } catch (Exception e) {
      e.printStackTrace();
      encryptData = null;
    }
    return encryptData;
  }

  /**
   * 字节数组转换为十六进制字符串
   *
   * @param bytearr 字节数组
   * @return 十六进制字符串
   */
  public static final String byteArr2HexString(byte[] bytearr) {
    if (bytearr == null) {
      return null;
    }
    final StringBuffer sb = new StringBuffer();

    for (int k = 0; k < bytearr.length; k++) {
      if ((bytearr[k] & 0xFF) < 16) {
        sb.append("0");
      }
      sb.append(Integer.toString(bytearr[k] & 0xFF, 16));
    }
    return sb.toString();
  }

  /**
   * 十六进制字符串转换为字节数组
   *
   * @param hexString 16进制字符串
   * @return 字节数组
   */
  public static final byte[] hexString2ByteArr(String hexString) {
    if ((hexString == null) || (hexString.length() % 2 != 0)) {
      return new byte[0];
    }

    byte[] dest = new byte[hexString.length() / 2];

    try {
      for (int i = 0; i < dest.length; i++) {
        dest[i] = (byte) Integer.parseInt(hexString.substring(2 * i, 2 * i + 2), 16);
      }
    } catch (NumberFormatException e) {
      e.printStackTrace();
      return new byte[0];
    } catch (IndexOutOfBoundsException e) {
      e.printStackTrace();
      return new byte[0];
    }
    return dest;
  }

}
