package com.fw.zycoder.demos.demos.guagua;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.fw.zycoder.appbase.fragment.AsyncLoadFragment;
import com.fw.zycoder.demos.R;

/**
 * Created by zhangyang131 on 16/7/18.
 */
public class ScartchFragment extends AsyncLoadFragment {

    @Override
    protected void onStartLoading() {

    }

    @Override
    protected void onInflated(View contentView, Bundle savedInstanceState) {
        initView();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_scartchout;
    }

    private void initView() {
        btn = (Button) mContentView.findViewById(R.id.btn);
        mScratchOutView = (ScratchOutView) mContentView.findViewById(R.id.scratch_out_view);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mScratchOutView.resetView();
            }
        });
    }

    Button btn;
    ScratchOutView mScratchOutView;
}
