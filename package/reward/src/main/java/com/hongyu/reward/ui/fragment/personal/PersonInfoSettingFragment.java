package com.hongyu.reward.ui.fragment.personal;

import android.app.Dialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.fw.zycoder.http.callback.DataCallback;
import com.fw.zycoder.utils.ImageUtil;
import com.fw.zycoder.utils.Log;
import com.hongyu.reward.R;
import com.hongyu.reward.appbase.BaseTakePhotoFragment;
import com.hongyu.reward.http.ResponesUtil;
import com.hongyu.reward.manager.AccountManager;
import com.hongyu.reward.model.BaseModel;
import com.hongyu.reward.model.LoginModel;
import com.hongyu.reward.model.NoticeEvent;
import com.hongyu.reward.request.EditUserInfoRequestBuilder;
import com.hongyu.reward.request.GetUserInfoRequestBuilder;
import com.hongyu.reward.ui.activity.personal.EditNicknameActivity;
import com.hongyu.reward.ui.activity.personal.EditUserGenderActivity;
import com.hongyu.reward.utils.T;
import com.hongyu.reward.utils.getpic.Consts;
import com.hongyu.reward.widget.CommonTextView;
import com.hongyu.reward.widget.RoundImageView;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.TResult;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;

/**
 * Created by zhangyang131 on 16/10/10.
 */
