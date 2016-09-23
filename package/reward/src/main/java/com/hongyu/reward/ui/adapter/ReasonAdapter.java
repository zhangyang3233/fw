package com.hongyu.reward.ui.adapter;

import android.view.View;
import android.view.ViewGroup;

import com.hongyu.reward.appbase.adapter.DataAdapter;
import com.hongyu.reward.model.ReasonModel;
import com.hongyu.reward.widget.ReasonView;

/**
 * Created by zhangyang131 on 16/9/21.
 */
public class ReasonAdapter extends DataAdapter<ReasonModel.Reason> {
  private ReasonModel.Reason selectReason;

  @Override
  public View getView(final int position, View convertView, ViewGroup parent) {
    ReasonView item = new ReasonView(parent.getContext());
    item.setContent(getItem(position).getId(), getItem(position).getReason());
    if (getSelectReason() == getItem(position)) {
      item.setSelect(true);
    } else {
      item.setSelect(false);
    }
    item.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        selectReason = getItem(position);
        notifyDataSetChanged();
      }
    });
    return item;
  }

  public ReasonModel.Reason getSelectReason() {
    if (getCount() == 0) {
      return null;
    }
    if (selectReason == null) {
      return getItem(0);
    } else {
      return selectReason;
    }
  }
}
