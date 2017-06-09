package com.fw.zycoder.appbase.utils.shake;

import android.app.Activity;
import android.app.Service;
import android.os.Vibrator;

public class VibratorHelper {

    /**
     * 震动
     * @param activity
     * @param milliseconds 震动毫秒数
     */
    public static void Vibrate(final Activity activity, long milliseconds) {  
        Vibrator vibrator = (Vibrator) activity  
                .getSystemService(Service.VIBRATOR_SERVICE);  
        vibrator.vibrate(milliseconds);  
    }

    /**
     *
     * @param activity
     * @param pattern long[] pattern = {0, 1000, 2000, 5000, 3000,1000}; // {间隔时间，震动持续时间，间隔时间，震动持续时间，间隔时间，震动持续时间}
     * @param isRepeat 是否重复
     */
    public static void Vibrate(final Activity activity, long[] pattern,  
            boolean isRepeat) {  
        Vibrator vibrator = (Vibrator) activity  
                .getSystemService(Service.VIBRATOR_SERVICE);  
        vibrator.vibrate(pattern, isRepeat ? 1 : -1);  
    }  
}  