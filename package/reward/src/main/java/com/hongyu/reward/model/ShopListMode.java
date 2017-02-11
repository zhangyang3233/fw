package com.hongyu.reward.model;

import android.text.TextUtils;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by zhangyang131 on 16/9/12.
 */
public class ShopListMode extends BaseModel {

  private int total;
  private ArrayList<ShopInfo> data;

  public int getTotal() {
    return total;
  }

  public void setTotal(int total) {
    this.total = total;
  }

  public ArrayList<ShopInfo> getData() {
    return data;
  }

  public void setData(ArrayList<ShopInfo> data) {
    this.data = data;
  }

  public static class ShopInfo implements BaseDataModel, Serializable {
    String order_id;
    String shop_id;
    String shop_name;
    String distance;
    String mapuid;
    String address;
    String save_time;
    String img;
    String location;
    String order_num;

    public String getOrder_id() {
      return order_id;
    }

    public void setOrder_id(String order_id) {
      this.order_id = order_id;
    }

    public String getAddress() {
      return address;
    }

    public void setAddress(String address) {
      this.address = address;
    }

    public String getShop_id() {
      return shop_id;
    }

    public void setShop_id(String shop_id) {
      this.shop_id = shop_id;
    }

    public String getShop_name() {
      return shop_name;
    }

    public void setShop_name(String shop_name) {
      this.shop_name = shop_name;
    }

    public String getDistanceStr() {
      if (TextUtils.isEmpty(distance)) {
        return distance;
      }
      int m;
      try {
        m = Integer.parseInt(distance);
        if (m < 1000) {
          return String.valueOf(m);
        } else {
          DecimalFormat df = new DecimalFormat("###0.00");
          double d1 = m / 1000f;
          return String.valueOf(df.format(d1) + "k");
        }
      } catch (NumberFormatException e) {
        e.printStackTrace();
      }
      return distance;
    }

    public String getDistance() {
      return distance;
    }



    public void setDistance(String distance) {
      this.distance = distance;
    }

    public String getMapuid() {
      return mapuid;
    }

    public void setMapuid(String mapuid) {
      this.mapuid = mapuid;
    }

    public String getSave_time() {
      return save_time;
    }

    public void setSave_time(String save_time) {
      this.save_time = save_time;
    }

    public String getImg() {
      return img;
    }

    public void setImg(String img) {
      this.img = img;
    }

    public String getLocation() {
      return location;
    }

    public void setLocation(String location) {
      this.location = location;
    }

    public String getOrder_num() {
      return order_num;
    }

    public void setOrder_num(String order_num) {
      this.order_num = order_num;
    }
  }
}
