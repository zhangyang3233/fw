package vip.help.gbb.activity.base;

import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author zhangyuwen 2015-12-07
 */
public class ViewHolder {
  private View mItemView;
  private SparseArray<View> mArray = new SparseArray<>();

  public ViewHolder(View v) {
    mItemView = v;
    if (v.getId() != View.NO_ID) {
      mArray.put(v.getId(), v);
    }
  }

  public View getItemView() {
    return mItemView;
  }

  private View findViewById(int id) {
    View view = mArray.get(id);
    if (view != null) {
      return view;
    }
    view = mItemView.findViewById(id);
    mArray.put(id, view);
    return view;
  }

  public View getView(int id) {
    return findViewById(id);
  }

  public TextView getTextView(int id) {
    return (TextView) findViewById(id);
  }

  public ImageView getImageView(int id) {
    return (ImageView) findViewById(id);
  }
}
