package com.hongyu.reward.ui.fragment.personal;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.fw.zycoder.http.callback.DataCallback;
import com.hongyu.reward.R;
import com.hongyu.reward.appbase.BaseLoadFragment;
import com.hongyu.reward.http.ResponesUtil;
import com.hongyu.reward.manager.AccountManager;
import com.hongyu.reward.model.BaseModel;
import com.hongyu.reward.model.NoticeEvent;
import com.hongyu.reward.request.EditUserInfoRequestBuilder;
import com.hongyu.reward.utils.T;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by zhangyang131 on 16/10/10.
 */
public class EditGenderFragment extends BaseLoadFragment implements View.OnClickListener {
  View mMaleLayout;
  View mFemaleLayout;
  ImageView mMaleSelect;
  ImageView mFemaleSelect;

  @Override
  protected void onStartLoading() {

  }

  @Override
  protected void onInflated(View contentView, Bundle savedInstanceState) {
    initView();
  }

  private void initView() {
    mMaleLayout = mContentView.findViewById(R.id.male_layout);
    mFemaleLayout = mContentView.findViewById(R.id.female_layout);
    mMaleSelect = (ImageView) mContentView.findViewById(R.id.male_layout_select);
    mFemaleSelect = (ImageView) mContentView.findViewById(R.id.female_layout_select);
    mMaleLayout.setOnClickListener(this);
    mFemaleLayout.setOnClickListener(this);
    freshUIByGender();
  }

  @Override
  protected int getLayoutResId() {
    return R.layout.edit_user_gender_layout;
  }


  @Override
  public void onClick(View v) { // 0男1女
    switch (v.getId()) {
      case R.id.male_layout:
        selectGender(0);
        break;
      case R.id.female_layout:
        selectGender(1);
        break;
    }
  }

  private void selectGender(final int gender) {
    if (AccountManager.getInstance().getUserInfo().getGender() == gender) {
      return;
    }
    showLoadingView();
    EditUserInfoRequestBuilder builder = new EditUserInfoRequestBuilder();
    builder.setGender(String.valueOf(gender));
    builder.setDataCallback(new DataCallback<BaseModel>() {
      @Override
      public void onDataCallback(BaseModel data) {
        if (isAdded()) {
          dismissLoadingView();
        }
        if (ResponesUtil.checkModelCodeOK(data)) {
          AccountManager.getInstance().getUserInfo().setGender(gender);
          freshUIByGender();
          EventBus.getDefault().post(new NoticeEvent(NoticeEvent.USER_GENDER_CHANGED));
        } else if (isAdded()) {
          T.show(ResponesUtil.getErrorMsg(data));
        }
      }
    });
    builder.build().submit();
  }

  private void freshUIByGender() {
    if (AccountManager.getInstance().getUserInfo().getGender() == 0) { // 男
      mMaleSelect.setVisibility(View.VISIBLE);
      mFemaleSelect.setVisibility(View.GONE);
    } else {
      mMaleSelect.setVisibility(View.GONE);
      mFemaleSelect.setVisibility(View.VISIBLE);
    }
  }
}
