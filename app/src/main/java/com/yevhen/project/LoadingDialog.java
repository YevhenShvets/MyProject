package com.yevhen.project;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;

public class LoadingDialog  extends Dialog {
    private Activity activity;

    public LoadingDialog(@NonNull Context context) {
        super(context);
        setCancelable(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.upgrade_dialoglayout);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
      /*  WindowManager.LayoutParams a = new WindowManager.LayoutParams();
        a.copyFrom(getWindow().getAttributes());
        a.width = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(a);*/
    }
}
