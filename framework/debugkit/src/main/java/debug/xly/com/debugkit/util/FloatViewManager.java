package debug.xly.com.debugkit.util;

import android.animation.ValueAnimator;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;

public class FloatViewManager extends Service implements KitActivityManager.OnAppBackgroundListener{
  private static final String LAYOUT_ID = "resLayoutId";
  private static boolean isWorked;
  private static OnClickListener l;
  private static View.OnLongClickListener l2;
  private int resLayoutId;
  private int statusBarHight;
  private int screenW;
  private int screenH;
  private int mTouchSlop;

  public static void setOnClicked(OnClickListener l){
    FloatViewManager.l = l;
  }

  public static void setOnLongClicked(View.OnLongClickListener l2){
    FloatViewManager.l2 = l2;
  }

  public static void removeFloatView(Context context) {
    Intent intent = new Intent(context, FloatViewManager.class);
    context.stopService(intent);
  }

  public static void addFloatView(Context context, int resLayoutId, OnClickListener l) {
    FloatViewManager.l = l;
    Intent intent = new Intent(context, FloatViewManager.class);
    intent.putExtra(LAYOUT_ID, resLayoutId);
    context.startService(intent);
  }

  // 定义浮动窗口布局
  LinearLayout mFloatLayout;
  WindowManager.LayoutParams wmParams;
  // 创建浮动窗口设置布局参数的对象
  WindowManager mWindowManager;


  private static final String TAG = "FxService";

  @Override
  public void onCreate() {
    super.onCreate();
    isWorked = true;
    initPixels();
    KitActivityManager.registerAppbackgroundListener(this);
  }

  private void initPixels() {
    statusBarHight = getStatusBarHeight(this);
    DisplayMetrics metrics = getResources().getDisplayMetrics();
    screenW = metrics.widthPixels;
    screenH = metrics.heightPixels;
    mTouchSlop = ViewConfiguration.get(this).getScaledTouchSlop();
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    resLayoutId = intent.getIntExtra(LAYOUT_ID, -1);
    Log.i(TAG, String.valueOf(resLayoutId));
    createFloatView();
    return super.onStartCommand(intent, flags, startId);
  }

  @Override
  public IBinder onBind(Intent intent) {
    // TODO Auto-generated method stub
    return null;
  }

  private void createFloatView() {
    wmParams = new WindowManager.LayoutParams();
    // 获取WindowManagerImpl.CompatModeWrapper
    mWindowManager =
        (WindowManager) getApplication().getSystemService(getApplication().WINDOW_SERVICE);
    // 设置window type
    wmParams.type = LayoutParams.TYPE_PHONE;
    // 设置图片格式，效果为背景透明
    wmParams.format = PixelFormat.RGBA_8888;
    // 设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
    wmParams.flags =
        // LayoutParams.FLAG_NOT_TOUCH_MODAL |
        LayoutParams.FLAG_NOT_FOCUSABLE;
    // LayoutParams.FLAG_NOT_TOUCHABLE
    // 调整悬浮窗显示的停靠位置为左侧置顶
    wmParams.gravity = Gravity.LEFT | Gravity.TOP;
    wmParams.x = screenW;
    wmParams.y = 0;
    // 设置悬浮窗口长宽数据
    wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
    wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

    LayoutInflater inflater = LayoutInflater.from(getApplication());
    // 获取浮动窗口视图所在布局
    mFloatLayout = (LinearLayout) inflater.inflate(resLayoutId, null);
    // 添加mFloatLayout
    mWindowManager.addView(mFloatLayout, wmParams);
    // 浮动窗口按钮
    mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0,
        View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
            .makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
    Log.i(TAG, "Width/2--->" + mFloatLayout.getMeasuredWidth() / 2);
    Log.i(TAG, "Height/2--->" + mFloatLayout.getMeasuredHeight() / 2);
    // 设置监听浮动窗口的触摸移动
    mFloatLayout.setOnTouchListener(new OnTouchListener() {
      float dx, dy, ox, oy;
      boolean isMove;

      @Override
      public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
          case MotionEvent.ACTION_DOWN:
            isMove = false;
            dx = event.getX();
            dy = event.getY();
            ox = event.getRawX();
            oy = event.getRawY();
            break;
          case MotionEvent.ACTION_MOVE:
            float nx = event.getRawX();
            float ny = event.getRawY();
            if (!isMove) {
              if (Math.abs(nx - ox) > mTouchSlop || Math.abs(ny - oy) > mTouchSlop) {
                isMove = true;
              } else {
                break;
              }
            }
            wmParams.x = (int) (nx - dx);
            wmParams.y = (int) (ny - dy - statusBarHight);
            mWindowManager.updateViewLayout(mFloatLayout, wmParams);
            break;
          case MotionEvent.ACTION_UP:
            int x = (int) (event.getRawX() - dx);
            ValueAnimator va =
                ValueAnimator.ofInt(x,
                    x + (v.getWidth() / 2) > screenW / 2 ? screenW - v.getWidth() : 0);
            va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
              @Override
              public void onAnimationUpdate(ValueAnimator animation) {
                int x = (Integer) animation.getAnimatedValue();
                wmParams.x = x;
                mWindowManager.updateViewLayout(mFloatLayout, wmParams);
              }
            });
            va.setDuration(200);
            va.start();
            break;
        }
        return isMove;
      }
    });

    mFloatLayout.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        if (l != null) {
          l.onClick(mFloatLayout);
        }
      }
    });
    mFloatLayout.setOnLongClickListener(new View.OnLongClickListener() {
      @Override
      public boolean onLongClick(View v) {
        if(l2 != null){
          l2.onLongClick(v);
          return true;
        }
        return false;
      }
    });
  }

  @Override
  public void onDestroy() {
    // TODO Auto-generated method stub
    super.onDestroy();
    if (mFloatLayout != null) {
      mWindowManager.removeView(mFloatLayout);
    }
    isWorked = false;
    KitActivityManager.unRegisterAppbackgroundListener(this);
  }

  public static int getStatusBarHeight(Context context) {
    int result = 0;
    int resourceId = context.getResources().getIdentifier("status_bar_height",
        "dimen", "android");
    if (resourceId > 0) {
      result = context.getResources().getDimensionPixelSize(resourceId);
    }
    return result;
  }


  public static boolean isWorked(Application application) {
    return isWorked;
  }

  @Override
  public void onBackground() {
    mFloatLayout.setVisibility(View.GONE);
  }

  @Override
  public void onForeground() {
    mFloatLayout.setVisibility(View.VISIBLE);
  }
}

