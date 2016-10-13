package com.hongyu.reward.model.dbmodel;

import android.text.TextUtils;

import org.litepal.crud.DataSupport;

/**
 * Created by zhangyang131 on 16/10/13.
 */
public class SearchModel extends DataSupport {
  String tag;

  public SearchModel() {}

  public SearchModel(String tag) {
    this.tag = tag;
  }

  public String getTag() {
    return tag;
  }

  public void setTag(String tag) {
    this.tag = tag;
  }


  public void saveIfTagNotExist() {
    if (TextUtils.isEmpty(tag)) {
      return;
    }
    saveIfNotExist("tag='" + tag + "'");
  }
}
