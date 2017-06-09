package debug.xly.com.debugkit.kit;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import debug.xly.com.debugkit.DebugKit;

/**
 * Created by zhangyang131 on 2017/6/1.
 */

public class KitActivity extends PreferenceActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getFragmentManager().beginTransaction().replace(android.R.id.content, new KitMainFragment())
        .commit();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    DebugKit.resetShakeHelper();
  }
}
