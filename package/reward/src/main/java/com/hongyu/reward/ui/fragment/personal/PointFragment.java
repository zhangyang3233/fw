package com.hongyu.reward.ui.fragment.personal;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.hongyu.reward.R;
import com.hongyu.reward.appbase.AsyncLoadListFragment;
import com.hongyu.reward.appbase.adapter.DataAdapter;
import com.hongyu.reward.appbase.fetcher.BaseFetcher;
import com.hongyu.reward.http.HttpHelper;
import com.hongyu.reward.http.ResponesUtil;
import com.hongyu.reward.manager.AccountManager;
import com.hongyu.reward.model.BillDetailModel;
import com.hongyu.reward.model.LoginModel;
import com.hongyu.reward.ui.adapter.PointAdapter;
import com.hongyu.reward.utils.T;
import com.hongyu.reward.widget.CommonTextView;
import com.hongyu.reward.widget.RoundImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangyang131 on 16/9/21.
 */
public class PointFragment extends AsyncLoadListFragment<BillDetailModel.BillItem> implements View.OnClickListener {

  private CommonTextView mCellScoreShop;
  private TextView mTvName;
  private RoundImageView mHeadImag;
  private TextView mTvScore;


  @Override
  public void onResume() {
    super.onResume();
    LoginModel.UserInfo userInfo = AccountManager.getInstance().getUser();
    mTvName.setText(userInfo.getNickname());
    mTvScore.setText(String.valueOf(userInfo.getScore()));
    mHeadImag.loadNetworkImageByUrl(userInfo.getHead_img());
  }

  @Override
  protected BaseFetcher<BillDetailModel.BillItem> newFetcher() {
    return new BaseFetcher<BillDetailModel.BillItem>() {
      @Override
      protected List<BillDetailModel.BillItem> fetchHttpData(int limit, int page) {
        BillDetailModel data = HttpHelper.getBillList("2", String.valueOf(page));
        if (data == null) {
          return null;
        }
        if (ResponesUtil.checkModelCodeOK(data)) {
          if (data.getData() == null) {
            return new ArrayList<>();
          } else {
            return data.getData();
          }
        } else {
          T.show(ResponesUtil.getErrorMsg(data));
        }
        return null;
      }
    };
  }

  @Override
  protected DataAdapter<BillDetailModel.BillItem> newContentAdapter() {
    return new PointAdapter();
  }

  @Override
  protected void onInflated(View contentView, Bundle savedInstanceState) {
    super.onInflated(contentView, savedInstanceState);
    initView();
  }

  private void initView() {
    mCellScoreShop = (CommonTextView) mContentView.findViewById(R.id.score_shop);
    mHeadImag = (RoundImageView) mContentView.findViewById(R.id.header_icon);
    mTvName = (TextView) mContentView.findViewById(R.id.name);
    mTvScore = (TextView) mContentView.findViewById(R.id.score);
    mCellScoreShop.setOnClickListener(this);
  }

  @Override
  protected int getLayoutResId() {
    return R.layout.activity_score_layout;
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.score_shop:
        T.show(R.string.tip_to_open);
        break;
    }
  }
}
