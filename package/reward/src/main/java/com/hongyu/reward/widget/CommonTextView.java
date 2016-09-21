package com.hongyu.reward.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hongyu.reward.R;

/**
 * 自定义的输入框  ... 左边有图标  右边有删除图标
 * @author Jfomt
 * @since 2014年10月1日 下午10:56:41 
 * @version 1.0
 */
public class CommonTextView extends RelativeLayout {
	private Context mContext;
	private RelativeLayout mView;
	private ImageView mEidtImageViewRight;
	private ImageView mEidtImageViewLeft;
	private TextView mTextView;
	
	private String accepted = "";
	private RelativeLayout mImageViewLeftView;
	private TextView mTextViewInfo;
	
	public CommonTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		initView();
	}

	public CommonTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		initView();
		
		//从全部的属性集合中过滤出来自定义的属性数组  
        //SrttingView是在R文件中自动生成的自定义属性数组  
        //返回所有自定义属性和自定义属性的值的数组  
        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.MyTextView);
        
        int res_id = arr.getResourceId(R.styleable.MyTextView_leftimg, 0);
        int res_bg = arr.getResourceId(R.styleable.MyTextView_leftbg, 0); 
        String hint_text = arr.getString(R.styleable.MyTextView_labeltext);
        boolean isClick = arr.getBoolean(R.styleable.MyTextView_isclick, false);
        
        if (res_id != 0) {
        	mImageViewLeftView.setVisibility(View.VISIBLE);
        	mEidtImageViewLeft.setImageResource(res_id);
        } else {
        	mImageViewLeftView.setVisibility(View.GONE);
        }
        if (isClick) {
        	mEidtImageViewRight.setVisibility(View.VISIBLE);
        } else {
        	mEidtImageViewRight.setVisibility(View.GONE);
        }
//        this.setClickable(isClick);
        mTextView.setText(TextUtils.isEmpty(hint_text) ? "" : hint_text);
        mImageViewLeftView.setBackgroundResource(res_bg);
        arr.recycle();
	}

	public CommonTextView(Context context) {
		super(context);
		mContext = context;
		initView();
	}
	
	/**
	 * 初始化页面布局
	 * @author Jfomt
	 * @since 2014年10月1日 下午10:58:59 
	 * @version 1.0
	 */
	private void initView() {
		//mView = (RelativeLayout) View.inflate(mContext, R.lauout.widget_edittext, null);
		//this.add(mView);
		mView = (RelativeLayout) View.inflate(mContext, R.layout.widget_textview, this);
		
		mEidtImageViewLeft = (ImageView)mView.findViewById(R.id.edit_img_left);
		mEidtImageViewRight = (ImageView)mView.findViewById(R.id.edit_img_right);
		mImageViewLeftView = (RelativeLayout)mView.findViewById(R.id.edit_img_left_view);
		mTextView = (TextView)mView.findViewById(R.id.text_view);
		mTextViewInfo = (TextView)mView.findViewById(R.id.info);
		
	}
	
	public void setText(String s) {
		mTextView.setText(s);
	}
	
	public void reqFouce() {
		mTextView.requestFocus();
	}
	
	public void setInfo(String s) {
		if (TextUtils.isEmpty(s)) {
			mTextViewInfo.setVisibility(View.GONE);
		} else {
			mTextViewInfo.setVisibility(View.VISIBLE);
			mTextViewInfo.setText(s);
		}
	}
	
	public String getInfo() {
		String s = mTextViewInfo.getText().toString();
		return s;
	}
	
	/**
	 * 设置可输入长度  
	 * @author Jfomt
	 * @since 2014年10月5日 上午12:11:00 
	 * @version 1.0
	 */
	public void setMaxLength(int len) {

	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
//		MLog.v("--- ontouch ---");
		return super.dispatchTouchEvent(ev);
		
	}


//	public void setOnClickListener(MOnClickListener mOnClickListener) {
//		mTextView.setOnClickListener(mOnClickListener);
//	}
//	
//	
//	public interface MOnClickListener extends OnClickListener {
//
//		
//	}
	
}
