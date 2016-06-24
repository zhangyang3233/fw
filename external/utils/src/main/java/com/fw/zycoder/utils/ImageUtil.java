package com.fw.zycoder.utils;

import android.app.ActivityManager;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageUtil {

  private static final String TAG = "db.Util";
  private static final String MAPS_PACKAGE_NAME = "com.google.android.apps.maps";
  private static final String MAPS_CLASS_NAME = "com.google.android.maps.MapsActivity";

  /**
   * Converts drawable to bitmap. if drawable is {@link BitmapDrawable}
   * will call {@link BitmapDrawable#getBitmap()} indeed
   * if you want to avoid this, use
   * {@link #decodeBitmapFromDrawble(Drawable, Config...)}
   * instead
   *
   * @param drawable drawable
   * @param config optional field, if provided, convert bitmap according to it; otherwise, using
   *          Config.RGB_565 by default
   * @return bitmap
   */
  public static Bitmap drawableToBitmap(Drawable drawable, Config... config) {
    if (drawable == null || drawable.getIntrinsicWidth() <= 0
        || drawable.getIntrinsicHeight() <= 0) {
      return null;
    }
    if (drawable instanceof BitmapDrawable) {
      Bitmap drawableBitmap = ((BitmapDrawable) drawable).getBitmap();
      if (drawableBitmap != null && !drawableBitmap.isRecycled()) {
        return drawableBitmap;
      }
    }
    return decodeBitmapFromDrawble(drawable, config);
  }

  /**
   * decode bitmap from drawable, <b>if drawble maybe a
   * {@link BitmapDrawable} , and can get from
   * {@link BitmapDrawable#getBitmap()} , use
   * {@link #drawableToBitmap(Drawable, Config...)}
   * instead</b>
   *
   * @param drawable
   * @param config
   * @return
   */
  public static Bitmap decodeBitmapFromDrawble(Drawable drawable, Config... config) {
    if (drawable == null || drawable.getIntrinsicWidth() <= 0
        || drawable.getIntrinsicHeight() <= 0) {
      return null;
    }
    Config bitmapConfig;
    if (config != null && config.length > 0) {
      bitmapConfig = config[0];
    } else {
      bitmapConfig = drawable.getOpacity() != PixelFormat.OPAQUE
          ? Config.ARGB_8888
          : Config.RGB_565;
    }
    Bitmap bitmap = creatBitmapSafty(drawable.getIntrinsicWidth(),
        drawable.getIntrinsicHeight(), bitmapConfig);
    if (bitmap == null) {
      return null;
    }
    Canvas canvas = new Canvas(bitmap);
    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable
        .getIntrinsicHeight());
    drawable.draw(canvas);
    return bitmap;
  }

  public static byte[] bitmapToPNGBytes(Bitmap bm) {
    if (bm != null) {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      bm.compress(CompressFormat.PNG, 100, baos);
      return baos.toByteArray();
    }
    return null;
  }

  /*
   * decodeResource to get a bitmap from res id
   * scaleSize The sample size is the number of pixels in either dimension that correspond to a
   * single pixel in the decoded bitmap.
   * lowQualityFlag if you want save more memory
   */
  public static Bitmap decodeResource(Resources res, int id, Config... config) {
    if (res == null) {
      return null;
    }

    try {
      BitmapFactory.Options op = new BitmapFactory.Options();
      if (config != null && config.length > 0) {
        op.inPreferredConfig = config[0];
      }
      return BitmapFactory.decodeResource(res, id, op);
    } catch (Throwable e) {
      e.printStackTrace();
    }
    return null;
  }

  public static Bitmap bytes2Bimap(byte[] bytes) {
    if (bytes != null && bytes.length != 0) {
      return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    } else {
      return null;
    }
  }

  // Rotates the bitmap by the specified degree.
  // If a new bitmap is created, the original bitmap is recycled.
  public static Bitmap rotate(Bitmap b, int degrees) {
    if (degrees != 0 && b != null) {
      Matrix m = new Matrix();
      m.setRotate(degrees, (float) b.getWidth() / 2, (float) b.getHeight() / 2);
      try {
        Bitmap b2 = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), m, true);
        if (b != b2) {
          b = b2;
        }
      } catch (OutOfMemoryError ex) {
        // We have no memory to rotate. Return the original bitmap.
      }
    }
    return b;
  }

  public static Bitmap transform(Matrix scaler, Bitmap source, int targetWidth, int targetHeight,
      boolean scaleUp) {
    int deltaX = source.getWidth() - targetWidth;
    int deltaY = source.getHeight() - targetHeight;
    if (!scaleUp && (deltaX < 0 || deltaY < 0)) {
      /*
       * In this case the bitmap is smaller, at least in one dimension,
       * than the target. Transform it by placing as much of the image as
       * possible into the target and leaving the top/bottom or left/right
       * (or both) black.
       */
      Bitmap b2 = creatBitmapSafty(targetWidth, targetHeight, Config.ARGB_8888);
      if (b2 == null) { // out of memory
        return source;
      }
      Canvas c = new Canvas(b2);

      int deltaXHalf = Math.max(0, deltaX / 2);
      int deltaYHalf = Math.max(0, deltaY / 2);
      Rect src =
          new Rect(deltaXHalf, deltaYHalf, deltaXHalf + Math.min(targetWidth, source.getWidth()),
              deltaYHalf
                  + Math.min(targetHeight, source.getHeight()));
      int dstX = (targetWidth - src.width()) / 2;
      int dstY = (targetHeight - src.height()) / 2;
      Rect dst = new Rect(dstX, dstY, targetWidth - dstX, targetHeight - dstY);
      c.drawBitmap(source, src, dst, null);
      return b2;
    }
    float bitmapWidthF = source.getWidth();
    float bitmapHeightF = source.getHeight();

    float bitmapAspect = bitmapWidthF / bitmapHeightF;
    float viewAspect = (float) targetWidth / targetHeight;

    if (bitmapAspect > viewAspect) {
      float scale = targetHeight / bitmapHeightF;
      if (scale < .9F || scale > 1F) {
        scaler.setScale(scale, scale);
      } else {
        scaler = null;
      }
    } else {
      float scale = targetWidth / bitmapWidthF;
      if (scale < .9F || scale > 1F) {
        scaler.setScale(scale, scale);
      } else {
        scaler = null;
      }
    }

    Bitmap b1;
    if (scaler != null) {
      // this is used for minithumb and crop, so we want to filter here.
      b1 = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), scaler, true);
    } else {
      b1 = source;
    }

    int dx1 = Math.max(0, b1.getWidth() - targetWidth);
    int dy1 = Math.max(0, b1.getHeight() - targetHeight);

    Bitmap b2 = Bitmap.createBitmap(b1, dx1 / 2, dy1 / 2, targetWidth, targetHeight);
    return b2;
  }

  /**
   * Creates a centered bitmap of the desired size. Recycles the input.
   *
   * @param source
   */
  public static Bitmap scaleUp(Bitmap source, int width, int height) {
    return ImageUtil.scaleUp(source, width, height, true);
  }

  public static Bitmap scaleUp(Bitmap source, int width, int height, boolean recycle) {
    if (source == null) {
      return null;
    }

    float scale;
    if (source.getWidth() < source.getHeight()) {
      scale = width / (float) source.getWidth();
    } else {
      scale = height / (float) source.getHeight();
    }
    Matrix matrix = new Matrix();
    matrix.setScale(scale, scale);
    Bitmap scaledBitmap = transform(matrix, source, width, height, true);
    return scaledBitmap;
  }

  /**
   * Creates a centered bitmap of the desired size. Recycles the input.
   *
   * @param source
   */
  public static Bitmap scaleDown(Bitmap source, int width, int height) {
    return ImageUtil.scaleDown(source, width, height, true);
  }

  public static Bitmap scaleDown(Bitmap source, int width, int height, boolean recycle) {
    if (source == null) {
      return null;
    }

    float scale;
    if (source.getWidth() < source.getHeight()) {
      scale = width / (float) source.getWidth();
    } else {
      scale = height / (float) source.getHeight();
    }
    Matrix matrix = new Matrix();
    matrix.setScale(scale, scale);
    Bitmap miniThumbnail = transform(matrix, source, width, height, false);
    return miniThumbnail;
  }

  public static Bitmap createFromUri(Context context, Uri contentUri, int maxResolutionX,
      int maxResolutionY) {
    if (contentUri == null) {
      return null;
    }

    BitmapFactory.Options options = new BitmapFactory.Options();
    try {
      BufferedInputStream bufferedInput =
          new BufferedInputStream(context.getContentResolver().openInputStream(contentUri), 16384);
      if (bufferedInput != null) {
        options.inSampleSize = computeSampleSize(bufferedInput, maxResolutionX, maxResolutionY);

        bufferedInput =
            new BufferedInputStream(context.getContentResolver().openInputStream(contentUri),
                16384);
        if (bufferedInput != null) {
          options.inDither = false;
          options.inJustDecodeBounds = false;
          return BitmapFactory.decodeStream(bufferedInput, null, options);
        }
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    return null;
  }

  private static int computeSampleSize(InputStream stream, int maxResolutionX, int maxResolutionY) {
    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inJustDecodeBounds = true;
    BitmapFactory.decodeStream(stream, null, options);
    int maxNumOfPixels = maxResolutionX * maxResolutionY;
    int minSideLength = Math.min(maxResolutionX, maxResolutionY) / 2;
    return computeSampleSize(options, minSideLength, maxNumOfPixels);
  }

  private static int computeSampleSize(BitmapFactory.Options options, int minSideLength,
      int maxNumOfPixels) {
    int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
    int roundedSize;
    if (initialSize <= 8) {
      roundedSize = 1;
      while (roundedSize < initialSize) {
        roundedSize <<= 1;
      }
    } else {
      roundedSize = (initialSize + 7) / 8 * 8;
    }
    return roundedSize;
  }

  private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength,
      int maxNumOfPixels) {
    double w = options.outWidth;
    double h = options.outHeight;

    int lowerBound =
        (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
    int upperBound =
        (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength),
            Math.floor(h / minSideLength));

    if (upperBound < lowerBound) {
      return lowerBound;
    }

    if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
      return 1;
    } else if (minSideLength == -1) {
      return lowerBound;
    } else {
      return upperBound;
    }
  }

  /**
   * Generate bitmap.
   *
   * @param data byte array data
   * @param maxWidth max width
   * @param maxHeight max height
   * @param config optional field, if provided, convert bitmap according to it; otherwise, using
   *          Config.RGB_565 by default
   * @return bitmap
   */
  public static Bitmap decodeBitmap(byte[] data, final int maxWidth, final int maxHeight,
      Config... config) {
    if (data == null) {
      return null;
    }
    BitmapFactory.Options decodeOptions = ImageUtil.getDefaultBitmapOptions();
    if (config != null && config.length > 0) {
      decodeOptions.inPreferredConfig = config[0];
    }
    Bitmap bitmap;
    if (maxWidth == 0 && maxHeight == 0) {
      bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, decodeOptions);
    } else {
      // If we have to resize this image, first get the natural bounds.
      decodeOptions.inJustDecodeBounds = true;
      BitmapFactory.decodeByteArray(data, 0, data.length, decodeOptions);
      int actualWidth = decodeOptions.outWidth;
      int actualHeight = decodeOptions.outHeight;

      // Then compute the dimensions we would ideally like to decode to.
      int desiredWidth = ImageUtil.getResizedDimension(maxWidth, maxHeight,
          actualWidth, actualHeight);
      int desiredHeight = ImageUtil.getResizedDimension(maxHeight, maxWidth,
          actualHeight, actualWidth);

      // Decode to the nearest power of two scaling factor.
      decodeOptions.inJustDecodeBounds = false;
      decodeOptions.inSampleSize =
          ImageUtil.findBestSampleSize(actualWidth, actualHeight, desiredWidth, desiredHeight);
      bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, decodeOptions);
    }
    return bitmap;
  }

  /**
   * Generate bitmap.
   *
   * @param filePath source image file path
   * @param maxWidth max width
   * @param maxHeight max height
   * @param config optional field, if provided, convert bitmap according to it; otherwise, using
   *          Config.RGB_565 by default
   * @return bitmap
   */
  public static Bitmap decodeBitmap(String filePath, final int maxWidth, final int maxHeight,
      Config... config) {

    BitmapFactory.Options decodeOptions = ImageUtil.getDefaultBitmapOptions();
    if (config != null && config.length > 0) {
      decodeOptions.inPreferredConfig = config[0];
    }
    Bitmap bitmap;
    if (maxWidth == 0 && maxHeight == 0) {
      bitmap = BitmapFactory.decodeFile(filePath, decodeOptions);
    } else {
      // If we have to resize this image, first get the natural bounds.
      decodeOptions.inJustDecodeBounds = true;
      BitmapFactory.decodeFile(filePath, decodeOptions);
      int actualWidth = decodeOptions.outWidth;
      int actualHeight = decodeOptions.outHeight;

      // Then compute the dimensions we would ideally like to decode to.
      int desiredWidth = ImageUtil.getResizedDimension(maxWidth, maxHeight,
          actualWidth, actualHeight);
      int desiredHeight = ImageUtil.getResizedDimension(maxHeight, maxWidth,
          actualHeight, actualWidth);

      // Decode to the nearest power of two scaling factor.
      decodeOptions.inJustDecodeBounds = false;
      decodeOptions.inSampleSize =
          ImageUtil.findBestSampleSize(actualWidth, actualHeight, desiredWidth, desiredHeight);
      bitmap = BitmapFactory.decodeFile(filePath, decodeOptions);
    }
    return bitmap;
  }

  /**
   * Scales one side of a rectangle to fit aspect ratio.
   *
   * @param maxPrimary Maximum size of the primary dimension (i.e. width for
   *          max width), or zero to maintain aspect ratio with secondary
   *          dimension
   * @param maxSecondary Maximum size of the secondary dimension, or zero to
   *          maintain aspect ratio with primary dimension
   * @param actualPrimary Actual size of the primary dimension
   * @param actualSecondary Actual size of the secondary dimension
   */
  private static int getResizedDimension(int maxPrimary, int maxSecondary, int actualPrimary,
      int actualSecondary) {
    // If no dominant value at all, just return the actual.
    if (maxPrimary == 0 && maxSecondary == 0) {
      return actualPrimary;
    }

    // If primary is unspecified, scale primary to match secondary's scaling ratio.
    if (maxPrimary == 0) {
      double ratio = (double) maxSecondary / (double) actualSecondary;
      return (int) (actualPrimary * ratio);
    }

    if (maxSecondary == 0) {
      return maxPrimary;
    }

    double ratio = (double) actualSecondary / (double) actualPrimary;
    int resized = maxPrimary;
    if (resized * ratio > maxSecondary) {
      resized = (int) (maxSecondary / ratio);
    }
    return resized;
  }

  /**
   * Returns the largest power-of-two divisor for use in downscaling a bitmap
   * that will not result in the scaling past the desired dimensions.
   *
   * @param actualWidth Actual width of the bitmap
   * @param actualHeight Actual height of the bitmap
   * @param desiredWidth Desired width of the bitmap
   * @param desiredHeight Desired height of the bitmap
   */
  // Visible for testing.
  private static int findBestSampleSize(
      int actualWidth, int actualHeight, int desiredWidth, int desiredHeight) {
    double wr = (double) actualWidth / desiredWidth;
    double hr = (double) actualHeight / desiredHeight;
    double ratio = Math.min(wr, hr);
    float n = 1.0f;
    while ((n * 2) <= ratio) {
      n *= 2;
    }

    return (int) n;
  }

  private static BitmapFactory.Options getDefaultBitmapOptions() {
    BitmapFactory.Options options = new BitmapFactory.Options();
    Context appContext = GlobalConfig.getAppContext();
    if (appContext != null) {
      int memoryClass = ((ActivityManager) appContext.getSystemService(Context.ACTIVITY_SERVICE))
          .getMemoryClass();
      // Set memory class threshold as 64M
      if (memoryClass >= 64) {
        options.inPreferredConfig = Config.ARGB_8888;
      } else {
        options.inPreferredConfig = Config.RGB_565;
      }
    } else {
      options.inPreferredConfig = Config.RGB_565;
    }
    if (SystemUtil.aboveApiLevel(Build.VERSION_CODES.HONEYCOMB)) {
      options.inPurgeable = false;
    } else {
      options.inPurgeable = true;
    }
    options.inInputShareable = true;
    return options;
  }

  public static ImageSize getImageSize(String path) {
    ImageSize size = new ImageSize();
    BitmapFactory.Options option = new BitmapFactory.Options();
    option.inJustDecodeBounds = true;
    BitmapFactory.decodeFile(path, option);
    size.width = option.outWidth;
    size.height = option.outHeight;
    return size;
  }

  public static Bitmap toRoundBitmapWithBorder(Bitmap bitmap, int outSideBorderColor,
      int inSideBorderColor, int borderWidth) {
    if (bitmap == null) {
      return null;
    }
    final Bitmap b = toRoundBitmap(bitmap);
    if (b == null) {
      return bitmap;
    }
    final int color = 0xff424242;
    final int w = b.getWidth();
    final int h = b.getHeight();
    final int width = (int) (Math.sqrt(2) * w + 2 * borderWidth);
    final int height = (int) (Math.sqrt(2) * h + 2 * borderWidth);
    Bitmap newBitmap = creatBitmapSafty(width, height, Config.ARGB_8888);
    if (newBitmap == null) {
      return b;
    }
    Canvas canvas = new Canvas(newBitmap);
    Paint paint = new Paint();
    paint.setAntiAlias(true);
    paint.setFilterBitmap(true);
    paint.setDither(true);
    // 画实心圆
    paint.setStyle(Paint.Style.FILL);
    paint.setColor(inSideBorderColor == 0 ? color : inSideBorderColor);
    canvas.drawCircle(width / 2, height / 2, width / 2 - borderWidth, paint);
    // 画图片
    canvas.drawBitmap(b, (width - w) / 2, (height - h) / 2, null);
    // 画空心圆
    paint.setStyle(Paint.Style.STROKE);
    paint.setColor(outSideBorderColor == 0 ? color : outSideBorderColor);
    paint.setStrokeWidth(borderWidth);
    canvas.drawCircle(width / 2, height / 2, (width - borderWidth) / 2, paint);
    return newBitmap;
  }

  public static Bitmap toRoundBitmap(Bitmap bitmap) {
    int width = bitmap.getWidth();
    int height = bitmap.getHeight();
    float roundPx;
    float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
    if (width <= height) {
      roundPx = width / 2;
      top = 0;
      bottom = width;
      left = 0;
      right = width;
      height = width;
      dst_left = 0;
      dst_top = 0;
      dst_right = width;
      dst_bottom = width;
    } else {
      roundPx = height / 2;
      float clip = (width - height) / 2;
      left = clip;
      right = width - clip;
      top = 0;
      bottom = height;
      width = height;
      dst_left = 0;
      dst_top = 0;
      dst_right = height;
      dst_bottom = height;
    }

    Bitmap output = creatBitmapSafty(width, height, Config.ARGB_8888);
    if (output == null) { // out of memory
      return bitmap;
    }
    Canvas canvas = new Canvas(output);

    final int color = 0xff424242;
    final Paint paint = new Paint();
    final Rect src = new Rect((int) left, (int) top, (int) right,
        (int) bottom);
    final Rect dst = new Rect((int) dst_left, (int) dst_top,
        (int) dst_right, (int) dst_bottom);
    final RectF rectF = new RectF(dst);

    paint.setAntiAlias(true);

    canvas.drawARGB(0, 0, 0, 0);
    paint.setColor(color);
    canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

    paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
    canvas.drawBitmap(bitmap, src, dst, paint);
    return output;
  }

  public static void setWallpaper(Context context, String imagePath) {
    File image = new File(imagePath);
    FileInputStream in = null;
    try {
      in = new FileInputStream(image);
      WallpaperManager.getInstance(context).setStream(in);
    } catch (IOException e) {
      Log.w("ImageUtil", "set wallpaper meet io exception, file:" + imagePath, e);
    } finally {
      if (in != null) {
        try {
          in.close();
        } catch (IOException e) {
          // ignore
        }
      }
    }
  }

  public static boolean savePicToPath(Bitmap b, File path, CompressFormat format) {
    if (b == null || path == null) {
      return false;
    }
    boolean sdCardExist = Environment.getExternalStorageState().equals(
        Environment.MEDIA_MOUNTED); // check sd card
    if (!sdCardExist) {
      return false;
    }

    if (!path.getParentFile().exists()) {
      path.getParentFile().mkdirs();
    }

    FileOutputStream fos = null;
    try {
      fos = new FileOutputStream(path);
      boolean success = b.compress(format, 90, fos);
      fos.flush();
      return success;
    } catch (Throwable e) {
      e.printStackTrace();
    } finally {
      IOUtils.close(fos);
    }
    return false;
  }

  /**
   * safety call of
   * {@link Bitmap#createBitmap(int, int, Config)} if
   * config is one of {@link Config#ARGB_4444} or
   * {@link Config#ARGB_8888} and OutOfMemoery is caught, we will change
   * config to {@link Config#RGB_565}
   *
   * @param width
   * @param height
   * @param config
   * @return null if there is no memery for the bitmap
   */
  public static Bitmap creatBitmapSafty(int width, int height, Config config) {
    Bitmap output = null;
    try {
      output = Bitmap.createBitmap(width, height, config);
    } catch (Throwable e) {
      if (config == Config.ARGB_8888) {
        return creatBitmapSafty(width, height, Config.RGB_565);
      }
    }
    return output;
  }

  public static class ImageSize {
    public int width;
    public int height;
  }
}
