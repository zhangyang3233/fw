package com.hongyu.reward.ui.activity.personal;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hongyu.reward.R;
import com.hongyu.reward.appbase.BaseSingleFragmentActivity;
import com.hongyu.reward.manager.AccountManager;
import com.hongyu.reward.ui.fragment.personal.EditNicknameFragment;
import com.hongyu.reward.widget.TitleContainer;

/**
 * Created by zhangyang131 on 16/10/10.
 */
public class EditNicknameActivity extends BaseSingleFragmentActivity {
  TextView rightBtn;
  LinearLayout right_container;

  public static void launch(Context context) {
    Intent i = new Intent(context, EditNicknameActivity.class);
    AccountManager.launchAfterLogin(context, i);

  }

  @Override
  protected Class<? extends Fragment> getSingleContentFragmentClass() {
    return EditNicknameFragment.class;
  }

  @Override
  protected String getTitleText() {
    return "编辑昵称";
  }

  // @Override
  // protected void setCustomTitleView() {
  // super.setCustomTitleView();
  // right_container = (LinearLayout) findViewById(R.id.right_container);
  // right_container.removeAllViews();
  // rightBtn = new Button(this);
  // rightBtn.setTextColor(getResources().getColor(R.color.colorAccent));
  // rightBtn.setTextSize(18);
  // rightBtn.setOnClickListener(new View.OnClickListener() {
  // @Override
  // public void onClick(View v) {
  // if(mFragment instanceof EditNicknameFragment){
  // ((EditNicknameFragment)mFragment).editFinish();
  // }
  //
  // }
  // });
  // right_container.addView(rightBtn);
  // }


  @Override
  protected <V extends TitleContainer> void setCustomTitleView(V view) {
    super.setCustomTitleView(view);
    if (view instanceof View) {
      right_container = (LinearLayout) ((View) view).findViewById(R.id.common_title_view_layout_right_container);
      right_container.removeAllViews();
      rightBtn = new TextView(this);
      rightBtn.setTextColor(getResources().getColor(R.color.colorAccent));
      rightBtn.setTextSize(16);
      rightBtn.setText("完成");
      rightBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          if (mFragment instanceof EditNicknameFragment) {
            ((EditNicknameFragment) mFragment).editFinish();
          }
        }
      });
      right_container.addView(rightBtn);
    }

  }
}
