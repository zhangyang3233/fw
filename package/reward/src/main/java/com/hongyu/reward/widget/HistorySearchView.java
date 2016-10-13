package com.hongyu.reward.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.fw.zycoder.utils.CollectionUtils;
import com.fw.zycoder.utils.ViewUtils;
import com.hongyu.reward.R;
import com.hongyu.reward.model.dbmodel.SearchModel;

import java.util.ArrayList;

/**
 * Created by zhangyang131 on 16/10/13.
 */
public class HistorySearchView extends ListView {
  HistorySearchClicked mHistorySearchClicked;
  SearchAdapter mSearchAdapter;

  public HistorySearchClicked getmHistorySearchClicked() {
    return mHistorySearchClicked;
  }

  public void setHistorySearchClicked(HistorySearchClicked mHistorySearchClicked) {
    this.mHistorySearchClicked = mHistorySearchClicked;
  }

  public HistorySearchView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init();
  }


  public HistorySearchView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public HistorySearchView(Context context) {
    super(context);
    init();
  }

  private void init() {
    mSearchAdapter = new SearchAdapter();
    ArrayList<SearchModel> list = (ArrayList<SearchModel>) SearchModel.findAll(SearchModel.class);
    mSearchAdapter.setList(list);
    setAdapter(mSearchAdapter);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
  }

  public void refresh(){
    ArrayList<SearchModel> list = (ArrayList<SearchModel>) SearchModel.findAll(SearchModel.class);
    mSearchAdapter.setList(list);
    mSearchAdapter.notifyDataSetChanged();
  }


  public class SearchAdapter extends BaseAdapter {
    ArrayList<SearchModel> list;

    public ArrayList<SearchModel> getList() {
      return list;
    }

    public void setList(ArrayList<SearchModel> list) {
      this.list = list;
      notifyDataSetChanged();
    }

    @Override
    public int getCount() {
      return CollectionUtils.isEmpty(list) ? 0 : list.size() + 1;
    }

    @Override
    public SearchModel getItem(int position) {
      return list.get(position);
    }

    @Override
    public long getItemId(int position) {
      return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
      View v;
      if (position < list.size()) {
        v = ViewUtils.newInstance(getContext(), R.layout.history_search_item);
        ImageView right_img = (ImageView) v.findViewById(R.id.right_img);
        TextView text = (TextView) v.findViewById(R.id.text);
        text.setText(getItem(position).getTag());
        right_img.setOnClickListener(new OnClickListener() {
          @Override
          public void onClick(View v) {
            list.remove(position).delete();
            notifyDataSetChanged();
          }
        });
        v.setOnClickListener(new OnClickListener() {
          @Override
          public void onClick(View v) {
            if(mHistorySearchClicked != null){
              mHistorySearchClicked.historySearchClicked(getItem(position).getTag());
            }
          }
        });
      } else {
        v = ViewUtils.newInstance(getContext(), R.layout.history_clear_search_view);
        v.setOnClickListener(new OnClickListener() {
          @Override
          public void onClick(View v) {
            SearchModel.deleteAll(SearchModel.class);
            list.clear();
            notifyDataSetChanged();
          }
        });
      }
      return v;
    }

  }

  public interface  HistorySearchClicked{
    void historySearchClicked(String historyStr);
  }
}
