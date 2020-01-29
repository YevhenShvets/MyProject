package com.yevhen.project;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.yevhen.project.Class.Info_arduino;
import com.yevhen.project.Class.Wifi;

import java.util.ArrayList;
import java.util.List;
import com.yevhen.project.Connect_to_wifi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */

public class Connect_hub_fragment extends Fragment {
    View view;
    WifiManager wifiManager;
    List<Wifi> listWifi;
    ListView wifi_list;
    Button upg;
    Button open_settings;
    TextView text_error_list;

    static int REQUEST_WIFI_STATE = 1;


    public Connect_hub_fragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_1, container, false);

        upg = (Button)view.findViewById(R.id.connect_wifi_upg);
        open_settings = (Button) view.findViewById(R.id.connect_wifi_open_settings_button);
        text_error_list = (TextView) view.findViewById(R.id.connect_error_text);
        ConnectWifi();

        open_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                startActivityForResult(intent,REQUEST_WIFI_STATE);
            }
        });
        upg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LoadingDialog ld = new LoadingDialog(getActivity());
                ld.show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ld.dismiss();
                        ConnectWifi();
                    }},3000);
            }
        });

        return view;
    }

    @SuppressLint("WifiManagerLeak")
    public void ConnectWifi(){
        //WifiMANAGER
        wifiManager = (WifiManager)  getContext().getSystemService(Context.WIFI_SERVICE);
        final WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        //створення списку вайфай
        if(!wifiManager.isWifiEnabled()){
            Toast.makeText(getContext(),"Wifi не ввімкнений, але ми ввімкнули", Toast.LENGTH_LONG).show();
            wifiManager.setWifiEnabled(true);
        }

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
       // registerReceiver(wifiScanReceiver, intentFilter);
        boolean s =  wifiManager.startScan();

        Wifi wifi = new Wifi(1,"");
        listWifi = new ArrayList<>();
        List<ScanResult> a = wifiManager.getScanResults();
        wifi_list = (ListView) view.findViewById(R.id.connect_wifi_list);
        if(a.size()==0){
            text_error_list.setVisibility(View.VISIBLE);
            MyAdapter adapter = new MyAdapter(getContext(), R.layout.list_item, listWifi);
            wifi_list.setAdapter(adapter);
        }else {
            text_error_list.setVisibility(View.INVISIBLE);
            int img = 1;
            for (ScanResult scanResult : a) {
                if (scanResult.level >= -50) img = R.drawable.wifi_icons_1;
                else if (scanResult.level >= -60) img = R.drawable.wifi_icons_2;
                else if (scanResult.level >= -70) img = R.drawable.wifi_icons_3;
                else if (scanResult.level < -70) img = R.drawable.wifi_icons_4;
                wifi = new Wifi(img, scanResult.SSID);
                listWifi.add(wifi);
            }

            MyAdapter adapter = new MyAdapter(getContext(), R.layout.list_item, listWifi);
            wifi_list.setAdapter(adapter);

            wifi_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    List<ScanResult> wifiManagerScanResults = wifiManager.getScanResults();
                    ScanResult scan = wifiManagerScanResults.get(position);
                    String s1 = "", s;
                    s = wifiManager.getConnectionInfo().getSSID().toString();
                    for (int i = 1; i < s.length() - 1; i++) {
                        s1 = s1 + s.charAt(i);
                    }
                    if (s1.equals(scan.SSID.toString())) {
                        Toast.makeText(getContext(), "Ви вибрали підключений WIFI", Toast.LENGTH_LONG).show();
                    } else {
                        //Створюємо діалогове вікно для підключення до HUB
                        Dialog_connect_hub cdd = new Dialog_connect_hub(getContext(), scan.SSID.toString(), scan.capabilities);
                        cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        cdd.show();
                    }
                }
            });
        }
    }


    BroadcastReceiver wifiScanReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context c, Intent intent) {
            boolean success = intent.getBooleanExtra(
                    WifiManager.EXTRA_RESULTS_UPDATED, false);
            if (success) {
            }
            else {
                Toast.makeText(c,"Помилка", Toast.LENGTH_LONG).show();
            }

        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //Перевірка, чи підключено до WIFI
        if(requestCode == REQUEST_WIFI_STATE){
            WifiManager wifiManager = (WifiManager) getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            if(wifiManager!=null) {
                if (wifiManager.getConnectionInfo().getSSID() != "<unknown ssid>") {
                    Toast.makeText( getContext(), "Підключено до " + wifiManager.getConnectionInfo().getSSID(), Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText( getContext(), "Ви не підключилися", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText( getContext(), "Виникла помилка, повторно підключіться до WIFI", Toast.LENGTH_SHORT).show();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

}

