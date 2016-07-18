package com.fw.zycoder.demos;

import android.support.v4.app.Fragment;

import com.fw.zycoder.demos.base.DemoActivity;

public class MainActivity extends DemoActivity {
  @Override
  protected Class<? extends Fragment> getSingleContentFragmentClass() {
    return MainFragment.class;
  }

//  @Override
//  protected void onCreate(Bundle savedInstanceState) {
//    super.onCreate(savedInstanceState);
//    setListAdapter(new SimpleAdapter(this, ActivityDataUtils.getData(this),
//        android.R.layout.simple_list_item_1,
//        new String[] {"title"}, new int[] {android.R.id.text1}));
//    getListView().setTextFilterEnabled(true);
//    getListView().setBackgroundColor(getResources().getColor(R.color.default_background));
//  }
//
//  @Override
//  @SuppressWarnings("unchecked")
//  protected void onListItemClick(ListView l, View v, int position, long id) {
//    Map<String, Object> map = (Map<String, Object>) l.getItemAtPosition(position);
//    Intent intent = (Intent) map.get("intent");
//    startActivity(intent);
//  }

}
