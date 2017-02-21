package com.hongyu.reward.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.fw.zycoder.utils.Log;
import com.hongyu.reward.R;


/**
 * Created by zhangyang131 on 16/9/7.
 */
public class AppLoadingView {
  private int defaultTextResourceId = R.string.base_default_loading;
  private Context context;
  private boolean cancelable;
  private boolean outSideCancel;
  private View rootView;
  private TextView textView;
  private Dialog dialog;
  private String loadingText;
  private DialogInterface.OnCancelListener mListener;

  public AppLoadingView(Context context) {
    this.context = context;
    this.loadingText = context.getString(defaultTextResourceId);
    onCreate();
  }

  public AppLoadingView(Context context, String text) {
    this.loadingText = text;
    this.context = context;
    onCreate();
  }

  public boolean isShowing() {
    if (dialog != null && dialog.isShowing()) {
      return true;
    }
    return false;
  }

  public void setLoadingText(String loadingText) {
    this.loadingText = loadingText;
    if (textView != null) {
      textView.setText(loadingText);
    }
  }

  public boolean isCancelable() {
    return cancelable;
  }

  public void setCancelable(boolean cancelable) {
    this.cancelable = cancelable;
    if (dialog != null) {
      dialog.setCancelable(cancelable);
    }
  }

  public boolean isOutSideCancel() {
    return outSideCancel;
  }

  public void setOutSideCancel(boolean outSideCancel) {
    this.outSideCancel = outSideCancel;
    if (dialog != null) {
      dialog.setCanceledOnTouchOutside(outSideCancel);
    }
  }

  public void onCreate() {
    LayoutInflater inflater = LayoutInflater.from(context);
    this.rootView = (inflater.inflate(R.layout.app_loading_view, null));
    this.textView = (TextView) rootView.findViewById(R.id.loading_text);
    this.textView.setText(loadingText);
    this.dialog = new Dialog2(context, R.style.MProgressDialog);
    this.dialog.setContentView(rootView);
    this.dialog.setCancelable(cancelable);
    this.dialog.setOnCancelListener(mListener);
    this.dialog.setCanceledOnTouchOutside(outSideCancel);
    mListener = null; // 每次只用一次
  }

  public void show() {
    this.dialog.show();
  }

  public void dismiss() {
    this.dialog.dismiss();
  }

  public void setCancelListener(DialogInterface.OnCancelListener listener){
    this.mListener = listener;
    this.dialog.setOnCancelListener(mListener);
  }

  class Dialog2 extends Dialog{

    public Dialog2(Context context) {
      super(context);
    }

    public Dialog2(Context context, int themeResId) {
      super(context, themeResId);
    }

    @Override
    public void dismiss() {
      super.dismiss();
      Log.i("dismiss");
    }

    @Override
    public void cancel() {
      super.cancel();
      Log.i("cancel");
    }
  }
}
