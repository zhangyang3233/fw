package com.hongyu.reward.model;

import android.text.TextUtils;

import java.text.DecimalFormat;

/**
 * Created by zhangyang131 on 16/9/22.
 */
public class AppLocation {
  private static final long MAX_PAST_TIME = 1000 * 60 * 5;
  private double latitude;
  private double longitude;
  private String city;
  private long setTime;

  public AppLocation(double latitude, double longitude, String city) {
    this.latitude = latitude;
    this.longitude = longitude;
    this.city = city;
    this.setTime = System.currentTimeMillis();
  }

  public double getLatitude() {
    return latitude;
  }

  public void setLatitude(double latitude) {
    this.latitude = latitude;
  }

  public double getLongitude() {
    return longitude;
  }

  public void setLongitude(double longitude) {
    this.longitude = longitude;
  }

  public String getCity() {
    if (!TextUtils.isEmpty(city) && city.endsWith("市")) {
      return city.substring(0, city.length() - 1);
    }
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public boolean isPast() {
    return System.currentTimeMillis() - setTime > MAX_PAST_TIME;
  }

  @Override
  public String toString() {
    DecimalFormat df = new DecimalFormat("###.######");
    String location = df.format(latitude) + "," + df.format(longitude);
    return location;
  }


  @Override
  public boolean equals(Object o) {
    if (o == null) {
      return false;
    } else if (this == o) {
      return true;
    } else if (!(o instanceof AppLocation)) {
      return false;
    } else {
      if (((AppLocation) o).city.equals(this.city) && ((AppLocation) o).latitude == this.latitude
          && ((AppLocation) o).longitude == this.longitude) {
        return true;
      }
      return false;
    }
  }

  public boolean like(AppLocation appLocation) {
    return like(appLocation, 20);
  }

  public boolean like(AppLocation appLocation, int fuzzy) {
    if (appLocation == null) {
      return false;
    }
    if (distance(this.longitude, this.latitude, appLocation.longitude,
        appLocation.latitude) < fuzzy) {
      return true;
    }
    return false;
  }

  private static double R = 6378137; // 地球半径

  public static double distance(double long1, double lat1, double long2,
      double lat2) {
    double a, b;
    lat1 = lat1 * Math.PI / 180.0;
    lat2 = lat2 * Math.PI / 180.0;
    a = lat1 - lat2;
    b = (long1 - long2) * Math.PI / 180.0;
    double d;
    double sa2, sb2;
    sa2 = Math.sin(a / 2.0);
    sb2 = Math.sin(b / 2.0);
    d = 2
        * R
        * Math.asin(Math.sqrt(sa2 * sa2 + Math.cos(lat1)
            * Math.cos(lat2) * sb2 * sb2));
    return d;
  }
}
