package com.fw.zycoder.utils;

import java.text.DecimalFormat;

/**
 * @author liuxu34@wanda.cn (Liu Xu)
 */
public class SizeConvertUtil {

  private static final long KB = 1024L;
  private static final long GB = KB * KB * KB;
  private static final long MB = KB * KB;

  private static final DecimalFormat decimalFormat = new DecimalFormat("#.##");

  public static String formatSize(float sizeOfMB) {
    StringBuilder gameSize = new StringBuilder();
    if (sizeOfMB >= 1024) {
      gameSize.append(decimalFormat.format(sizeOfMB / 1024)).append(" GB");
    } else {
      gameSize.append(decimalFormat.format(sizeOfMB)).append(" MB");
    }
    return gameSize.toString();
  }


  public static long transMB2Byte(float sizeByMB) {
    return (long) (sizeByMB * MB);
  }

  public static double transByte2MB(float sizeByByte) {
    return (double) (sizeByByte / MB);
  }

  public static String transByte2MBAndFormat(float sizeByByte) {
    return decimalFormat.format((double) (sizeByByte / MB));
  }

}
