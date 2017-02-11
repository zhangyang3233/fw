package com.hongyu.reward.ui.fragment.order;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hongyu.reward.R;
import com.hongyu.reward.appbase.BaseFragment;
import com.hongyu.reward.ui.activity.PreViewActivity;
import com.hongyu.reward.ui.activity.order.InputWaitNumActivity;
import com.hongyu.reward.widget.NetImageView;

import static com.hongyu.reward.R.id.address;

/**
 * Created by zhangyang131 on 2017/2/9.
 */

public class InputWaitNumFragment extends BaseFragment implements View.OnClickListener {
  private String order_id;
  private String shop_img;
  private String shop_address;
  private String shop_name;


  private NetImageView image;
  private TextView shop_name_tv;
  private TextView address_tv;
  private EditText pn; // 就餐人数
  private EditText waitPerson; // 等待人数
  private EditText waitNum; // 排位号
  private Button ok;


  @Override
  protected void onInflated(View contentView, Bundle savedInstanceState) {
    getData();
    initView();
  }

  private void initView() {
    image = (NetImageView) mContentView.findViewById(R.id.image);
    shop_name_tv = (TextView) mContentView.findViewById(R.id.shop_name);
    address_tv = (TextView) mContentView.findViewById(address);
    pn = (EditText) mContentView.findViewById(R.id.edit1);
    waitPerson = (EditText) mContentView.findViewById(R.id.edit2);
    waitNum = (EditText) mContentView.findViewById(R.id.edit3);
    ok = (Button) mContentView.findViewById(R.id.ok);
    ok.setOnClickListener(this);
    image.loadNetworkImageByUrl(shop_img);
    shop_name_tv.setText(shop_name);
    address_tv.setText(shop_address);
  }

  private void getData() {
    order_id = getArguments().getString(InputWaitNumActivity.ORDER_ID);
    shop_img = getArguments().getString(InputWaitNumActivity.IMAGE);
    shop_name = getArguments().getString(InputWaitNumActivity.SHOP_NAME);
    shop_address = getArguments().getString(InputWaitNumActivity.ADDRESS);
  }

  @Override
  protected int getLayoutResId() {
    return R.layout.input_wait_num_layout;
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.ok:
        if (checkInput()) {
          String jcrs = pn.getText().toString();
          String ddrs = waitPerson.getText().toString();
          String pwh = waitNum.getText().toString();
          PreViewActivity.launch(getActivity(), order_id, shop_name, shop_address,
              shop_img, jcrs, ddrs, pwh, null);
        }
        break;
    }
  }

  private boolean checkInput() {
    String jcrs = pn.getText().toString();
    String ddrs = waitPerson.getText().toString();
    String pwh = waitNum.getText().toString();
    if (TextUtils.isEmpty(jcrs)) {
      pn.setError("请输入就餐人数");
      return false;
    } else if (TextUtils.isEmpty(ddrs)) {
      waitPerson.setError("请输入等待人数");
      return false;
    } else if (TextUtils.isEmpty(pwh)) {
      waitNum.setError("请输入排位号");
      return false;
    }
    return true;
  }
}
