package com.hongyu.reward.manager;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.fw.zycoder.utils.CollectionUtils;
import com.fw.zycoder.utils.SPUtil;
import com.hongyu.reward.config.Constants;
import com.hongyu.reward.interfaces.CityChangedListener;
import com.hongyu.reward.interfaces.GetLocationListener;
import com.hongyu.reward.model.AppLocation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangyang131 on 16/9/22.
 */
public class LocationManager {
  private static LocationManager instance;
  private LocationClient mLocationClient = null;
  private MyLocationListener myListener = new MyLocationListener();
  private ArrayList<GetLocationListener> ls = new ArrayList<>();
  private AppLocation locationInfo;
  private ArrayList<CityChangedListener> cityChangedListeners = new ArrayList<>();

  public void addCitiChangedListener(CityChangedListener l) {
    if (!cityChangedListeners.contains(l)) {
      cityChangedListeners.add(l);
    }
  }

  public boolean removeCityChangedListener(CityChangedListener l) {
    return cityChangedListeners.remove(l);
  }

  public static LocationManager getInstance() {
    if (instance == null) {
      instance = new LocationManager();
    }
    return instance;
  }

  public AppLocation getLocation() {
    return locationInfo;
  }

  public void addLocationListener(GetLocationListener l) {
    if (!ls.contains(l)) {
      ls.add(l);
    }
  }

  public void removeLocationListener(GetLocationListener l) {
    ls.remove(l);
  }

  public void start() {
    if (mLocationClient != null && !mLocationClient.isStarted()) {
      mLocationClient.start();
    }
  }

  public boolean isStarted() {
    return mLocationClient.isStarted();
  }

  public void stop() {
    mLocationClient.stop();
  }

  public void init(Context context) {
    myListener = new MyLocationListener();
    mLocationClient = new LocationClient(context); // 声明LocationClient类
    initLocation();
    mLocationClient.registerLocationListener(myListener); // 注册监听函数
  }

  public static void saveCity(String city) {
    String locationCity = getSavedCity();
    if (!TextUtils.isEmpty(locationCity) && locationCity.equals(city)) {
      return;
    }
    SPUtil.putString(Constants.Pref.CURRENT_CITY, city);
    for (CityChangedListener l : getInstance().cityChangedListeners) {
      l.onCityChanged(locationCity, city);
    }
  }

  public static String getSavedCity() {
    return SPUtil.getString(Constants.Pref.CURRENT_CITY, "");
  }

  private void initLocation() {
     LocationClientOption option = mLocationClient.getLocOption();
    // option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//
    // 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
    // option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系
     int span = 1000*5;
     option.setScanSpan(span);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
     option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
    // option.setOpenGps(true);// 可选，默认false,设置是否使用gps
    // option.setLocationNotify(true);// 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
    // option.setIsNeedLocationDescribe(false);//
    // 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
    // option.setIsNeedLocationPoiList(true);// 可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
    // option.setIgnoreKillProcess(false);//
    // 可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
     option.SetIgnoreCacheException(true);// 可选，默认false，设置是否收集CRASH信息，默认收集
    // option.setEnableSimulateGps(false);// 可选，默认false，设置是否需要过滤gps仿真结果，默认需要
     mLocationClient.setLocOption(option);

  }

  public class MyLocationListener implements BDLocationListener {


    @Override
    public void onReceiveLocation(BDLocation location) {
      logLocation(location);
      notifyListener(location);
    }

    private void notifyListener(BDLocation location) {
      int resultType = location.getLocType();
      boolean success;
      if (resultType == 61 || resultType == 65 || resultType == 66 || resultType == 67
          || resultType == 161) {
        success = true;
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        locationInfo = new AppLocation(latitude, longitude, location.getCity());
      } else {
        success = false;
      } // locationInfo

      if (!CollectionUtils.isEmpty(ls)) {
        for (GetLocationListener l : ls) {
          if (success) {
            l.onSuccess(locationInfo);
          } else {
            l.onFailed(location.getLocTypeDescription());
          }
        }
        ls.clear();
      }
    }


