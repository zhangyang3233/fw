package vip.help.gbb.activity.base;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 *
 * ViewPager的适配器，可能需要实现的特性
 * 0.对外提供与BaseAdapter一样的接口，方便使用
 * 1.view的回收利用（需要支持多种view type吗？）
 * 2.loop循环（enableLoop，loopDelay）
 * 3.
 */
public abstract class BasePagerAdapter<T> extends PagerAdapter {

  protected Context mContext;
  protected List<T> mList;
  private LayoutInflater mLayoutInflater;

  public BasePagerAdapter(Context context) {
    mContext = context;
    mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
  }

  public Context getContext() {
    return mContext;
  }

  public List<T> getData() {
    return mList;
  }

  public void setData(List<T> list) {
    mList = list;
    notifyDataSetChanged();
  }

  public T getItem(int position) {
    return mList == null ? null : mList.get(position);
  }

  @Override
  public Object instantiateItem(ViewGroup container, int position) {
    int type = getItemViewType(position);
    View convertView = getRecycleView(type);
    ViewHolder vh;
    if (convertView == null) {
      convertView = newView(mLayoutInflater, position, container);
      vh = new ViewHolder(convertView);
      convertView.setTag(vh);
    } else {
      vh = (ViewHolder) convertView.getTag();
    }
    T item = getItem(position);
    bindView(position, item, vh, container);
    container.addView(convertView);
    return convertView;
  }

  protected int getItemViewType(int position) {
    return 1;
  }

  @Override
  public void destroyItem(ViewGroup container, int position, Object object) {
    View view = (View) object;
    int type = getItemViewType(position);
    putRecycleView(view, type);
    container.removeView(view);
  }

  @Override
  public int getCount() {
    return mList == null ? 0 : mList.size();
  }

  @Override
  public boolean isViewFromObject(View view, Object object) {
    return view == object;
  }

  protected View getRecycleView(int type) {
    return null;
  }

  protected void putRecycleView(View view, int type) {

  }

  protected abstract View newView(LayoutInflater inflater, int position, ViewGroup parent);

  protected abstract void bindView(final int position, T item, ViewHolder vh, ViewGroup parent);

  @Override
  public int getItemPosition(Object object) {
    return POSITION_NONE;
  }
}
