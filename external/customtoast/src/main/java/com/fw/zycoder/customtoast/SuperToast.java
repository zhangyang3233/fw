package com.fw.zycoder.customtoast;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.widget.Toast;

/**
 * Created by zhangyang131 on 16/6/22.
 */
public class SuperToast extends Toast {

    /**
     * Construct an empty Toast object.  You must call {@link #setView} before you
     * can call {@link #show}.
     *
     * @param context The context to use.  Usually your {@link Application}
     *                or {@link Activity} object.
     */
    public SuperToast(Context context) {
        super(context);
    }

    @Override
    public void setDuration(int duration) {
        super.setDuration(duration);
    }
}
