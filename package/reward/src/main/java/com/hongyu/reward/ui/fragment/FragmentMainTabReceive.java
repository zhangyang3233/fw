package com.hongyu.reward.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hongyu.reward.R;
import com.hongyu.reward.appbase.AsyncLoadListFragment;
import com.hongyu.reward.appbase.adapter.DataAdapter;
import com.hongyu.reward.appbase.fetcher.BaseFetcher;

import java.util.List;

/**
 * 领取任务
 * aopayun - fragment - 领取任务商家列表 *
 * =======================================
 * Copyright 2015 yun.aopa.org.cn
 * =======================================
 *
 * @since 2016-7-18 下午12:12:25
 * @author centos
 * @version 1.1.0
 */
public class FragmentMainTabReceive extends AsyncLoadListFragment {TextView mTitle;
	LinearLayout mRightContainer;
	LinearLayout mLeftContainer;
	ImageView mRightBtn;


	@Override
	protected void onInflated(View contentView, Bundle savedInstanceState) {
		super.onInflated(contentView, savedInstanceState);
		initView();
	}

	private void initView() {
		mTitle = (TextView) mContentView.findViewById(R.id.title);
		mRightContainer = (LinearLayout) mContentView.findViewById(R.id.right_container);
		mLeftContainer = (LinearLayout) mContentView.findViewById(R.id.left_container);
		initTitle();
	}

	private void initTitle() {
		mTitle.setText(R.string.title_get_award);
		mRightBtn = (ImageView) LayoutInflater.from(getActivity()).inflate(R.layout.search_right_button_layout, null, false);
		mRightContainer.addView(mRightBtn);
	}

	@Override
	protected BaseFetcher newFetcher() {
		return new BaseFetcher() {
			@Override
			protected List fetchHttpData(int limit, int page) {
				return null;
			}
		};
	}

	@Override
	protected DataAdapter newContentAdapter() {
		return new DataAdapter() {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				return null;
			}
		};
	}

	@Override
	protected int getLayoutResId() {
		return R.layout.tab_title_load_list_fragment;
	}
}
