package debug.xly.com.debugkit.kit;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import debug.xly.com.debugkit.R;

/**
 * Created by zhangyang131 on 2017/6/1.
 */

public class KitMainFragment2 extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.debug_kit);
    }
}
