package debug.xly.com.debugkit.kit;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.widget.Toast;

import debug.xly.com.debugkit.R;
import debug.xly.com.debugkit.util.AppUtil;
import debug.xly.com.debugkit.util.DataCleanManager;
import debug.xly.com.debugkit.util.L;

/**
 * Created by zhangyang131 on 2017/6/1.
 */

public class KitMainFragment extends PreferenceFragment {
  Preference baseInfoPre;
  Preference appInfoPre;
  Preference crashPre;
  Preference anrPre;
  Preference clearSpPre;
  Preference print_log;
  Preference startAction;
  Preference gotoSettingPage;
  Preference sys_page;


  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    addPreferencesFromResource(R.xml.debug_kit);
    initPreference();
  }

  private void initPreference() {
    baseInfoPre = findPreference(getResources().getString(R.string.base_info));
    appInfoPre = findPreference(getResources().getString(R.string.app_info));
    startAction = findPreference(getResources().getString(R.string.start_action));
    print_log = findPreference(getResources().getString(R.string.print_log));
    gotoSettingPage = findPreference(getResources().getString(R.string.setting_page));
    sys_page = findPreference(getResources().getString(R.string.sys_page));
    crashPre = findPreference(getResources().getString(R.string.create_error));
    anrPre = findPreference(getResources().getString(R.string.create_anr));
    clearSpPre = findPreference(getResources().getString(R.string.clear_sp));
    baseInfoPre.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
      @Override
      public boolean onPreferenceClick(Preference preference) {
        startDeviceInfoActivity();
        return true;
      }
    });
    appInfoPre.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
      @Override
      public boolean onPreferenceClick(Preference preference) {
        startAppInfoActivity();
        return true;
      }
    });
    gotoSettingPage.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
      @Override
      public boolean onPreferenceClick(Preference preference) {
//        String command =
//            getResources().getString(R.string.shell_command, getActivity().getPackageName());
//        CommandExecution.execCommand(command, false);
        startActivity(AppUtil.getAppDetailSettingIntent(getActivity()));
        return true;
      }
    });

    crashPre.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
      @Override
      public boolean onPreferenceClick(Preference preference) {
        throw new RuntimeException("create crash!");
      }
    });
    anrPre.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
      @Override
      public boolean onPreferenceClick(Preference preference) {
        try {
          Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        return true;
      }
    });
    clearSpPre.setSummary(DataCleanManager.getAppAllCatchSize(getActivity()));
    clearSpPre.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
      @Override
      public boolean onPreferenceClick(Preference preference) {
        DataCleanManager.cleanApplicationData(getActivity());
        Toast.makeText(getActivity(), "清除缓存成功！", Toast.LENGTH_LONG).show();
        clearSpPre.setSummary(DataCleanManager.getAppAllCatchSize(getActivity()));
        return true;
      }
    });

    print_log.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
      @Override
      public boolean onPreferenceChange(Preference preference, Object newValue) {
        L.setPrintLog(getActivity(), (Boolean) newValue);
        return true;
      }
    });
    startAction.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
      @Override
      public boolean onPreferenceClick(Preference preference) {

        return true;
      }
    });
    sys_page.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
      @Override
      public boolean onPreferenceClick(Preference preference) {
        Intent i = new Intent(getActivity(), SysPageDemoActivity.class);
        startActivity(i);
        return true;
      }
    });
  }

  private void startDeviceInfoActivity() {
    Intent i = new Intent(getActivity(), BaseInfoActivity.class);
    i.putExtra(BaseInfoActivity.TYPE, BaseInfoActivity.TYPE_DEVICE);
    startActivity(i);
  }

  private void startAppInfoActivity() {
    Intent i = new Intent(getActivity(), BaseInfoActivity.class);
    i.putExtra(BaseInfoActivity.TYPE, BaseInfoActivity.TYPE_APP);
    startActivity(i);
  }

  public String getStringLog() {
    return "";
  }
}
