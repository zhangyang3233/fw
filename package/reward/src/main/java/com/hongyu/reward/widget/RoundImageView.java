package com.hongyu.reward.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.fw.zycoder.utils.GlobalConfig;
import com.hongyu.reward.R;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;


/**
 * Created by zhangyang131 on 16/8/30.
 */
public class RoundImageView extends RoundedImageView {

  public RoundImageView(Context context) {
    super(context);
  }

  public RoundImageView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public void loadNetworkImageByUrl(String url) {
    if(TextUtils.isEmpty(url)){
      setImageResource(R.mipmap.defalut_head_img);
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
    }else {
      Picasso.with(GlobalConfig.getAppContext())
              .load(url)
              .fit()
              .error(resourceId)
              .into(this);
    }
  }
}
