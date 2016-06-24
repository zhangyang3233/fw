package com.fw.zycoder.demos.demos.customToast;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import com.fw.zycoder.demos.R;


public class ToastTestActivity extends AppCompatActivity {

  private static final String NAVIGAION_SELECTION = "navigationSelection";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_custom_toast);

    final ActionBar actionBar = getSupportActionBar();
    actionBar.setDisplayShowTitleEnabled(false);
    actionBar.setDisplayShowHomeEnabled(false);
    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(actionBar.getThemedContext(),
        R.layout.spinner_item, android.R.id.text1,
        getResources().getStringArray(R.array.navigation_list));
    arrayAdapter.setDropDownViewResource(R.layout.spinner_item);

    actionBar.setListNavigationCallbacks(arrayAdapter, new ActionBar.OnNavigationListener() {

      Fragment fragment;

      @Override
      public boolean onNavigationItemSelected(int itemPosition, long itemId) {

        switch (itemPosition) {

          case 0:

            fragment = new FragmentSuperToast();

            break;


          case 1:

            fragment = new FragmentSuperActivityToast();

            break;


          case 2:

            fragment = new FragmentSuperCardToast();

            break;


          default:

            fragment = new FragmentSuperToast();

            break;

        }

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();

        return false;

      }
    });


    if (savedInstanceState != null) {

      actionBar.setSelectedNavigationItem(savedInstanceState.getInt(NAVIGAION_SELECTION));

    }

  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {

    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.main, menu);

    return true;

  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);

    outState.putInt(NAVIGAION_SELECTION, getSupportActionBar().getSelectedNavigationIndex());

  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {

    switch (item.getItemId()) {

      case R.id.wiki:

        switch (getActionBar().getSelectedNavigationIndex()) {

          case 0:

            gotoWebsite(getResources().getString(R.string.url_wiki_supertoast));

            break;

          case 1:

            gotoWebsite(getResources().getString(R.string.url_wiki_superactivitytoast));

            break;

          case 2:

            gotoWebsite(getResources().getString(R.string.url_wiki_supercardtoast));

            break;

        }

        return true;


      case R.id.github:

        gotoWebsite(getResources().getString(R.string.url_project_page));

        return true;

      default:

        return super.onOptionsItemSelected(item);

    }


  }

  private void gotoWebsite(String url) {

    Intent intent = new Intent(Intent.ACTION_VIEW);
    intent.setData(Uri.parse(url));
    startActivity(intent);

  }

}
