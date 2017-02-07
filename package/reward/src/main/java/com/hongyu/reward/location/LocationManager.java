package com.hongyu.reward.location;

import android.content.Context;
import android.text.TextUtils;

import com.fw.zycoder.http.callback.DataCallback;
import com.fw.zycoder.utils.CollectionUtils;
import com.fw.zycoder.utils.GlobalConfig;
import com.fw.zycoder.utils.Log;
import com.fw.zycoder.utils.MainThreadPostUtils;
import com.fw.zycoder.utils.SPUtil;
import com.hongyu.reward.config.Constants;
import com.hongyu.reward.http.ResponesUtil;
import com.hongyu.reward.interfaces.CityChangedListener;
import com.hongyu.reward.manager.AccountManager;
import com.hongyu.reward.model.AppLocation;
import com.hongyu.reward.model.BaseModel;
import com.hongyu.reward.request.UpdateLocationRequestBuidler;

import java.util.ArrayList;

/**
 * Created by zhangyang131 on 16/9/22.
 */
public class LocationManager implements LocationListenerDelegate {
  private static LocationManager instance;
  private LocationClientInterface mLocationClient = null;
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

  public synchronized static LocationManager getInstance() {
    if (instance == null) {
      instance = new LocationManager();
    }
    return instance;
  }

  public AppLocation getLocation() {
    return locationInfo;
  }

  public void setLocation(AppLocation appLocation) {
    checkChangedAndNotify(appLocation);
    this.locationInfo = appLocation;
  }

  private void checkChangedAndNotify(final AppLocation appLocation) {
    if (appLocation == null) {
      return;
    }
    boolean needUpdate = false;
    if (this.locationInfo == null) {
      needUpdate = true;
    } else if (this.locationInfo.equals(appLocation)) {
      Log.i("位置相等");
      return;
    } else if (this.locationInfo.like(appLocation)) {
      needUpdate = true;
    }
    if (needUpdate) {
      MainThreadPostUtils.post(new Runnable() {
        @Override
        public void run() {
          updateLocation(appLocation);
        }
      });
    }
  }

  private void updateLocation(AppLocation appLocation) {
    if (!AccountManager.getInstance().isLogin()) {
      return;
    }
    UpdateLocationRequestBuidler builder = new UpdateLocationRequestBuidler(appLocation.toString());
    builder.setDataCallback(new DataCallback<BaseModel>() {
      @Override
      public void onDataCallback(BaseModel data) {
        if (ResponesUtil.checkModelCodeOK(data)) {
          Log.i("更新位置成功");
        } else {
          Log.e("更新位置失败：" + ResponesUtil.getErrorMsg(data));
        }
      }
    });
    builder.build().submit();
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
    try {
      if(mLocationClient == null){
        init(GlobalConfig.getAppContext());
      }
      mLocationClient.start();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void stop() {
    mLocationClient.stop();
  }

  public void init(Context context) {
    mLocationClient = LocationClientFactory.create(context, LocationType.GAODE);
    mLocationClient.initConfig();
    mLocationClient.setLocationListener(this); // 注册监听函数
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


  @Override
  public void notifyLocation(AppLocation appLocation) {
    setLocation(appLocation);
    if (!CollectionUtils.isEmpty(ls)) {
      for (GetLocationListener l : ls) {
        l.onSuccess(locationInfo);
      }
    }
    ls.clear();
  }

  @Override
  public void failed(String msg) {
    if (!CollectionUtils.isEmpty(ls)) {
      for (GetLocationListener l : ls) {
        l.onFailed(msg);
      }
    }
    ls.clear();
  }

  @Override
  public void onLog(String logStr) {
    // Log.i("locationManager", logStr);
  }
}
