package com.wanda.uicomp.deprecated.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wanda.uicomp.R;

public class ProgressiveDialog extends Dialog {

  private TextView mLoadingTextView;
  private ProgressBar mProgressBar;
  private View mContentView;

  public ProgressiveDialog(Context context) {
    super(context, R.style.ProgressiveDialog);
    mContentView = getLayoutInflater().inflate(R.layout.progress_view, null);
    setContentView(mContentView);
    mContentView.setBackgroundResource(R.drawable.custom_progressive_dialog_bg);
    mLoadingTextView = (TextView) findViewById(android.R.id.text1);
    mProgressBar = (ProgressBar) findViewById(android.R.id.progress);
    setCanceledOnTouchOutside(false);
    setMessage(0);
  }

  public void setMessage(int id) {
    if (0 == id) {
      mLoadingTextView.setText(R.string.loading);
    } else if (0 < id) {
      mLoadingTextView.setText(id);
    }
  }

  public void setBackground(int resid) {
    if (resid > 0) {
      mContentView.setBackgroundResource(resid);
    }
  }

  public void setIcon(Drawable d) {
    if (null != d) {
      mProgressBar.setIndeterminateDrawable(d);
    }
  }
}
