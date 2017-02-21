package com.hongyu.reward.ui.fragment.tabhost;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.fw.zycoder.utils.CollectionUtils;
import com.fw.zycoder.utils.MainThreadPostUtils;
import com.fw.zycoder.utils.SPUtil;
import com.hongyu.reward.R;
import com.hongyu.reward.appbase.AsyncLoadListFragment;
import com.hongyu.reward.appbase.adapter.DataAdapter;
import com.hongyu.reward.appbase.fetcher.BaseFetcher;
import com.hongyu.reward.config.Constants;
import com.hongyu.reward.http.HttpHelper;
import com.hongyu.reward.interfaces.CityChangedListener;
import com.hongyu.reward.interfaces.OrderClickUtil;
import com.hongyu.reward.location.GetLocationListener;
import com.hongyu.reward.location.LocationManager;
import com.hongyu.reward.model.AppLocation;
import com.hongyu.reward.model.NoticeEvent;
import com.hongyu.reward.model.OrderIdRequestModel;
import com.hongyu.reward.model.OrderModel;
import com.hongyu.reward.model.ShopListMode;
import com.hongyu.reward.request.ChangedOpenNewOrderPushRequestBuilder;
import com.hongyu.reward.request.ChangedOpenNewOrderPushRequestBuilder.PushType;
import com.hongyu.reward.request.GetReceiveOrderRequestBuilder;
import com.hongyu.reward.ui.activity.SearchActivity;
import com.hongyu.reward.ui.activity.ShopOrderListActivity;
import com.hongyu.reward.ui.adapter.ShopListAdapter;
import com.zy.widgets.text.SwitchButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * 领取任务
 * aopayun - fragment - 领取任务商家列表 *
 * =======================================
 * Copyright 2015 yun.aopa.org.cn
 * =======================================
 *
 * @author centos
 * @version 1.1.0
 * @since 2016-7-18 下午12:12:25
 */
public class FragmentMainTabReceive extends AsyncLoadListFragment<ShopListMode.ShopInfo> {
    TextView mTitle;
    LinearLayout mRightContainer;
    LinearLayout mLeftContainer;
    ImageView mRightBtn;
    TextView foot;
    SwitchButton switch_view;
    CityChangedListener cityChangedListener;


    protected void checkLazyLoad() {
        if (isVisible && isPrepared) {
            loadingData();
        }
    }

    @Override
    protected void loadingData() {
        onPullDownToRefresh();
    }

    @Override
    protected void onInflated(View contentView, Bundle savedInstanceState) {
        super.onInflated(contentView, savedInstanceState);
        initView();
    }

    private void initView() {
        mTitle = (TextView) mContentView.findViewById(R.id.title);
        mRightContainer = (LinearLayout) mContentView.findViewById(R.id.right_container);
        mLeftContainer = (LinearLayout) mContentView.findViewById(R.id.left_container);
        switch_view = (SwitchButton) mContentView.findViewById(R.id.switch_view);
        initTitle();
    }

