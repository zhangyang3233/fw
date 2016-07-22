package com.fw.zycoder.demos;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fw.zycoder.appbase.adapter.DataAdapter;
import com.fw.zycoder.appbase.fetcher.BaseFetcher;
import com.fw.zycoder.appbase.fragment.AsyncLoadListFragment;
import com.fw.zycoder.demos.utils.ActivityDataUtils;
import com.fw.zycoder.utils.ViewUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by zhangyang131 on 16/7/18.
 */
public class MainFragment extends AsyncLoadListFragment<Map<String, Object>> {

    @Override
    protected BaseFetcher<Map<String, Object>> newFetcher() {
        return new BaseFetcher<Map<String, Object>>() {
            @Override
            protected List<Map<String, Object>> fetchHttpData(int limit, int page) {
                return ActivityDataUtils.getData(getActivity());
            }
        };
    }

    @Override
    protected DataAdapter<Map<String, Object>> newContentAdapter() {
        return new IDataAdapter();
    }

    class IDataAdapter extends DataAdapter<Map<String, Object>> {

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View item = ViewUtils.newInstance(getActivity(), android.R.layout.simple_list_item_1);
            TextView tv = (TextView) item.findViewById(android.R.id.text1);
            tv.setText((String) mData.get(position).get("title"));
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map<String, Object> map = mData.get(position);
                    Intent intent = (Intent) map.get("intent");
                    startActivity(intent);
                }
            });
            return item;
        }
    }
}
