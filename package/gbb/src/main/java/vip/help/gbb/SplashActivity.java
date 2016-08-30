package vip.help.gbb;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;

import com.fw.zycoder.appbase.activity.BaseSlideActivity;

import vip.help.gbb.host.activity.TabHostActivity;

/**
 * Created by zhangyang131 on 16/7/22.
 */
public class SplashActivity extends BaseSlideActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    SplashFragment f =
        (SplashFragment) Fragment.instantiate(this, SplashFragment.class.getName(), null);
    if (f != null) {
      replaceFragment(f);
    }

    Handler a = new Handler();
    a.postDelayed(new Runnable() {
      @Override
      public void run() {
        if (isFinishing()) {
          return;
        }

        if (isDestroyed()) {
          return;
        }
        TabHostActivity.launch(SplashActivity.this);
        overridePendingTransition(0,0);
        finish();
      }
    }, getDelayedTime());
  }

  @Override
  public boolean getCanFlingBack() {
    return false;
  }

  public long getDelayedTime() {
    SharedPreferences sp = getPreferences(MODE_PRIVATE);
    boolean isFirstLuanch = sp.getBoolean(IS_FIRST_LAUNCH, true);
    if (isFirstLuanch) {
      SharedPreferences.Editor ed = sp.edit();
      ed.putBoolean(IS_FIRST_LAUNCH, false);
      ed.commit();
      return 0;
    }
    return 2000;
  }

  private static final String IS_FIRST_LAUNCH = "is first launch";
}
