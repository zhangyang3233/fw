package com.fw.zycoder.demos.demos.guagua;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.fw.zycoder.demos.R;
import com.fw.zycoder.utils.DensityUtils;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by zhangyang
 */
public class ScratchOutView extends SurfaceView
    implements
      SurfaceHolder.Callback {

  private WScratchViewThread mThread;
  private ArrayList<Path> mPathList = new ArrayList<Path>();
  private int mOverlayColor = -1;// default color is dark gray
  private int mOverlayImageResource = R.mipmap.scratch_area;
  private Paint mOverlayPaint;
  private int mPathPaintWidth;
  private boolean mIsScratchable = true, isShow = true;
  private boolean mIsAntiAlias = true;
  private boolean mIsFirstTouchDown = true;
  private Path path;
  private float startX = 0;
  private float startY = 0;
  private boolean mScratchStart = false;
  private boolean isAutoScratchOut = true;
  private int autoScratchOutPercent = 20;
  private Bitmap mOverlayBitmap;
  private Bitmap tempBitmap;
  private Paint computeOverlayPaint;
  private BitmapFactory.Options op;

  private Random mRandom;

  public ScratchOutView(Context context) {
    this(context, null);
  }

  public ScratchOutView(Context ctx, AttributeSet attrs) {
    super(ctx, attrs);
    init(ctx);
  }

  private Drawable getScratchOverlayDrawable() {
    return getContext().getResources().getDrawable(mOverlayImageResource, getContext().getTheme());
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    if (getScratchOverlayDrawable() != null) {
      int sizeW = MeasureSpec.getSize(widthMeasureSpec);
      int modeH = MeasureSpec.getMode(heightMeasureSpec);
      if (modeH == MeasureSpec.AT_MOST) {
        int sizeH = sizeW * getScratchOverlayDrawable().getIntrinsicHeight()
            / getScratchOverlayDrawable().getIntrinsicWidth();
        setMeasuredDimension(sizeW, sizeH);
        return;
      }
    }
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    if (isInEditMode()) {
      canvas.drawColor(Color.WHITE);
      BitmapDrawable bd = (BitmapDrawable) getScratchOverlayDrawable();
      Matrix m = new Matrix();
      float scaleW = getMeasuredWidth() / (bd.getIntrinsicWidth() * 1.5f);
      float scaleH = getMeasuredHeight() / (bd.getIntrinsicHeight() * 1.5f);
      m.setScale(scaleW, scaleH);
      canvas.drawBitmap(bd.getBitmap(), m, new Paint());
    }
  }

  private void init(Context context) {
    mPathPaintWidth = DensityUtils.dp2px(context, 15);
    mOverlayPaint = new Paint();
    mOverlayPaint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
    mOverlayPaint.setStyle(Paint.Style.STROKE);
    mOverlayPaint.setStrokeCap(Paint.Cap.ROUND);
    mOverlayPaint.setAntiAlias(mIsAntiAlias);
    mOverlayPaint.setStrokeWidth(mPathPaintWidth);
    mOverlayPaint.setStrokeJoin(Paint.Join.ROUND);
    mRandom = new Random();
    if (isInEditMode()) {
      return;
    }
    setZOrderOnTop(true);
    SurfaceHolder holder = getHolder();
    holder.addCallback(this);
    holder.setFormat(PixelFormat.TRANSPARENT);
  }

  private Canvas computeCanvas;
  private Bitmap canvasBitmap;

  private void setOverlayBitmap(Canvas canvas, Resources mResources) {
    if (null == computeOverlayPaint) {
      computeOverlayPaint = new Paint();
      computeOverlayPaint.setShadowLayer(0, 0, 0, 0);
    }
    if (null == op) {
      op = new BitmapFactory.Options();
    }
    op.inJustDecodeBounds = true;
    // op.inJustDecodeBounds = true;表示我们只读取Bitmap的宽高等信息，不读取像素。
    mOverlayBitmap = BitmapFactory.decodeResource(mResources, mOverlayImageResource, op); // 获取尺寸信息
    // op.outWidth表示的是图像真实的宽度
    // 获取比例大小
    int wRatio =
        (int) Math.ceil(op.outWidth / getMeasuredWidth());
    int hRatio =
        (int) Math.ceil(op.outHeight / getMeasuredHeight());
    // 如果超出指定大小，则缩小相应的比例
    if (wRatio > 1 && hRatio > 1) {
      if (wRatio > hRatio) {
        // 如果太宽，我们就缩小宽度到需要的大小，注意，高度就会变得更加的小。
        op.inSampleSize = wRatio;
      } else {
        op.inSampleSize = hRatio;
      }
    }
    op.inJustDecodeBounds = false;
    mOverlayBitmap = null;
    // op.inDensity = 0;
    op.inPreferredConfig = Bitmap.Config.RGB_565; // ARGB_4444
    try {
      tempBitmap = BitmapFactory.decodeResource(mResources, mOverlayImageResource, op);
      // 从原Bitmap创建一个给定宽高的Bitmap
      mOverlayBitmap =
          Bitmap.createScaledBitmap(tempBitmap,
              getMeasuredWidth(),
              getMeasuredHeight(), false);
    } catch (Exception ex) {
      mOverlayBitmap = null;
      tempBitmap = null;
      init(getContext());
    }
    tempBitmap = null;
    canvas.drawBitmap(mOverlayBitmap, 0, 0, computeOverlayPaint);
    mOverlayBitmap = null;
  }

  @Override
  public boolean dispatchTouchEvent(MotionEvent event) {
    return super.dispatchTouchEvent(event);
  }

  @Override
  public boolean onTouchEvent(MotionEvent me) {
    if (mThread == null)
      return false;

    synchronized (mThread.getSurfaceHolder()) {
      if (!mIsScratchable) {
        return true;
      }

      float x = me.getX();
      float y = me.getY();

      switch (me.getAction()) {
        case MotionEvent.ACTION_DOWN:
          path = new Path();
          path.moveTo(x, y);
          startX = x;
          startY = y;
          mPathList.add(path);

          float radius = (float) (mRandom.nextFloat() * 2 * Math.PI);
          double dx = Math.sin(radius) * DensityUtils.px2dp(getContext(), 10);
          double dy = Math.cos(radius) * DensityUtils.px2dp(getContext(), 10);
          path.lineTo((float) (x + dx), (float) (y + dy));

          if (touchCallBack != null && mIsFirstTouchDown) {
            mIsFirstTouchDown = false;
            touchCallBack.onStartScratch(this);
          }

          break;
        case MotionEvent.ACTION_MOVE:
          if (mScratchStart) {
            path.lineTo(x, y);
          } else {
            if (isScratch(startX, x, startY, y)) {
              mScratchStart = true;
              path.lineTo(x, y);
            }
          }
          break;
        case MotionEvent.ACTION_UP:
          mScratchStart = false;

          if (isAutoScratchOut) {
            int scratchPercent = computeScratchOutAreaSize();// 刮开的面积百分比

            // 刮开面积超过一定百分比 则自动全部展现出来
            if (scratchPercent >= autoScratchOutPercent) {
              post(new Runnable() {

                @Override
                public void run() {
                  // TODO Auto-generated method stub
                  isShow = false;
                  invalidSurfaceView();
                  if (touchCallBack != null)
                    touchCallBack
                        .onAutoScratchOut(ScratchOutView.this);
                }
              });
            }
          }

          if (touchCallBack != null)
            touchCallBack.onFinishScratch(this);
          break;
      }
      return true;
    }
  }

  // 计算刮开的面积 遍历判断bitmap中是0的像素点比例（为0的就是透明区域）
  private int computeScratchOutAreaSize() {
    if (canvasBitmap == null)
      return 0;
    int[] pixels = new int[canvasBitmap.getWidth()
        * canvasBitmap.getHeight()];
    int width = getWidth();
    int height = getHeight();
    canvasBitmap.getPixels(pixels, 0, width, 0, 0, width, height);

    int sum = pixels.length;
    int num = 0;
    for (int i = 0; i < sum; i++) {
      if (pixels[i] == 0) {
        num++;
      }
    }

    return num * 100 / sum;
  }

  private boolean isScratch(float oldX, float x, float oldY, float y) {
    float distance = (float) Math.sqrt(Math.pow(oldX - x, 2)
        + Math.pow(oldY - y, 2));
    if (distance > mPathPaintWidth * 2) {
      return true;
    } else {
      return false;
    }
  }

  @Override
  public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
    // do nothing
  }

  @Override
  public void surfaceCreated(SurfaceHolder arg0) {
    resetView();
  }

  @Override
  public void surfaceDestroyed(SurfaceHolder arg0) {
    destroySurfaceThread();
  }

  private void destroySurfaceThread() {
    if (mThread == null)
      return;
    mThread.setRunning(false);
    mThread.interrupt();

    while (mThread != null) {
      try {
        mThread.join();
        mThread = null;
      } catch (InterruptedException e) {
        // do nothing but keep retry
      }
    }
  }

  private class WScratchViewThread extends Thread {
    private SurfaceHolder mSurfaceHolder;
    private ScratchOutView mView;
    private boolean mRun = false;

    public WScratchViewThread(SurfaceHolder surfaceHolder) {
      mSurfaceHolder = surfaceHolder;
    }

    public void setRunning(boolean run) {
      mRun = run;
    }

    public SurfaceHolder getSurfaceHolder() {
      return mSurfaceHolder;
    }

    @SuppressLint("WrongCall")
    @Override
    public void run() {
      Canvas canvas;
      long currentTimeMillis;
      while (mRun) {
        canvas = null;
        currentTimeMillis = System.currentTimeMillis();
        try {
          canvas = mSurfaceHolder.lockCanvas(null);
          synchronized (mSurfaceHolder) {
            if (canvas != null) {
              draw(canvas);
            }
          }
          Thread.sleep(Math.max(0, 33 - (System.currentTimeMillis() - currentTimeMillis)));// 每秒30帧
        } catch (Exception ex) {
          ex.printStackTrace();
        } finally {
          if (canvas != null) {
            mSurfaceHolder.unlockCanvasAndPost(canvas);
          }
        }

      }
    }

    private void draw(Canvas canvas) {
      if (!isShow) {
        // 清除刮奖蒙层
        canvas.drawColor(Color.TRANSPARENT, Mode.CLEAR);
        recycle();
        return;
      }

      if (computeCanvas == null) {
        canvasBitmap = Bitmap.createBitmap(getWidth(), getHeight(),
            Config.ARGB_8888);
        computeCanvas = new Canvas(canvasBitmap);
      }
      Resources mResources = getContext().getResources();
      // 绘制遮盖层
      if (mOverlayImageResource != -1 && mOverlayColor == -1) {
        setOverlayBitmap(canvas, mResources);
      } else if (mOverlayColor != -1 && mOverlayImageResource == -1) {
        canvas.drawColor(mOverlayColor);
      }
      computeCanvas.drawColor(mResources.getColor(R.color.white));
      for (Path path : mPathList) {
        canvas.drawPath(path, mOverlayPaint);
        computeCanvas.drawPath(path, mOverlayPaint);
      }
    }
  }

  private void invalidSurfaceView() {
    Canvas canvas = null;
    SurfaceHolder mSurfaceHolder = getHolder();
    try {
      canvas = mSurfaceHolder.lockCanvas(null);
      synchronized (mSurfaceHolder) {
        if (canvas != null) {
          draw(canvas);
        }
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      if (canvas != null) {
        mSurfaceHolder.unlockCanvasAndPost(canvas);
      }
    }
  }

  private void recycle() {
    if (mThread != null)
      synchronized (mThread.getSurfaceHolder()) {
      mPathList.clear();
      }

    if (canvasBitmap != null)
      canvasBitmap.recycle();
    canvasBitmap = null;
    computeCanvas = null;
  }

  public void resetView() {
    mIsFirstTouchDown = true;
    mIsScratchable = true;
    isShow = true;
    mPathList.clear();
    if (mThread == null) {
      mThread = new WScratchViewThread(getHolder());
      mThread.setRunning(true);
      mThread.start();
      recycle();
    }
  }

  // 底部全部显现出来
  public void destroyView() {
    recycle();

    destroySurfaceThread();

    mIsScratchable = false;
    isShow = false;
    invalidSurfaceView();
  }

  public boolean isScratchable() {
    return mIsScratchable;
  }

  public void setScratchable(boolean flag) {
    mIsScratchable = flag;
  }

  public void setOverlayColor(int color) {
    mOverlayColor = color;
  }

  public void setOverlayImageResource(int mOverlayImageResource) {
    this.mOverlayImageResource = mOverlayImageResource;
  }

  public void setPathPaintWidth(int w) {
    mPathPaintWidth = w;
  }

  public void setAntiAlias(boolean flag) {
    mIsAntiAlias = flag;
  }

  public final boolean isShow() {
    return isShow;
  }

  public final void setShow(boolean isShow) {
    this.isShow = isShow;
  }

  public final void setAutoScratchOut(boolean isAutoScratchOut) {
    this.isAutoScratchOut = isAutoScratchOut;
  }

  public final void setAutoScratchOutPercent(int autoScratchOutPercent) {
    this.autoScratchOutPercent = autoScratchOutPercent;
  }

  private IScratchView touchCallBack;

  public IScratchView getTouchCallBack() {
    return touchCallBack;
  }

  public void setTouchCallBack(IScratchView impl) {
    this.touchCallBack = impl;
  }

  public interface IScratchView {
    void onFinishScratch(ScratchOutView sv);

    // 开始刮 第一次
    void onStartScratch(ScratchOutView sv);

    // 完全显示出来
    void onAutoScratchOut(ScratchOutView sv);
  }
}
