package com.fw.zycoder.demos.demos.guagua;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.fw.zycoder.demos.R;
import com.fw.zycoder.demos.base.DemoActivity;

/**
 * Created by zhangyang on 16/7/14.
 */
public class ScartchOutActivity extends DemoActivity {
    Button btn;
    ScratchOutView mScratchOutView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scartchout);
        initView();
    }

    private void initView() {
        btn = (Button) findViewById(R.id.btn);
        mScratchOutView = (ScratchOutView) findViewById(R.id.scratch_out_view);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mScratchOutView.resetView();
            }
        });
    }
}
