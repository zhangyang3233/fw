package debug.xly.com.debugkit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import debug.xly.com.debugkit.kit.KitActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.debugkit_activity_main);
    }

    public void asdf(View view){
        Intent intent = new Intent(this, KitActivity.class);
        this.startActivity(intent);
    }

}
