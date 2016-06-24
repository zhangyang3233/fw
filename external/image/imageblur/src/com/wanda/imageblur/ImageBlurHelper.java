package com.wanda.imageblur;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class ImageBlurHelper {
  /**
   * 使用bitmap的pix array进行模糊
   * 
   * @param source 原图
   * @param radius 模糊因子（1～100）,越大越模糊
   * @param scaleFactor 对原图缩小的倍数（可以加速模糊计算时间）
   * @return
   */
  public static Bitmap blurIntArray(Bitmap source, int radius, int scaleFactor) {
    Bitmap bmp = getScaledBitmap(source, scaleFactor);
    if (bmp == null) {
      return null;
    }

    return FastBlur.doBlurJniArray(bmp, radius, true);
  }

  /**
   * 直接使用bitmap进行模糊
   *
   * @param source 原图
   * @param radius 模糊因子（1～100）,越大越模糊
   * @param scaleFactor 对原图缩小的倍数（可以加速模糊计算时间）
   * @return
   */
  public static Bitmap blurBitmap(Bitmap source, int radius, int scaleFactor) {
    Bitmap bmp = getScaledBitmap(source, scaleFactor);
    if (bmp == null) {
      return null;
    }

    return FastBlur.doBlurJniBitMap(bmp, radius, true);
  }

  private static Bitmap getScaledBitmap(Bitmap source, int scaleFactor) {
    if (source == null || scaleFactor < 1) {
      return null;
    }

    int bw = source.getWidth();
    int bh = source.getHeight();
    int hbw = (int) bw / scaleFactor;
    int hbh = (int) bh / scaleFactor;
    Bitmap overlay = Bitmap.createBitmap(hbw, hbh, Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(overlay);
    Paint paint = new Paint();
    paint.setFlags(Paint.FILTER_BITMAP_FLAG);
    canvas.drawBitmap(source, new Rect(0, 0, bw, bh), new Rect(0, 0, hbw, hbh), paint);

    return overlay;
  }
}
