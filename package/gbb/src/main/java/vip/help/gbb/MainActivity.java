package vip.help.gbb;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.fw.zycoder.utils.MainThreadPostUtils;

import vip.help.gbb.host.activity.TabHostActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MainThreadPostUtils.postDelayed(new Runnable() {
            @Override
            public void run() {
                TabHostActivity.launch(MainActivity.this);
            }
        }, 2000);
    }
}
