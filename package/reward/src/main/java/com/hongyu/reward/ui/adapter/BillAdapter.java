package com.hongyu.reward.ui.adapter;

import android.text.TextUtils;
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

        int accountType = getItemAccountType(item.getAccount_type());

        switch (accountType) {
            case 1://订单收入
            case 3://评论积分
            case 4://分享积分
                holder.price_tv.setTextColor(0xfffea228);
                holder.price_tv.setText("+" + item.getValue());
                break;
            case 2://提现冻结
            case 5://提现成功
            case 6://提现失败
                holder.price_tv.setTextColor(0xff35b962);
                holder.price_tv.setText("-" + item.getValue());
                break;
            default:
                holder.price_tv.setTextColor(0xff333333);
                holder.price_tv.setText("" + item.getValue());
                break;
        }

        switch (accountType) {
            case 1://订单收入
                holder.type_tv.setText("订单收入");
                break;
            case 3://评论积分
                holder.type_tv.setText("评论奖励");
                break;
            case 4://分享积分
                holder.type_tv.setText("分享奖励");
                break;
            case 2://提现冻结
                holder.type_tv.setText("提现冻结");
                break;
            case 5://提现成功
                holder.type_tv.setText("提现成功");
                break;
            case 6://提现失败
                holder.type_tv.setText("提现失败");
                break;
            default:
                holder.type_tv.setText("未知");
                break;
        }


        return convertView;
    }

    private int getItemAccountType(String account_type) {
        if (TextUtils.isEmpty(account_type)) {
            return 0;
        } else if ("1".equals(account_type)) {
            return 1;
        } else if ("2".equals(account_type)) {
            return 2;
        } else if ("3".equals(account_type)) {
            return 3;
        } else if ("4".equals(account_type)) {
            return 4;
        } else if ("5".equals(account_type)) {
            return 5;
        } else if ("6".equals(account_type)) {
            return 6;
        } else {
            int at = 0;
            try {
                at = Integer.parseInt(account_type);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return at;
        }
    }

    class Holder {
        View rootView;
        TextView type_tv;
        TextView price_tv;
        TextView data_tv;

    }
}
