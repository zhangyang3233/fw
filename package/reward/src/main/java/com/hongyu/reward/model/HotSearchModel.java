package com.hongyu.reward.model;

import java.util.ArrayList;

/**
 * Created by zhangyang131 on 16/10/13.
 */
public class HotSearchModel extends BaseModel{
    private DataModel data;

    public DataModel getData() {
        return data;
    }

    public void setData(DataModel data) {
        this.data = data;
    }

    public static class DataModel {
        private ArrayList<String> tag;

        public ArrayList<String> getTag() {
            return tag;
        }

        public void setTag(ArrayList<String> tag) {
            this.tag = tag;
        }
    }
//    ArrayList<SearchModel> data;
//
//    public ArrayList<SearchModel> getData() {
//        return data;
//    }
//
//    public void setData(ArrayList<SearchModel> data) {
//        this.data = data;
//    }


}
