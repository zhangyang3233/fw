package com.zy.widgets.text;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.zy.widgets.R;


/**
 * 金钱输入框
 * 只允许输入到精度为分
 * 只允许输入数字和"."符号
 * 只允许一行
 * 不允许输入负数
 * 有最大金额限制 maxValue == -1 为不限制
 * 超过最大值则设置为最大值
 * Created by zhangyang131 on 2016/10/19.
 */
public class MoneyEditText extends EditText implements TextWatcher {
  public static final int DEFAULT_MAX_VALUE = 1000 * 10000; // 默认输入最大一千万
  private int maxValue = DEFAULT_MAX_VALUE;

  public MoneyEditText(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
    parseAttributes(attrs);
  }

  public MoneyEditText(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
    parseAttributes(attrs);
  }

  private void parseAttributes(AttributeSet attrs) {
    TypedArray typedArray =
        getContext().obtainStyledAttributes(attrs, R.styleable.MoneyEditText);
    maxValue = typedArray.getInt(R.styleable.MoneyEditText_maxValue, maxValue);
    typedArray.recycle();
  }

  public MoneyEditText(Context context) {
    super(context);
    init();
  }

  private void init() {
    setInputType(EditorInfo.TYPE_CLASS_NUMBER | EditorInfo.TYPE_NUMBER_FLAG_DECIMAL);
    setEllipsize(TextUtils.TruncateAt.END);
    setMaxLines(1);
    addTextChangedListener(this);
  }

  @Override
  public void beforeTextChanged(CharSequence s, int start, int count, int after) {

  }

  @Override
  public void onTextChanged(CharSequence s, int start, int before, int count) {

  }

  @Override
  public void afterTextChanged(Editable s) {
    fixTextFitMoney(s);
  }

  private void fixTextFitMoney(CharSequence s) {
    if (TextUtils.isEmpty(s)) {
      return;
    }
    try {
      if (maxValue >= 0) {
          double d = Double.parseDouble(s.toString());
          if (d > maxValue) {
              setText(String.valueOf(maxValue));
              setSelection(String.valueOf(maxValue).length());
              return;
          }
      }
    } catch (NumberFormatException e) {
      e.printStackTrace();
    }

    int index = s.toString().indexOf(".");
    if (index < 0) {
      return;
    }
    String ff = s.toString().substring(index);
    if (ff.length() > 3) {
      String finalStr = s.toString().substring(0, s.length() - ff.length() + 3);
      setText(finalStr);
      setSelection(finalStr.length());
    }
  }
}
