package com.hongyu.reward.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.fw.zycoder.utils.GlobalConfig;
import com.hongyu.reward.R;
import com.hongyu.reward.appbase.BaseLoadFragment;
import com.hongyu.reward.ui.activity.PhotoViewActivity;
import com.squareup.picasso.Picasso;

/**
 * Created by zhangyang131 on 2017/2/10.
 */

public class PhotoViewFragment extends BaseLoadFragment {
    private String img_url;
    ImageView pic;

    @Override
    protected void onStartLoading() {

    }

    @Override
    protected void onInflated(View contentView, Bundle savedInstanceState) {
        pic = (ImageView) mContentView.findViewById(R.id.pic);
        img_url = getArguments().getString(PhotoViewActivity.IMG_URL);
        Picasso.with(GlobalConfig.getAppContext())
                .load(img_url)
                .fit()
                .error(R.mipmap.defalut_image)
                .into(pic);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.photo_activity;
    }
}
