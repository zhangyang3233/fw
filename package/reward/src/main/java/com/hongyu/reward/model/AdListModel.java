package com.hongyu.reward.model;

import java.util.ArrayList;

/**
 * Created by zhangyang131 on 16/9/11.
 */
public class AdListModel extends BaseModel {
    private ArrayList<AdModel> data;

    public ArrayList<AdModel> getData() {
        return data;
    }

    public void setData(ArrayList<AdModel> data) {
        this.data = data;
    }
}
