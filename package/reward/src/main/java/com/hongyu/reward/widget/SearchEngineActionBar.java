package com.hongyu.reward.widget;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fw.zycoder.utils.KeyBoardUtils;
import com.fw.zycoder.utils.ViewUtils;
import com.hongyu.reward.R;


/**
 * @author mapeng@wanda.cn
 */
public class SearchEngineActionBar extends RelativeLayout implements TitleContainer{

  private EditText mEditText;
  private ImageView mClearInputView;
  private TextView mBackBtn;
  // private TextView mSearchBtn;

  private boolean mTextChangedListenerClosed;

  private boolean isDetachedFromWindow = false;

  private OnActionListener mOnActionListener;

  public SearchEngineActionBar(Context context) {
    super(context);
  }

  public SearchEngineActionBar(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override
  public void setTitle(CharSequence title) {
    mEditText.setText(title);
  }

  @Override
  public void setTitle(int resId) {

  }

  @Override
  public void setLeftView(View view, ViewGroup.LayoutParams layoutParams) {

  }

  @Override
  public void setRightView(View view, ViewGroup.LayoutParams layoutParams) {

  }

  public interface OnActionListener {
    void input(String word);

    void startSearch(String word);

    void click(String word);

    void back(String word);
  }

  public void setOnActionListener(OnActionListener onActionListener) {
    mOnActionListener = onActionListener;
  }


  public static SearchEngineActionBar newInstance(ViewGroup parent) {
    return (SearchEngineActionBar) ViewUtils.newInstance(parent,
        R.layout.search_engine_action_bar);
  }

  public static SearchEngineActionBar newInstance(Context context) {
    return (SearchEngineActionBar) ViewUtils.newInstance(context,
        R.layout.search_engine_action_bar);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    initView();
    initListener();
  }

  private void initView() {
    mClearInputView = (ImageView) findViewById(R.id.iv_clear);
    mEditText = (EditText) findViewById(R.id.et_search);
    mBackBtn = (TextView) findViewById(R.id.cancel);
    mEditText.requestFocus();
  }

  private void initListener() {
    mEditText.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        mTextChangedListenerClosed = false;
        if (mOnActionListener != null) {
          mOnActionListener.click(mEditText.getText().toString());
        }
        setCursorVisible(true);
      }
    });

    mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

      @Override
      public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
          if (mOnActionListener != null) {
            mOnActionListener.startSearch(mEditText.getText().toString());
          }
        }
        return false;
      }
    });

    mEditText.addTextChangedListener(new TextWatcher() {

      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (mOnActionListener != null && !mTextChangedListenerClosed) {
          if (s != null && s.length() > 0) {
            mOnActionListener.input(s.toString());
          } else {
            mOnActionListener.input(null);
          }
        }
      }

      @Override
      public void afterTextChanged(Editable s) {}
    });

    mClearInputView.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        mTextChangedListenerClosed = false;
        mEditText.setText("");
        setCursorVisible(true);
      }
    });

    mBackBtn.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        KeyBoardUtils.closeKeybord(mEditText, getContext());

        if (mOnActionListener != null) {
          mOnActionListener.back(mEditText.getText().toString());
        }

      }
    });

  }

  @Override
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    isDetachedFromWindow = true;
  }

  public void setTextChangedListenerClosed(boolean textChangedListenerClosed) {
    mTextChangedListenerClosed = textChangedListenerClosed;
  }

  public EditText getEditText() {
    return mEditText;
  }

  public void setText(String text) {
    mEditText.setText(text);
  }

  public void setClearIconVisible(boolean visible) {
    mClearInputView.setVisibility(visible ? VISIBLE : GONE);
  }

  public void setCursorVisible(boolean visible) {
    mEditText.setCursorVisible(visible);
  }

}
