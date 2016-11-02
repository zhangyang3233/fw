package com.hongyu.reward.model;

import java.util.List;

/**
 * Created by zhangyang131 on 16/10/17.
 */
public class BillDetailModel extends BaseModel {

  /**
   * date : 06月17日18:51
   * order_type : 0
   * price : 121.00
   * shop_name : 汤城小厨(银泰店)
   * status : 已完成
   * value : 1000.00
   */

  private List<BillItem> data;

  public List<BillItem> getData() {
    return data;
  }

  public void setData(List<BillItem> data) {
    this.data = data;
  }

  public static class BillItem {
    private String date;
    private String order_type;
    private String price;
    private String shop_name;
    private String status;
    private String value;
    private String account_type;

    public String getAccount_type() {
      return account_type;
    }

    public void setAccount_type(String account_type) {
      this.account_type = account_type;
    }

    public String getDate() {
      return date;
    }

    public void setDate(String date) {
      this.date = date;
    }

    public String getOrder_type() {
      return order_type;
    }

    public void setOrder_type(String order_type) {
      this.order_type = order_type;
    }

    public String getPrice() {
      return price;
    }

    public void setPrice(String price) {
      this.price = price;
    }

    public String getShop_name() {
      return shop_name;
    }

    public void setShop_name(String shop_name) {
      this.shop_name = shop_name;
    }

    public String getStatus() {
      return status;
    }

    public void setStatus(String status) {
      this.status = status;
    }

    public String getValue() {
      return value;
    }

    public void setValue(String value) {
      this.value = value;
    }
  }
}
