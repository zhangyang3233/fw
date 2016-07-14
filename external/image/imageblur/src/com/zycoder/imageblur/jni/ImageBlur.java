package com.zycoder.imageblur.jni;

import android.graphics.Bitmap;

public class ImageBlur {
  static {
    System.loadLibrary("ImageBlur");
  }

  public static native void nativeBlurIntArray(int[] pImg, int w, int h, int r);

  public static native void nativeBlurBitMap(Bitmap bitmap, int r);
}
