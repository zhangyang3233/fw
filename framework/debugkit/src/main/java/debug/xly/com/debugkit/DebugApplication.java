package debug.xly.com.debugkit;

import android.app.Application;

/**
 * Created by zhangyang131 on 2017/5/25.
 */

public class DebugApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        DebugKit.init(this);
    }



}
