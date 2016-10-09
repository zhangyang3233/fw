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
    if(!TextUtils.isEmpty(city) && city.endsWith("市")){
      return city.substring(0, city.length()-1);
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
}
