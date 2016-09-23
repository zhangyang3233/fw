package com.hongyu.reward.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fw.zycoder.utils.ViewUtils;
import com.hongyu.reward.R;
import com.hongyu.reward.model.MyEvaluateReceiveModel;

/**
 * Created by zhangyang131 on 16/9/21.
 */
public class TagView extends TextView {
  public TagView(Context context) {
    super(context);
  }

  public TagView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public static TagView newInstance(ViewGroup parent) {
    TagView tagView = (TagView) ViewUtils.newInstance(parent, R.layout.tagview_layout);
    return tagView;
  }

  public static TagView newInstance(Context context) {
    TagView tagView = (TagView) ViewUtils.newInstance(context, R.layout.tagview_layout);
    return tagView;
  }

  public void setTagModel(MyEvaluateReceiveModel.TagModel tagModel) {
    setText(tagModel.getTag() + " " + tagModel.getNum());
  }
}
