package com.hongyu.reward.widget;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by zhangyang131 on 2016/10/19.
 */
public class MoneyEditTextView extends EditText implements TextWatcher {

    public MoneyEditTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public MoneyEditTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MoneyEditTextView(Context context) {
        super(context);
        init();
    }

    private void init() {
        setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
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
        if(TextUtils.isEmpty(s)){
            return;
        }
        int index = s.toString().indexOf(".");
        if(index<0){
            return;
        }
        String ff = s.toString().substring(index);
        if(ff.length() > 3){
            String finalStr = s.toString().substring(0, s.length() - ff.length() +3);
            setText(finalStr);
            setSelection(finalStr.length());
        }

    }
}
