package com.hongyu.reward.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.hongyu.reward.appbase.BaseSingleFragmentActivity;
import com.hongyu.reward.ui.fragment.SearchShopFragment;

/**
 * Created by zhangyang131 on 16/10/13.
 */
public class SearchShopActivity extends BaseSingleFragmentActivity {
    boolean isPublish;
    public static final String SHOP_NAME = "Shop_name";

    public static void launch(Context context, String shopName, boolean isPublish){
        Intent i = new Intent(context, SearchShopActivity.class);
        i.putExtra(SHOP_NAME, shopName);
        i.putExtra(SearchActivity.PUBLISH,isPublish);
        context.startActivity(i);
    }

    @Override
    protected Class<? extends Fragment> getSingleContentFragmentClass() {
        return SearchShopFragment.class;
    }

    @Override
    protected String getTitleText() {
        return getIntent().getExtras().getString(SHOP_NAME, "");
    }
}
