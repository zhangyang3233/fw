package com.fw.zycoder.demos;

import android.content.Context;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.fw.zycoder.demos.base.DemoActivity;

/**
 * Created by zhangyang131 on 16/6/21.
 */
public class TestActivity extends DemoActivity {

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_test);
    TextView tv = (TextView) findViewById(R.id.tv);
    tv.setText(hasNfc(this));

  }

  public static String hasNfc(Context context) {
    NfcManager manager = (NfcManager) context.getSystemService(Context.NFC_SERVICE);
    NfcAdapter adapter = manager.getDefaultAdapter();
    if (adapter == null) {
      return "不支持NFC";
    }
    if (adapter.isEnabled()) {
      return "已开启NFC功能";
    }
    return "支持NFC,但未开启";
  }
}
