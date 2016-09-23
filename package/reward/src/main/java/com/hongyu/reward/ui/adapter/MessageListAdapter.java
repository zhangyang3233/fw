package com.hongyu.reward.ui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fw.zycoder.utils.ViewUtils;
import com.hongyu.reward.R;
import com.hongyu.reward.appbase.adapter.DataAdapter;
import com.hongyu.reward.model.MsgModel.MessageModel;

/**
 * 
 * adapter - shop - 商家列表适配器
 *
 * =======================================
 * Copyright 2016-2017
 * =======================================
 *
 * @since 2016-7-9 上午11:48:04
 * @author centos
 *
 */
public class MessageListAdapter extends DataAdapter<MessageModel> {
  public OnMessageItemClickListener messageItemClickListener;

  public MessageListAdapter() {}


  public void setOnItemClickListener(OnMessageItemClickListener messageItemClickListener) {
    this.messageItemClickListener = messageItemClickListener;
  }

  @Override
  public long getItemId(int position) {
    return 0;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup arg2) {
    Holder holder;
    if (convertView == null) {
      holder = new Holder();
      convertView = ViewUtils.newInstance(arg2, R.layout.item_listview_message);
      holder.title = (TextView) convertView.findViewById(R.id.title);
      holder.content = (TextView) convertView.findViewById(R.id.content);
      holder.date = (TextView) convertView.findViewById(R.id.date);
      convertView.setTag(holder);
    } else {
      holder = (Holder) convertView.getTag();
    }

    final MessageModel model = getItem(position);
    if (model == null) return null;

    holder.title.setText(model.title);
    holder.content.setText(model.content);
    holder.date.setText(model.date);

    return convertView;
  }

  public interface OnMessageItemClickListener {
    void onClick(MessageModel model);
  }

  private class Holder {
    private TextView title;
    private TextView content;
    private TextView date;
  }

}
