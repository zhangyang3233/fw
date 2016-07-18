package com.handmark.pulltorefresh.library;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * A sample implementation of how to the RefreshableListFragment with
 * ListFragment. This implementation simply replaces the ListView that
 * ListFragment creates with a new PullToRefreshListView. This means that
 * ListFragment still works 100% (e.g. <code>setListShown(...)</code>).
 * 
 * The new RefreshableListView is created in the method
 * <code>onCreateonCreateRefreshableListViewListView()</code>. If you wish to
 * customise the RefreshableListView then override this method and return your
 * customised instance.
 * 
 */
public class RefreshableListFragment extends ListFragment {

  private RefreshableListView mRefreshableListView;

  @Override
  public final View onCreateView(LayoutInflater inflater,
      ViewGroup container, Bundle savedInstanceState) {
    View layout = super.onCreateView(inflater, container,
        savedInstanceState);

    ListView lv = (ListView) layout.findViewById(android.R.id.list);
    ViewGroup parent = (ViewGroup) lv.getParent();

    // Remove ListView and add PullToRefreshListView in its place
    int lvIndex = parent.indexOfChild(lv);
    parent.removeViewAt(lvIndex);
    mRefreshableListView = onCreateRefreshableListView(inflater,
        savedInstanceState);
    parent.addView(mRefreshableListView, lvIndex, lv.getLayoutParams());

    return layout;
  }

  /**
   * @return The {@link RefreshableListView} attached to this ListFragment.
   */
  public final RefreshableListView getRefreshableListView() {
    return mRefreshableListView;
  }

  /**
   * Returns the {@link RefreshableListView} which will replace the ListView
   * created from ListFragment. You should override this method if you wish to
   * customise the {@link RefreshableListView} from the default.
   *
   * @param inflater
   *          - LayoutInflater which can be used to inflate from XML.
   * @param savedInstanceState
   *          - Bundle passed through from
   *          {@link ListFragment#onCreateView(LayoutInflater, ViewGroup, Bundle)
   *          onCreateView(...)}
   * @return The {@link RefreshableListView} which will replace the ListView.
   */
  protected RefreshableListView onCreateRefreshableListView(
      LayoutInflater inflater, Bundle savedInstanceState) {
    return new RefreshableListView(getActivity());
  }

}
