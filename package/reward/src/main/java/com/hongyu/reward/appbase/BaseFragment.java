package com.hongyu.reward.appbase;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hongyu.reward.R;

import java.lang.reflect.Field;


/**
 * @author zhangyang
 */
public abstract class BaseFragment extends Fragment {
  public static final int GUIDE_VERSION = 1;
  protected View mContentView;
  protected boolean mIsInflated;

  protected boolean isVisible;
  protected boolean isPrepared;
  protected boolean isLoaded;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mContentView = inflater.inflate(getLayoutResId(), container, false);
    isPrepared = true;
    isLoaded = false;
    checkLazyLoad();
    return mContentView;
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    if (mContentView != null) {
      onInflated(mContentView, savedInstanceState);
      mIsInflated = true;
    }
  }

  public boolean onKeyDown(int keyCode, KeyEvent event) {
    return false;
  }

  /**
   * Called when the Fragment is inflated
   *
   * @param contentView
   * @param savedInstanceState
   */
  protected abstract void onInflated(View contentView, Bundle savedInstanceState);

  /**
   * @return the layout resource id of the fragment content
   */
  protected abstract int getLayoutResId();


  /**
   * Get the root view of the fragment.
   *
   * @deprecated use {@link Fragment#getView()} instead,
   *             but need to check that getView will return null after onDetached, so be careful.
   * @return the root view
   */
  public View getContentView() {
    return mContentView;
  }


  /**
   * set activity title on actionbar
   *
   * @param title
   */
  public void setTitle(CharSequence title) {
    FragmentActivity activity = getActivity();
    if (activity instanceof AppTitleActivity) {
      activity.setTitle(title);
    }
  }


  @Override
  public void setUserVisibleHint(boolean isVisibleToUser) {
    isVisible = isVisibleToUser;
    checkLazyLoad();
    super.setUserVisibleHint(isVisibleToUser);
  }


  protected void checkLazyLoad() {
    if (isVisible && isPrepared && !isLoaded) {
      loadingData();
      isLoaded = true;
    }
  }

  // 实现fragment的懒加载，在fragment第一次显示的时候，加载数据
  protected void loadingData() {

  }

  @Override
  public void onDetach() {
    super.onDetach();
    try {
      Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
      childFragmentManager.setAccessible(true);
      childFragmentManager.set(this, null);
    } catch (NoSuchFieldException e) {
      throw new RuntimeException(e);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void onResume() {
    super.onResume();
  }

  protected void recordEvent(View view) {}

  public void onNewIntent(Intent intent) {

  }

  protected void replaceFragment(Fragment newFragment) {
    replaceFragment(newFragment, null, false);
  }

  protected void replaceFragment(Fragment newFragment, Bundle arguments, boolean isAddStack) {
    if (!isAdded()) {
      return;
    }
    FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
    if (arguments != null) {
      newFragment.setArguments(arguments);
    }
    transaction.replace(R.id.fragment_container, newFragment);
    if (isAddStack) {
      transaction.addToBackStack(null);
    }
    if (!isDetached()) {
      transaction.commitAllowingStateLoss();
    }
  }

}
