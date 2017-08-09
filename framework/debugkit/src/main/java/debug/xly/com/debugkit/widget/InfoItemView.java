package debug.xly.com.debugkit.widget;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import debug.xly.com.debugkit.R;

/**
 * Created by zhangyang131 on 2017/6/7.
 */

public class InfoItemView extends RelativeLayout implements View.OnClickListener {
  TextView titleTv;
  TextView infoTv;
  String title;
  String[] infos;
  int iconRes;

  public InfoItemView(Context context) {
    super(context);
    init();
  }

  public InfoItemView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public InfoItemView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public InfoItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    init();
  }

  private void init() {
    titleTv = (TextView) findViewById(R.id.title);
    infoTv = (TextView) findViewById(R.id.info);
  }

  public static InfoItemView newInstance(Context context) {
    LayoutInflater inflater = LayoutInflater.from(context);
    InfoItemView item = (InfoItemView) inflater.inflate(R.layout.debugkit_info_item_layout, null, false);
    item.init();
    return item;
  }

  public void put(String title, String info) {
    titleTv.setText(title);
    infoTv.setText(info);
  }

  public void put(String title, int iconRes, String... infos) {
    this.iconRes = iconRes == 0 ? R.mipmap.debug : iconRes;
    this.title = title;
    this.infos = infos;
    titleTv.setText(title);
    infoTv.setText(null);
    setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {
    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
    builder.setIcon(iconRes);
    builder.setTitle(title);
    builder.setMessage(getMessage());
    builder.create().show();
  }

  String getMessage() {
    StringBuilder message = new StringBuilder();
    for (int i = 0; i < infos.length; i++) {
      if (i != 0) {
        message.append("\n");
      }
      if(infos[i] != null){
        message.append(infos[i]);
      }
    }
    return message.toString();
  }
}
