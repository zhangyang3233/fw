package com.fw.zycoder.appbase.utils.shake;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;

public class ShakeSensorListener implements SensorEventListener {
  private static long S = 1000000000;
  private static final String TAG = ShakeSensorListener.class.getSimpleName();
  private boolean isShake = true;
  private OnShakeInterface mOnShakeInterface;
  private long timestamp;
  private static final int ACCELERATE_VALUE = 20;

  public ShakeSensorListener(OnShakeInterface mOnShakeInterface) {
    this.mOnShakeInterface = mOnShakeInterface;
  }

  public void reset() {
    isShake = true;
    timestamp = 0;
  }

  public OnShakeInterface getOnShakeInterface() {
    return mOnShakeInterface;
  }

  public void setOnShakeInterface(OnShakeInterface mOnShakeInterface) {
    this.mOnShakeInterface = mOnShakeInterface;
  }

  @Override
  public void onSensorChanged(SensorEvent event) {
    if (!isShake) {
      return;
    }

    if (isEnough(event) && mOnShakeInterface != null) {
      mOnShakeInterface.onShake();
      isShake = false;
    }
  }

  private boolean isEnough(SensorEvent event) {
    float[] values = event.values;
    /**
     * 一般在这三个方向的重力加速度达到20就达到了摇晃手机的状态 x : x轴方向的重力加速度，向右为正 y :
     * y轴方向的重力加速度，向前为正 z : z轴方向的重力加速度，向上为正
     */
    float x = Math.abs(values[0]);
    float y = Math.abs(values[1]);
    float z = Math.abs(values[2]);

    double d = Math.sqrt(x * x + y * y + z * z);

    if (d >= ACCELERATE_VALUE) {
      if (timestamp == 0 || event.timestamp - timestamp > 2 * S) {
        Log.i(TAG, event.timestamp - timestamp + "");
        timestamp = event.timestamp;
        return false;
      } else if (event.timestamp - timestamp > 1 * S) {
        return true;
      }
    }
    return false;
  }

  @Override
  public void onAccuracyChanged(Sensor sensor, int accuracy) {
    // TODO Auto-generated method stub
  }

}
