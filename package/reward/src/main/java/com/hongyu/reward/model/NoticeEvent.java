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
    public static final int USER_MONEY_CHANGED = 6;
    public static final int NEW_ORDER_CREATE_CLICK = 7;
    public static final int TAB2_NEED_FRESH = 8;
    public static final int RECEIVE_REQUEST_SUCCESS = 9;
    public static final int REGIST_SUCCESS = 10;
    public static final int USER_LOGINED = 11;
    public static final int NEW_ORDER = 12;
    public static final int NEW_ORDER_CLEAR = 13;

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
