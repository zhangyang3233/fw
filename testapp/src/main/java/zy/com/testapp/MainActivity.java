package zy.com.testapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.math.BigDecimal;

public class MainActivity extends AppCompatActivity {
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.tv);
        tv.setText(getCalcMoney(0.9f));
    }

    private String getCalcMoney(float m) {
        BigDecimal result = new BigDecimal(String.valueOf(m));
        if (m <= 0) {
            result = new BigDecimal(0);
        } else if(0<m && m<=200){
            result = result.multiply(new BigDecimal(String.valueOf(0.9)));
        } else if(200<m && m<=500){
            result = result.multiply(new BigDecimal(String.valueOf(0.95)));
        } else if(500<m && m<=1000){
            result = new BigDecimal(m);
        }
        result = result.setScale(2, BigDecimal.ROUND_DOWN);
        return result.toString();
    }
}
