package com.hongyu.reward.ui.fragment.order;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fw.zycoder.http.callback.DataCallback;
import com.hongyu.reward.R;
import com.hongyu.reward.appbase.BaseLoadFragment;
import com.hongyu.reward.model.OrderInfoModel;
import com.hongyu.reward.model.OrderModel;
import com.hongyu.reward.request.GetOrderInfoRequestBuilder;
import com.hongyu.reward.ui.activity.order.OrderFinishActivity;
import com.hongyu.reward.ui.dialog.DialogFactory;
import com.hongyu.reward.utils.T;
import com.hongyu.reward.widget.FiveStarSingle;
import com.hongyu.reward.widget.RoundImageView;

/**
 * Created by zhangyang131 on 16/10/3.
 */
public class OrderFinishFragment extends BaseLoadFragment implements View.OnClickListener {
    private String order_id;
    private ImageView mIvStar_5;
    private ImageView mIvStar_4;
    private ImageView mIvStar_3;
    private ImageView mIvStar_2;
    private ImageView mIvStar_1;
    private TextView mTvStar;
    private RoundImageView mRvHeader;
    private TextView mTvName;
    private TextView mTvGcr;
    private TextView mTvOrderNum;
    private FiveStarSingle mScore;
    private TextView mTvShopName;
    private TextView mTvOrderType;
    private TextView mTvTime;
    private TextView mTvContent;
    private View mBtnShare;


    @Override
    protected void onStartLoading() {
        GetOrderInfoRequestBuilder builder = new GetOrderInfoRequestBuilder(order_id);
        builder.setDataCallback(new DataCallback<OrderInfoModel>() {
            @Override
            public void onDataCallback(OrderInfoModel order) {
                refreshData(order.getData().getOrder());
            }
        });
        builder.build().submit();
    }

    private void refreshData(OrderModel order) {
        mTvGcr.setText("好评率:" + order.getGcr());
        mTvName.setText(order.getNickname());
        mTvShopName.setText(order.getShop_name());
        mTvTime.setText(order.getmTvTime());
        mTvStar.setText(order.getGood());
        mTvOrderNum.setText("成交:" + order.getOrder_num() + "单");
        String good = TextUtils.isEmpty(order.getGood()) ? "0" : order.getGood();
        mScore.setData(Float.parseFloat(good), false);

        showStar(Integer.parseInt(good));

        if (order.getType() == 0) {
            mTvOrderType.setText("即时");
        } else {
            mTvOrderType.setText("预约");
        }
    }

    @Override
    protected void onInflated(View contentView, Bundle savedInstanceState) {
        initView();
        initData();
    }

    private void initData() {
        order_id = getArguments().getString(OrderFinishActivity.ORDER_ID);
    }

    private void initView() {
        mRvHeader = (RoundImageView) mContentView.findViewById(R.id.header_icon);
        mTvName = (TextView) mContentView.findViewById(R.id.name);
        mTvShopName = (TextView) mContentView.findViewById(R.id.shop_name);
        mTvOrderType = (TextView) mContentView.findViewById(R.id.order_type);
        mTvTime = (TextView) mContentView.findViewById(R.id.time);
        mTvContent = (TextView) mContentView.findViewById(R.id.content);
        mTvGcr = (TextView) mContentView.findViewById(R.id.gcr);
        mTvOrderNum = (TextView) mContentView.findViewById(R.id.order_num);
        mScore = (FiveStarSingle) mContentView.findViewById(R.id.my_score);

        mTvStar = (TextView) mContentView.findViewById(R.id.o_star);
        mIvStar_1 = (ImageView) mContentView.findViewById(R.id.o_star1);
        mIvStar_2 = (ImageView) mContentView.findViewById(R.id.o_star2);
        mIvStar_3 = (ImageView) mContentView.findViewById(R.id.o_star3);
        mIvStar_4 = (ImageView) mContentView.findViewById(R.id.o_star4);
        mIvStar_5 = (ImageView) mContentView.findViewById(R.id.o_star5);

        mBtnShare = mContentView.findViewById(R.id.share);

        mBtnShare.setOnClickListener(this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_order_finish_layout;
    }

    @Override
    public void onClick(View v) {
        DialogFactory.showShareView(getActivity(), new DialogFactory.OnWhichListener() {

            @Override
            public void onConfirmClick(int which) {
                share(which);
            }
        });
    }

    private void share(int which) {
        if (which == 1) {
            T.show("分享到微信");
        } else if (which == 2) {
            T.show("分享到朋友圈");
        } else if (which == 3) {
            T.show("分享到QQ");
        }
    }

    private void showStar(int star_num) {
        switch (star_num) {
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
            case 0:
                mIvStar_1.setImageResource(R.mipmap.icon_star_gary_h);
                mIvStar_2.setImageResource(R.mipmap.icon_star_gary_h);
                mIvStar_3.setImageResource(R.mipmap.icon_star_gary_h);
                mIvStar_4.setImageResource(R.mipmap.icon_star_gary_h);
                mIvStar_5.setImageResource(R.mipmap.icon_star_gary_h);
                break;
        }

        mTvStar.setText(String.valueOf(star_num));
    }
}
