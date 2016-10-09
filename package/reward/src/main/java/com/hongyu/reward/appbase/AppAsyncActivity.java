package com.hongyu.reward.appbase;

import android.content.DialogInterface;
import android.os.Bundle;

import com.hongyu.reward.widget.AppLoadingView;


/**
 *
 * @author zhangyang
 */
public abstract class AppAsyncActivity extends AppTitleActivity {
  private AppLoadingView appLoadingView;

  @Override
  protected void onCreate(Bundle onSaveInstanceState) {
    super.onCreate(onSaveInstanceState);
    initAppLoadingView();
  }

  private void initAppLoadingView() {
    appLoadingView = new AppLoadingView(this);
  }


  public void setCancelable(boolean cancelable) {
    appLoadingView.setCancelable(cancelable);
  }


  public void showLoadingView() {
    appLoadingView.show();
  }

  public void showLoadingView(String content) {
    appLoadingView.setLoadingText(content);
    appLoadingView.show();
  }

  public void dissmissLoadingView() {
    appLoadingView.dismiss();
  }

  public void setCancelListener(DialogInterface.OnCancelListener listener){
    appLoadingView.setCancelListener(listener);
  }

}

