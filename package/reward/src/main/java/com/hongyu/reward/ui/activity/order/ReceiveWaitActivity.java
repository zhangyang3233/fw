package com.hongyu.reward.ui.activity.order;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.hongyu.reward.R;
import com.hongyu.reward.appbase.BaseSingleFragmentActivity;
import com.hongyu.reward.ui.fragment.order.ReceiveWaitFragment;

/**
 * 领取人看到的界面(待付款状态)
 * Created by zhangyang131 on 16/10/9.
 */
public class ReceiveWaitActivity extends BaseSingleFragmentActivity {
    public static final String ORDER_ID = "order_id";

    public static void launch(Context context, String order_id) {
        Intent intent = new Intent(context, ReceiveWaitActivity.class);
        intent.putExtra(ORDER_ID, order_id);
        context.startActivity(intent);
    }

    @Override
    protected Class<? extends Fragment> getSingleContentFragmentClass() {
        return ReceiveWaitFragment.class;
    }

    @Override
    protected String getTitleText() {
        return getString(R.string.task_start);
    }


}
