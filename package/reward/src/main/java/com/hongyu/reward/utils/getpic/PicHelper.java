package com.hongyu.reward.utils.getpic;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.fw.zycoder.utils.MainThreadPostUtils;
import com.hongyu.reward.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by zhangyang131 on 16/10/10.
 */
public class PicHelper {
    Fragment fragment;
    Activity activity;
    Dialog mPickDialog;
    GetPicFinish callback;

    public PicHelper(Fragment fragment, GetPicFinish callback) {
        this.fragment = fragment;
        this.activity = fragment.getActivity();
        this.callback = callback;
    }

    public PicHelper(Activity activity, GetPicFinish callback) {
        this.activity = activity;
        this.callback = callback;
    }

    public void getPic() {
        showDialog();
    }


    private void showDialog() {
        if (mPickDialog == null) {
            mPickDialog = new Dialog(activity, R.style.SquaredShareMenuStyle);
            WindowManager.LayoutParams dialogParams = mPickDialog.getWindow()
                    .getAttributes();
            dialogParams.gravity = Gravity.BOTTOM;
            mPickDialog.getWindow().getAttributes().dimAmount = (float) 0.5;
            View v = LayoutInflater.from(activity).inflate(
                    R.layout.layout_pick_head_select, null);
            v.setMinimumWidth(activity.getResources().getDisplayMetrics().widthPixels);
            v.findViewById(R.id.select_from_gallery).setOnClickListener(
                    new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            hidePickDialog();
                            Intent intent = new Intent(Intent.ACTION_PICK, null);
                            intent.setDataAndType(
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                    "image/*");
                            // fake solution for some phone can't call the gallery
                            if (intent.resolveActivity(activity.getPackageManager()) != null) {
                                startActivityForResult(intent, Consts.REQUEST_CODE_PICK_FROM_GALLERY);
                            }
                        }
                    });
            v.findViewById(R.id.select_take_a_picture).setOnClickListener(
                    new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            hidePickDialog();
                            Intent intent = new Intent(
                                    MediaStore.ACTION_IMAGE_CAPTURE);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                                    Uri.fromFile(new File(Consts.mShootPath)));
                            // fake solution for some phone can't call the camera
                            if (intent.resolveActivity(activity.getPackageManager()) != null) {
                                startActivityForResult(intent, Consts.REQUEST_CODE_TAKE_A_PICTURE);
                            }
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

    public void onGetActivityResult(int requestCode, Intent data) {
        switch (requestCode) {
            case Consts.REQUEST_CODE_PICK_FROM_GALLERY:
                if (data != null) {// 从相册选择
                    startPhotoZoom(data.getData());
                }
                break;
            case Consts.REQUEST_CODE_TAKE_A_PICTURE:// 拍照
                final File temp = new File(Consts.mShootPath);
                if (!temp.exists()) {
                    MainThreadPostUtils.toast(R.string.label_save_picture_failed);
                    return;
                }
                startPhotoZoom(Uri.fromFile(temp));
                break;
            case Consts.REQUEST_CODE_CROP_A_PICTURE:// 剪裁图片
                if (data != null) {
                    saveCropPic(data);
                    // 上传头像
                    callback.finish(Consts.mTempHeadPath);
                }
                break;
            default:
                break;
        }
    }


    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    private void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", Consts.WIDTH_HEAD_OUTPUT);
        intent.putExtra("outputY", Consts.HEIGHT_HEAD_OUTPUT);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, Consts.REQUEST_CODE_CROP_A_PICTURE);
    }

    private void startActivityForResult(Intent intent, int requestCodeCropAPicture) {
        if (fragment != null) {
            fragment.startActivityForResult(intent, requestCodeCropAPicture);
        } else {
            activity.startActivityForResult(intent, requestCodeCropAPicture);
        }
    }

    /**
     * 保存裁剪之后的图片数据
     *
     * @param picdata
     */
    private void saveCropPic(Intent picdata) {
        Bundle extras = picdata.getExtras();
        if (extras != null) {
            final Bitmap photo = extras.getParcelable("data");
            saveBitmap(photo);
        }
    }


    public void saveBitmap(Bitmap bm) {
        final File dir = new File(Consts.DIRECTORY_HEAD);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        final File f = new File(Consts.mTempHeadPath);
        if (f.exists()) {
            f.delete();
        }
        final File newFile = new File(Consts.mTempHeadPath);
        try {
            FileOutputStream out = new FileOutputStream(newFile);
            bm.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public interface GetPicFinish {
        void finish(String filePath);
    }
}
