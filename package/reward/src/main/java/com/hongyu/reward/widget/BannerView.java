package com.hongyu.reward.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.fw.zycoder.utils.MainThreadPostUtils;
import com.hongyu.reward.R;
import com.hongyu.reward.model.AdModel;
import com.hongyu.reward.ui.adapter.BannerPagerAdapter;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.circlenavigator.CircleNavigator;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zhangyang131 on 16/9/10.
 */
public class BannerView extends FrameLayout implements ViewPager.OnPageChangeListener {
    BannerPagerAdapter adapter;
    private Context mContext;
    private ViewGroup mView;
    private ViewPager mViewPager;
    MagicIndicator magicIndicator;
    CircleNavigator circleNavigator;
    TimerTask timerTask;
    Timer timer;
    boolean isAtt;

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        isAtt = true;
        timer.schedule(timerTask, 5000, 5000);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isAtt = false;
        timer.cancel();
    }

    public BannerView(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public BannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    public void initView() {
        initAdapter();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        mView = (ViewGroup) inflater.inflate(R.layout.widget_banner_layout, null);
        mViewPager = (ViewPager) mView.findViewById(R.id.viewPager);
        mViewPager.setAdapter(adapter);
        magicIndicator = (MagicIndicator) mView.findViewById(R.id.magic_indicator);
        circleNavigator = new CircleNavigator(mContext);
        addView(mView);
        initTimer();
    }

    private void initTimer() {
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                MainThreadPostUtils.post(new Runnable() {
                    @Override
                    public void run() {
                        if (adapter != null && adapter.getCount() > 1 && mViewPager != null) {
                            int currentItem = mViewPager.getCurrentItem();
                            int next = (currentItem >= (adapter.getCount() - 1)) ? 0 : (currentItem + 1);
                            mViewPager.setCurrentItem(next, true);
                        }
                    }
                });
            }
        };
    }


    private void initAdapter() {
        if (adapter == null) {
            adapter = new BannerPagerAdapter(getContext());
            adapter.setOnItemClickListener(adapter.getListener());
        }
    }

    public void setData(ArrayList<AdModel> list) {
        initAdapter();
        adapter.setData(list);
        circleNavigator.setCircleCount(adapter.getCount());
        circleNavigator.setCircleColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
        circleNavigator.setCircleClickListener(new CircleNavigator.OnCircleClickListener() {
            @Override
            public void onClick(int index) {
                mViewPager.setCurrentItem(index);
            }
        });
        magicIndicator.setNavigator(circleNavigator);
        ViewPagerHelper.bind(magicIndicator, mViewPager);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == 0) {
            int index = mViewPager.getCurrentItem();
            if (index == 0 && adapter.getCount() >= 4) {
                mViewPager.setCurrentItem(adapter.getCount() - 2, false);
            } else if (index == adapter.getCount() - 1) {
                mViewPager.setCurrentItem(1, false);
            }
        }
    }
}
