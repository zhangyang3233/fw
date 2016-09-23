package com.hongyu.reward.utils;

/**
 * Created by zhangyang131 on 16/9/23.
 */
public class DoubleClickUtil {
    private Object tag;
    private long time;
    private static DoubleClickUtil instance;

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public static DoubleClickUtil getInstance(){
        if(instance == null){
            instance = new DoubleClickUtil();
        }
        return instance;
    }

    public static boolean isDouble(Object tag){
        if(tag == null){
            throw new RuntimeException();
        }
        boolean isDouble = false;
        if(tag.equals(getInstance().getTag())){
            if(System.currentTimeMillis() - getInstance().getTime() < 1500){
                isDouble = true;
            }
        }
        getInstance().setTag(tag);
        getInstance().setTime(System.currentTimeMillis());
        return isDouble;
    }
}
