package com.hongyu.reward.ui.fragment.personal;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.fw.zycoder.http.callback.DataCallback;
import com.hongyu.reward.R;
import com.hongyu.reward.appbase.BaseLoadFragment;
import com.hongyu.reward.http.ResponesUtil;
import com.hongyu.reward.manager.AccountManager;
import com.hongyu.reward.model.BaseModel;
import com.hongyu.reward.request.ResetPwdWithOldPwdRequestBuilder;
import com.hongyu.reward.utils.T;

/**
 * Created by zhangyang131 on 16/9/22.
 */
public class UpdatePwdFragment extends BaseLoadFragment implements View.OnClickListener {
    EditText pwd;
    EditText new_pwd;
    EditText new_pwd2;
    Button commit;

    @Override
    protected void onStartLoading() {

    }

    @Override
    protected void onInflated(View contentView, Bundle savedInstanceState) {
        initView();
    }

    private void initView() {
        pwd = (EditText) mContentView.findViewById(R.id.pwd);
        new_pwd = (EditText) mContentView.findViewById(R.id.new_pwd);
        new_pwd2 = (EditText) mContentView.findViewById(R.id.new_pwd2);
        commit = (Button) mContentView.findViewById(R.id.commit);
        pwd.setHint("请输入旧密码");
        new_pwd.setHint("请输入新密码");
        new_pwd2.setHint("请再次输入新密码");
        commit.setOnClickListener(this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_update_pwd_layout;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.commit:
                String oldPwdStr = pwd.getText().toString();
                String newPwdStr = new_pwd.getText().toString();
                if (checkInput()) {
                    ResetPwdWithOldPwdRequestBuilder builder = new ResetPwdWithOldPwdRequestBuilder(oldPwdStr, newPwdStr);
                    builder.setDataCallback(new DataCallback<BaseModel>() {
                        @Override
                        public void onDataCallback(BaseModel data) {
                            if (ResponesUtil.checkModelCodeOK(data)) {
                                T.show("修改密码成功");
                                AccountManager.getInstance().logout();
                            } else {
                                T.show(ResponesUtil.getErrorMsg(data));
                            }
                        }
                    });
                    builder.build().submit();
                }
                break;
        }
    }

    private boolean checkInput() {
        String oldPwdStr = pwd.getText().toString();
        String newPwdStr = new_pwd.getText().toString();
        String newPwd2Str = new_pwd2.getText().toString();
        if (TextUtils.isEmpty(oldPwdStr)) {
            pwd.setError("请输入原密码");
            return false;
        }
        if (TextUtils.isEmpty(newPwdStr)) {
            new_pwd.setError("请输入新密码");
            return false;
        }
        if (TextUtils.isEmpty(newPwd2Str)) {
            new_pwd2.setError("请再次输入新密码");
            return false;
        }
        if (!newPwdStr.equals(newPwd2Str)) {
            new_pwd2.setError("两次密码输入不一致");
            return false;
        }
        return true;
    }
}
