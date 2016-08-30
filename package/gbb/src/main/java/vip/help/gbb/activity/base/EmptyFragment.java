package vip.help.gbb.activity.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.fw.zycoder.appbase.fragment.BaseFragment;

import vip.help.gbb.R;


/**
 * @author zhangyuwen 2016-03-14
 */
public class EmptyFragment extends BaseFragment {

  public static Fragment newInstance() {
    return new EmptyFragment();
  }

  @Override
  protected int getLayoutResId() {
    return R.layout.fragment_empty;
  }

  @Override
  protected void onInflated(View contentView, Bundle savedInstanceState) {

  }
}
