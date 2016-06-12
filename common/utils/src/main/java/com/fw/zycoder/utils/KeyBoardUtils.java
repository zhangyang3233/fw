package com.fw.zycoder.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * 打开或关闭软键盘
 * 
 * @author zhy
 * 
 */
public class KeyBoardUtils {
  /**
   * 打卡软键盘
   * 
   * @param mEditText
   *          输入框
   * @param mContext
   *          上下文
   */
  public static void openKeybord(EditText mEditText, Context mContext) {
    InputMethodManager imm = (InputMethodManager) mContext
        .getSystemService(Context.INPUT_METHOD_SERVICE);
    imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN);
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
        InputMethodManager.HIDE_IMPLICIT_ONLY);
  }

  /**
   * 关闭软键盘
   * 
   * @param mEditText
   *          输入框
   * @param mContext
   *          上下文
   */
  public static void closeKeybord(EditText mEditText, Context mContext) {
    InputMethodManager imm = (InputMethodManager) mContext
        .getSystemService(Context.INPUT_METHOD_SERVICE);

    imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
  }


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
