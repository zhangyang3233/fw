package com.hongyu.reward.ui.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.hongyu.reward.R;
import com.hongyu.reward.model.AdModel;
import com.hongyu.reward.ui.activity.BrowserActivity;
import com.hongyu.reward.ui.activity.XCHDActivity;
import com.hongyu.reward.widget.NetImageView;

import java.util.ArrayList;

/**
 * 
 * manager - banner - 广告管理器
 *
 * =======================================
 * Copyright 2016-2017
 * =======================================
 *
 * @since 2016-6-17 下午10:51:59
 * @author centos
 *
 */
public class BannerPagerAdapter extends PagerAdapter {
  public OnBannerItemClickListener bannerItemClickListener;
  private Context context;
  private ArrayList<AdModel> adsList = new ArrayList<AdModel>();

  public BannerPagerAdapter(Context context) {
    this.context = context;
  }

  public static void gelleryToPage(Context context, AdModel adModel) {
    if(adModel.getPosition_id() == 7){// 喜茶活动
      XCHDActivity.launch(context);
    }else if (!TextUtils.isEmpty(adModel.position_url) && adModel.position_url.startsWith("http")) {
      BrowserActivity.launch(context, adModel.position_url, adModel.getPosition_text());
    }
  }

  public void addData(ArrayList<AdModel> dataList) {
    adsList.addAll(dataList);
    notifyDataSetChanged();
  }

  public void setData(ArrayList<AdModel> dataList) {
    adsList = null;
    adsList = dataList;
    notifyDataSetChanged();
  }

  @Override
  public int getCount() {
    return adsList == null ? 0 : adsList.size();

  }

  public void update(ArrayList<AdModel> dataList) {
    if (dataList != null && dataList.size() > 0) {
      adsList.clear();
      this.adsList = dataList;
      notifyDataSetChanged();
    }
  }

  // 判断当前页面显示的数据 与 新页面的数据是否相同
  @Override
  public boolean isViewFromObject(View arg0, Object arg1) {
    return arg0 == arg1;
  }

  // 初始化数据
  @Override
  public Object instantiateItem(ViewGroup viewPager, final int position) {
    View view = LayoutInflater.from(context).inflate(R.layout.layout_header_ad, null);
    NetImageView image = (NetImageView) view.findViewById(R.id.iv_ad_img);
    TextView tip = (TextView) view.findViewById(R.id.tip);
    image.setScaleType(ScaleType.FIT_XY);
    image.loadNetworkImageByUrl(getItem(position).position_img);
    String tipStr = getItem(position).getPosition_text();
    if(TextUtils.isEmpty(tipStr)){
      tip.setVisibility(View.GONE);
    }else{
      tip.setVisibility(View.VISIBLE);
      tip.setText(getItem(position).getPosition_text());
    }
    viewPager.addView(view);

    image.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        bannerItemClickListener.onClick(getItem(position));
      }
    });
    return view;
  }

  // 销毁数据
  @Override
  public void destroyItem(ViewGroup viewPager, int position, Object object) {
    viewPager.removeView((View) object);
  }

  public AdModel getItem(int i) {
    return adsList.get(i);
  }

  public void setOnItemClickListener(OnBannerItemClickListener bannerItemClickListener) {
    this.bannerItemClickListener = bannerItemClickListener;
  }

  public OnBannerItemClickListener getListener() {
    return new AdsClickListener(context);
  }

  public interface OnBannerItemClickListener {
    public void onClick(AdModel adModel);
  }

  private class AdsClickListener implements OnBannerItemClickListener {
    private Context mContext;

    public AdsClickListener(Context context) {
      mContext = context;
    }

    @Override
    public void onClick(AdModel adModel) {
      BannerPagerAdapter.gelleryToPage(mContext, adModel);
    }

  }

}
