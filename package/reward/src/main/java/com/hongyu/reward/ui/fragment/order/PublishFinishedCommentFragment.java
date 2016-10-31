package com.hongyu.reward.ui.fragment.order;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fw.zycoder.http.callback.DataCallback;
import com.hongyu.reward.R;
import com.hongyu.reward.appbase.BaseLoadFragment;
import com.hongyu.reward.config.Constants;
import com.hongyu.reward.http.ResponesUtil;
import com.hongyu.reward.model.BaseModel;
import com.hongyu.reward.model.OrderCommentModel;
import com.hongyu.reward.model.OrderInfoModel;
import com.hongyu.reward.model.OrderModel;
import com.hongyu.reward.model.ReceiveModel;
import com.hongyu.reward.request.CommentRequsetBuilder;
import com.hongyu.reward.request.GetCommentTagRequestBuilder;
import com.hongyu.reward.request.GetOrderCommentRequestBuider;
import com.hongyu.reward.request.GetOrderInfoRequestBuilder;
import com.hongyu.reward.ui.activity.order.PublishFinishedCommentActivity;
import com.hongyu.reward.ui.dialog.DialogFactory;
import com.hongyu.reward.utils.StatusUtil;
import com.hongyu.reward.utils.T;
import com.hongyu.reward.widget.FiveStarSingle;
import com.hongyu.reward.widget.RoundImageView;
import com.hongyu.reward.wxapi.WXEntryActivity;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by zhangyang131 on 16/10/3.
 */
