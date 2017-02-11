package com.hongyu.reward.ui.asynctask;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.fw.zycoder.utils.ImageUtil;
import com.hongyu.reward.utils.ImageFactory;

/**
 * Created by zhangyang131 on 2017/2/9.
 */

public class DealImgBase64AsyncTask extends AsyncTask<String, String, String> {
  GetPicBase64Callback callback;

  public DealImgBase64AsyncTask(GetPicBase64Callback callback) {
    this.callback = callback;
  }

  @Override
  protected void onPreExecute() {
    super.onPreExecute();
    if (callback != null) {
      callback.onPre();
    }

  }

  @Override
  protected String doInBackground(String... params) {
    String imageBase64 = null;
    try {
      Bitmap bm = ImageFactory.getimage(params[0]);
      imageBase64 = ImageUtil.getBitmapToBase64(bm);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return imageBase64;
  }

  @Override
  protected void onPostExecute(String imageBase64) {
    super.onPostExecute(imageBase64);
    if (callback != null) {
      if (!TextUtils.isEmpty(imageBase64)) {
        callback.getPicBase64(imageBase64);
      } else {
        callback.onError();
      }
    }
  }

  public interface GetPicBase64Callback {
    public void onPre();

    public void getPicBase64(String base64);

    public void onError();
  }
}
