package com.hongyu.reward.ui.activity.personal;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.hongyu.reward.appbase.BaseSingleFragmentActivity;
import com.hongyu.reward.manager.AccountManager;
import com.hongyu.reward.ui.fragment.personal.EditGenderFragment;

/**
 * Created by zhangyang131 on 16/10/10.
 */
public class EditUserGenderActivity extends BaseSingleFragmentActivity {
  public static final String GENDER = "gender";

  public static void launch(Context context, int gender) {
    Intent i = new Intent(context, EditUserGenderActivity.class);
    i.putExtra(GENDER, gender);
    AccountManager.launchAfterLogin(context, i);
  }

  @Override
  protected Class<? extends Fragment> getSingleContentFragmentClass() {
    return EditGenderFragment.class;
  }

  @Override
  protected String getTitleText() {
    return "选择性别";
  }
}
