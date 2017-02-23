package com.hongyu.reward.appbase;

import android.content.Intent;
import android.os.Bundle;

import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoFragment;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;

/**
 * Created by zhangyang131 on 2017/2/22.
 */

public abstract class BaseTakePhotoFragment extends BaseLoadFragment
    implements
      TakePhoto.TakeResultListener,
      InvokeListener {
  private static final String TAG = TakePhotoFragment.class.getName();
  private InvokeParam invokeParam;
  private TakePhoto takePhoto;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    getTakePhoto().onCreate(savedInstanceState);
    super.onCreate(savedInstanceState);
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    getTakePhoto().onSaveInstanceState(outState);
    super.onSaveInstanceState(outState);
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    getTakePhoto().onActivityResult(requestCode, resultCode, data);
    super.onActivityResult(requestCode, resultCode, data);
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, String[] permissions,
      int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    PermissionManager.TPermissionType type =
        PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    PermissionManager.handlePermissionsResult(getActivity(), type, invokeParam, this);
  }

  /**
   * 获取TakePhoto实例
   * 
   * @return
   */
  public TakePhoto getTakePhoto() {
    if (takePhoto == null) {
      takePhoto =
          (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
    }
    return takePhoto;
  }

//  @Override
//  public void takeSuccess(TResult result) {
//    Log.i(TAG, "takeSuccess：" + result.getImage().getCompressPath());
//  }
//
//  @Override
//  public void takeFail(TResult result, String msg) {
//    Log.i(TAG, "takeFail:" + msg);
//  }
//
//  @Override
//  public void takeCancel() {
//    Log.i(TAG, getResources().getString(com.jph.takephoto.R.string.msg_operation_canceled));
//  }

  @Override
  public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
    PermissionManager.TPermissionType type =
        PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
    if (PermissionManager.TPermissionType.WAIT.equals(type)) {
      this.invokeParam = invokeParam;
    }
    return type;
  }
}
