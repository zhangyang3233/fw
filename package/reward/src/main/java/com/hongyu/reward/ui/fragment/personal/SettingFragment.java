package com.hongyu.reward.ui.fragment.personal;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;

import com.fw.zycoder.utils.SPUtil;
import com.hongyu.reward.R;
import com.hongyu.reward.appbase.BaseLoadFragment;
import com.hongyu.reward.config.Constants;
import com.hongyu.reward.manager.AccountManager;
import com.hongyu.reward.manager.InitPushManager;
import com.hongyu.reward.ui.activity.BrowserActivity;
import com.hongyu.reward.ui.activity.personal.UpdatePwdActivity;
import com.hongyu.reward.ui.dialog.CommonTwoBtnDialogFragment;
import com.hongyu.reward.widget.CommonTextView;
import com.zy.widgets.text.SwitchButton;

/**
 * Created by zhangyang131 on 16/9/22.
 */
public class SettingFragment extends BaseLoadFragment
    implements
      View.OnClickListener,
      CompoundButton.OnCheckedChangeListener {
  private CommonTextView mSettingPwd;
  private Button mLogout;
  private CommonTextView mSettingHelp;
  private CommonTextView mSettingTerms;
  private CommonTextView mSettingAboutUs;
  private SwitchButton mBtnVibration;
  private SwitchButton mBtnSound;
  private boolean audioCues;
  private boolean shockCues;

  @Override
  protected void onStartLoading() {

  }

  @Override
  protected void onInflated(View contentView, Bundle savedInstanceState) {
    initView();
  }

  private void initView() {
    audioCues = SPUtil.getBoolean(Constants.Pref.AUDIO_CUES, true);
    shockCues = SPUtil.getBoolean(Constants.Pref.SHOCK_CUES, true);
    mBtnSound = (SwitchButton) mContentView.findViewById(R.id.sound);
    mBtnVibration = (SwitchButton) mContentView.findViewById(R.id.vibration);
    mSettingPwd = (CommonTextView) mContentView.findViewById(R.id.setting_update_pwd);
    mSettingHelp = (CommonTextView) mContentView.findViewById(R.id.setting_help);
    mSettingTerms = (CommonTextView) mContentView.findViewById(R.id.setting_terms);
    mSettingAboutUs = (CommonTextView) mContentView.findViewById(R.id.setting_about_us);
    mLogout = (Button) mContentView.findViewById(R.id.logout);
    mBtnSound.setChecked(audioCues);
    mBtnVibration.setChecked(shockCues);
    mBtnSound.setOnCheckedChangeListener(this);
    mBtnVibration.setOnCheckedChangeListener(this);
    mSettingPwd.setOnClickListener(this);
    mSettingHelp.setOnClickListener(this);
    mSettingTerms.setOnClickListener(this);
    mSettingAboutUs.setOnClickListener(this);
    mLogout.setOnClickListener(this);
  }

  @Override
  protected int getLayoutResId() {
    return R.layout.activity_setting_layout;
  }

  @Override
  public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
    switch (buttonView.getId()) {
      case R.id.vibration:
        SPUtil.putBoolean(Constants.Pref.SHOCK_CUES, isChecked);
        break;
      case R.id.sound:
        SPUtil.putBoolean(Constants.Pref.AUDIO_CUES, isChecked);
        break;
    }
    InitPushManager.getInstance().resetPushConfig();
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.setting_update_pwd:
        UpdatePwdActivity.launch(getActivity());
        break;
      case R.id.setting_help:
        BrowserActivity.launch(getActivity(), Constants.Server.API_PREFIX + "/page/html?id=2",
            "帮助中心");
        break;
      case R.id.setting_terms:
        BrowserActivity.launch(getActivity(), Constants.Server.API_PREFIX + "/page/html?id=3",
            "法律条款");
        break;
      case R.id.setting_about_us:
        BrowserActivity.launch(getActivity(), Constants.Server.API_PREFIX + "/page/html?id=1",
            "关于我们");
        break;
      case R.id.logout:
        showLogoutDialog();
        break;
    }
  }

  private void showLogoutDialog() {
    CommonTwoBtnDialogFragment dialog = new CommonTwoBtnDialogFragment();
    dialog.setContent(getString(R.string.sure_to_logout));
    dialog.setLeft(getString(R.string.dialog_cancel),
        new CommonTwoBtnDialogFragment.OnClickListener() {
          @Override
          public void onClick(Dialog dialog) {
            dialog.dismiss();
          }
        });
    dialog.setRight(getString(R.string.dialog_ok),
        new CommonTwoBtnDialogFragment.OnClickListener() {
          @Override
          public void onClick(Dialog dialog) {
            dialog.dismiss();
            AccountManager.getInstance().logout();
            getActivity().finish();
          }
        });
    dialog.show(getChildFragmentManager(), getClass().getSimpleName());
  }
}
