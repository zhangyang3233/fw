package com.hongyu.reward.ui.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.hongyu.reward.R;

/**
 * Created by zhangyang131 on 16/9/13.
 */
public class SingleBtnDialogFragment extends AbsDialogFragment {
  TextView content_tv;
  TextView btn;
  CharSequence content;

  CharSequence btnStr;

  OnClickListener btnClick = new OnClickListener() {
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

  public void setBtn(CharSequence leftText, OnClickListener btnClick) {
    this.btnStr = leftText;
    this.btnClick = btnClick;
  }

  @Override
  protected int getLayout() {
    return R.layout.common_dialog_single_btn_layout;
  }

  @Override
  protected void init(Bundle var1, View view) {
    content_tv = (TextView) view.findViewById(R.id.content_tv);
    btn = (TextView) view.findViewById(R.id.btn);
    content_tv.setText(content);
    btn.setText(btnStr);
    btn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        btnClick.onClick(getDialog());
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
