package com.hongyu.reward.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.hongyu.reward.R;
import com.hongyu.reward.appbase.BaseSingleFragmentActivity;
import com.hongyu.reward.model.ShopListMode;
import com.hongyu.reward.ui.fragment.RewardPublishInfoFragment;

/**
 * Created by zhangyang131 on 16/9/12.
 */
public class RewardPublishInfoActivity extends BaseSingleFragmentActivity {

    public static void launch(Context context, ShopListMode.ShopInfo shopInfo){
        Intent i = new Intent(context, RewardPublishInfoActivity.class);
        i.putExtra(ShopListMode.ShopInfo.class.getSimpleName(), shopInfo);
        context.startActivity(i);
    }

    @Override
    protected Class<? extends Fragment> getSingleContentFragmentClass() {
        return RewardPublishInfoFragment.class;
    }

    @Override
    protected String getTitleText() {
        return getString(R.string.input_reward_info_title);
    }

    @Override
    public boolean getCanFlingBack() {
        return true;
    }
}
