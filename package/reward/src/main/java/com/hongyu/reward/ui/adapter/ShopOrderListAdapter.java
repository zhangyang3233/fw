package com.hongyu.reward.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fw.zycoder.utils.GlobalConfig;
import com.hongyu.reward.R;
import com.hongyu.reward.appbase.adapter.DataAdapter;
import com.hongyu.reward.model.RewardModel;
import com.hongyu.reward.widget.NetImageView;

/**
 * Created by zhangyang131 on 16/9/12.
 */
public class ShopOrderListAdapter extends DataAdapter<RewardModel> {
  OnItemClickListener mOnItemClickListener;

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    Holder holder;
    if (convertView == null) {
      holder = new Holder();
      convertView =
          LayoutInflater.from(parent.getContext()).inflate(R.layout.item_listview_shop_order, null);
      holder.rootView = convertView;
      holder.cover = (NetImageView) convertView.findViewById(R.id.image);
      holder.per = (TextView) convertView.findViewById(R.id.reward_per);
      holder.name = (TextView) convertView.findViewById(R.id.shop_name);
      holder.price = (TextView) convertView.findViewById(R.id.price);
      convertView.setTag(holder);
    } else {
      holder = (Holder) convertView.getTag();
    }

    final RewardModel model = getItem(position);
    if (model == null) return null;

    holder.cover.loadNetworkImageByUrl(model.getImg());
    holder.per.setText(
        GlobalConfig.getAppContext().getString(R.string.reward_name, model.getNickname()));
    holder.name.setText(model.shop_name);
    holder.price.setText("￥ " + model.price);
    if (mOnItemClickListener != null) {
      holder.rootView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          mOnItemClickListener.itemOnClick(model);
        }
      });
    }

    return convertView;
  }

  public OnItemClickListener getmOnItemClickListener() {
    return mOnItemClickListener;
  }

  public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener) {
    this.mOnItemClickListener = mOnItemClickListener;
  }

  private class Holder {
    View rootView;
    TextView per;
    TextView name;
    TextView price;
    NetImageView cover;
  }

  public interface OnItemClickListener {
    void itemOnClick(RewardModel mode);
  }
}
