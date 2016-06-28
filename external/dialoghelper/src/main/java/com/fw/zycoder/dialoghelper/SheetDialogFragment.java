package com.fw.zycoder.dialoghelper;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;

/**
 * Created by zhangyang131 on 16/6/27.
 */
public abstract class SheetDialogFragment extends DialogFragment {

  protected abstract int getLayoutId();

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    Dialog d = new Dialog(getActivity(), R.style.DialogStyle);
    d.setContentView(getLayoutId());
    d.getWindow().getAttributes().width = getResources().getDisplayMetrics().widthPixels;
    d.getWindow().setGravity(Gravity.BOTTOM);
    d.setCanceledOnTouchOutside(true);
    return d;
  }

}
