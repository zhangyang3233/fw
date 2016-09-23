package com.hongyu.reward.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.hongyu.reward.R;

/**
 * Created by zhangyang131 on 16/9/13.
 */
public class NumInputDialog extends AbsDialogFragment {
  EditText content;
  Button okBtn;
  ActionListener mActionListener;

  public ActionListener getmActionListener() {
    return mActionListener;
  }

  public void setmActionListener(ActionListener mActionListener) {
    this.mActionListener = mActionListener;
  }

  @Override
  protected int getLayout() {
    return R.layout.dialog_select_input_num;
  }

  @Override
  protected void init(Bundle var1, View view) {
    okBtn = (Button) view.findViewById(R.id.ok);
    content = (EditText) view.findViewById(R.id.content);
    okBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        mActionListener.onFinish(content.getText().toString());
        dismiss();
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

  @Override
  public boolean isSheetStyle() {
    return true;
  }

  @Override
  public void onStart() {
    super.onStart();
    getDialog().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
    getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    Display display = ((WindowManager) this.getActivity().getSystemService(Context.WINDOW_SERVICE))
        .getDefaultDisplay();
    WindowManager.LayoutParams lp = this.getDialog().getWindow().getAttributes();
    lp.width = display.getWidth(); // 设置宽度
    lp.gravity = Gravity.BOTTOM;
    lp.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE;
    getDialog().getWindow().setAttributes(lp);
    InputMethodManager imm =
        (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
  }

  public interface ActionListener {
    void onFinish(String numStr);
  }
}
