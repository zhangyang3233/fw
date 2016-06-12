package com.fw.zycoder.utils;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;

import java.math.BigDecimal;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author liuxu34@wanda.cn (Liu Xu)
 */
public class StringUtil {

  public static String toUpperCase(String string) {
    int length = string.length();
    StringBuilder builder = new StringBuilder(length);
    for (int i = 0; i < length; i++) {
      builder.append(toUpperCase(string.charAt(i)));
    }
    return builder.toString();
  }

  public static char toUpperCase(char c) {
    return isLowerCase(c) ? (char) (c & 0x5f) : c;
  }

  public static boolean isLowerCase(char c) {
    return (c >= 'a') && (c <= 'z');
  }

  public static String join(List<String> stringList, String separator) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < stringList.size(); i++) {
      String s = stringList.get(i);
      if (s != null) {
        sb.append(s);
      }
      if (i < stringList.size() - 1) {
        sb.append(separator);
      }
    }

    return sb.toString();
  }

  public static String getString(int resId) {
    return GlobalConfig.getAppContext().getString(resId);
  }

  public static String getString(int resId, Object... formatArgs) {
    return GlobalConfig.getAppContext().getString(resId, formatArgs);
  }

  public static String[] getStringArray(int resId) {
    return GlobalConfig.getAppContext().getResources().getStringArray(resId);
  }

  public static String formatScaleDecimal(int scale, double value) {
    BigDecimal b = new BigDecimal(value);
    double f1 = b.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    return String.format("%." + scale + "f", f1);
  }

  public static String replaceBlank(String str) {
    String dest = "";
    if (str != null) {
      Pattern p = Pattern.compile("\\s*|\t|\r|\n");
      Matcher m = p.matcher(str);
      dest = m.replaceAll("");
    }
    return dest;
  }

  public static boolean isFreePrice(String price) {
    if (TextUtils.isEmpty(price)) {
      return false;
    }
    for (int i = 0; i < price.length(); i++) {
      char c = price.charAt(i);
      if (c > '0' && c < '9') {
        return false;
      }
    }
    return true;
  }

  public static SpannableString appendBitmapToEnd(String s, int resId) {
    Bitmap bitmap = BitmapFactory.decodeResource(GlobalConfig.getAppContext().getResources(), resId);
    ImageSpan imgSpan = new ImageSpan(GlobalConfig.getAppContext(), bitmap);
    SpannableString spanString = new SpannableString(s);
    spanString.setSpan(imgSpan, s.length() - 1, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    return spanString;
  }
}
