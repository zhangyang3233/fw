package com.hongyu.reward.ui.activity.personal;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.hongyu.reward.appbase.BaseSingleFragmentActivity;
import com.hongyu.reward.ui.fragment.personal.EditNicknameFragment;

/**
 * Created by zhangyang131 on 16/10/10.
 */
public class EditNicknameActivity extends BaseSingleFragmentActivity {

    public static void launch(Context context){
        Intent i = new Intent(context, EditNicknameActivity.class);
        context.startActivity(i);

    }

    @Override
    protected Class<? extends Fragment> getSingleContentFragmentClass() {
        return EditNicknameFragment.class;
    }

    @Override
    protected String getTitleText() {
        return "编辑昵称";
    }
}
