package com.hongyu.reward.ui.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fw.zycoder.http.callback.DataCallback;
import com.hongyu.reward.R;
import com.hongyu.reward.appbase.BaseLoadFragment;
import com.hongyu.reward.http.ResponesUtil;
import com.hongyu.reward.model.BaseModel;
import com.hongyu.reward.model.NoticeEvent;
import com.hongyu.reward.request.ReceiveOrderRequestBuilder;
import com.hongyu.reward.ui.activity.PreViewActivity;
import com.hongyu.reward.ui.activity.order.InputWaitNumActivity;
import com.hongyu.reward.ui.asynctask.DealImgBase64AsyncTask;
import com.hongyu.reward.utils.ImageFactory;
import com.hongyu.reward.utils.T;
import com.hongyu.reward.widget.NetImageView;

import org.greenrobot.eventbus.EventBus;

import static com.hongyu.reward.R.id.ddzs;

/**
 * Created by zhangyang131 on 2017/2/10.
 */

public class PreViewFragment extends BaseLoadFragment implements View.OnClickListener {
    private String order_id;
    private String shop_name;
    private String shop_address;
    private String shop_img;
    private String jcrs;
    private String ddrs;
    private String pwh;
    private String photopath;

    TextView shop_name_tv;
    TextView address_tv;
    NetImageView image;
    TextView jcrs_tv;
    TextView ddzs_tv;
    TextView pwh_tv;
    Button send;
    View write_layout;
    View photo_layout;
    ImageView photo_img;

    @Override
    protected void onInflated(View contentView, Bundle savedInstanceState) {
        getData();
        initView();
    }

    private void getData() {
        order_id = getArguments().getString(PreViewActivity.ORDER_ID);
        shop_name = getArguments().getString(PreViewActivity.SHOP_NAME);
        shop_address = getArguments().getString(PreViewActivity.SHOP_ADDRESS);
        shop_img = getArguments().getString(PreViewActivity.SHOP_IMG);
        jcrs = getArguments().getString(PreViewActivity.JCRS);
        ddrs = getArguments().getString(PreViewActivity.DDRS);
        pwh = getArguments().getString(PreViewActivity.PWH);
        photopath = getArguments().getString(PreViewActivity.PHOTOPATH);
    }

    private void initView() {
        shop_name_tv = (TextView) mContentView.findViewById(R.id.shop_name);
        address_tv = (TextView) mContentView.findViewById(R.id.address);
        image = (NetImageView) mContentView.findViewById(R.id.image);
        jcrs_tv = (TextView) mContentView.findViewById(R.id.jcrs);
        ddzs_tv = (TextView) mContentView.findViewById(ddzs);
        pwh_tv = (TextView) mContentView.findViewById(R.id.pwh);
        send = (Button) mContentView.findViewById(R.id.send);
        write_layout = mContentView.findViewById(R.id.write_layout);
        photo_layout = mContentView.findViewById(R.id.photo_layout);
        photo_img = (ImageView) mContentView.findViewById(R.id.photo_img);

        shop_name_tv.setText(shop_name);
        address_tv.setText(shop_address);
        image.loadNetworkImageByUrl(shop_img);

        if (TextUtils.isEmpty(photopath)) {
            write_layout.setVisibility(View.VISIBLE);
            photo_layout.setVisibility(View.GONE);
            jcrs_tv.setText("就餐人数：" + jcrs);
            ddzs_tv.setText("您前面还有：" + ddrs + "桌等待");
            pwh_tv.setText(pwh);
        } else {
            write_layout.setVisibility(View.GONE);
            photo_layout.setVisibility(View.VISIBLE);
            Bitmap bm = ImageFactory.getimage(photopath);
            photo_img.setImageBitmap(bm);
        }
        send.setOnClickListener(this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.preview_layout;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send:
                send();
                break;
        }

    }

    private void send() {
        if (!TextUtils.isEmpty(photopath)) { // 图片
            new DealImgBase64AsyncTask(new DealImgBase64AsyncTask.GetPicBase64Callback() {
                @Override
                public void onPre() {
                    showLoadingView();
                }

                @Override
                public void getPicBase64(String imageBase64) {
                    if (imageBase64 != null) {
                        // 上传
                        ReceiveOrderRequestBuilder builder =
                                new ReceiveOrderRequestBuilder(order_id, imageBase64);
                        builder.setDataCallback(new DataCallback<BaseModel>() {
                            @Override
                            public void onDataCallback(BaseModel data) {
                                if (!isAdded()) {
                                    return;
                                }
                                dismissLoadingView();
                                if (ResponesUtil.checkModelCodeOK(data)) {
                                    T.show("领取成功");
                                    EventBus.getDefault().post(new NoticeEvent(NoticeEvent.ORDER_STATUS_CHANGED));
                                    EventBus.getDefault().post(new NoticeEvent(NoticeEvent.RECEIVE_REQUEST_SUCCESS));
                                    getActivity().finish();
                                } else {
                                    T.show(ResponesUtil.getErrorMsg(data));
                                }
                            }
                        });
                        builder.build().submit();
                    }
                }

                @Override
                public void onError() {
                    dismissLoadingView();
                    T.show("未知错误");
                }
            }).execute(photopath);
        } else { // 手填
            showLoadingView();
            ReceiveOrderRequestBuilder builder =
                    new ReceiveOrderRequestBuilder(order_id, ddrs, pwh,jcrs);
            builder.setDataCallback(new DataCallback<BaseModel>() {
                @Override
                public void onDataCallback(BaseModel data) {
                    if (!isAdded()) {
                        return;
                    }
                    dismissLoadingView();
                    if (ResponesUtil.checkModelCodeOK(data)) {
                        T.show("领取成功");
                        EventBus.getDefault().post(new NoticeEvent(NoticeEvent.ORDER_STATUS_CHANGED));
                        EventBus.getDefault().post(new NoticeEvent(NoticeEvent.RECEIVE_REQUEST_SUCCESS));
                        getActivity().finish();
                        InputWaitNumActivity.finishIfNot();
                    } else {
                        T.show(ResponesUtil.getErrorMsg(data));
                    }
                }
            });
            builder.build().submit();
        }
    }

    @Override
    protected void onStartLoading() {

    }
}
