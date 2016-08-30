package com.fw.zycoder.demos.demos.recyclerview;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fw.zycoder.appbase.fragment.BaseFragment;
import com.fw.zycoder.demos.R;

/**
 * Created by zhangyang131 on 16/7/25.
 */
public class RecyclerViewDemoFragment extends BaseFragment {
  RecyclerView mRecyclerView;
  HomeAdapter mAdapter;

  @Override
  protected void onInflated(View contentView, Bundle savedInstanceState) {
    initView();
  }

  private void initView() {
    mRecyclerView = (RecyclerView) mContentView.findViewById(R.id.recyclerView);
    mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    mRecyclerView.setAdapter(mAdapter = new HomeAdapter());
    mRecyclerView.addItemDecoration(new MyItemDecoration());
  }

  @Override
  protected int getLayoutResId() {
    return R.layout.fragment_recyclerview;
  }

  class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      MyViewHolder holder = new MyViewHolder(
          LayoutInflater.from(getActivity()).inflate(R.layout.item_recyclerview, parent, false));
      return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tv.setText(String.valueOf(position));
    }

    @Override
    public int getItemCount() {
      return 20;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
      TextView tv;

      public MyViewHolder(View itemView) {
        super(itemView);
        tv = (TextView) itemView.findViewById(R.id.id_num);
      }
    }
  }
}
