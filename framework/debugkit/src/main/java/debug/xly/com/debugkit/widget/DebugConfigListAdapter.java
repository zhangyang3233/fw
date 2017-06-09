package debug.xly.com.debugkit.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import debug.xly.com.debugkit.R;

public class DebugConfigListAdapter extends BaseAdapter {

  private final static String TAG = "DebugSettingListAdapter";

  private LayoutInflater mInflater;
  private Context mContext;

  private CharSequence[] mNames = null;
  private CharSequence[] mItems = null;
  private int mSelectedIdx = -1;

  public DebugConfigListAdapter(Context context, CharSequence[] items, CharSequence[] names) {
    mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    mContext = context;
    mItems = items;
    mNames = names;
  }

  public void clear(){
    this.mItems = null;
  }

  public void setSelectedIndex(int selected) {
    mSelectedIdx = selected;
  }

  @Override
  public int getCount() {
    if (mItems == null) {
      return 0;
    }
    return mItems.length;
  }

  @Override
  public Object getItem(int position) {
    return null;
  }

  @Override
  public long getItemId(int position) {
    return 0;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    ViewHolder holder = null;
    if (convertView == null) {
      convertView = mInflater.inflate(R.layout.debug_info_list_item_view, null);
      if (convertView != null) {
        holder = loadViewHolder(convertView);
        convertView.setTag(holder);
      }
    } else {
      holder = (ViewHolder) convertView.getTag();
    }
    holder.radioBtn.setChecked(position == mSelectedIdx);
    holder.infoTag.setText(mNames[position]);
    holder.infoContent.setText(mItems[position]);
    return convertView;
  }

  private ViewHolder loadViewHolder(View v) {
    ViewHolder vh = new ViewHolder();
    vh.infoTag = (TextView) v.findViewById(R.id.debug_info_tag);
    vh.infoContent = (TextView) v.findViewById(R.id.debug_info_content);
    vh.radioBtn = (RadioButton) v.findViewById(R.id.debug_selected_radio_btn);
    return vh;
  }

  class ViewHolder {
    public TextView infoTag;
    public TextView infoContent;
    public RadioButton radioBtn;
  }

}
