package com.hongyu.reward.model;

/**
 * Created by zhangyang131 on 16/10/10.
 */
public class NoticeEvent extends BaseModel {
    public static final int USER_IMG_CHANGED = 1;
    public static final int USER_NICKNAME_CHANGED = 2;
    public static final int USER_GENDER_CHANGED = 3;
    public static final int ORDER_STATUS_CHANGED = 4;
    public static final int USER_POINT_CHANGED = 5;
    private int type;
    private String data;

    public NoticeEvent(int type) {
        this.type = type;
    }

    public NoticeEvent(int type, String data) {
        this.type = type;
        this.data = data;
    }

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
