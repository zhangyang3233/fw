package com.fw.zycoder.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * @author liuxu34@wanda.cn (Liu Xu)
 */
public class InputUtils {

  @TargetApi(Build.VERSION_CODES.CUPCAKE)
  public static void hideSoftInputMode(Context context, View windowToken) {
    ((InputMethodManager) context
        .getSystemService(Context.INPUT_METHOD_SERVICE))
        .hideSoftInputFromWindow(windowToken.getWindowToken(),
            InputMethodManager.HIDE_NOT_ALWAYS);
  }

  @TargetApi(Build.VERSION_CODES.CUPCAKE)
  public static void showSoftInputMode(EditText editText) {
    editText.setFocusable(true);
    editText.setFocusableInTouchMode(true);
    editText.requestFocus();
    InputMethodManager inputManager = (InputMethodManager) editText
        .getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
    inputManager.showSoftInput(editText, 0);
  }
}
