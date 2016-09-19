package com.hongyu.reward.ui.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView.ScaleType;

import com.hongyu.reward.R;
import com.hongyu.reward.model.AdModel;
import com.hongyu.reward.ui.activity.BrowserActivity;
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
  private Context context;
  private ArrayList<AdModel> adsList = new ArrayList<AdModel>();
  public OnBannerItemClickListener bannerItemClickListener;

  public BannerPagerAdapter(Context context) {
    this.context = context;
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
    if (adsList == null || adsList.size() == 0) {
      return 0;
    } else if (adsList.size() == 1) {
      return 1;
    } else {
      return adsList.size() + 2;
    }

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
    NetImageView image = null;
    image = (NetImageView) view.findViewById(R.id.iv_ad_img);

    image.setScaleType(ScaleType.FIT_XY);

    image.loadNetworkImageByUrl(getItem(position).position_img);
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
    if (adsList != null ) {
      if(getCount()>=4){
        if(i == 0){
          return adsList.get(adsList.size()-1);
        }else if(i == adsList.size()-2){
          return adsList.get(0);
        }else{
          return adsList.get((i-1)%adsList.size());
        }
      }
      return adsList.get(i);
    }
    return null;
  }

  public void setOnItemClickListener(OnBannerItemClickListener bannerItemClickListener) {
    this.bannerItemClickListener = bannerItemClickListener;
  }

  public interface OnBannerItemClickListener {
    public void onClick(AdModel adModel);
  }

  public static void gelleryToPage(Context context, AdModel adModel) {
    if (!TextUtils.isEmpty(adModel.position_url) && adModel.position_url.startsWith("http://")) {
      BrowserActivity.launch(context, adModel.position_url);
    }
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

  public OnBannerItemClickListener getListener() {
    return new AdsClickListener(context);
  }

}
