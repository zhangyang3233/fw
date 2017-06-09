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
import debug.xly.com.debugkit.util.AppUtil;
import debug.xly.com.debugkit.util.DeivceInfoUtil;
import debug.xly.com.debugkit.util.IPUtils;
import debug.xly.com.debugkit.util.PkgSizeObserver;
import debug.xly.com.debugkit.widget.InfoGroupLayout;

/**
 * Created by zhangyang131 on 2017/6/5.
 */

public class BaseInfoActivity extends AppCompatActivity {
  public static final String TYPE = "type";
  public static final int TYPE_DEVICE = 0;
  public static final int TYPE_APP = 1;
  InfoGroupLayout mContainLayout;

  // 屏幕
  String mDensity;// 密度
  String mDensityX;// 精确密度
  String mResolution;// 分辨率
  String mSize;// 屏幕尺寸

  // 系统
  String mSdkInfo;
  String mLinuxInfo;
  String mBasebandVersion;
  String mCompileTime;

  // 硬件
  String mModel;
  String mManufacturer;
  String mMainboard;
  String mBrand;
  String mAbis;

  // cpu
  String mCpuCount;
  String mCpuByte;
  String mCpuType;
  String mCpuMaxRate;
  String mCpuMinRate;

  // 本机id
  String mImei;
  String mPhoneNum;
  String mSimCardNumber;
  String mServiceProvider;

  // 网络
  String mNetwork;
  String mIpv4;
  String mMac;

  // 内存
  String mMemory;
  String mSDCard;

  // app
  String mAppVersionName;
  String mAppVersionInt;
  String mAppName;
  String mAppPackageName;
  String mPackingTime;
  String mAppSha1;
  String mAppMD5;

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
    mAppVersionName = "app版本：" + AppUtil.getVersionName(this);
    mAppVersionInt = "内部版本号：" + AppUtil.getVersionCode(this);
    mAppName = "应用名：" + getResources().getString(R.string.app_name);
    mAppPackageName = "包名：" + AppUtil.getAppProcessName(this);
    // app_size = "app大小：" + PkgSizeObserver.getPkgSize(this)[2];
    mPackingTime = "app打包时间：" + BuildConfig.releaseTime;
    mAppSha1 = "sha1：" + AppUtil.getKeyStoreSign(this, "sha1");
    mAppMD5 = "md5：" + AppUtil.getKeyStoreSign(this, "md5");
    mContainLayout.put("APP详情", 0, mAppName, mAppVersionName, mAppPackageName, mAppVersionInt,
        mPackingTime, mAppSha1, mAppMD5);
    mContainLayout.addDivider();
    final InfoGroupLayout.OnValueChangedListencer l1 = mContainLayout.put("app安装大小：");
    mContainLayout.addDivider();
    final InfoGroupLayout.OnValueChangedListencer l2 = mContainLayout.put("app数据：");
    mContainLayout.addDivider();
    final InfoGroupLayout.OnValueChangedListencer l3 = mContainLayout.put("app缓存：");
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
    mContainLayout.put("屏幕", 0, mResolution, mDensityX, mDensity, mSize);

    // 系统
    mContainLayout.put("系统", 0, mSdkInfo, mBasebandVersion, mCompileTime, mLinuxInfo);

    // 硬件
    mContainLayout.put("硬件", 0, mModel, mBrand, mManufacturer, mMainboard, mImei, mMemory, mSDCard,
        mAbis);

    // 网络
    mContainLayout.put("网络", 0, mNetwork, mIpv4, mMac, mServiceProvider, mPhoneNum, mSimCardNumber);

    // cpu
    mContainLayout.put("cpu", 0, mCpuByte, mCpuCount, mCpuMaxRate, mCpuMinRate, mCpuType);
  }

  private void getDeviceInfos() {
    // 屏幕
    DisplayMetrics dm = getResources().getDisplayMetrics();
    mDensity = "密度：" + dm.densityDpi + "dpi/" + getResources().getString(R.string.dm_dpi) + "/"
        + dm.density + "x";
    mDensityX = "精确密度：" + dm.xdpi + "x" + dm.ydpi;
    mResolution = "分辨率：" + dm.widthPixels + "x" + dm.heightPixels;
    mSize = "屏幕尺寸："
        + DeivceInfoUtil.getScreenSizeOfDevice2(getWindowManager().getDefaultDisplay(), dm) + "英寸";

    // 系统
    mSdkInfo = "系统版本：" + android.os.Build.VERSION.RELEASE + "/" + android.os.Build.VERSION.CODENAME
        + "/API"
        + android.os.Build.VERSION.SDK_INT;
    mBasebandVersion = "基带版本：" + android.os.Build.BOARD;
    mLinuxInfo = "Linux内核：" + DeivceInfoUtil.getLinuxKernalInfoEx();
    mCompileTime =
        "编译时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(Build.TIME));

    // 内存
    mMemory = "内存：" + AppUtil.getAvailMemory(this) + "(可用)/" + AppUtil.getTotalMemory(this)
        + "(共)";
    mSDCard = "sdcard：" + AppUtil.getSDAvailableSize(this) + "(可用)/" + AppUtil.getSDTotalSize(this)
        + "(共)";

    // 硬件
    mModel = "型号：" + android.os.Build.MODEL;
    mBrand = "品牌：" + android.os.Build.BRAND;
    mManufacturer = "制造商：" + android.os.Build.MANUFACTURER;
    mMainboard = "主板：" + Build.BOARD;
    if (android.os.Build.VERSION.SDK_INT >= 21) {
      String abisStr = "";
      for (int i = 0; i < Build.SUPPORTED_ABIS.length; i++) {
        if (i != 0) {
          abisStr += ",";
        }
        abisStr += Build.SUPPORTED_ABIS[i];
      }
      if (abisStr != null) {
        mAbis = "ABIs：" + abisStr;
      }
    }

    // cpu
    mCpuCount = "cpu个数：" + DeivceInfoUtil.getNumCores() + "核";
    mCpuByte = "cpu位数：" + DeivceInfoUtil.getArchType(this) + "位";
    mCpuType = "cpu型号：" + DeivceInfoUtil.getCpuName();
    mCpuMaxRate = "cpu最高频率：" + DeivceInfoUtil.cpuFromat(DeivceInfoUtil.getMaxCpuFreq());
    mCpuMinRate = "cpu最低频率：" + DeivceInfoUtil.cpuFromat(DeivceInfoUtil.getMinCpuFreq());

    // 本机id
    TelephonyManager tm = ((TelephonyManager) getSystemService(TELEPHONY_SERVICE));
    mImei = "imei：" + tm.getDeviceId();

    if (tm.getSimState() > TelephonyManager.SIM_STATE_ABSENT) {
      try {
        mServiceProvider = "服务商：" + tm.getNetworkOperatorName();
        mPhoneNum = "tel：" + tm.getLine1Number(); // 手机号码，有的可得，有的不可得
        mSimCardNumber = "sim卡序列号：" + tm.getSimSerialNumber();// String
      } catch (Exception e) {}
    }


    // 网络
    mNetwork = "网络：" + AppUtil.getNetworkType(this);
    String ip = IPUtils.getIp(this);
    mIpv4 = "ipv4：" + (ip == null ? "无网络连接" : ip);
    mMac = "mac：" + AppUtil.getMacAddress(this);
  }

  private void initView() {
    mContainLayout = (InfoGroupLayout) findViewById(R.id.contain_layout);
  }

}
