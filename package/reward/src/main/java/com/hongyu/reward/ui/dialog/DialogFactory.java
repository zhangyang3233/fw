package com.hongyu.reward.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.fw.zycoder.utils.DisplayUtil;
import com.hongyu.reward.R;
import com.hongyu.reward.utils.T;

public class DialogFactory {

  /**
   * 输入就餐人数
   *
   * @auther jiawenze
   * @since 2016-7-12 上午5:09:15
   * @tags @param context
   * @tags @param listen
   */
  public static void showNumInputView(final Context context,
      final OnWhichBackStringListener listen) {
    LayoutInflater factory = LayoutInflater.from(context);
    final View view = factory.inflate(R.layout.dialog_select_input_num, null);

    final Dialog builder = new AlertDialog.Builder(context, R.style.DialogStyle).create();
    builder.show();
    builder.setContentView(view);

    // 软键盘把dialog整个推上去
    builder.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
    builder.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

    Window window = builder.getWindow();
    window.setGravity(Gravity.BOTTOM);
    window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    window.setWindowAnimations(R.style.dialog_sheet_window_anim);
    WindowManager.LayoutParams lp = window.getAttributes();
    lp.width = DisplayUtil.getScreenWidth(context);
    lp.gravity = Gravity.BOTTOM;
    window.setAttributes(lp);

    InputMethodManager imm =
        (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);


    final EditText content = (EditText) view.findViewById(R.id.content);
    View ok = view.findViewById(R.id.ok);
    View cancel = view.findViewById(R.id.cancel);
    ok.setOnClickListener(new android.view.View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String num = content.getText().toString().trim();
        builder.dismiss();
        listen.onConfirmClick(new String[] {num});
      }
    });

    cancel.setOnClickListener(new android.view.View.OnClickListener() {
      @Override
      public void onClick(View v) {
        builder.dismiss();
      }
    });

  }


  /**
   * 设置悬赏金额
   * 
   * @param context
   * @param listen
   */
  public static void showPriceInputView(Context context, final OnWhichBackStringListener listen) {
    LayoutInflater factory = LayoutInflater.from(context);
    final View view = factory.inflate(R.layout.select_input_price, null);

    final Dialog builder = new AlertDialog.Builder(context, R.style.DialogStyle).create();
    builder.show();
    builder.setContentView(view);

    builder.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
    builder.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

    Window window = builder.getWindow();
    window.setGravity(Gravity.BOTTOM);
    window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    window.setWindowAnimations(R.style.dialog_sheet_window_anim);

    final EditText content = (EditText) view.findViewById(R.id.content);
    View ok = view.findViewById(R.id.ok);
    View cancel = view.findViewById(R.id.cancel);

    WindowManager windowManager = builder.getWindow().getWindowManager();
    WindowManager.LayoutParams lp = builder.getWindow().getAttributes();
    lp.width = DisplayUtil.getScreenWidth(context);
    builder.getWindow().setAttributes(lp);

    InputMethodManager imm =
        (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

    ok.setOnClickListener(new android.view.View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String num = content.getText().toString().trim();
        builder.dismiss();
        listen.onConfirmClick(new String[] {num});
      }
    });

    cancel.setOnClickListener(new android.view.View.OnClickListener() {
      @Override
      public void onClick(View v) {
        builder.dismiss();
      }
    });
  }

  /**
   * 输入排号信息
   *
   * @auther jiawenze
   * @since 2016-7-18 上午7:53:30
   * @tags @param context
   * @tags @param listen
   */
  public static void showNumeralInputView(final Context context,
      final OnDialogActionListener mOnDialogActionListener) {
    LayoutInflater factory = LayoutInflater.from(context);
    final View view = factory.inflate(R.layout.select_input_numeral, null);

    final Dialog builder = new AlertDialog.Builder(context, R.style.DialogStyle).create();
    builder.show();
    builder.setContentView(view);

    // 软键盘把dialog整个推上去
    builder.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
    builder.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

    Window window = builder.getWindow();
    window.setGravity(Gravity.BOTTOM);
    window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    window.setWindowAnimations(R.style.dialog_sheet_window_anim);

    final EditText con1 = (EditText) view.findViewById(R.id.con_1);
    final EditText con2 = (EditText) view.findViewById(R.id.con_2);
    final EditText con3 = (EditText) view.findViewById(R.id.con_3);
    View ok = view.findViewById(R.id.ok);
    View cancel = view.findViewById(R.id.cancel);

    WindowManager.LayoutParams lp = builder.getWindow().getAttributes();
    lp.width = DisplayUtil.getScreenWidth(context);
    builder.getWindow().setAttributes(lp);

    InputMethodManager imm =
        (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

    ok.setOnClickListener(new android.view.View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String indexNum = con1.getText().toString();
        String waitNum = con2.getText().toString();
        String pNum = con3.getText().toString();
        if (TextUtils.isEmpty(indexNum)) {
          T.show("请输入排位号");
          return;
        }
        if (TextUtils.isEmpty(waitNum)) {
          T.show("请输入还需等待几桌");
          return;
        }
        if (TextUtils.isEmpty(pNum)) {
          T.show("请输入几人桌");
          return;
        }
        if (mOnDialogActionListener != null) {
          mOnDialogActionListener.onFinish(indexNum, waitNum, pNum);
          builder.dismiss();
        }
      }
    });

    cancel.setOnClickListener(new android.view.View.OnClickListener() {
      @Override
      public void onClick(View v) {
        builder.dismiss();
      }
    });

  }


  public static void showShareView(Context context, final OnWhichListener listen) {
    LayoutInflater factory = LayoutInflater.from(context);
    final View view = factory.inflate(R.layout.select_share, null);

    final Dialog builder = new AlertDialog.Builder(context).create();
    builder.show();
    builder.setContentView(view);

    Window window = builder.getWindow();
    window.setGravity(Gravity.BOTTOM);
    window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    window.setWindowAnimations(R.style.dialog_sheet_window_anim);

    View wechat = view.findViewById(R.id.wechat);
    View wechats = view.findViewById(R.id.wechats);
    View qq = view.findViewById(R.id.qq);

    WindowManager windowManager = builder.getWindow().getWindowManager();
    Display display = windowManager.getDefaultDisplay();
    WindowManager.LayoutParams lp = builder.getWindow().getAttributes();
    lp.width = (int) (display.getWidth() - 0); // 设置宽度
    builder.getWindow().setAttributes(lp);

    wechat.setOnClickListener(new android.view.View.OnClickListener() {
      @Override
      public void onClick(View v) {
        builder.dismiss();
        listen.onConfirmClick(1);
      }
    });

    wechats.setOnClickListener(new android.view.View.OnClickListener() {
      @Override
      public void onClick(View v) {
        builder.dismiss();
        listen.onConfirmClick(2);
      }
    });

    qq.setOnClickListener(new android.view.View.OnClickListener() {
      @Override
      public void onClick(View v) {
        builder.dismiss();
        listen.onConfirmClick(3);
      }
    });

  }

  public interface OnWhichListener {
    public void onConfirmClick(int which);
  }

  public interface OnWhichBackStringListener {
    void onConfirmClick(String[] content);
  }

  public interface OnDialogActionListener {
    void onFinish(String indexNum, String waitNum, String pNum);
  }

}
