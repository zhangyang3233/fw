package com.hongyu.reward.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fw.zycoder.circleimageview.RoundedCornerImageView;
import com.hongyu.reward.R;


/**
 * Created by zhangyang131 on 16/8/30.
 */
public class RoundImageView extends RoundedCornerImageView {

    public RoundImageView(Context context) {
        super(context);
    }

    public RoundImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void loadNetworkImageByUrl(String url) {
        Glide.with(getContext()).load(url).error(R.mipmap.defalut_image)
                .skipMemoryCache(false).diskCacheStrategy(DiskCacheStrategy.ALL).into(this);
    }

    public void loadNetworkImageByUrl(String url, int resourceId) {
        Glide.with(getContext()).load(url).error(resourceId)
                .skipMemoryCache(false).diskCacheStrategy(DiskCacheStrategy.ALL).into(this);
    }
}
