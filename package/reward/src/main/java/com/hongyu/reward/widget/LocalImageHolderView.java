package com.hongyu.reward.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.hongyu.reward.R;
import com.hongyu.reward.model.AdModel;

public class LocalImageHolderView implements Holder<AdModel> {
  private NetImageView img;
  private TextView tip;

  @Override
  public View createView(Context context) {
    View view = LayoutInflater.from(context).inflate(R.layout.layout_header_ad, null);
    img = (NetImageView) view.findViewById(R.id.iv_ad_img);
    tip = (TextView) view.findViewById(R.id.tip);
    return view;
  }

  @Override
  public void UpdateUI(Context context, int position, AdModel data) {
      img.loadNetworkImageByUrl(data.getPosition_img());
      tip.setText(data.getPosition_text());
  }
}
