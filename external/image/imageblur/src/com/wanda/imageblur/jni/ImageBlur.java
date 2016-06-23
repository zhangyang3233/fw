package com.wanda.imageblur.jni;

import android.graphics.Bitmap;

public class ImageBlur {
  public static native void nativeBlurIntArray(int[] pImg, int w, int h, int r);

  public static native void nativeBlurBitMap(Bitmap bitmap, int r);

  static {
    System.loadLibrary("ImageBlur");
  }
}