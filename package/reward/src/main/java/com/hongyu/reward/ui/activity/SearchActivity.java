package com.hongyu.reward.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.WindowManager;

import com.hongyu.reward.R;
import com.hongyu.reward.appbase.BaseSingleFragmentActivity;
import com.hongyu.reward.model.dbmodel.SearchModel;
import com.hongyu.reward.ui.fragment.SearchFragment;
import com.hongyu.reward.widget.SearchEngineActionBar;
import com.hongyu.reward.widget.TitleContainer;
import com.readystatesoftware.systembartint.SystemBarTintManager;

/**
 * Created by zhangyang131 on 16/10/13.
 */
public class SearchActivity extends BaseSingleFragmentActivity
    implements
      SearchEngineActionBar.OnActionListener {
  public static final String PUBLISH = "publish";
  private boolean isPublish;

  public static void launch(Context context, boolean isPublish) {
    Intent i = new Intent(context, SearchActivity.class);
    i.putExtra(PUBLISH, isPublish);
    context.startActivity(i);
  }

  @Override
  protected String getTitleText() {
    return "";
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    isPublish = getIntent().getBooleanExtra(PUBLISH, true);
  }

  @Override
  protected TitleContainer getMyTitleContainer() {
    return SearchEngineActionBar.newInstance(this);
  }

  @Override
  protected <V extends TitleContainer> void setCustomTitleView(V view) {
    super.setCustomTitleView(view);
    SearchEngineActionBar searchEngineActionBar = (SearchEngineActionBar) view;
    searchEngineActionBar.setOnActionListener(this);
  }


  @Override
  protected Class<? extends Fragment> getSingleContentFragmentClass() {
    return SearchFragment.class;
  }

  @Override
  protected void initWindow() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
      SystemBarTintManager tintManager = new SystemBarTintManager(this);
      tintManager.setStatusBarTintEnabled(true);
      tintManager.setNavigationBarTintEnabled(true);
      tintManager.setTintColor(getResources().getColor(R.color.gray));
    }
  }

  @Override
  public void input(String word) {

  }

  @Override
  public void startSearch(String word) {
    SearchModel searchModel = new SearchModel(word);
    searchModel.saveIfTagNotExist();
    SearchShopActivity.launch(this, word, isPublish);
  }

  @Override
  public void click(String word) {

  }

  @Override
  public void back(String word) {
    finish();
  }
}