public class PersonInfoSettingFragment extends BaseTakePhotoFragment
    implements
      View.OnClickListener {
  View headLayout;
  RoundImageView header_icon;
  CommonTextView nickname_layout;
  CommonTextView gender_layout;
  boolean isNeedFresh;
  Dialog mPickDialog;
  CropOptions options;

  private CropOptions getOptions() {
    if (options == null) {
      options = new CropOptions.Builder().setAspectX(1).setAspectY(1).setWithOwnCrop(true).create();
    }
    return options;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    EventBus.getDefault().register(this);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    EventBus.getDefault().unregister(this);
  }

  @Override
  protected void onStartLoading() {
    if (isNeedFresh) {
      showLoadingView();
      GetUserInfoRequestBuilder builder = new GetUserInfoRequestBuilder();
      builder.setDataCallback(new DataCallback<LoginModel>() {
        @Override
        public void onDataCallback(LoginModel data) {
          if (!isAdded()) {
            return;
          }
          dismissLoadingView();
          if (ResponesUtil.checkModelCodeOK(data)) {
            refreshData(data.getData());
          } else {
            T.show(ResponesUtil.getErrorMsg(data));
          }
        }
      });
      builder.build().submit();
      isNeedFresh = false;
    } else {
      LoginModel.UserInfo userInfo = AccountManager.getInstance().getUser();
      refreshData(userInfo);
    }

  }

  private void refreshData(LoginModel.UserInfo data) {
    header_icon.loadNetworkImageByUrl(data.getHead_img());
    nickname_layout.setRightText(data.getNickname());
    gender_layout.setRightText(data.getGender() == 0 ? "男" : "女");
  }

  @Override
  protected void onInflated(View contentView, Bundle savedInstanceState) {
    initView();
  }

  private void initView() {
    header_icon = (RoundImageView) mContentView.findViewById(R.id.header_icon);
    headLayout = mContentView.findViewById(R.id.head_layout);
    nickname_layout = (CommonTextView) mContentView.findViewById(R.id.nickname_layout);
    gender_layout = (CommonTextView) mContentView.findViewById(R.id.gender_layout);
    headLayout.setOnClickListener(this);
    nickname_layout.setOnClickListener(this);
    gender_layout.setOnClickListener(this);
  }

  @Override
  protected int getLayoutResId() {
    return R.layout.activity_person_info_setting;
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.head_layout:
        showDialog();
        break;
      case R.id.nickname_layout:
        EditNicknameActivity.launch(getActivity());
        break;
      case R.id.gender_layout:
        EditUserGenderActivity.launch(getActivity(),
            AccountManager.getInstance().getUser().getGender());
        break;

    }
  }

  // @Override
  // public void onActivityResult(int requestCode, int resultCode, Intent data) {
  // super.onActivityResult(requestCode, resultCode, data);
  // Log.i("img", "requestCode"+requestCode+"resultCode:"+resultCode);
  // if (resultCode == Activity.RESULT_OK) {
  // mPicHelper.onGetActivityResult(requestCode, data);
  // }
  // }

  private void uploadNewAvatar(String imgPath) {
    Log.i("img", "根据照片地址选照片：" + imgPath);
    // 照片地址
    // mTempHeadPath
    File imageFile = new File(imgPath);
    String imageBase64 = ImageUtil.getBitmapToBase64(imageFile);
    if (imageBase64 != null) {
      // 上传
      showLoadingView();
      EditUserInfoRequestBuilder builder = new EditUserInfoRequestBuilder();
      builder.setHeader_img(imageBase64);
      builder.setDataCallback(new DataCallback<BaseModel>() {
        @Override
        public void onDataCallback(BaseModel data) {
          if (ResponesUtil.checkModelCodeOK(data)) {
            // 修改成功
            NoticeEvent noticeEvent = new NoticeEvent(NoticeEvent.USER_IMG_CHANGED);
            EventBus.getDefault().post(noticeEvent);
            if (isAdded()) {
              dismissLoadingView();
            }
          } else if (isAdded()) {
            T.show(ResponesUtil.getErrorMsg(data));
            dismissLoadingView();
          }
        }
      });
      builder.build().submit();
    }
  }

  @Subscribe
  public void onEventMainThread(NoticeEvent noticeEvent) {
    if (noticeEvent.getType() == NoticeEvent.USER_IMG_CHANGED
        || noticeEvent.getType() == NoticeEvent.USER_NICKNAME_CHANGED
        || noticeEvent.getType() == NoticeEvent.USER_GENDER_CHANGED) {
      isNeedFresh = true;
      requestLoad();
    }
  }


  @Override
  public void takeSuccess(TResult result) {
    String filePath = result.getImage().getOriginalPath();
    uploadNewAvatar(filePath);
  }

  @Override
  public void takeFail(TResult result, String msg) {

  }

  @Override
  public void takeCancel() {

  }

  private void showDialog() {
    if (mPickDialog == null) {
      mPickDialog = new Dialog(getActivity(), R.style.SquaredShareMenuStyle);
      WindowManager.LayoutParams dialogParams = mPickDialog.getWindow()
          .getAttributes();
      dialogParams.gravity = Gravity.BOTTOM;
      mPickDialog.getWindow().getAttributes().dimAmount = (float) 0.5;
      View v = LayoutInflater.from(getActivity()).inflate(
          R.layout.layout_pick_head_select, null);
      v.setMinimumWidth(getActivity().getResources().getDisplayMetrics().widthPixels);
      v.findViewById(R.id.select_from_gallery).setOnClickListener(
          new View.OnClickListener() {

            @Override
            public void onClick(View v) {
              hidePickDialog();
              getTakePhoto().onPickFromGalleryWithCrop(Uri.fromFile(new File(Consts.mShootPath)),
                  getOptions());

            }
          });
      v.findViewById(R.id.select_take_a_picture).setOnClickListener(
          new View.OnClickListener() {

            @Override
            public void onClick(View v) {
              hidePickDialog();
              getTakePhoto().onPickFromCaptureWithCrop(Uri.fromFile(new File(Consts.mShootPath)),
                  getOptions());
            }
          });
      v.findViewById(R.id.select_cancel).setOnClickListener(
          new View.OnClickListener() {

            @Override
            public void onClick(View v) {
              hidePickDialog();
            }
          });
      mPickDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

        @Override
        public void onCancel(DialogInterface dialog) {
          hidePickDialog();
        }
      });
      mPickDialog.setContentView(v);
      mPickDialog.show();
    }
  }

  private void hidePickDialog() {
    if (mPickDialog != null) {
      if (mPickDialog.isShowing()) {
        mPickDialog.dismiss();
      }
      mPickDialog = null;
    }
  }
}
