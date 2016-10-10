package com.hongyu.reward.model;

/**
 * Created by zhangyang131 on 16/10/10.
 */
public class NoticeEvent extends BaseModel {
    public static final int USER_IMG_CHANGED = 1;
    private int type;
    private String data;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
