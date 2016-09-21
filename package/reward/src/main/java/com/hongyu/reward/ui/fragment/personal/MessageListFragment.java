package com.hongyu.reward.ui.fragment.personal;

import com.hongyu.reward.appbase.AsyncLoadListFragment;
import com.hongyu.reward.appbase.adapter.DataAdapter;
import com.hongyu.reward.appbase.fetcher.BaseFetcher;
import com.hongyu.reward.http.HttpHelper;
import com.hongyu.reward.model.MsgModel;
import com.hongyu.reward.model.MsgModel.MessageModel;
import com.hongyu.reward.ui.adapter.MessageListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangyang131 on 16/9/21.
 */
public class MessageListFragment extends AsyncLoadListFragment<MessageModel> {

    @Override
    protected BaseFetcher<MessageModel> newFetcher() {
        return new BaseFetcher<MessageModel>() {
            @Override
            protected List<MessageModel> fetchHttpData(int limit, int page) {
                MsgModel msgs = HttpHelper.getMsgList(String.valueOf(page));
                if(msgs == null){
                    return null;
                }else if(msgs.getData() == null){
                    return new ArrayList<>();
                }else{
                    return msgs.getData();
                }
            }
        };
    }

    @Override
    protected DataAdapter<MessageModel> newContentAdapter() {
        return new MessageListAdapter();
    }

    @Override
    protected int getPageSize() {
        return 10;
    }
}
