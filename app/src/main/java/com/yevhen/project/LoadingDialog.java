package com.yevhen.project;

import android.app.Activity;
import android.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.WindowManager;

public class LoadingDialog {
    private Activity activity;
    private AlertDialog dialog;

    LoadingDialog(Activity activity){
        this.activity  = activity;
    }

    void startLoadingDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.upgrade_dialoglayout,null));
        builder.setCancelable(false);

        dialog = builder.create();

        dialog.show();
    }

    void dismissDialog(){
        dialog.dismiss();
    }

    //Як використовувати ТУТ!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    /* final LoadingDialog loadingDialog = new LoadingDialog(MainActivity.this);
        loadingDialog.startLoadingDialog();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadingDialog.dismissDialog();
            }
        },2000);*/

}
