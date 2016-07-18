package com.wanda.uicomp.deprecated;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;

public abstract class FragmentGroup extends Fragment {
  protected static final int INVALID_FRAGMENT_ID = -1;
  private static final String SAVE_INSTANCE_STATE_PRIMARY_FRAGMENT_TAG = "primary_fragment_tag";
  private static final String SAVE_INSTANCE_STATE_SECONDARY_FRAGMENT_TAG = "secondary_fragment_tag";

  private String mCurrentPrimaryFragmentTag;
  protected Fragment mCurrentPrimaryFragment;

  private String mCurrentSecondaryFragmentTag;
  protected Fragment mCurrentSecondaryFragment;

  private int mCurrentPrimaryFragmentId = INVALID_FRAGMENT_ID;
  private int mCurrentSecondaryFragmentId = INVALID_FRAGMENT_ID;

  protected FragmentManager mFragmentManager;

  /**
   * this function will be invoked in fragment life cycle onActivityCreated()
   * to initialize first visible primary fragment
   */
  protected abstract void initPrimaryFragment();

  /**
   * get primary fragment class
   * 
   * @param fragmentId
   * @return
   */
  protected abstract Class<? extends Fragment> getPrimaryFragmentClass(
      int fragmentId);

  /**
   * get arguments for primary fragment
   * 
   * @param fragmentId
   * @return
   */
  protected abstract Bundle getPrimaryFragmentArguments(int fragmentId);

