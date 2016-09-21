package com.hongyu.reward.ui.fragment.personal;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.fw.zycoder.http.callback.DataCallback;
import com.fw.zycoder.utils.CollectionUtils;
import com.hongyu.reward.R;
import com.hongyu.reward.appbase.AsyncLoadListFragment;
import com.hongyu.reward.appbase.adapter.DataAdapter;
import com.hongyu.reward.appbase.fetcher.BaseFetcher;
import com.hongyu.reward.http.HttpHelper;
import com.hongyu.reward.http.ResponesUtil;
import com.hongyu.reward.model.BaseModel;
import com.hongyu.reward.model.ReasonModel;
import com.hongyu.reward.request.ReportRequestBuilder;
import com.hongyu.reward.ui.activity.personal.ContactSuccessActivity;
import com.hongyu.reward.ui.adapter.ReasonAdapter;
import com.hongyu.reward.utils.T;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangyang131 on 16/9/21.
 */
public class ContactFragment extends AsyncLoadListFragment<ReasonModel.Reason>
    implements
      View.OnClickListener {

  private ReasonAdapter adapter;
  private EditText mEtContent;
  private View mBtnCommit;

  @Override
  protected int getLayoutResId() {
    return R.layout.activity_contact_layout;
  }

  @Override
  protected boolean needToLoadMore() {
    return false;
  }

  @Override
  public boolean needPullDown() {
    return false;
  }


  @Override
  protected BaseFetcher<ReasonModel.Reason> newFetcher() {
    return new BaseFetcher<ReasonModel.Reason>() {
      @Override
      protected List<ReasonModel.Reason> fetchHttpData(int limit, int page) {
        ReasonModel reasonModel = HttpHelper.getReasonList();
        if (reasonModel == null) {
          return null;
        } else if (CollectionUtils.isEmpty(reasonModel.getData())) {
          return new ArrayList<>();
        } else {
          return reasonModel.getData();
        }
      }
    };
  }

  @Override
  protected void onInflated(View contentView, Bundle savedInstanceState) {
    super.onInflated(contentView, savedInstanceState);
    initView();
  }

  private void initView() {
    mEtContent = (EditText) mContentView.findViewById(R.id.content);
    mBtnCommit = mContentView.findViewById(R.id.commit);
    mBtnCommit.setOnClickListener(this);
  }

  @Override
  protected DataAdapter<ReasonModel.Reason> newContentAdapter() {
    if (adapter == null) {
      adapter = new ReasonAdapter();
    }
    return adapter;
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.commit:
        commit();
        break;
    }
  }

  private void commit() {
    ReasonModel.Reason reason = adapter.getSelectReason();
    if (reason == null) {
      T.show("请选择一个原因");
      return;
    }
    String content = mEtContent.getText().toString().trim();
    showLoadingView();
    ReportRequestBuilder builder = new ReportRequestBuilder(reason.getId(), content);
    builder.setDataCallback(new DataCallback<BaseModel>() {
      @Override
      public void onDataCallback(BaseModel data) {
        if (!isAdded()) {
          return;
        }
        dismissLoadingView();
        if (ResponesUtil.checkModelCodeOK(data)) {
          ContactSuccessActivity.launch(getActivity());
          getActivity().finish();
        } else {
          T.show(ResponesUtil.getErrorMsg(data));
        }
      }
    });
    builder.build().submit();
  }


}