    private void initTitle() {
        mTitle.setText(R.string.title_get_award);
        mRightBtn = (ImageView) LayoutInflater.from(getActivity())
                .inflate(R.layout.search_right_button_layout, null, false);
        mRightContainer.addView(mRightBtn);
        mRightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchActivity.launch(getActivity(), false);
            }
        });
        switch_view.setChecked(getReceiveNewOrderPush());
        switch_view.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SPUtil.putBoolean(Constants.Pref.PUSH_NEW_ORDER, isChecked);
                ChangedOpenNewOrderPushRequestBuilder builder =
                        new ChangedOpenNewOrderPushRequestBuilder(isChecked
                                ? PushType.on
                                : PushType.off);
                builder.build().submit();
            }
        });
    }

    private boolean getReceiveNewOrderPush() {
        return SPUtil.getBoolean(Constants.Pref.PUSH_NEW_ORDER, true);
    }

    @Override
    protected BaseFetcher newFetcher() {
        return new BaseFetcher() {
            @Override
            protected List<ShopListMode.ShopInfo> fetchHttpData(int limit, int page) {
                if (page == 1) {
                    OrderModel myReceive = getReceiveOrder();
                    if (myReceive != null) {
                        List<ShopListMode.ShopInfo> list = new ArrayList<ShopListMode.ShopInfo>();
                        ShopListMode.ShopInfo shopInfo = new ShopListMode.ShopInfo();
                        shopInfo.setImg(myReceive.getImg());
                        shopInfo.setShop_name(myReceive.getShop_name());
                        shopInfo.setOrder_id(myReceive.getOrder_id());
                        list.add(shopInfo);
                        showFootView();
                        return list;
                    }
                }
                return getDataList(page);
            }
        };
    }

    private void showFootView() {
        MainThreadPostUtils.post(new Runnable() {
            @Override
            public void run() {
                if (!(getRefreshableView() instanceof ListView)) {
                    return;
                }
                ListView listView = (ListView) getRefreshableView();
                if(foot == null){
                    listView.addFooterView(getFootView());
                }
            }
        });
    }

    private void hideFootView() {
        MainThreadPostUtils.post(new Runnable() {
            @Override
            public void run() {
                if (!(getRefreshableView() instanceof ListView)) {
                    return;
                }
                ListView listView = (ListView) getRefreshableView();
                listView.removeFooterView(foot);
                foot = null;
            }
        });
    }

    private View getFootView() {
        if (foot == null) {
            foot = new TextView(getActivity());
            foot.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            foot.setText("请尽快完成您领取的任务，然后才可查看其它待领取的任务。");
            foot.setTextColor(0xffe2262f);
            foot.setTextSize(12);
            foot.setGravity(Gravity.CENTER);
            foot.setPadding(5,5,5,5);
        }
        return foot;
    }


    private OrderModel getReceiveOrder() {
        GetReceiveOrderRequestBuilder builder = new GetReceiveOrderRequestBuilder();
        OrderIdRequestModel myReceive = builder.build().submitSync().get();
        return myReceive==null?null:myReceive.getData();
    }

    @Nullable
    private List<ShopListMode.ShopInfo> getDataList(int page) {
        hideFootView();
        AppLocation location = LocationManager.getInstance().getLocation();
        if (location == null) {
            LocationManager.getInstance().addLocationListener(new GetLocationListener() {
                @Override
                public void onSuccess(AppLocation locationInfo) {
                    onPullDownToRefresh();
                }

                @Override
                public void onFailed(String msg) {

                }
            });
            return null;
        }
        String locationStr = location.toString();
        String city = LocationManager.getSavedCity();
        // if(city.equals(location.getCity())){ // 当前城市一致,传坐标
        // city = null;
        // }
        ShopListMode listMode = HttpHelper.getReceiveShopList(String.valueOf(page),
                locationStr, city, null);
        if (listMode == null) {
            return null;
        } else if (CollectionUtils.isEmpty(listMode.getData())) {
            return new ArrayList();
        } else {
            return listMode.getData();
        }
    }


    @Override
    protected DataAdapter newContentAdapter() {
        ShopListAdapter adapter = new ShopListAdapter();
        adapter.setmOnItemClickListener(new ShopListAdapter.OnItemClickListener() {
            @Override
            public void itemOnClick(ShopListMode.ShopInfo mode) {
                if(!TextUtils.isEmpty(mode.getOrder_id())){
                    OrderClickUtil.orderOnClick(getActivity(), mode.getOrder_id());
                    return;
                }
                ShopOrderListActivity.launch(getActivity(), mode.getShop_name(), mode.getShop_id());
            }
        });
        return adapter;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cityChangedListener = new CityChangedListener() {
            @Override
            public void onCityChanged(String oldCity, String newCity) {
                onPullDownToRefresh();
            }
        };
        LocationManager.getInstance().addCitiChangedListener(cityChangedListener);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocationManager.getInstance().removeCityChangedListener(cityChangedListener);
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.receive_list_fragment;
    }

    @Override
    protected int getPageSize() {
        return 10;
    }

    @Subscribe
    public void onEventMainThread(NoticeEvent noticeEvent) {
        if (noticeEvent.getType() == NoticeEvent.TAB2_NEED_FRESH) {
            requestLoad();
        }
    }
}
