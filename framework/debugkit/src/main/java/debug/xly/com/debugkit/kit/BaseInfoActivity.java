package debug.xly.com.debugkit.kit;

import android.content.pm.PackageStats;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.util.DisplayMetrics;

import java.text.SimpleDateFormat;
import java.util.Date;

import debug.xly.com.debugkit.BuildConfig;
import debug.xly.com.debugkit.R;
import debug.xly.com.debugkit.util.DeivceInfoUtil;
import debug.xly.com.debugkit.util.IPUtils;
import debug.xly.com.debugkit.util.AppUtil;
import debug.xly.com.debugkit.util.PkgSizeObserver;
import debug.xly.com.debugkit.widget.InfoGroupLayout;

/**
 * Created by zhangyang131 on 2017/6/5.
 */

public class BaseInfoActivity extends AppCompatActivity {
  public static final String TYPE = "type";
  public static final int TYPE_DEVICE = 0;
  public static final int TYPE_APP = 1;
  InfoGroupLayout containLayout;

  // 屏幕

  String density;// 密度
  String densityX;// 精确密度
  String wh;// 分辨率
  String size;// 屏幕尺寸

  // 系统
  String SDK_INFO;
  String Linux_info;
  String jidiabenban;
  String bianyishijian;

  // 硬件
  String xinghao;
  String zhizaoshang;
  String zhuban;
  String pinpai;
  String abis;


  // cpu
  String cpu_count;
  String cpu_byte;
  String cpu_type;
  String cpu_max_rate;
  String cpu_min_rate;

  // 本机id
  String imei;
  String phoneNum;
  String sim_card_number;
  String fuwushang;

  // 网络
  String network;
  String ipv4;
  String mac;

  // 内存
  String neicun;
  String sdcrad;

