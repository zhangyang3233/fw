package com.hongyu.reward.ui.fragment.personal;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.fw.zycoder.http.callback.DataCallback;
import com.hongyu.reward.R;
import com.hongyu.reward.appbase.BaseLoadFragment;
import com.hongyu.reward.http.ResponesUtil;
import com.hongyu.reward.manager.AccountManager;
import com.hongyu.reward.model.MyEvaluateReceiveModel;
import com.hongyu.reward.request.GetMyEvaluateRequestBuilder;
import com.hongyu.reward.utils.T;
import com.hongyu.reward.widget.FiveStarSingle;
import com.hongyu.reward.widget.RoundImageView;
import com.hongyu.reward.widget.StarMultiple;

import org.json.JSONObject;

/**
 * Created by zhangyang131 on 16/9/21.
 */
public class MyEvaluateFragment extends BaseLoadFragment {
  private RoundImageView mHeadImg;
  private TextView mTvName;
  private TextView mTvOrderNum;
  private TextView mTvGcr;
  private FiveStarSingle mScoreView;
//  private TagFlowLayout mFlowLayout;
//  private TagAdapter adapter;
  private StarMultiple mStarMultiple;

  @Override
  protected void onStartLoading() {
    showLoadingView();
    GetMyEvaluateRequestBuilder builder = new GetMyEvaluateRequestBuilder(0, 0);
    builder.setDataCallback(new DataCallback<JSONObject>() {
      @Override
      public void onDataCallback(JSONObject data) {
        if (!isAdded()) {
          return;
        }
        dismissLoadingView();
        if (ResponesUtil.checkModelCodeOK(data)) {
          JSONObject evaluate = data.optJSONObject("data");
          MyEvaluateReceiveModel model = MyEvaluateReceiveModel.decode(evaluate);
          refreshData(model);
        } else {
          T.show(ResponesUtil.getErrorMsg(data));
        }
      }
    });
    builder.build().submit();
  }

  private void refreshData(MyEvaluateReceiveModel data) {
    mTvName.setText(data.getNickname());
    mTvGcr.setText("好评率：" + data.getGcr());
    mTvOrderNum.setText("成交：" + data.getOrder_num() + "单");
    mScoreView.setData(data.getScore(), false);
//    adapter.setTagDatas(data.getTag_list());
//    adapter.notifyDataChanged();
    mStarMultiple.setData(data.getScore_list());
    mStarMultiple.setVisibility(View.VISIBLE);
    mHeadImg.loadNetworkImageByUrl(AccountManager.getInstance().getUser().getHead_img());
  }

  @Override
  protected void onInflated(View contentView, Bundle savedInstanceState) {
    initView();
  }

  private void initView() {
//    mFlowLayout = (TagFlowLayout) mContentView.findViewById(R.id.flow_layout);
    mHeadImg = (RoundImageView) mContentView.findViewById(R.id.header_icon);
    mTvName = (TextView) mContentView.findViewById(R.id.name);
    mTvGcr = (TextView) mContentView.findViewById(R.id.gcr);
    mTvOrderNum = (TextView) mContentView.findViewById(R.id.order_num);
    mScoreView = (FiveStarSingle) mContentView.findViewById(R.id.my_score);
    mStarMultiple = (StarMultiple) mContentView.findViewById(R.id.star_multiple);
//    adapter = new TagAdapter<MyEvaluateReceiveModel.TagModel>() {
//      @Override
//      public View getView(FlowLayout parent, int position,
//          MyEvaluateReceiveModel.TagModel tagModel) {
//        TagView tagView = TagView.newInstance(getActivity());
//        tagView.setTagModel(getItem(position));
//        return tagView;
//      }
//    };
//    mFlowLayout.setAdapter(adapter);
  }

  @Override
  protected int getLayoutResId() {
    return R.layout.fragment_evaluate_receive;
  }
}
