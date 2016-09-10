package com.hongyu.reward.ui.main.fragment;

import android.view.View;
import android.view.ViewGroup;

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
public class FragmentMainTabReceive extends AsyncLoadListFragment {

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
}
