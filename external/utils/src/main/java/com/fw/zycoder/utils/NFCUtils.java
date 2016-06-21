package com.fw.zycoder.utils;

import android.content.Context;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;

/**
 * Created by zhangyang131 on 16/6/21.
 */
public class NFCUtils {
  /** 手机不支持NFC */
  public static final int UNSUPPORT = 1;
  /** 手机支持但是未开启NFC功能 */
  public static final int SUPPORT_UNAVAILABLE = 2;
  /** 手机支持且开启NFC功能 */
  public static final int SUPPORT_AVAILABLE = 3;

  /**
   * 获取手机NFC的状态
   * 
   * @param context
   * @return UNSUPPORT or SUPPORT_UNAVAILABLE or SUPPORT_AVAILABLE
   */
  public static int getNFCStatus(Context context) {
    NfcManager manager = (NfcManager) context.getSystemService(Context.NFC_SERVICE);
    NfcAdapter adapter = manager.getDefaultAdapter();
    if (adapter == null) {
      return UNSUPPORT;
    }
    if (adapter.isEnabled()) {
      return SUPPORT_AVAILABLE;
    }
    return SUPPORT_UNAVAILABLE;
  }
}
