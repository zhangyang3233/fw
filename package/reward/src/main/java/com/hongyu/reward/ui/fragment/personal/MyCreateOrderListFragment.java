package com.hongyu.reward.ui.fragment.personal;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

import com.hongyu.reward.R;
import com.hongyu.reward.appbase.OrderPagerAdapter;
import com.hongyu.reward.appbase.PagerFragment;
import com.hongyu.reward.model.OrderModel;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;

import java.util.ArrayList;

/**
 * Created by zhangyang131 on 16/9/20.
 */
public class MyCreateOrderListFragment extends PagerFragment {
  private static final String[] CHANNELS = new String[] {"全部", "已经完成", "待付款", "待评价", "客诉单"};
  MagicIndicator magicIndicator;
  OrderPagerAdapter adapter;

  @Override
  protected FragmentPagerAdapter getPagerAdapter() {
    if (adapter == null) {
      adapter = new OrderPagerAdapter(this.getChildFragmentManager());
    }
    ArrayList<Fragment> list = new ArrayList<>();
    list.add(Fragment.instantiate(getActivity(), OrderListFragment.class.getName(), getBundle(0)));
    list.add(Fragment.instantiate(getActivity(), OrderListFragment.class.getName(), getBundle(1)));
    list.add(Fragment.instantiate(getActivity(), OrderListFragment.class.getName(), getBundle(2)));
    list.add(Fragment.instantiate(getActivity(), OrderListFragment.class.getName(), getBundle(3)));
    list.add(Fragment.instantiate(getActivity(), OrderListFragment.class.getName(), getBundle(4)));
    adapter.setListFragments(list);
    return adapter;
  }

  private Bundle getBundle(int index){
    Bundle bundle = new Bundle();
    bundle.putInt(OrderListFragment.ISME, 0);
    switch (index){
      case 0:
        bundle.putInt(OrderListFragment.TYPE, OrderModel.STATUS_ALL);
        break;
      case 1:
        bundle.putInt(OrderListFragment.TYPE, OrderModel.STATUS_FINISHED);
        break;
      case 2:
        bundle.putInt(OrderListFragment.TYPE, OrderModel.STATUS_PENDING_PAY);
        break;
      case 3:
        bundle.putInt(OrderListFragment.TYPE, OrderModel.STATUS_PENDING_COMMENT);
        break;
      case 4:
        bundle.putInt(OrderListFragment.TYPE, OrderModel.STATUS_PENDING_COMPLAINT);
        break;
    }
    return bundle;
  }

  @Override
  protected void onStartLoading() {

  }

  @Override
  protected int getLayoutResId() {
    return R.layout.order_pager_layout;
  }

  @Override
  protected void onInflated(View contentView, Bundle savedInstanceState) {
    super.onInflated(contentView, savedInstanceState);
    magicIndicator = (MagicIndicator) mContentView.findViewById(R.id.magic_indicator);
    CommonNavigator commonNavigator = new CommonNavigator(getActivity());
    getPagerAdapter();
    CommonNavigatorAdapter mCommonNavigatorAdapter = new CommonNavigatorAdapter() {
      @Override
      public int getCount() {
        return adapter.getCount();
      }

      @Override
      public IPagerTitleView getTitleView(Context context, final int index) {
        ColorTransitionPagerTitleView colorTransitionPagerTitleView =
            new ColorTransitionPagerTitleView(context);
        colorTransitionPagerTitleView.setNormalColor(Color.GRAY);
        colorTransitionPagerTitleView
            .setSelectedColor(getActivity().getResources().getColor(R.color.colorPrimaryDark));
        colorTransitionPagerTitleView.setText(CHANNELS[index]);
        colorTransitionPagerTitleView.setTextSize(17);
        colorTransitionPagerTitleView.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            mViewPager.setCurrentItem(index);
          }
        });
        return colorTransitionPagerTitleView;
      }

      @Override
      public IPagerIndicator getIndicator(Context context) {
        LinePagerIndicator linePagerIndicator = new LinePagerIndicator(context);
        linePagerIndicator
            .setColors(getActivity().getResources().getColor(R.color.colorPrimaryDark));
        return linePagerIndicator;
      }

    };
    commonNavigator.setAdapter(mCommonNavigatorAdapter);
    magicIndicator.setNavigator(commonNavigator);
    ViewPagerHelper.bind(magicIndicator, mViewPager);
  }
}
