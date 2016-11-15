package com.hongyu.reward.location.client;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.fw.zycoder.utils.Log;
import com.hongyu.reward.location.LocationClientInterface;
import com.hongyu.reward.location.LocationListenerDelegate;
import com.hongyu.reward.model.AppLocation;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GaodeClient extends LocationClientInterface {
  AMapLocationClient mAMapLocationClient;

  public GaodeClient(Context context) {
    mAMapLocationClient = new AMapLocationClient(context);
  }

  @Override
  public void initConfig() {
    AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
    mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
    mLocationOption.setInterval(5000); // 设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
    mLocationOption.setNeedAddress(true);// 设置是否返回地址信息（默认返回地址信息）
    // 设置是否强制刷新WIFI，默认为true，强制刷新。
    mLocationOption.setWifiActiveScan(false);
    mLocationOption.setHttpTimeOut(10000);
    mAMapLocationClient.setLocationOption(mLocationOption);
  }

  @Override
  public void start() {
    if (mAMapLocationClient != null) {
      if (!mAMapLocationClient.isStarted()) {
        mAMapLocationClient.startLocation();
      }
    }
  }

  @Override
  public void stop() {
    if (mAMapLocationClient != null) {
      if (mAMapLocationClient.isStarted()) {
        mAMapLocationClient.stopLocation();
      }
    }
  }

  @Override
  public void setLocationListener(final LocationListenerDelegate locationListener) {
    this.locationListener = locationListener;
    mAMapLocationClient.setLocationListener(new AMapLocationListener() {
      @Override
      public void onLocationChanged(AMapLocation location) {
        locationListener.onLog(getLog(location));
        AppLocation appLocation = null;
        boolean success = false;
        if (location != null) {
          if (location.getErrorCode() == 0) {
            // 可在其中解析amapLocation获取相应内容。
            success = true;
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            appLocation = new AppLocation(latitude, longitude, location.getCity());
          } else {
            success = false;
            // 定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
            Log.e("AmapError", "location Error, ErrCode:"
                + location.getErrorCode() + ", errInfo:"
                + location.getErrorInfo());
          }
        }
        if (success || appLocation != null) {
          locationListener.notifyLocation(appLocation);
        } else {
          locationListener.failed("location Error, ErrCode:"
              + location.getErrorCode() + ", errInfo:"
              + location.getErrorInfo());
        }
      }
    });
  }

  private String getLog(AMapLocation amapLocation) {
    if(amapLocation == null){
      return "null";
    }
    // Receive Location
    StringBuffer sb = new StringBuffer(256);
    sb.append("当前定位结果来源:" + amapLocation.getLocationType() + "\n");// 获取当前定位结果来源，如网络定位结果，详见定位类型表
    sb.append("纬度: " + amapLocation.getLatitude() + "\n");// 获取纬度
    sb.append("经度: " + amapLocation.getLongitude() + "\n");// 获取经度
    sb.append("精度信息: " + amapLocation.getAccuracy() + "\n");// 获取精度信息
    sb.append("地址: " + amapLocation.getAddress() + "\n");// 地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
    sb.append("国家信息: " + amapLocation.getCountry() + "\n");// 国家信息
    sb.append("省信息: " + amapLocation.getProvince() + "\n");// 省信息
    sb.append("城市信息: " + amapLocation.getCity() + "\n");// 城市信息
    sb.append("城区信息: " + amapLocation.getDistrict() + "\n");// 城区信息
    sb.append("街道信息: " + amapLocation.getStreet() + "\n");// 街道信息
    sb.append("街道门牌号信息: " + amapLocation.getStreetNum() + "\n");// 街道门牌号信息
    sb.append("城市编码: " + amapLocation.getCityCode() + "\n");// 城市编码
    sb.append("地区编码: " + amapLocation.getAdCode() + "\n");// 地区编码
    sb.append("获取当前定位点的AOI信息: " + amapLocation.getAoiName() + "\n");// 获取当前定位点的AOI信息
    sb.append("获取GPS的当前状态: " + amapLocation.getGpsAccuracyStatus() + "\n");// 获取GPS的当前状态
    // 获取定位时间
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date date = new Date(amapLocation.getTime());
    sb.append("定位时间： " + df.format(date));
    return sb.toString();
  }

}
