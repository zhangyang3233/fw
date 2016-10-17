package com.hongyu.reward.ui.fragment;

import com.hongyu.reward.appbase.AsyncLoadListFragment;
import com.hongyu.reward.appbase.adapter.DataAdapter;
import com.hongyu.reward.appbase.fetcher.BaseFetcher;
import com.hongyu.reward.http.HttpHelper;
import com.hongyu.reward.http.ResponesUtil;
import com.hongyu.reward.model.BillDetailModel;
import com.hongyu.reward.ui.adapter.BillAdapter;
import com.hongyu.reward.utils.T;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangyang131 on 16/10/17.
 */
public class BillFragment extends AsyncLoadListFragment<BillDetailModel.BillItem> {

  @Override
  protected BaseFetcher<BillDetailModel.BillItem> newFetcher() {
    return new BaseFetcher<BillDetailModel.BillItem>() {
      @Override
      protected List<BillDetailModel.BillItem> fetchHttpData(int limit, int page) {
        BillDetailModel data = HttpHelper.getBillList("1", String.valueOf(page));
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
    return new BillAdapter();
  }
}
