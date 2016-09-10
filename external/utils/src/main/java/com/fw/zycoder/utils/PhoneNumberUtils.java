package com.fw.zycoder.utils;

import android.text.TextUtils;

import java.util.regex.Pattern;

/**
 * Created by zhangyang131 on 16/9/8.
 */
public class PhoneNumberUtils {

  /**
   * 验证手机号码是否合规，这是通用版本
   *
   * @param phoneNumber
   * @return boolean
   */
  public final static boolean isPhoneNum(String phoneNumber) {
    if(TextUtils.isEmpty(phoneNumber)){
      return false;
    }
    /*
     * 1XX YYYY YYYY，一共11位数字，1开头 头三位定义如下： 中国移动:
     * 134、135、136、137、138、139、150
     * 、151、152、157、158、159、147、182、183、184[1]、187、188 中国联通:
     * 130、131、132、154、155、156、185、186、145(属于联通无线上网卡号段) 中国电信: 133 、153
     * 、180 、181 、189 汇总为：1[358][0-9]，不支持无数据业务的无线上网卡
     */
    return Pattern.matches("^1[345789][0-9]\\d{8}$", phoneNumber);
  }

}
