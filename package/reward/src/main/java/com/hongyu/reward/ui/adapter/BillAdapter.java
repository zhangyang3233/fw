package com.hongyu.reward.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hongyu.reward.R;
import com.hongyu.reward.appbase.adapter.DataAdapter;
import com.hongyu.reward.model.BillDetailModel;

/**
 * Created by zhangyang131 on 16/10/17.
 */
public class BillAdapter extends DataAdapter<BillDetailModel.BillItem> {


  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    Holder holder;
    if (convertView == null) {
      holder = new Holder();
      convertView =
          LayoutInflater.from(parent.getContext()).inflate(R.layout.bill_item_layout, null);
      holder.rootView = convertView;
      holder.type_tv = (TextView) convertView.findViewById(R.id.type_tv);
      holder.price_tv = (TextView) convertView.findViewById(R.id.price_tv);
      holder.data_tv = (TextView) convertView.findViewById(R.id.data_tv);
      convertView.setTag(holder);
    } else {
      holder = (Holder) convertView.getTag();
    }
    BillDetailModel.BillItem item = getItem(position);
    holder.data_tv.setText(item.getDate());
    float p = 0;
    try {
      p = Float.parseFloat(item.getValue());
    } catch (NumberFormatException e) {
    }
    if (p > 0) {
      holder.type_tv.setText("收入");
      holder.price_tv.setTextColor(0xfffea228);
      holder.price_tv.setText("+" + item.getValue());
    } else {
      holder.type_tv.setText("支付宝提现");
      holder.price_tv.setTextColor(0xff35b962);
      holder.price_tv.setText("-" + item.getValue());
    }
    return convertView;
  }

  class Holder {
    View rootView;
    TextView type_tv;
    TextView price_tv;
    TextView data_tv;

  }
}
