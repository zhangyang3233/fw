package com.hongyu.reward.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fw.zycoder.utils.GlobalConfig;
import com.hongyu.reward.R;


public class NetImageView extends ImageView {

  public NetImageView(Context context) {
    super(context);
  }

  public NetImageView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public void loadNetworkImageByUrl(String url) {
    Glide.with(GlobalConfig.getAppContext()).load(url).error(R.mipmap.defalut_image)
        .skipMemoryCache(false).diskCacheStrategy(DiskCacheStrategy.ALL).into(this);
  }

  public void loadNetworkImageByUrl(String url, int resourceId) {
    Glide.with(GlobalConfig.getAppContext()).load(url).error(resourceId)
        .skipMemoryCache(false).diskCacheStrategy(DiskCacheStrategy.ALL).into(this);
  }
}