  /**
   * get primary fragment's container ViewGroup resource id
   * 
   * @return
   */
  protected abstract int getPrimaryFragmentStubId(int fragmentId);

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    mFragmentManager = getChildFragmentManager();
    super.onViewCreated(view, savedInstanceState);
  }

  protected FragmentTransaction beginPrimaryFragmentTransaction(
      int enterFragmentId, int exitFragmentId) {
    FragmentTransaction ft = mFragmentManager.beginTransaction();
    return ft;
  }

  public void switchPrimaryFragment(int fragmentId) {
    Class<? extends Fragment> clz = getPrimaryFragmentClass(fragmentId);
    mCurrentPrimaryFragmentTag = clz.getName();
    Fragment fragment = mFragmentManager
        .findFragmentByTag(mCurrentPrimaryFragmentTag);
    FragmentTransaction ft = beginPrimaryFragmentTransaction(fragmentId,
        mCurrentPrimaryFragmentId);
    mCurrentPrimaryFragmentId = fragmentId;
    if (mCurrentPrimaryFragment != null) {
      ft.detach(mCurrentPrimaryFragment);
      if (fragment == mCurrentPrimaryFragment) {
        fragment = null;
      }
    }
    Bundle args = getPrimaryFragmentArguments(fragmentId);
    if (fragment == null) {
      fragment = Fragment.instantiate(getActivity(), clz.getName());
      fragment.setArguments(args);
      ft.replace(getPrimaryFragmentStubId(fragmentId), fragment,
          mCurrentPrimaryFragmentTag);
    } else {
      Bundle existedArgs = fragment.getArguments();
      if (existedArgs != null) {
        existedArgs.putAll(args);
      }
      ft.attach(fragment);
    }
    mCurrentPrimaryFragment = fragment;
    ft.commitAllowingStateLoss();
  }

  protected FragmentTransaction beginSecondaryFragmentTransaction(
      int enterFragmentId, int exitFragmentId) {
    FragmentTransaction ft = mFragmentManager.beginTransaction();
    return ft;
  }

  public void switchSecondaryFragment(int fragmentId) {
    Class<? extends Fragment> clz = getSecondaryFragmentClass(fragmentId);
    if (clz == null) {
      return;
    }
    mCurrentSecondaryFragmentTag = clz.getName();
    Fragment fragment = mFragmentManager
        .findFragmentByTag(mCurrentSecondaryFragmentTag);
    FragmentTransaction ft = beginSecondaryFragmentTransaction(fragmentId,
        mCurrentSecondaryFragmentId);
    mCurrentSecondaryFragmentId = fragmentId;
    if (mCurrentSecondaryFragment != null) {
      ft.detach(mCurrentSecondaryFragment);
      if (fragment == mCurrentSecondaryFragment) {
        fragment = null;
      }
    }
    Bundle args = getSecondaryFragmentArguments(fragmentId);
    if (fragment == null) {
      fragment = Fragment.instantiate(getActivity(), clz.getName());
      fragment.setArguments(args);
      ft.replace(getSecondaryFragmentStubId(fragmentId), fragment,
          mCurrentSecondaryFragmentTag);
    } else {
      Bundle existedArgs = fragment.getArguments();
      if (existedArgs != null) {
        existedArgs.putAll(args);
      }
      ft.attach(fragment);
    }
    mCurrentSecondaryFragment = fragment;
    ft.commitAllowingStateLoss();
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    outState.putString(SAVE_INSTANCE_STATE_PRIMARY_FRAGMENT_TAG,
        mCurrentPrimaryFragmentTag);
    outState.putString(SAVE_INSTANCE_STATE_SECONDARY_FRAGMENT_TAG,
        mCurrentSecondaryFragmentTag);
    super.onSaveInstanceState(outState);
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    if (savedInstanceState != null) {
      String primaryFragmentTag = savedInstanceState
          .getString(SAVE_INSTANCE_STATE_PRIMARY_FRAGMENT_TAG);
      if (!TextUtils.isEmpty(primaryFragmentTag)) {
        mCurrentPrimaryFragmentTag = primaryFragmentTag;
        mCurrentPrimaryFragment = mFragmentManager
            .findFragmentByTag(mCurrentPrimaryFragmentTag);
      }
      String secondaryFragmentTag = savedInstanceState
          .getString(SAVE_INSTANCE_STATE_SECONDARY_FRAGMENT_TAG);
      if (!TextUtils.isEmpty(secondaryFragmentTag)) {
        mCurrentSecondaryFragmentTag = secondaryFragmentTag;
        mCurrentSecondaryFragment = mFragmentManager
            .findFragmentByTag(mCurrentSecondaryFragmentTag);
      }
    }
    if (mCurrentPrimaryFragment == null) {
      initPrimaryFragment();
    }
    if (mCurrentSecondaryFragment == null) {
      initSecondaryFragment();
    }
    super.onActivityCreated(savedInstanceState);
  }

  @Override
  public void onDestroyView() {
    if (mCurrentPrimaryFragment != null
        && mCurrentPrimaryFragment.isAdded()) {
      mFragmentManager.beginTransaction().remove(mCurrentPrimaryFragment)
          .commitAllowingStateLoss();
    }
    mCurrentPrimaryFragment = null;

    if (mCurrentSecondaryFragment != null
        && mCurrentSecondaryFragment.isAdded()) {
      mFragmentManager.beginTransaction()
          .remove(mCurrentSecondaryFragment)
          .commitAllowingStateLoss();
    }
    mCurrentSecondaryFragment = null;
    super.onDestroyView();
  }

  /**
   * this function will be invoked in fragment life cycle onActivityCreated()
   * to initialize first visible secondary fragment
   */
  protected void initSecondaryFragment() {}

  /**
   * get secondary fragment class
   * 
   * @param fragmentId
   * @return
   */
  protected Class<? extends Fragment> getSecondaryFragmentClass(int fragmentId) {
    return null;
  }

  /**
   * get arguments for secondary fragment
   * 
   * @param fragmentId
   * @return
   */
  protected Bundle getSecondaryFragmentArguments(int fragmentId) {
    return null;
  }

  /**
   * get secondary fragment's container ViewGroup resource id
   * 
   * @return
   */
  protected int getSecondaryFragmentStubId(int fragmentId) {
    return 0;
  }
}
