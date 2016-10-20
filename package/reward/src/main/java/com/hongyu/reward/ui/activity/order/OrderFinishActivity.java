package com.hongyu.reward.ui.activity.order;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.hongyu.reward.appbase.BaseSingleFragmentActivity;
import com.hongyu.reward.manager.AccountManager;
import com.hongyu.reward.ui.fragment.order.OrderFinishFragment;

/**
 * Created by zhangyang131 on 16/10/3.
 */
public class OrderFinishActivity extends BaseSingleFragmentActivity {
    public static final String ORDER_ID = "order_id";

    public static void launch(Context context, String orderId){
        Intent i = new Intent(context, OrderFinishActivity.class);
        i.putExtra(ORDER_ID, orderId);
        AccountManager.launchAfterLogin(context, i);
    }

    @Override
    protected Class<? extends Fragment> getSingleContentFragmentClass() {
        return OrderFinishFragment.class;
    }

    @Override
    protected String getTitleText() {
        return "订单详情";
    }
}
