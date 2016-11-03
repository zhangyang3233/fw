package com.hongyu.reward.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.hongyu.reward.R;
import com.hongyu.reward.model.AdModel;
import com.hongyu.reward.ui.adapter.BannerPagerAdapter;

import java.util.ArrayList;

/**
 * Created by zhangyang131 on 2016/11/2.
 */

public class BannerView2 extends ConvenientBanner<AdModel> implements OnItemClickListener {
  private ArrayList<AdModel> list;

  public BannerView2(Context context) {
    super(context);
  }

  public BannerView2(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public BannerView2(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  private void init() {
    setPages(
        new CBViewHolderCreator<LocalImageHolderView>() {
          @Override
          public LocalImageHolderView createHolder() {
            return new LocalImageHolderView();
          }
        }, list)
            // 设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
            .setPageIndicator(
                new int[] {R.mipmap.ic_page_indicator, R.mipmap.ic_page_indicator_focused})
            // 设置指示器的方向
            .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT)
            // .setOnPageChangeListener(this)//监听翻页事件
            .setOnItemClickListener(this);
  }

  public void setData(ArrayList<AdModel> list) {
    this.list = list;
    init();
    notifyDataSetChanged();
  }

  @Override
  public void onItemClick(int position) {
    BannerPagerAdapter.gelleryToPage(getContext(), list.get(position));
  }
}
