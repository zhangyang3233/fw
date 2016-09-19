package com.hongyu.reward.ui.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.hongyu.reward.R;

/**
 * Created by zhangyang131 on 16/9/13.
 */
public class CommonTwoBtnDialogFragment extends AbsDialogFragment {
  TextView content_tv;
  TextView btn_left;
  TextView btn_right;
  CharSequence content;

  CharSequence leftBtnStr;
  CharSequence rightBtnStr;

  OnClickListener leftClick = new OnClickListener() {
    @Override
    public void onClick(Dialog dialog) {
      dialog.dismiss();
    }
  };
  OnClickListener rightClick = new OnClickListener() {
    @Override
    public void onClick(Dialog dialog) {
      dialog.dismiss();
    }
  };

  public CharSequence getContent() {
    return content;
  }

  public void setContent(CharSequence content) {
    this.content = content;
  }

  public void setLeft(CharSequence leftText, OnClickListener leftClick) {
    this.leftBtnStr = leftText;
    this.leftClick = leftClick;
  }

  public void setRight(CharSequence rightBtnStr, OnClickListener rightClick) {
    this.rightBtnStr = rightBtnStr;
    this.rightClick = rightClick;
  }


  @Override
  protected int getLayout() {
    return R.layout.common_dialog_layout;
  }

  @Override
  protected void init(Bundle var1, View view) {
    content_tv = (TextView) view.findViewById(R.id.content_tv);
    btn_left = (TextView) view.findViewById(R.id.btn_left);
    btn_right = (TextView) view.findViewById(R.id.btn_right);
    content_tv.setText(content);
    btn_left
        .setText(TextUtils.isEmpty(leftBtnStr) ? getString(R.string.dialog_cancel) : leftBtnStr);
    btn_right
        .setText(TextUtils.isEmpty(rightBtnStr) ? getString(R.string.dialog_ok) : rightBtnStr);
    btn_left.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        leftClick.onClick(getDialog());
      }
    });
    btn_right.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        rightClick.onClick(getDialog());
      }
    });
  }

  @Override
  protected boolean onKeyBack() {
    return false;
  }

  @Override
  protected boolean getCanCancelOutSide() {
    return false;
  }

  @Override
  public void onDialogCreated(Dialog dialog) {

  }

  public interface OnClickListener {
    void onClick(Dialog dialog);
  }

}
