package com.hongyu.reward.ui.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fw.zycoder.http.callback.DataCallback;
import com.hongyu.reward.R;
import com.hongyu.reward.appbase.BaseLoadFragment;
import com.hongyu.reward.http.ResponesUtil;
import com.hongyu.reward.model.PublishReqeustModel;
import com.hongyu.reward.model.ShopListMode;
import com.hongyu.reward.request.PublishRequestBuilder;
import com.hongyu.reward.ui.activity.RewardPublishWaitActivity;
import com.hongyu.reward.ui.dialog.CommonTwoBtnDialogFragment;
import com.hongyu.reward.ui.dialog.DialogFactory;
import com.hongyu.reward.utils.T;
import com.hongyu.reward.widget.NetImageView;

import java.io.Serializable;

/**
 * Created by zhangyang131 on 16/9/12.
 */
public class RewardPublishInfoFragment extends BaseLoadFragment implements View.OnClickListener {
  ShopListMode.ShopInfo shopInfo;
  NetImageView image;
  TextView shop_name;
  TextView reward_per;
  TextView reward_time;
  TextView km;
  TextView order_type;
  TextView diners_count;
  TextView reward_money;
  TextView diners_count_info; // 就餐人数
  TextView reward_money_info; // 悬赏金额
  TextView reward_type;
  View met_num;
  View price;
  Button publish;


  @Override
  protected void onStartLoading() {
    Bundle b = getArguments();
    Serializable serializable = b.getSerializable(ShopListMode.ShopInfo.class.getSimpleName());
    if (serializable != null) {
      shopInfo = (ShopListMode.ShopInfo) serializable;
    }
    initView();
  }

  private void initView() {
    if (shopInfo == null) {
      T.show(R.string.not_get_shop_info);
      getActivity().finish();
      return;
    }
    image.loadNetworkImageByUrl(shopInfo.getImg());
    shop_name.setText(shopInfo.getShop_name());
    reward_per.setText(getString(R.string.reward_count, shopInfo.getOrder_num()));
    reward_time.setText(getString(R.string.save_time, shopInfo.getSave_time()));
    km.setText(getString(R.string.shop_distance, shopInfo.getDistance()));
    reward_type.setText(getString(R.string.reward_type));
    order_type.setText(getString(R.string.reward_time_duration, String.valueOf(10)));
    diners_count.setText(getString(R.string.diners_count));
    reward_money.setText(getString(R.string.reward_money));
    met_num.setOnClickListener(this);
    price.setOnClickListener(this);
    publish.setOnClickListener(this);
  }

  @Override
  protected void onInflated(View contentView, Bundle savedInstanceState) {
    image = (NetImageView) mContentView.findViewById(R.id.image);
    shop_name = (TextView) mContentView.findViewById(R.id.shop_name);
    reward_per = (TextView) mContentView.findViewById(R.id.reward_per);
    reward_time = (TextView) mContentView.findViewById(R.id.reward_time);
    km = (TextView) mContentView.findViewById(R.id.km);
    reward_type = (TextView) mContentView.findViewById(R.id.reward_type);
    order_type = (TextView) mContentView.findViewById(R.id.order_type);
    met_num = mContentView.findViewById(R.id.met_num);
    diners_count = (TextView) met_num.findViewById(R.id.text_view);
    diners_count_info = (TextView) met_num.findViewById(R.id.info);
    price = mContentView.findViewById(R.id.price);
    reward_money = (TextView) price.findViewById(R.id.text_view);
    reward_money_info = (TextView) price.findViewById(R.id.info);
    publish = (Button) mContentView.findViewById(R.id.publish);
  }

  @Override
  protected int getLayoutResId() {
    return R.layout.activity_reward_publish_info_layout;
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.publish: // 发布悬赏
        if (checkInput()) {
          CommonTwoBtnDialogFragment dialog = new CommonTwoBtnDialogFragment();
          dialog.setContent(getString(R.string.public_sure));
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
                  publish();
                }
              });
          dialog.show(getChildFragmentManager(), getActivity().getClass().getSimpleName());
        }
        break;
      case R.id.met_num: // 选择就餐人数
        DialogFactory.showNumInputView(getActivity(),
            new DialogFactory.OnWhichBackStringListener() {
              @Override
              public void onConfirmClick(String[] content) {
                diners_count_info.setText(content[0]);
              }
            });
        break;
      case R.id.price: // 选择悬赏金额
        DialogFactory.showPriceInputView(getActivity(),
            new DialogFactory.OnWhichBackStringListener() {
              @Override
              public void onConfirmClick(String[] content) {
                reward_money_info.setText(content[0]);
              }
            });
        break;
    }
  }

  private boolean checkInput() {
    if (TextUtils.isEmpty(diners_count_info.getText())) {
      T.show(R.string.tip_please_diners_count);
      return false;
    }
    if (TextUtils.isEmpty(reward_money_info.getText())) {
      T.show(R.string.tip_please_reward_money);
      return false;
    }
    int diners_count = 0;
    try {
      diners_count = Integer.parseInt(diners_count_info.getText().toString());
    } catch (NumberFormatException e) {

    }
    if (diners_count <= 0) {
      T.show(R.string.tip_diners_count_not_be_0);
      return false;
    }
    int reward_money_count = 0;
    try {
      reward_money_count = Integer.parseInt(reward_money_info.getText().toString());
    } catch (NumberFormatException e) {

    }
    if (reward_money_count <= 0) {
      T.show(R.string.tip_reward_money_not_be_0);
      return false;
    }
    return true;
  }

  private void publish() {
    PublishRequestBuilder builder = new PublishRequestBuilder(shopInfo.getShop_name(),
        diners_count_info.getText().toString(), reward_money_info.getText().toString(),
        shopInfo.getLocation(), 0, shopInfo.getMapuid());
    builder.setDataCallback(new DataCallback<PublishReqeustModel>() {
      @Override
      public void onDataCallback(PublishReqeustModel data) {
        if(!isAdded()){
          return;
        }
        dismissLoadingView();
        if (ResponesUtil.checkModelCodeOK(data)) {// 发布成功
          RewardPublishWaitActivity.launch(getActivity(), data.getData().getOrder_id(),
              shopInfo.getShop_name(), shopInfo.getImg());
          T.show(R.string.public_success);
          getActivity().finish();
        } else {
          T.show(ResponesUtil.getErrorMsg(data));
        }
      }
    });
    showLoadingView();
    builder.build().submit();
  }

}
