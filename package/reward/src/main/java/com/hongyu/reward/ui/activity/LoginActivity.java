package com.hongyu.reward.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.hongyu.reward.R;
import com.hongyu.reward.appbase.BaseSingleFragmentActivity;
import com.hongyu.reward.ui.fragment.LoginFragment;


/**
 * Created by zhangyang131 on 16/9/8.
 */
public class LoginActivity extends BaseSingleFragmentActivity {

  public static void launch(Context context) {
    Intent i = new Intent(context, LoginActivity.class);
    context.startActivity(i);
  }

  @Override
  protected String getTitleText() {
    return getResources().getString(R.string.login);
  }


  @Override
  protected Class<? extends Fragment> getSingleContentFragmentClass() {
    return LoginFragment.class;
  }

  @Override
  protected void onCreateMenu() {
    super.onCreateMenu();
    TextView register =
        (TextView) getLayoutInflater().inflate(R.layout.common_right_title_view, null);
    register.setText(R.string.register);
    register.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        RegisterActivity.launch(LoginActivity.this);
      }
    });
    setRightTitleView(register);
  }

  @Override
  public void onLogout() {}
}
