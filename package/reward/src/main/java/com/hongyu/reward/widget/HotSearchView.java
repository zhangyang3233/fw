package com.hongyu.reward.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.fw.zycoder.utils.CollectionUtils;
import com.fw.zycoder.utils.ViewUtils;
import com.hongyu.reward.R;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;

/**
 * Created by zhangyang131 on 16/10/13.
 */
public class HotSearchView extends FrameLayout {
  TagFlowLayout mTagFlowLayout;
  TagClickedListener mTagClickedListener;
  TagAdapter<String> mTagAdapter;

  public void setHots(ArrayList<String> hots) {
    if(CollectionUtils.isEmpty(hots)){
      setVisibility(View.GONE);
    }else{
      setVisibility(View.VISIBLE);
    }
    mTagAdapter.setTagDatas(hots);
    mTagAdapter.notifyDataChanged();
  }

  public TagClickedListener getmTagClickedListener() {
    return mTagClickedListener;
  }

  public void setmTagClickedListener(TagClickedListener mTagClickedListener) {
    this.mTagClickedListener = mTagClickedListener;
  }

  public HotSearchView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init();
  }

  public HotSearchView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public HotSearchView(Context context) {
    super(context);
    init();
  }

  private void init() {
    View root = LayoutInflater.from(getContext()).inflate(R.layout.hot_search_view, null);
    addView(root);
    mTagFlowLayout = (TagFlowLayout) root.findViewById(R.id.tag_layout);
    mTagAdapter = new TagAdapter<String>() {
      @Override
      public View getView(FlowLayout parent, int position, final String o) {
        TextView tv = (TextView) ViewUtils.newInstance(getContext(), R.layout.hot_search_item);
        tv.setText(o);
        tv.setOnClickListener(new OnClickListener() {
          @Override
          public void onClick(View v) {
            if (mTagClickedListener != null) {
              mTagClickedListener.tagClicked(o);
            }
          }
        });
        return tv;
      }
    };
    mTagFlowLayout.setAdapter(mTagAdapter);
  }

  public interface TagClickedListener {
    void tagClicked(String str);
  }

}
