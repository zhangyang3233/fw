package com.hongyu.reward.ui.fragment.order;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.fw.zycoder.http.callback.DataCallback;
import com.hongyu.reward.R;
import com.hongyu.reward.appbase.BaseLoadFragment;
import com.hongyu.reward.http.ResponesUtil;
import com.hongyu.reward.model.BaseModel;
import com.hongyu.reward.model.OrderInfoModel;
import com.hongyu.reward.model.OrderModel;
import com.hongyu.reward.model.ReceiveModel;
import com.hongyu.reward.request.CommentRequsetBuilder;
import com.hongyu.reward.request.GetOrderInfoRequestBuilder;
import com.hongyu.reward.ui.activity.order.CommentActivity;
import com.hongyu.reward.ui.dialog.DialogFactory;
import com.hongyu.reward.utils.StatusUtil;
import com.hongyu.reward.utils.T;
import com.hongyu.reward.widget.CommentTextView;
import com.hongyu.reward.widget.FiveStarSingle;
import com.hongyu.reward.widget.RoundImageView;

/**
 * Created by zhangyang131 on 16/10/3.
 */
public class CommentFragment extends BaseLoadFragment implements View.OnClickListener {
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


    private ImageView mIvStar_5;
    private ImageView mIvStar_4;
    private ImageView mIvStar_3;
    private ImageView mIvStar_2;
    private ImageView mIvStar_1;
    private TextView mTvStar;
    private CommentTextView mCtvTag_1;
    private CommentTextView mCtvTag_2;
    private CommentTextView mCtvTag_3;
    private CommentTextView mCtvTag_4;
    private EditText mEtContent;
    private Button mBtnEval;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        order_id = getArguments().getString(CommentActivity.ORDER_ID);
    }

    @Override
    protected void onStartLoading() {
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
    }

    private void refreshData(OrderModel order, ReceiveModel receive) {
        if(order != null){
            shop_name.setText(order.getShop_name());
            time.setText(order.getDate());
            status.setText(StatusUtil.getMsgByStatus(order.getStatus()));
            status.setTextColor(StatusUtil.getColorByStatus(order.getStatus()));
            price.setText("￥ "+order.getPrice());
            if (order.getType() == 0) {
                btn_appointment.setText("即时");
            } else {
                btn_appointment.setText("预约");
            }
        }

        if(receive != null){
//            header_icon.loadNetworkImageByUrl(receive.getHeadImg());
            name.setText(receive.getNickname());
            gcr.setText(receive.getGcr());
            order_num.setText(receive.getOrder_num() +"单");
            score.setData(receive.getScore(), true);
        }

    }


    @Override
    protected void onInflated(View contentView, Bundle savedInstanceState) {
        initView();
    }

    private void initView() {
        person_detail_layout = mContentView.findViewById(R.id.person_detail_layout);
        order_info_layout = mContentView.findViewById(R.id.order_info_layout);

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
        mIvStar_1 = (ImageView) mContentView.findViewById(R.id.star1);
        mIvStar_2 = (ImageView) mContentView.findViewById(R.id.star2);
        mIvStar_3 = (ImageView) mContentView.findViewById(R.id.star3);
        mIvStar_4 = (ImageView) mContentView.findViewById(R.id.star4);
        mIvStar_5 = (ImageView) mContentView.findViewById(R.id.star5);

        mCtvTag_1 = (CommentTextView) mContentView.findViewById(R.id.tag1);
        mCtvTag_2 = (CommentTextView) mContentView.findViewById(R.id.tag2);
        mCtvTag_3 = (CommentTextView) mContentView.findViewById(R.id.tag3);
        mCtvTag_4 = (CommentTextView) mContentView.findViewById(R.id.tag4);

        mEtContent = (EditText) mContentView.findViewById(R.id.content);
        mBtnEval = (Button) mContentView.findViewById(R.id.evaluate);
        initClickListener();
    }

    private void initClickListener() {
        mBtnEval.setOnClickListener(this);
        mIvStar_1.setOnClickListener(this);
        mIvStar_2.setOnClickListener(this);
        mIvStar_3.setOnClickListener(this);
        mIvStar_4.setOnClickListener(this);
        mIvStar_5.setOnClickListener(this);
        mCtvTag_1.setOnClickListener(this);
        mCtvTag_2.setOnClickListener(this);
        mCtvTag_3.setOnClickListener(this);
        mCtvTag_4.setOnClickListener(this);
        mEtContent.setOnClickListener(this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_reward_finish_layout;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.star1:
                showStar(1);
                break;
            case R.id.star2:
                showStar(2);
                break;
            case R.id.star3:
                showStar(3);
                break;
            case R.id.star4:
                showStar(4);
                break;
            case R.id.star5:
                showStar(5);
                break;
            case R.id.tag1:
                mCtvTag_1.select();
                break;
            case R.id.tag2:
                mCtvTag_2.select();
                break;
            case R.id.tag3:
                mCtvTag_3.select();
                break;
            case R.id.tag4:
                mCtvTag_4.select();
                break;
            case R.id.evaluate:
                if(!hasEval){
                    evaluate();
                }else{
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
        }

        mTvStar.setText(String.valueOf(star_num));
    }


    private void evaluate() {
        String tagstr = "";
        if (mCtvTag_1.isSelect()) tagstr += mCtvTag_1.getComment();
        if (mCtvTag_2.isSelect()) tagstr += "," + mCtvTag_2.getComment();
        if (mCtvTag_3.isSelect()) tagstr += "," + mCtvTag_3.getComment();
        if (mCtvTag_4.isSelect()) tagstr += "," + mCtvTag_4.getComment();

        String score = mTvStar.getText().toString().trim();
        String content = mEtContent.getText().toString().trim();
        CommentRequsetBuilder builder = new CommentRequsetBuilder(order_id, score, tagstr, content);
        builder.setDataCallback(new DataCallback<BaseModel>() {
            @Override
            public void onDataCallback(BaseModel data) {
                if(!isAdded()){
                    return;
                }
                if(ResponesUtil.checkModelCodeOK(data)){
                    T.show("评论成功");
                    hasEval();
                }else{
                    T.show(ResponesUtil.getErrorMsg(data));
                }

            }
        });
        builder.build().submit();
    }

    private void hasEval() {
        hasEval = true;
        mBtnEval.setText("分享");
    }
}
