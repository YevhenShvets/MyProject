package com.yevhen.project;

import android.view.View;

import androidx.fragment.app.Fragment;

import com.yevhen.project.Class.MyObject;

/**
 * A simple {@link Fragment} subclass.
 */

public abstract class DoubleClickListener implements View.OnClickListener {

    private static final long DOUBLE_CLICK_TIME_DELTA = 300;//milliseconds
    private long lastClickTime = 0;

    public DoubleClickListener() {
    }

    @Override
    public void onClick(View v) {
        long clickTime = System.currentTimeMillis();
        if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA){
            onDoubleClick(v);
        } else {
            onSingleClick(v);
        }
        lastClickTime = clickTime;
    }

    public abstract void onSingleClick(View v);
    public abstract void onDoubleClick(View v);
    public String getI(){
        return ""+ 1;
    }
}
