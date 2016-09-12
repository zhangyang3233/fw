package com.hongyu.reward.ui.adapter;

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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            holder = new Holder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_listview_shop, null);
            holder.cover = (NetImageView) convertView.findViewById(R.id.image);
            holder.per = (TextView) convertView.findViewById(R.id.reward_per);
            holder.time = (TextView) convertView.findViewById(R.id.reward_time);
            holder.name = (TextView) convertView.findViewById(R.id.shop_name);
            holder.km = (TextView) convertView.findViewById(R.id.km);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        final ShopListMode.ShopInfo model = getItem(position);
        if (model == null) return null;

        holder.cover.loadNetworkImageByUrl(model.getImg());
        holder.per.setText(GlobalConfig.getAppContext().getString(R.string.reward_count, model.getOrder_num()));
        holder.name.setText(model.getShop_name());
        holder.km.setText(GlobalConfig.getAppContext().getString(R.string.shop_distance, model.getDistance()));
        holder.time.setText(GlobalConfig.getAppContext().getString(R.string.save_time, model.getSave_time()));
        return convertView;
    }


    private class Holder {
        private TextView name;
        private TextView per;
        private TextView time;
        private TextView km;
        private NetImageView cover;
    }
}
