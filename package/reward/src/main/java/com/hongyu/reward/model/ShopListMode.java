package com.hongyu.reward.model;

import java.io.Serializable;
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
    String shop_id;
    String shop_name;
    String distance;
    String mapuid;
    String address;
    String save_time;
    String img;
    String location;
    String order_num;

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
