package com.yevhen.project;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;

import android.graphics.Point;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.MacAddress;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.net.wifi.ScanResult;

import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiEnterpriseConfig;
import android.net.wifi.WifiManager;

import android.net.wifi.WifiNetworkSpecifier;
import android.net.wifi.WifiNetworkSuggestion;

import android.net.wifi.hotspot2.PasspointConfiguration;
import android.os.Build;
import android.os.Bundle;

import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    Context context = this;
    TextView tv1;
    EditText e1, e2;
    Button b1;
    ImageView logo;
    float aa =100f;
    float mPrevX,mPrevY;
    boolean create_mode = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.build_layout);

        Button button  = (Button) findViewById(R.id.b1);
        final EditText e1 = (EditText) findViewById(R.id.E1);
        final EditText e2 = (EditText) findViewById(R.id.E2);
        final TextView tmp = (TextView) findViewById(R.id.tmp_text);
        Switch switch1 = (Switch) findViewById(R.id.tmp_switch);

        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                create_mode = isChecked;
            }
        });
        final FrameLayout frameLayout = (FrameLayout) findViewById(R.id.layout_tmp);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                //layoutParams.gravity = Gravity.CENTER;
                Button a = new Button(context);
                a.setWidth(Function.toint(e1.getText().toString()));
                a.setHeight(Function.toint(e2.getText().toString()));
                a.setText("BUTTON 1");

                a.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (create_mode) {
                            float currX, currY;
                            switch (event.getAction()) {
                                case MotionEvent.ACTION_DOWN:
                                    //tmp.setText("ACTION_DOWN"+ event.getRawX() + " X "+ event.getRawY()+"\n"+v.getX() +" X "+ v.getY());
                                    mPrevX = event.getX();
                                    mPrevY = event.getY();
                                    break;
                                case MotionEvent.ACTION_MOVE:
                                    //tmp.setText("ACTION_MOVE"+ event.getX() + " X "+ event.getY()+"\n"+v.getX() +" X "+ v.getY());
                                    currX = event.getRawX();
                                    currY = event.getRawY();

                                    v.setX(currX - mPrevX);
                                    v.setY(currY - mPrevY);

                                    break;
                                case MotionEvent.ACTION_UP:
                                    //tmp.setText("ACTION_UP"+ event.getX() + " X "+ event.getY()+"\n"+v.getX() +" X "+ v.getY());

                                    // v.setY(event.getRawY());
                                    // v.setX(event.getRawX());
                                    break;
                            }
                        }
                        return false;
                }});
                a.setX(aa);
                a.setY(0f);
                aa+=10f;
                frameLayout.addView(a,layoutParams);
            }
        });

        /*final LoadingDialog loadingDialog = new LoadingDialog(MainActivity.this);
        loadingDialog.show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadingDialog.dismiss();
            }
        },2000);*/
/*
        logo = findViewById(R.id.main_logo);
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                startActivity(intent);
            }
        });*/
    }

    public void Connect_wifi_b(View view) {
        Intent intent = new Intent(MainActivity.this, Connect_to_wifi.class);
        startActivity(intent);
    }

    public void Login_b(View view) {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}