  // app
  String app_version_name;
  String app_version_int;
  String app_name;
  String app_package_name;
  // String app_size;
  String packing_time;
  String app_sha1;
  String app_md5;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.base_info_layout);
    initView();
    if (isShowDeviceInfo()) {
      getDeviceInfos();
      setDeviceInfo();
    } else {
      getAppInfos();
      setAppInfo();
    }

  }

  private void getAppInfos() {
    app_version_name = "app版本：" + AppUtil.getVersionName(this);
    app_version_int = "内部版本号：" + AppUtil.getVersionCode(this);
    app_name = "应用名：" + getResources().getString(R.string.app_name);
    app_package_name = "包名：" + AppUtil.getAppProcessName(this);
    // app_size = "app大小：" + PkgSizeObserver.getPkgSize(this)[2];
    packing_time = "app打包时间：" + BuildConfig.releaseTime;
    app_sha1 = "sha1：" + AppUtil.getKeyStoreSign(this, "sha1");
    app_md5 = "md5：" + AppUtil.getKeyStoreSign(this, "md5");
    containLayout.put("APP详情", 0, app_name, app_version_name, app_package_name, app_version_int,
        packing_time, app_sha1, app_md5);
    containLayout.addDivider();
    final InfoGroupLayout.OnValueChangedListencer l1 = containLayout.put("app安装大小：");
    containLayout.addDivider();
    final InfoGroupLayout.OnValueChangedListencer l2 = containLayout.put("app数据：");
    containLayout.addDivider();
    final InfoGroupLayout.OnValueChangedListencer l3 = containLayout.put("app缓存：");
    PkgSizeObserver.getPkgSize(this, new PkgSizeObserver.OnGetValue() {
      @Override
      public void newValue(PackageStats pStats) {
        l1.onValueChanged(Formatter.formatFileSize(BaseInfoActivity.this, pStats.codeSize));
        l2.onValueChanged(Formatter.formatFileSize(BaseInfoActivity.this, pStats.dataSize));
        l3.onValueChanged(Formatter.formatFileSize(BaseInfoActivity.this, pStats.cacheSize));
      }
    });
  }

  private void setAppInfo() {}

  private boolean isShowDeviceInfo() {
    return getIntent().getExtras().getInt(TYPE) == TYPE_DEVICE;
  }

  private static final String sp = "\n";

  private void setDeviceInfo() {
    // 屏幕尺寸
    containLayout.put("屏幕", 0, wh, densityX, density, size);

    // 系统
    containLayout.put("系统", 0, SDK_INFO, jidiabenban, bianyishijian, Linux_info);

    // 硬件
    containLayout.put("硬件", 0, xinghao, pinpai, zhizaoshang, zhuban, imei, neicun, sdcrad, abis);

    // 网络
    containLayout.put("网络", 0, network, ipv4, mac, fuwushang, phoneNum, sim_card_number);

    // cpu
    containLayout.put("cpu", 0, cpu_byte, cpu_count, cpu_max_rate, cpu_min_rate, cpu_type);
  }

  private void getDeviceInfos() {
    // 屏幕
    DisplayMetrics dm = getResources().getDisplayMetrics();
    density = "密度：" + dm.densityDpi + "dpi/" + getResources().getString(R.string.dm_dpi) + "/"
        + dm.density + "x";
    densityX = "精确密度：" + dm.xdpi + "x" + dm.ydpi;
    wh = "分辨率：" + dm.widthPixels + "x" + dm.heightPixels;
    size = "屏幕尺寸："
        + DeivceInfoUtil.getScreenSizeOfDevice2(getWindowManager().getDefaultDisplay(), dm) + "英寸";

    // 系统
    SDK_INFO = "系统版本：" + android.os.Build.VERSION.RELEASE + "/" + android.os.Build.VERSION.CODENAME
        + "/API"
        + android.os.Build.VERSION.SDK_INT;
    jidiabenban = "基带版本：" + android.os.Build.BOARD;
    Linux_info = "Linux内核：" + DeivceInfoUtil.getLinuxKernalInfoEx();
    bianyishijian =
        "编译时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(Build.TIME));

    // 内存
    neicun = "内存：" + AppUtil.getAvailMemory(this) + "(可用)/" + AppUtil.getTotalMemory(this)
        + "(共)";
    sdcrad = "sdcard：" + AppUtil.getSDAvailableSize(this) + "(可用)/" + AppUtil.getSDTotalSize(this)
        + "(共)";

    // 硬件
    xinghao = "型号：" + android.os.Build.MODEL;
    pinpai = "品牌：" + android.os.Build.BRAND;
    zhizaoshang = "制造商：" + android.os.Build.MANUFACTURER;
    zhuban = "主板：" + Build.BOARD;
    if (android.os.Build.VERSION.SDK_INT >= 21) {
      String abisStr = "";
      for (int i = 0; i < Build.SUPPORTED_ABIS.length; i++) {
        if (i != 0) {
          abisStr += ",";
        }
        abisStr += Build.SUPPORTED_ABIS[i];
      }
      if (abisStr != null) {
        abis = "ABIs：" + abisStr;
      }
    }

    // cpu
    cpu_count = "cpu个数：" + DeivceInfoUtil.getNumCores() + "核";
    cpu_byte = "cpu位数：" + DeivceInfoUtil.getArchType(this) + "位";
    cpu_type = "cpu型号：" + DeivceInfoUtil.getCpuName();
    cpu_max_rate = "cpu最高频率：" + DeivceInfoUtil.cpuFromat(DeivceInfoUtil.getMaxCpuFreq());
    cpu_min_rate = "cpu最低频率：" + DeivceInfoUtil.cpuFromat(DeivceInfoUtil.getMinCpuFreq());

    // 本机id
    TelephonyManager tm = ((TelephonyManager) getSystemService(TELEPHONY_SERVICE));
    imei = "imei：" + tm.getDeviceId();

    if (tm.getSimState() > TelephonyManager.SIM_STATE_ABSENT) {
      try {
        fuwushang = "服务商：" + tm.getNetworkOperatorName();
        phoneNum = "tel：" + tm.getLine1Number(); // 手机号码，有的可得，有的不可得
        sim_card_number = "sim卡序列号：" + tm.getSimSerialNumber();// String
      } catch (Exception e) {}
    }


    // 网络
    network = "网络：" + AppUtil.getNetworkType(this);
    String ip = IPUtils.getIp(this);
    ipv4 = "ipv4：" + (ip == null ? "无网络连接" : ip);
    mac = "mac：" + AppUtil.getMacAddress(this);
  }

  private void initView() {
    containLayout = (InfoGroupLayout) findViewById(R.id.contain_layout);
  }

}
