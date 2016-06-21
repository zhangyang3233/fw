package com.fw.zycoder.demos;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.fw.zycoder.demos.utils.ActivityDataUtils;

import java.util.Map;

public class MainActivity extends ListActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setListAdapter(new SimpleAdapter(this, ActivityDataUtils.getData(this),
        android.R.layout.simple_list_item_1,
        new String[] {"title"}, new int[] {android.R.id.text1}));
    getListView().setTextFilterEnabled(true);

  }

  @Override
  @SuppressWarnings("unchecked")
  protected void onListItemClick(ListView l, View v, int position, long id) {
    Map<String, Object> map = (Map<String, Object>) l.getItemAtPosition(position);
    Intent intent = (Intent) map.get("intent");
    startActivity(intent);
  }



}
