package debug.xly.com.debugkit.util;

import android.app.Service;
import android.content.Context;
import android.os.Vibrator;

public class VibratorHelper {

    /**
     * 震动
     * @param context
     * @param milliseconds 震动毫秒数
     */
    public static void Vibrate(final Context context, long milliseconds) {
        Vibrator vibrator = (Vibrator) context
                .getSystemService(Service.VIBRATOR_SERVICE);  
        vibrator.vibrate(milliseconds);  
    }

    /**
     *
     * @param context
     * @param pattern long[] pattern = {0, 1000, 2000, 5000, 3000,1000}; // {间隔时间，震动持续时间，间隔时间，震动持续时间，间隔时间，震动持续时间}
     * @param isRepeat 是否重复
     */
    public static void Vibrate(final Context context, long[] pattern,
            boolean isRepeat) {  
        Vibrator vibrator = (Vibrator) context
                .getSystemService(Service.VIBRATOR_SERVICE);  
        vibrator.vibrate(pattern, isRepeat ? 1 : -1);  
    }  
}  