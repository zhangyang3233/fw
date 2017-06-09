package debug.xly.com.debugkit.shake;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;

/**
 * 摇一摇辅助类
 * Created by zhangyang131 on 2017/5/19.
 */
public class ShakeHelper {
    private SensorManager sensorManager;
    private ShakeSensorListener mShakeSensorListener;

    public ShakeHelper(Context context, OnShakeInterface mOnShakeInterface) {
        this.mShakeSensorListener = new ShakeSensorListener(mOnShakeInterface);
        this.sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
    }

    // 注册事件监听
    public void register(){
        sensorManager.registerListener(mShakeSensorListener,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_FASTEST);
    }

    // 注销事件监听
    public void unRegister(){
        sensorManager.unregisterListener(mShakeSensorListener);
    }

    public void prepareNext (){
        mShakeSensorListener.reset();
    }
}
