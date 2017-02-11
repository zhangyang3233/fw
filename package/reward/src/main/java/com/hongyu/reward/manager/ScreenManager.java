package com.hongyu.reward.manager;

import android.app.Activity;

import java.util.Stack;

/**
 * manager - screen - 屏幕管理器
 *
 * =======================================
 * Copyright 2016-2017 
 * =======================================
 *
 * @since 2016-6-17 下午10:19:52
 * @author centos
 *
 */
public class ScreenManager {

    private static Stack<Activity> activityStack;
    private static ScreenManager instance;
    
    public static ScreenManager getScreenManager(){
        if (instance == null) {
            instance = new ScreenManager();
        }
        return instance;
    }
    
    /**
     * 从栈中移除最后一个元素
     */
    public void popActivity(){
        Activity activity = currentActivity();
        if (activity != null) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
            activityStack.removeElement(activity);
            activity = null;
        }
    }
    
    /**
     * 从栈中移除指定的元素
     * @param activity
     */
    public void popActivity(Activity activity){
        if (activity != null) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
            activityStack.removeElement(activity);
        }
    }

    /**
     * 将新元素添加到栈中
     * @param activity
     */
    public void pushActivity(Activity activity){
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.addElement(activity);
    }
    
    /**
     * 获取栈顶元素
     * @return
     */
    public Activity currentActivity(){
        Activity activity = null;
        try {
            activity = activityStack.lastElement();
        } catch (Exception e) {
        }
        return activity;
    }
    
    /**
     * 移除全部元素，除了指定类型的以外
     * @param cls
     */
    public void popAllActivityExceptOne(Class<?> cls){
        int size = activityStack.size();
        for (int i = 0; i < size; i++) {
            Activity activity = activityStack.get(i);
            if (activity != null && activity.getClass().equals(cls)) {
                //nothing
            }else{
                popActivity(activity);
                size--;
                i--;
            }
        }
    }
    
    /**
     * 按照给定的Class，结束一个Activity
     * @param cls
     */
    public void finishActivityByClass(Class<?> cls) {
        int size = activityStack.size();
        for (int i = 0; i < size; i++) {
            Activity activity = activityStack.get(i);
            if (activity != null && activity.getClass().equals(cls)) {
                popActivity(activity);
                size--;
                i--;
            }
        }
    }
}
