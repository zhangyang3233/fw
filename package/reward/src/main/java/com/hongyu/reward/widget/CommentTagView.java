package com.hongyu.reward.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fw.zycoder.utils.ViewUtils;
import com.hongyu.reward.R;

/**
 * Created by zhangyang131 on 16/10/12.
 */
public class CommentTagView extends TextView {

  public CommentTagView(Context context) {
    super(context);
  }

  public CommentTagView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public static CommentTagView newInstance(Context context) {
    return (CommentTagView) ViewUtils.newInstance(context, R.layout.comment_tag_view_layout);
  }

  public static CommentTagView newInstance(ViewGroup parent) {
    return (CommentTagView) ViewUtils.newInstance(parent, R.layout.comment_tag_view_layout);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
  }

}
