package com.hongyu.reward.ui.city;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hongyu.reward.R;
import com.hongyu.reward.appbase.BaseLoadFragment;
import com.hongyu.reward.location.GetLocationListener;
import com.hongyu.reward.location.LocationManager;
import com.hongyu.reward.model.AppLocation;
import com.hongyu.reward.ui.city.adapter.CityListAdapter;
import com.hongyu.reward.ui.city.adapter.ResultListAdapter;
import com.hongyu.reward.ui.city.db.DBManager;
import com.hongyu.reward.ui.city.model.City;
import com.hongyu.reward.ui.city.model.LocateState;
import com.hongyu.reward.ui.city.view.SideLetterBar;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by zhangyang131 on 16/10/8.
 */
public class CityPickerFragment extends BaseLoadFragment implements View.OnClickListener {
  private ListView mListView;
  private ListView mResultListView;
  private SideLetterBar mLetterBar;
  private EditText searchBox;
  private ImageView clearBtn;
  private ViewGroup emptyView;

  private CityListAdapter mCityAdapter;
  private ResultListAdapter mResultAdapter;
  private List<City> mAllCities;
  private DBManager dbManager;
  private GetLocationListener mGetLocationListener;


  @Override
  protected void onStartLoading() {

  }

  @Override
  protected void onInflated(View contentView, Bundle savedInstanceState) {
    initData();
    initView();
    getLocation();
  }

  private void getLocation() {
    AppLocation appLocation = LocationManager.getInstance().getLocation();
    if (appLocation == null) {
      LocationManager.getInstance().addLocationListener(getLocationListener());
      LocationManager.getInstance().start();
    } else {
      mCityAdapter.updateLocateState(LocateState.SUCCESS, appLocation.getCity());
    }

  }

  private GetLocationListener getLocationListener() {
    if (mGetLocationListener == null) {
      mGetLocationListener = new GetLocationListener() {
        @Override
        public void onSuccess(AppLocation locationInfo) {
          mCityAdapter.updateLocateState(LocateState.SUCCESS, locationInfo.getCity());
        }

        @Override
        public void onFailed(String msg) {
          mCityAdapter.updateLocateState(LocateState.FAILED, null);
        }
      };
    }
    return mGetLocationListener;
  }

  @Override
  protected int getLayoutResId() {
    return R.layout.activity_city_list;
  }

  private void initData() {
    dbManager = new DBManager(getActivity());
    dbManager.copyDBFile();
    mAllCities = dbManager.getAllCities();
    mCityAdapter = new CityListAdapter(getActivity(), mAllCities);
    mCityAdapter.setOnCityClickListener(new CityListAdapter.OnCityClickListener() {
      @Override
      public void onCityClick(String name) {
        selectCity(name);
      }

      @Override
      public void onLocateClick() {
        Log.e("onLocateClick", "重新定位...");
        mCityAdapter.updateLocateState(LocateState.LOCATING, null);
        getLocation();
      }
    });

    mResultAdapter = new ResultListAdapter(getActivity(), null);
  }

  private void initView() {
    mListView = (ListView) mContentView.findViewById(R.id.listview_all_city);
    mListView.setAdapter(mCityAdapter);

    TextView overlay = (TextView) mContentView.findViewById(R.id.tv_letter_overlay);
    mLetterBar = (SideLetterBar) mContentView.findViewById(R.id.side_letter_bar);
    mLetterBar.setOverlay(overlay);
    mLetterBar.setOnLetterChangedListener(new SideLetterBar.OnLetterChangedListener() {
      @Override
      public void onLetterChanged(String letter) {
        int position = mCityAdapter.getLetterPosition(letter);
        mListView.setSelection(position);
      }
    });

    searchBox = (EditText) mContentView.findViewById(R.id.et_search);
    searchBox.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {}

      @Override
      public void afterTextChanged(Editable s) {
        String keyword = s.toString();
        if (TextUtils.isEmpty(keyword)) {
          clearBtn.setVisibility(View.GONE);
          emptyView.setVisibility(View.GONE);
          mResultListView.setVisibility(View.GONE);
        } else {
          clearBtn.setVisibility(View.VISIBLE);
          mResultListView.setVisibility(View.VISIBLE);
          List<City> result = dbManager.searchCity(keyword);
          if (result == null || result.size() == 0) {
            emptyView.setVisibility(View.VISIBLE);
          } else {
            emptyView.setVisibility(View.GONE);
            mResultAdapter.changeData(result);
          }
        }
      }
    });

    emptyView = (ViewGroup) mContentView.findViewById(R.id.empty_view);
    mResultListView = (ListView) mContentView.findViewById(R.id.listview_search_result);
    mResultListView.setAdapter(mResultAdapter);
    mResultListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        selectCity(mResultAdapter.getItem(position).getName());
      }
    });

    clearBtn = (ImageView) mContentView.findViewById(R.id.iv_search_clear);

    clearBtn.setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.iv_search_clear:
        searchBox.setText("");
        clearBtn.setVisibility(View.GONE);
        emptyView.setVisibility(View.GONE);
        mResultListView.setVisibility(View.GONE);
        break;
    }
  }

  private void selectCity(String city) {
    EventBus.getDefault().post(new City(city, null));
  }
}
