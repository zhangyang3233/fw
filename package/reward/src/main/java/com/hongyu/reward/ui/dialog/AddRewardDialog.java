package com.hongyu.reward.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.hongyu.reward.R;

import java.text.DecimalFormat;

/**
 * Created by zhangyang131 on 16/9/13.
 */
public class AddRewardDialog extends AbsDialogFragment implements View.OnClickListener {
  private static final String CURRENT_PRICE = "currentPrice";
  private TextView price;
  private TextView Add_10;
  private TextView Add_20;
  private TextView Add_50;
  private TextView cancel;
  private Button ok;
  private float currentPrice;
  private float addPrice = 10f;
  private AddPriceListener addPriceListener;

  public static AddRewardDialog newInstance(float currentPrice) {
    AddRewardDialog f = new AddRewardDialog();
    Bundle args = new Bundle();
    args.putFloat(CURRENT_PRICE, currentPrice);
    f.setArguments(args);
    return f;
  }

  public void setAddPriceListener(AddPriceListener addPriceListener) {
    this.addPriceListener = addPriceListener;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    currentPrice = getArguments().getFloat(CURRENT_PRICE);
  }

  @Override
  protected int getLayout() {
    return R.layout.dialog_add_price;
  }

  @Override
  protected void init(Bundle var1, View view) {
    price = (TextView) view.findViewById(R.id.price);
    Add_10 = (TextView) view.findViewById(R.id.add_10);
    Add_20 = (TextView) view.findViewById(R.id.add_20);
    Add_50 = (TextView) view.findViewById(R.id.add_50);
    cancel = (TextView) view.findViewById(R.id.cancel);
    ok = (Button) view.findViewById(R.id.ok);
    Add_10.setOnClickListener(this);
    Add_20.setOnClickListener(this);
    Add_50.setOnClickListener(this);
    cancel.setOnClickListener(this);
    ok.setOnClickListener(this);
    refreshUI();
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.add_10:
        addPrice = 10f;
        refreshUI();
        break;
      case R.id.add_20:
        addPrice = 20f;
        refreshUI();
        break;
      case R.id.add_50:
        addPrice = 50f;
        refreshUI();
        break;
      case R.id.cancel:
        dismiss();
        break;
      case R.id.ok:
        if (addPriceListener != null) {
          addPriceListener.addPrice(addPrice);
        }
        dismiss();
        break;
    }
  }

  private void refreshUI() {
    if (addPrice == 10f) {
      Add_10.setBackgroundResource(R.drawable.common_button_main);
      Add_20.setBackgroundResource(R.drawable.common_button_yellow);
      Add_50.setBackgroundResource(R.drawable.common_button_yellow);
    } else if (addPrice == 20f) {
      Add_10.setBackgroundResource(R.drawable.common_button_yellow);
      Add_20.setBackgroundResource(R.drawable.common_button_main);
      Add_50.setBackgroundResource(R.drawable.common_button_yellow);
    } else {
      Add_10.setBackgroundResource(R.drawable.common_button_yellow);
      Add_20.setBackgroundResource(R.drawable.common_button_yellow);
      Add_50.setBackgroundResource(R.drawable.common_button_main);
    }
    DecimalFormat df = new DecimalFormat("##.#");
    price.setText(df.format(currentPrice + addPrice));
  }


  @Override
  protected boolean onKeyBack() {
    return false;
  }

  @Override
  protected boolean getCanCancelOutSide() {
    return true;
  }

  @Override
  public void onDialogCreated(Dialog dialog) {

  }

  @Override
  public boolean isSheetStyle() {
    return true;
  }

  @Override
  public void onStart() {
    super.onStart();
    Display display = ((WindowManager) this.getActivity().getSystemService(Context.WINDOW_SERVICE))
        .getDefaultDisplay();
    WindowManager.LayoutParams lp = this.getDialog().getWindow().getAttributes();
    lp.width = display.getWidth(); // 设置宽度
    lp.gravity = Gravity.BOTTOM;
    lp.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE;
    getDialog().getWindow().setAttributes(lp);
  }

  public interface AddPriceListener {
    void addPrice(float addPrice);
  }

}
