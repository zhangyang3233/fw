package com.hongyu.reward.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.hongyu.reward.R;
import com.hongyu.reward.appbase.BaseLoadFragment;

/**
 * Created by zhangyang131 on 16/10/17.
 */
public class WithdrawalFinishFragment extends BaseLoadFragment {
    Button ok;

    @Override
    protected void onStartLoading() {

    }

    @Override
    protected void onInflated(View contentView, Bundle savedInstanceState) {
        ok = (Button) mContentView.findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_withdraw_finish_layout;
    }
}
