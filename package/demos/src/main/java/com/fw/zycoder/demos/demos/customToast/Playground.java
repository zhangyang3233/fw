package com.fw.zycoder.demos.demos.customToast;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.fw.zycoder.customtoast.SuperToast;
import com.fw.zycoder.customtoast.utils.Style;
import com.fw.zycoder.demos.R;


/**
 * Use this class to play around with the library. Simply run this activity
 * in a specified run configuration
 */
public class Playground extends Activity {

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.playground);

    final Button showButton = (Button) findViewById(R.id.show_button);
    showButton.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View view) {

        final SuperToast superToast = new SuperToast(Playground.this, Style.getStyle(Style.GREEN));
        superToast.setDuration(4500);
        superToast.setOnDismissListener(new SuperToast.OnDismissListener() {

          @Override
          public void onDismiss(View view) {

            Log.e("SuperToast", "On Dismiss");

          }
        });

        superToast.show();

      }
    });

  }

}
