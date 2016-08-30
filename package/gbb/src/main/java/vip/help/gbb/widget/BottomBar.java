package vip.help.gbb.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.fw.zycoder.utils.ViewUtils;

import vip.help.gbb.R;

public class BottomBar extends RelativeLayout {
  private LinearLayout mBottomBar;
//  private ImageView mCenterTab;

  private OnTabChangeListener mOnTabChangeListener;


  public BottomBar(Context context) {
    this(context, null);
  }

  public BottomBar(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public BottomBar(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    LayoutInflater.from(context).inflate(R.layout.classic_activity_bottom_bar_internal, this);
    mBottomBar = (LinearLayout) findViewById(R.id.ll_bottom_bar);
//    mCenterTab = (ImageView) findViewById(R.id.iv_center_tab);
  }


  public static BottomBar newInstance(Context context) {
    return (BottomBar) ViewUtils.newInstance(context, R.layout.classic_activity_bottom_bar);
  }

  public static BottomBar newInstance(ViewGroup parent) {
    return (BottomBar) ViewUtils.newInstance(parent, R.layout.classic_activity_bottom_bar);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();

    for (int i = 0; i < mBottomBar.getChildCount(); ++i) {
      final View child = mBottomBar.getChildAt(i);
      final int position = i;
      child.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
//          if (position != -1) {
            selectTab(v);
            if (mOnTabChangeListener != null) {
              mOnTabChangeListener.onTabSelected(position);
            }
//          }
        }
      });
    }

//    mCenterTab.setOnClickListener(new OnClickListener() {
//      @Override
//      public void onClick(View v) {
//
//        if (mOnTabChangeListener != null) {
//          mOnTabChangeListener.onTabSelected(2);
//        }
//      }
//    });
  }

  private void selectTab(View view) {
    for (int i = 0; i < mBottomBar.getChildCount(); ++i) {
      View child = mBottomBar.getChildAt(i);
      if (child != view) {
        child.setSelected(false);
      } else {
        child.setSelected(true);
      }
    }
  }

  public void selectTab(int position) {
    View child = mBottomBar.getChildAt(position);
    selectTab(child);
  }

  public void addOnTabChangeListener(OnTabChangeListener l) {
    mOnTabChangeListener = l;
  }

  public interface OnTabChangeListener {
    void onTabSelected(int position);
  }

  @Override
  protected void onVisibilityChanged(View changedView, int visibility) {
    super.onVisibilityChanged(changedView, visibility);
  }
}
