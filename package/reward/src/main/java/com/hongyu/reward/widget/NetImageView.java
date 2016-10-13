package com.hongyu.reward.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.fw.zycoder.utils.GlobalConfig;
import com.hongyu.reward.R;
import com.squareup.picasso.Picasso;


public class NetImageView extends ImageView {

  public NetImageView(Context context) {
    super(context);
  }

  public NetImageView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public void loadNetworkImageByUrl(String url) {
    if(TextUtils.isEmpty(url)){
      setImageResource(R.mipmap.defalut_image);
    }else{
      Picasso.with(GlobalConfig.getAppContext())
              .load(url)
              .fit()
              .error(R.mipmap.defalut_image)
              .into(this);
    }
  }

  public void loadNetworkImageByUrl(String url, int resourceId) {
    if(TextUtils.isEmpty(url)){
      setImageResource(resourceId);
    }else{
      Picasso.with(GlobalConfig.getAppContext())
              .load(url)
              .fit()
              .error(resourceId)
              .into(this);
    }
  }
}
