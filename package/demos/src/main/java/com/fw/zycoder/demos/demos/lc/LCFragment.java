package com.fw.zycoder.demos.demos.lc;

import android.os.Bundle;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fw.zycoder.appbase.fragment.BaseFragment;
import com.fw.zycoder.demos.R;
import com.fw.zycoder.utils.MainThreadPostUtils;
import com.fw.zycoder.utils.Spanny;

import java.math.BigDecimal;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zhangyang131 on 2016/10/20.
 */
public class LCFragment extends BaseFragment implements View.OnClickListener {
    private int zhen = 10; // 一秒10帧
    // 一年有多少秒
    private final BigDecimal ms = new BigDecimal(365).multiply(new BigDecimal(24))
            .multiply(new BigDecimal(3600));
    private float nhlv = 7.5f; // 年化
    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            MainThreadPostUtils.post(new Runnable() {
                @Override
                public void run() {
                    zzz();
                }
            });
        }
    };


    double money = 10000;
    long startTime;

    Timer timer = new Timer();
    EditText ed;
    TextView tv;
    TextView tip;
    Button btn;


    /**
     * 计算每一秒能赚多少钱
     *
     * @param l     年化
     * @param money 总钱
     * @return
     */
    private BigDecimal getDtime(float l, BigDecimal money) {
        // 一年的盈利
        BigDecimal yl = money.multiply(new BigDecimal(l / 100f));
        BigDecimal result = yl.divide(ms, 40, BigDecimal.ROUND_HALF_EVEN);
        return result;
    }

    @Override
    protected void onInflated(View contentView, Bundle savedInstanceState) {
        ed = (EditText) mContentView.findViewById(R.id.v1);
        tv = (TextView) mContentView.findViewById(R.id.v2);
        btn = (Button) mContentView.findViewById(R.id.v3);
        tip = (TextView) mContentView.findViewById(R.id.v4);
        btn.setOnClickListener(this);
        timer.schedule(timerTask, 0, zhen);
        startTime = System.currentTimeMillis();
    }

    private void zzz() {
        BigDecimal b = getDtime(nhlv, new BigDecimal(money)).multiply(new BigDecimal(System.currentTimeMillis() - startTime)).divide(new BigDecimal(1000), 6, BigDecimal.ROUND_HALF_EVEN).add(new BigDecimal(money));
        tv.setText(b.toString());
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_lc;
    }

    @Override
    public void onClick(View v) {
        startTime = System.currentTimeMillis();
        money = Double.valueOf(ed.getText().toString());
        settip();
    }

    private void settip() {
        Spanny s = new Spanny();
        s.append("当前年化：").append(nhlv + "%", new ForegroundColorSpan(0xffffcccc)).append("\n");
        s.append("一年能赚：").append(getYXByT("y"), new ForegroundColorSpan(0xffffcccc)).append("\n");
        s.append("一月能赚：").append(getYXByT("moth"), new ForegroundColorSpan(0xffffcccc)).append("\n");
        s.append("一天能赚：").append(getYXByT("d"), new ForegroundColorSpan(0xffffcccc)).append("\n");
        s.append("一小时能赚：").append(getYXByT("h"), new ForegroundColorSpan(0xffffcccc)).append("\n");
        s.append("一分钟能赚：").append(getYXByT("min"), new ForegroundColorSpan(0xffffcccc)).append("\n");
        s.append("一秒能赚：").append(getYXByT("s"), new ForegroundColorSpan(0xffffcccc)).append("\n");
        tip.setText(s);
    }

    private CharSequence getYXByT(String t) {
        if(t.equals("y")){
            return String.valueOf(money*nhlv/100f);
        }else if(t.equals("moth")){
            return String.valueOf(money*nhlv/100f/12);
        }else if(t.equals("d")){
            return String.valueOf(money*nhlv/100f/365);
        }else if(t.equals("h")){
            return String.valueOf(money*nhlv/100f/365/24);
        }else if(t.equals("min")){
            return String.valueOf(money*nhlv/100f/365/24/60);
        }else if(t.equals("s")){
            return String.valueOf(money*nhlv/100f/365/24/60/60);
        }
        return null;
    }
}