public class PublishFinishedCommentFragment extends BaseLoadFragment
    implements
      View.OnClickListener {
  private boolean hasEval;
  private String order_id;
  private View person_detail_layout;
  private View order_info_layout;

  private RoundImageView header_icon;
  private TextView name;
  private TextView gcr;
  private TextView order_num;
  private FiveStarSingle score;


  private TextView shop_name;
  private TextView btn_appointment;
  private TextView time;
  private TextView status;
  private TextView price;
  private TextView tip_bottom;


  private ImageView mIvStar_5;
  private ImageView mIvStar_4;
  private ImageView mIvStar_3;
  private ImageView mIvStar_2;
  private ImageView mIvStar_1;
  private TextView mTvStar;
  private View starCommentLayout;

  private Button mBtnEval;
  private TextView tip_content;
  int mSelectScore;
  ArrayList<String> tagsStr;
  IWXAPI api;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    order_id = getArguments().getString(PublishFinishedCommentActivity.ORDER_ID);
    api = WXEntryActivity.registWX(getActivity());
  }

  @Override
  protected void onStartLoading() {
    showLoadingView();
    GetOrderInfoRequestBuilder builder = new GetOrderInfoRequestBuilder(order_id);
    builder.setDataCallback(new DataCallback<OrderInfoModel>() {
      @Override
      public void onDataCallback(OrderInfoModel data) {
        if (!isAdded()) {
          return;
        }
        if (ResponesUtil.checkModelCodeOK(data)) {
          refreshData(data.getData().getOrder(), data.getData().getReceive());
        } else {
          T.show(ResponesUtil.getErrorMsg(data));
        }

      }
    });
    builder.build().submit();

    GetOrderCommentRequestBuider buider2 = new GetOrderCommentRequestBuider(order_id);
    buider2.setDataCallback(new DataCallback<OrderCommentModel>() {
      @Override
      public void onDataCallback(OrderCommentModel data) {
        if (!isAdded()) {
          return;
        }
        dismissLoadingView();
        if (ResponesUtil.checkModelCodeOK(data)) {
          if (data.getData() != null && !TextUtils.isEmpty(data.getData().getScore())) { // 评论过
            hasEval = true;
            tagsStr = data.getData().getTag();
            showStar(Integer.parseInt(data.getData().getScore()));
            mBtnEval.setText("分享");
            tip_bottom.setText("分享成功即可获得1000积分奖励，积分未来可抵现悬赏！");
          } else {
            showEval();
          }
        } else {
          T.show(ResponesUtil.getErrorMsg(data));
        }
      }
    });
    buider2.build().submit();


  }

  private void showEval() {
    showLoadingView();
    GetCommentTagRequestBuilder builder1 = new GetCommentTagRequestBuilder();
    builder1.setDataCallback(new DataCallback<JSONObject>() {
      @Override
      public void onDataCallback(JSONObject data) {
        if (!isAdded()) {
          return;
        }
        dismissLoadingView();
        if (ResponesUtil.checkModelCodeOK(data)) {
//          JSONObject evaluate = data.optJSONObject("data");
//          tags = CommentTagModel.parse(evaluate);
          resetTitle();
        } else {
          T.show(ResponesUtil.getErrorMsg(data));
        }
      }
    });
    builder1.build().submit();
  }

  private void refreshData(OrderModel order, ReceiveModel receive) {
    if (order != null) {
      shop_name.setText(order.getShop_name());
      time.setText(order.getDate());
      status.setText(StatusUtil.getMsgByStatus(order.getStatus()));
      status.setTextColor(StatusUtil.getColorByStatus(order.getStatus()));
      price.setText("￥ " + order.getPrice());
      if (order.getType() == 0) {
        btn_appointment.setText("即时");
      } else {
        btn_appointment.setText("预约");
      }
    }

    if (receive != null) {
      // header_icon.loadNetworkImageByUrl(receive.getHeadImg());
      header_icon.loadNetworkImageByUrl(receive.getImg(), R.mipmap.defalut_head_img);
      name.setText("领赏人："+receive.getNickname());
      gcr.setText("好评率："+receive.getGcr());
      order_num.setText("成交：" + receive.getOrder_num() + "单");
      score.setData(receive.getGcr(), false);
    }
  }

  private void resetTitle() {
    if (hasEval) { // 没有评论过
      setTitle("订单详情");
      tip_content.setText("您的匿名评价");
    }
  }


  @Override
  protected void onInflated(View contentView, Bundle savedInstanceState) {
    initView();
  }

  private void initView() {
    person_detail_layout = mContentView.findViewById(R.id.person_detail_layout);
    order_info_layout = mContentView.findViewById(R.id.order_info_layout);
    tip_bottom = (TextView) mContentView.findViewById(R.id.tip_bottom);

    header_icon = (RoundImageView) person_detail_layout.findViewById(R.id.header_icon);
    name = (TextView) person_detail_layout.findViewById(R.id.name);
    gcr = (TextView) person_detail_layout.findViewById(R.id.gcr);
    order_num = (TextView) person_detail_layout.findViewById(R.id.order_num);
    score = (FiveStarSingle) person_detail_layout.findViewById(R.id.my_score);


    shop_name = (TextView) order_info_layout.findViewById(R.id.shop_name);
    btn_appointment = (TextView) order_info_layout.findViewById(R.id.btn_appointment);
    time = (TextView) order_info_layout.findViewById(R.id.time);
    status = (TextView) order_info_layout.findViewById(R.id.status);
    price = (TextView) order_info_layout.findViewById(R.id.price);


    mTvStar = (TextView) mContentView.findViewById(R.id.star);
    starCommentLayout = mContentView.findViewById(R.id.comment_layout);
    mIvStar_1 = (ImageView) starCommentLayout.findViewById(R.id.star1);
    mIvStar_2 = (ImageView) starCommentLayout.findViewById(R.id.star2);
    mIvStar_3 = (ImageView) starCommentLayout.findViewById(R.id.star3);
    mIvStar_4 = (ImageView) starCommentLayout.findViewById(R.id.star4);
    mIvStar_5 = (ImageView) starCommentLayout.findViewById(R.id.star5);
    mBtnEval = (Button) mContentView.findViewById(R.id.evaluate);
    tip_content = (TextView) mContentView.findViewById(R.id.tip_content);
    initClickListener();
    showStar(5);
  }

  private void initClickListener() {
    mBtnEval.setOnClickListener(this);
    mIvStar_1.setOnClickListener(this);
    mIvStar_2.setOnClickListener(this);
    mIvStar_3.setOnClickListener(this);
    mIvStar_4.setOnClickListener(this);
    mIvStar_5.setOnClickListener(this);
  }

  @Override
  protected int getLayoutResId() {
    return R.layout.activity_reward_finish_layout;
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.star1:
        if (hasEval) return;
        showStar(1);
        break;
      case R.id.star2:
        if (hasEval) return;
        showStar(2);
        break;
      case R.id.star3:
        if (hasEval) return;
        showStar(3);
        break;
      case R.id.star4:
        if (hasEval) return;
        showStar(4);
        break;
      case R.id.star5:
        if (hasEval) return;
        showStar(5);
        break;
      case R.id.evaluate:
        if (!hasEval) {
          evaluate();
        } else {
          showShareDialog1();
        }
        break;
    }
  }

  private void showShareDialog1() {
    if(!isWXAppInstalledAndSupported()){
      T.show("微信不可用，请检查微信是否安装。");
      return;
    }
    DialogFactory.showShareInputView(getActivity(), new DialogFactory.OnWhichBackStringListener() {
      @Override
      public void onConfirmClick(String[] content) {
        showShareDialog2(content[0], content[1],  order_id);
      }
    });

  }

  private boolean isWXAppInstalledAndSupported() {
    IWXAPI msgApi = WXAPIFactory.createWXAPI(getActivity(), Constants.WX.AppID);
    msgApi.registerApp(Constants.WX.AppID);
    boolean sIsWXAppInstalledAndSupported = msgApi.isWXAppInstalled()
            && msgApi.isWXAppSupportAPI();
    return sIsWXAppInstalledAndSupported;
  }

  private void showShareDialog2(final String text1, final String text2, final String order_id) {
    DialogFactory.showShareView(getActivity(), new DialogFactory.OnWhichListener() {
      @Override
      public void onConfirmClick(int which) {
        share(which, text1, text2, order_id);
      }
    });
  }

  private void share(int which, String text1, String text2, final String order_id) {
    if (which == 1) { // 分享到微信
      WXEntryActivity.publishShare(api, order_id, text1,
              text2, 1, order_id);
    } else if (which == 2) { // 分享到朋友圈
      WXEntryActivity.publishShare(api, order_id, text1,
              text2, 2, order_id);
    }
  }

  private void showStar(int star_num) {
    mSelectScore = star_num;
    switch (mSelectScore) {
      case 0:
        mIvStar_1.setImageResource(R.mipmap.icon_star_gary_h);
        mIvStar_2.setImageResource(R.mipmap.icon_star_gary_h);
        mIvStar_3.setImageResource(R.mipmap.icon_star_gary_h);
        mIvStar_4.setImageResource(R.mipmap.icon_star_gary_h);
        mIvStar_5.setImageResource(R.mipmap.icon_star_gary_h);
        break;
      case 1:
        mIvStar_1.setImageResource(R.mipmap.icon_star_orange_h);
        mIvStar_2.setImageResource(R.mipmap.icon_star_gary_h);
        mIvStar_3.setImageResource(R.mipmap.icon_star_gary_h);
        mIvStar_4.setImageResource(R.mipmap.icon_star_gary_h);
        mIvStar_5.setImageResource(R.mipmap.icon_star_gary_h);
        break;
      case 2:
        mIvStar_1.setImageResource(R.mipmap.icon_star_orange_h);
        mIvStar_2.setImageResource(R.mipmap.icon_star_orange_h);
        mIvStar_3.setImageResource(R.mipmap.icon_star_gary_h);
        mIvStar_4.setImageResource(R.mipmap.icon_star_gary_h);
        mIvStar_5.setImageResource(R.mipmap.icon_star_gary_h);
        break;
      case 3:
        mIvStar_1.setImageResource(R.mipmap.icon_star_orange_h);
        mIvStar_2.setImageResource(R.mipmap.icon_star_orange_h);
        mIvStar_3.setImageResource(R.mipmap.icon_star_orange_h);
        mIvStar_4.setImageResource(R.mipmap.icon_star_gary_h);
        mIvStar_5.setImageResource(R.mipmap.icon_star_gary_h);
        break;
      case 4:
        mIvStar_1.setImageResource(R.mipmap.icon_star_orange_h);
        mIvStar_2.setImageResource(R.mipmap.icon_star_orange_h);
        mIvStar_3.setImageResource(R.mipmap.icon_star_orange_h);
        mIvStar_4.setImageResource(R.mipmap.icon_star_orange_h);
        mIvStar_5.setImageResource(R.mipmap.icon_star_gary_h);
        break;
      case 5:
        mIvStar_1.setImageResource(R.mipmap.icon_star_orange_h);
        mIvStar_2.setImageResource(R.mipmap.icon_star_orange_h);
        mIvStar_3.setImageResource(R.mipmap.icon_star_orange_h);
        mIvStar_4.setImageResource(R.mipmap.icon_star_orange_h);
        mIvStar_5.setImageResource(R.mipmap.icon_star_orange_h);
        break;
    }
    mTvStar.setText(String.valueOf(mSelectScore));
    resetTitle();
  }


  private void evaluate() {
    String score = mTvStar.getText().toString().trim();
    if(score.equals("0")){
      T.show("请给领赏人评价后再提交");
      return;
    }
    CommentRequsetBuilder builder =
        new CommentRequsetBuilder(order_id, score);
    builder.setDataCallback(new DataCallback<BaseModel>() {
      @Override
      public void onDataCallback(BaseModel data) {
        if (!isAdded()) {
          return;
        }
        if (ResponesUtil.checkModelCodeOK(data)) {
          T.show("评价成功");
          requestLoad();
        } else {
          T.show(ResponesUtil.getErrorMsg(data));
        }
      }
    });
    builder.build().submit();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    api = null;
  }

}
