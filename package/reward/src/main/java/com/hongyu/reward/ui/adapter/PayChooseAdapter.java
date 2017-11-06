package com.hongyu.reward.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hongyu.reward.R;

/**
 * Created by zhangyang131 on 2017/8/16.
 */

public class PayChooseAdapter extends BaseAdapter {
  String[] texts;
  int[] imgs;
  Context context;

  public PayChooseAdapter(String[] texts, int[] imgs, Context context) {
    this.texts = texts;
    this.imgs = imgs;
    this.context = context;
  }

  @Override
  public int getCount() {
    return texts.length;
  }

  @Override
  public Object getItem(int position) {
    return null;
  }

  @Override
  public long getItemId(int position) {
    return 0;
  }

  @Override
  public View getView(int position,
      View contentView, ViewGroup parent) {
    View view = LayoutInflater.from(context).inflate(R.layout.item_select, null, false);
    TextView textView =
        (TextView) view.findViewById(R.id.text);
    // 获得array.xml中的数组资源getStringArray返回的是一个String数组
    String text = texts[position];
    textView.setText(text);
    ImageView imgView = (ImageView) view.findViewById(R.id.img);
    imgView.setImageResource(imgs[position]);
    return view;
  }
}

