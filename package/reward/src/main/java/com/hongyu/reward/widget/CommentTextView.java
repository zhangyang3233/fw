package com.hongyu.reward.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hongyu.reward.R;

/**
 * 
 * widget - comment - 自定义的评论标签
 *
 * =======================================
 * Copyright 2016-2017 
 * =======================================
 *
 * @since 2016-7-11 下午9:34:05
 * @author centos
 *
 */
public class CommentTextView extends RelativeLayout {
	private Context mContext;
	private RelativeLayout mView;
	private TextView mTvTag;
	private boolean isSelect = false;
	
	public CommentTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		initView();
	}

	public CommentTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		initView();
	}

	public CommentTextView(Context context) {
		super(context);
		mContext = context;
		initView();
	}
	
	private void initView() {
		//mView = (RelativeLayout) View.inflate(mContext, R.lauout.widget_edittext, null);
		//this.add(mView);
		mView = (RelativeLayout) View.inflate(mContext, R.layout.widget_comment_textview, this);
		String tag = (String) mView.getTag();
		mTvTag = (TextView) mView.findViewById(R.id.tag);
		mTvTag.setText(tag);
	}
	
	public void setText(String s) {
		mTvTag.setText(s);
	}
	
	public String getComment() {
		return mTvTag.getText().toString().trim();
	}
	
	public void select() {
		if (isSelect) {
			isSelect = false;
			mView.setBackgroundResource(R.drawable.common_button_yellow);
			mTvTag.setTextColor(mContext.getResources().getColor(R.color.text_yellow));
		} else {
			isSelect = true;
			mView.setBackgroundResource(R.drawable.common_button_main);
			mTvTag.setTextColor(mContext.getResources().getColor(R.color.app_main_title_text_color));
		}
	}
	
	public boolean isSelect() {
		return this.isSelect;
	}
	
}
