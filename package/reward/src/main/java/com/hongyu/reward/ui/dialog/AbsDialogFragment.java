package com.hongyu.reward.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;

import com.fw.zycoder.utils.ViewUtils;
import com.hongyu.reward.R;

/**
 * Created by zhangyang131 on 16/9/13.
 */
public abstract class AbsDialogFragment extends DialogFragment {
  private View mContentView;

  public AbsDialogFragment() {}

  protected abstract int getLayout();

  protected abstract void init(Bundle var1, View var2);

  protected abstract boolean onKeyBack();

  protected abstract boolean getCanCancelOutSide();

  public Dialog onCreateDialog(Bundle savedInstanceState) {
    Dialog dialog = new Dialog(getActivity(), R.style.CommonDialog);
    mContentView = ViewUtils.newInstance(getActivity(), getLayout());
    dialog.setContentView(mContentView);
    dialog.setCanceledOnTouchOutside(getCanCancelOutSide());
    onDialogCreated(dialog);
    return dialog;
  }

  public abstract void onDialogCreated(Dialog dialog);

  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
      public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        return keyCode == 4 && event.getAction() == 0 ? AbsDialogFragment.this.onKeyBack() : false;
      }
    });
    init(savedInstanceState, mContentView);
  }

  public void setDefaultProperty() {
    if (this.getActivity() != null && !this.getActivity().isFinishing()) {
      if (this.getDialog() == null) {
        return;
      }
      WindowManager.LayoutParams params = this.getDialog().getWindow().getAttributes();
      Display display =
          ((WindowManager) this.getActivity().getSystemService(Context.WINDOW_SERVICE))
              .getDefaultDisplay();
      params.width = (int) ((double) display.getWidth() * 0.8D);
      params.height = WindowManager.LayoutParams.WRAP_CONTENT;
      this.getDialog().getWindow().setAttributes(params);
    }
  }

  protected void setSheetProperty() {
    if (this.getActivity() != null && !this.getActivity().isFinishing()) {
      if (this.getDialog() == null) {
        return;
      }
      WindowManager.LayoutParams params = this.getDialog().getWindow().getAttributes();
      Display display =
          ((WindowManager) this.getActivity().getSystemService(Context.WINDOW_SERVICE))
              .getDefaultDisplay();
      params.width = (int) ((double) display.getWidth());
      params.height = WindowManager.LayoutParams.WRAP_CONTENT;
      params.gravity = Gravity.BOTTOM;
      params.windowAnimations = R.style.dialog_sheet_window_anim;
      this.getDialog().getWindow().setAttributes(params);
    }
  }

  @Override
  public void onStart() {
    super.onStart();
    if (isSheetStyle()) {// 底部滑出
      setSheetProperty();
    } else if (isCenterStyle()) { // 居中显示
      setDefaultProperty();
    }

  }



  public void dismiss() {
    try {
      super.dismiss();
    } catch (Exception var2) {}

  }

  public boolean isSheetStyle() {
    return false;
  }

  public boolean isCenterStyle() {
    return true;
  }


}
