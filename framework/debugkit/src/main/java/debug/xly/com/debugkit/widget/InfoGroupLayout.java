package debug.xly.com.debugkit.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by zhangyang131 on 2017/6/7.
 */

public class InfoGroupLayout extends LinearLayout {
  String title;
  String[] infos;
  int iconRes;

  public InfoGroupLayout(Context context) {
    super(context);
  }

  public InfoGroupLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public InfoGroupLayout(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public InfoGroupLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
  }

  public OnValueChangedListencer put(final String key){
    final InfoItemView item = InfoItemView.newInstance(getContext());
    item.put(key, "加载中...");
    OnValueChangedListencer l = new OnValueChangedListencer() {
      @Override
      public void onValueChanged(String newValue) {
        item.put(key, newValue);
      }
    };
    addView(item);
    return l;
  }

  public void put(String title, int iconRes, String... infos) {
    this.title = title;
    this.infos = infos;
    this.iconRes = iconRes;
    InfoItemView titleItem = InfoItemView.newInstance(getContext());
    titleItem.setBackgroundColor(0xffeeeeee);
    titleItem.put(title, null);
    addView(titleItem);

    for (int i = 0; i < infos.length; i++) {
      String[] info = infos[i].split("：");
      InfoItemView item = InfoItemView.newInstance(getContext());
      if (info == null || info.length < 2) {
        continue;
      }
      item.put(info[0], info[1]);
      if (i != 0) {
        addDivider();
      }
      addView(item);
    }
  }

  public void addDivider() {
    View divider = new View(getContext());
    LayoutParams param = new LayoutParams(LayoutParams.MATCH_PARENT, 1);
    divider.setLayoutParams(param);
    divider.setBackgroundColor(0xffdddddd);
    addView(divider);
  }

  public interface OnValueChangedListencer{
    void onValueChanged(String newValue);
  }
}