    // 61 ： GPS定位结果，GPS定位成功。
    // 62 ： 无法获取有效定位依据，定位失败，请检查运营商网络或者wifi网络是否正常开启，尝试重新请求定位。
    // 63 ： 网络异常，没有成功向服务器发起请求，请确认当前测试手机网络是否通畅，尝试重新请求定位。
    // 65 ： 定位缓存的结果。
    // 66 ： 离线定位结果。通过requestOfflineLocaiton调用时对应的返回结果。
    // 67 ： 离线定位失败。通过requestOfflineLocaiton调用时对应的返回结果。
    // 68 ： 网络连接失败时，查找本地离线定位时对应的返回结果。
    // 161： 网络定位结果，网络定位定位成功。
    // 162： 请求串密文解析失败，一般是由于客户端SO文件加载失败造成，请严格参照开发指南或demo开发，放入对应SO文件。
    // 167： 服务端定位失败，请您检查是否禁用获取位置信息权限，尝试重新请求定位。
    // 502： key参数错误，请按照说明文档重新申请KEY。
    // 505： key不存在或者非法，请按照说明文档重新申请KEY。
    // 601： key服务被开发者自己禁用，请按照说明文档重新申请KEY。
    // 602： key mcode不匹配，您的ak配置过程中安全码设置有问题，请确保：sha1正确，“;”分号是英文状态；且包名是您当前运行应用的包名，请按照说明文档重新申请KEY。
    // 501～700：key验证失败，请按照说明文档重新申请KEY。



    private void logLocation(BDLocation location) {
      // Receive Location
      StringBuffer sb = new StringBuffer(256);
      sb.append("time : ");
      sb.append(location.getTime());
      sb.append("\nerror code : ");
      sb.append(location.getLocType());
      sb.append("\nlatitude : ");
      sb.append(location.getLatitude());
      sb.append("\nlontitude : ");
      sb.append(location.getLongitude());
      sb.append("\nradius : ");
      sb.append(location.getRadius());
      if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
        sb.append("\nspeed : ");
        sb.append(location.getSpeed());// 单位：公里每小时
        sb.append("\nsatellite : ");
        sb.append(location.getSatelliteNumber());
        sb.append("\nheight : ");
        sb.append(location.getAltitude());// 单位：米
        sb.append("\ndirection : ");
        sb.append(location.getDirection());// 单位度
        sb.append("\naddr : ");
        sb.append(location.getAddrStr());
        sb.append("\ndescribe : ");
        sb.append("gps定位成功");

      } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
        sb.append("\naddr : ");
        sb.append(location.getAddrStr());
        // 运营商信息
        sb.append("\noperationers : ");
        sb.append(location.getOperators());
        sb.append("\ndescribe : ");
        sb.append("网络定位成功");
      } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
        sb.append("\ndescribe : ");
        sb.append("离线定位成功，离线定位结果也是有效的");
      } else if (location.getLocType() == BDLocation.TypeServerError) {
        sb.append("\ndescribe : ");
        sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
      } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
        sb.append("\ndescribe : ");
        sb.append("网络不同导致定位失败，请检查网络是否通畅");
      } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
        sb.append("\ndescribe : ");
        sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
      }
      sb.append("\nlocationdescribe : ");
      sb.append(location.getLocationDescribe());// 位置语义化信息
      List<Poi> list = location.getPoiList();// POI数据
      if (list != null) {
        sb.append("\npoilist size = : ");
        sb.append(list.size());
        for (Poi p : list) {
          sb.append("\npoi= : ");
          sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
        }
      }
      Log.i("BaiduLocationApiDem", sb.toString());
    }

  }

  // @TargetApi(23)
  // private ArrayList<String> getPersimmions() {
  // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
  // ArrayList<String> permissions = new ArrayList<String>();
  // /***
  // * 定位权限为必须权限，用户如果禁止，则每次进入都会申请
  // */
  // // 定位精确位置
  // if (GlobalConfig.getAppContext().checkSelfPermission(
  // Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
  // permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
  // }
  // if (GlobalConfig.getAppContext().checkSelfPermission(
  // Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
  // permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
  // }
  // /*
  // * 读写权限和电话状态权限非必要权限(建议授予)只会申请一次，用户同意或者禁止，只会弹一次
  // */
  // // 读写权限
  // addPermission(permissions, Manifest.permission.WRITE_EXTERNAL_STORAGE);
  // // 读取电话状态权限
  // addPermission(permissions, Manifest.permission.READ_PHONE_STATE);
  // return permissions;
  // }
  // return null;
  // }

  // @TargetApi(23)
  // private boolean addPermission(ArrayList<String> permissionsList, String permission) {
  // if (GlobalConfig.getAppContext().checkSelfPermission(permission) !=
  // PackageManager.PERMISSION_GRANTED) { // 如果应用没有获得对应权限,则添加到列表中,准备批量申请
  // if (shouldShowRequestPermissionRationale(permission)) {
  // return true;
  // } else {
  // permissionsList.add(permission);
  // return false;
  // }
  //
  // } else {
  // return true;
  // }
  // }

}
