package com.hongyu.reward.ui.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fw.zycoder.utils.GlobalConfig;
import com.hongyu.reward.R;
import com.hongyu.reward.appbase.adapter.DataAdapter;
import com.hongyu.reward.model.ShopListMode;
import com.hongyu.reward.widget.NetImageView;

/**
 * Created by zhangyang131 on 16/9/12.
 */
public class ShopListAdapter extends DataAdapter<ShopListMode.ShopInfo> {
  OnItemClickListener mOnItemClickListener;

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    Holder holder;
    if (convertView == null) {
      holder = new Holder();
      convertView =
          LayoutInflater.from(parent.getContext()).inflate(R.layout.item_listview_shop, null);
      holder.rootView = convertView;
      holder.cover = (NetImageView) convertView.findViewById(R.id.image);
      holder.per = (TextView) convertView.findViewById(R.id.reward_per);
      holder.time = (TextView) convertView.findViewById(R.id.reward_time);
      holder.name = (TextView) convertView.findViewById(R.id.shop_name);
      holder.km = (TextView) convertView.findViewById(R.id.km);
      holder.divider_view = convertView.findViewById(R.id.divider_view);
      holder.info_detail_layout = convertView.findViewById(R.id.info_detail_layout);
      convertView.setTag(holder);
    } else {
      holder = (Holder) convertView.getTag();
    }

    final ShopListMode.ShopInfo model = getItem(position);
    if (model == null) return null;

    holder.cover.loadNetworkImageByUrl(model.getImg());
    holder.name.setText(model.getShop_name());

    if(!TextUtils.isEmpty(model.getOrder_id())){
      holder.info_detail_layout.setVisibility(View.GONE);
      holder.km.setVisibility(View.GONE);
    }else{
      holder.km.setVisibility(View.VISIBLE);
      holder.info_detail_layout.setVisibility(View.VISIBLE);
      holder.per.setText(
              GlobalConfig.getAppContext().getString(R.string.reward_count, model.getOrder_num()));
      holder.km.setText(
              GlobalConfig.getAppContext().getString(R.string.shop_distance, model.getDistanceStr()));
      holder.time
              .setText(GlobalConfig.getAppContext().getString(R.string.save_time, model.getSave_time()));
    }

    if (mOnItemClickListener != null) {
      holder.rootView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          mOnItemClickListener.itemOnClick(model);
        }
      });
    }
    if(isLastItem(position)){
      holder.divider_view.setVisibility(View.GONE);
    }else{
      holder.divider_view.setVisibility(View.VISIBLE);
    }

    return convertView;
  }

  private boolean isLastItem(int position) {
    return position == getCount() -1;
  }

  public OnItemClickListener getmOnItemClickListener() {
    return mOnItemClickListener;
  }

  public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener) {
    this.mOnItemClickListener = mOnItemClickListener;
  }

  public interface OnItemClickListener {
    void itemOnClick(ShopListMode.ShopInfo mode);
  }

  private class Holder {
    private View rootView;
    private TextView name;
    private TextView per;
    private TextView time;
    private TextView km;
    private View divider_view;
    private View info_detail_layout;
    private NetImageView cover;
  }
}
