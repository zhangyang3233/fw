package com.hongyu.reward.location.client;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.hongyu.reward.location.LocationClientInterface;
import com.hongyu.reward.location.LocationListenerDelegate;
import com.hongyu.reward.model.AppLocation;

import java.util.List;

public class BaiduClient extends LocationClientInterface {
  LocationClient mLocationClient;
  LocationListenerDelegate locationListener = null;

  public BaiduClient(Context context) {
    mLocationClient = new LocationClient(context);
  }

  @Override
  public void initConfig() {
    LocationClientOption option = mLocationClient.getLocOption();
    // option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//
    // 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
    // option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系
    int span = 1000 * 5;
    option.setScanSpan(span);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
    option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
    // option.setOpenGps(true);// 可选，默认false,设置是否使用gps
    // option.setLocationNotify(true);// 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
    // option.setIsNeedLocationDescribe(false);//
    // 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
    // option.setIsNeedLocationPoiList(true);//
    // 可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
    // option.setIgnoreKillProcess(false);//
    // 可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
    option.SetIgnoreCacheException(true);// 可选，默认false，设置是否收集CRASH信息，默认收集
    // option.setEnableSimulateGps(false);// 可选，默认false，设置是否需要过滤gps仿真结果，默认需要
    mLocationClient.setLocOption(option);
  }

  @Override
  public void start() {
    if (!mLocationClient.isStarted()) {
      mLocationClient.start();
    }
  }

  @Override
  public void stop() {
    if (mLocationClient.isStarted()) {
      mLocationClient.stop();
    }
  }

  @Override
  public void setLocationListener(final LocationListenerDelegate locationListener) {
    this.locationListener = locationListener;
    mLocationClient.registerLocationListener(new BDLocationListener() {
      @Override
      public void onReceiveLocation(BDLocation location) {
        int resultType = location.getLocType();
        AppLocation appLocation = null;
        boolean success;
        if (resultType == 61 || resultType == 65 || resultType == 66 || resultType == 67
            || resultType == 161) {
          success = true;
          double latitude = location.getLatitude();
          double longitude = location.getLongitude();
          appLocation = new AppLocation(latitude, longitude, location.getCity());
        } else {
          success = false;
        } // locationInfo
        locationListener.onLog(getLog(location));
        if (success || appLocation != null) {
          locationListener.notifyLocation(appLocation);
        } else {
          locationListener.failed("location Error, Desc:"
              + location.getLocTypeDescription() + "  resultType: " + resultType);
        }
      }
    });
  }


  private String getLog(BDLocation location) {
    if (location == null) {
      return "null";
    }
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
    return sb.toString();
  }
}
