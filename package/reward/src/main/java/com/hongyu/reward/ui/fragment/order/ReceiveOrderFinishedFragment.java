package com.hongyu.reward.ui.fragment.order;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fw.zycoder.http.callback.DataCallback;
import com.hongyu.reward.R;
import com.hongyu.reward.appbase.BaseLoadFragment;
import com.hongyu.reward.http.ResponesUtil;
import com.hongyu.reward.model.BaseModel;
import com.hongyu.reward.model.OrderCommentModel;
import com.hongyu.reward.model.OrderInfoModel;
import com.hongyu.reward.model.OrderModel;
import com.hongyu.reward.request.CommentRequsetBuilder;
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

/**
 * Created by zhangyang131 on 16/10/3.
 */
public class ReceiveOrderFinishedFragment extends BaseLoadFragment
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
  private TextView tip_content;
  private View starCommentLayout;

  private TextView cost;
  private Button mBtnEval;
  int mSelectScore;
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
          refreshData(data.getData().getOrder());
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
            showStar(Integer.parseInt(data.getData().getScore()));
            mBtnEval.setText("分享");
            tip_bottom.setText("分享成功即可获得1000积分奖励，积分未来可抵现悬赏！");
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
    if (hasEval) { // 没有评论过
      setTitle("订单详情");
      tip_content.setText("您的匿名评价");
    }
  }

  private void refreshData(OrderModel order) {
    if (order != null) {
      cost.setText("￥" + order.getPrice());
      header_icon.loadNetworkImageByUrl(order.getHead_img(), R.mipmap.defalut_head_img);
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
      name.setText("悬赏人：" + order.getNickname());
      gcr.setText("好评率：" + order.getGcr());
      order_num.setText("成交: " + order.getOrder_num() + "单");
      score.setData(order.getGcr(), false);
    }

  }



  @Override
  protected void onInflated(View contentView, Bundle savedInstanceState) {
    initView();
  }

  private void initView() {
    person_detail_layout = mContentView.findViewById(R.id.person_detail_layout);
    order_info_layout = mContentView.findViewById(R.id.order_info_layout);
    order_info_layout.setVisibility(View.GONE);
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
    tip_content = (TextView) mContentView.findViewById(R.id.tip_content);
    tip_content.setText("请给悬赏人打分");
    starCommentLayout = mContentView.findViewById(R.id.comment_layout);
    mIvStar_1 = (ImageView) starCommentLayout.findViewById(R.id.star1);
    mIvStar_2 = (ImageView) starCommentLayout.findViewById(R.id.star2);
    mIvStar_3 = (ImageView) starCommentLayout.findViewById(R.id.star3);
    mIvStar_4 = (ImageView) starCommentLayout.findViewById(R.id.star4);
    mIvStar_5 = (ImageView) starCommentLayout.findViewById(R.id.star5);
    mBtnEval = (Button) mContentView.findViewById(R.id.evaluate);
    ViewStub stub = (ViewStub) mContentView.findViewById(R.id.money_title_stub);
    cost = (TextView) stub.inflate().findViewById(R.id.cost);

    mBtnEval.setText("评价");
    initClickListener();
    showStar(0);
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
          showShareDialog();
        }
        break;
    }
  }

  private void showShareDialog() {
    DialogFactory.showShareView(getActivity(), new DialogFactory.OnWhichListener() {
      @Override
      public void onConfirmClick(int which) {
        share(which);
      }
    });
  }

  private void share(int which) {
    if (which == 1) {// 分享到微信
      WXEntryActivity.receiveShare(api, shop_name.getText().toString(), 1, order_id);
    } else if (which == 2) { // 分享到朋友圈
      WXEntryActivity.receiveShare(api, shop_name.getText().toString(), 2, order_id);
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
  }


  private void evaluate() {
    String score = mTvStar.getText().toString().trim();
    if(score.equals("0")){
      T.show("请给悬赏人评价后再提交");
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
          T.show("打分成功");
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
