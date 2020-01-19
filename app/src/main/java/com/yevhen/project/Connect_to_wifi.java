package com.yevhen.project;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.net.wifi.ScanResult;

import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import android.os.Bundle;

import android.os.Handler;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.yevhen.project.Class.Info_arduino;
import com.yevhen.project.Class.Wifi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Connect_to_wifi extends AppCompatActivity {
    WifiManager wifiManager;
    Context context = this;
    List<Wifi> listWifi;
    ListView wifi_list;
    Button upg;
    Button open_settings;

    static int REQUEST_WIFI_STATE = 1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //Перевірка, чи підключено до WIFI
        if(requestCode == REQUEST_WIFI_STATE){
            WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            if(wifiManager!=null) {
                if (wifiManager.getConnectionInfo().getSSID() != "<unknown ssid>") {
                    Toast.makeText(Connect_to_wifi.this, "Підключено до " + wifiManager.getConnectionInfo().getSSID(), Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(Connect_to_wifi.this, "Ви не підключилися", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(Connect_to_wifi.this, "Виникла помилка, повторно підключіться до WIFI", Toast.LENGTH_SHORT).show();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_1);

        upg = (Button)findViewById(R.id.connect_wifi_upg);
        open_settings = (Button) findViewById(R.id.connect_wifi_open_settings_button);

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
                wifi_list.setVisibility(View.INVISIBLE);
                final LoadingDialog ld = new LoadingDialog(Connect_to_wifi.this);
                ld.show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        wifi_list.setVisibility(View.VISIBLE);
                        ld.dismiss();
                        ConnectWifi();
                    }},3000);
            }
        });
    }

    @Override
    protected void onResume() {
        ConnectWifi();
        super.onResume();
    }

    public void ConnectWifi(){
        //WifiMANAGER
        wifiManager = (WifiManager)  getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        final WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        //створення списку вайфай
        if(!wifiManager.isWifiEnabled()){
            Toast.makeText(context,"Wifi не ввімкнений, але ми ввімкнули", Toast.LENGTH_LONG).show();
            wifiManager.setWifiEnabled(true);
        }

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        registerReceiver(wifiScanReceiver, intentFilter);
        boolean s =  wifiManager.startScan();

        Wifi wifi = new Wifi(1,"");
        listWifi = new ArrayList<>();
        List<ScanResult> a = wifiManager.getScanResults();
        int img = 1;
        for (ScanResult scanResult : a) {
            if(scanResult.level>=-50) img = R.drawable.wifi_icons_1;
            else if(scanResult.level>=-60) img = R.drawable.wifi_icons_2;
            else if(scanResult.level>=-70) img = R.drawable.wifi_icons_3;
            else if(scanResult.level< -70) img= R.drawable.wifi_icons_4;
            wifi = new Wifi(img,scanResult.SSID);
            listWifi.add(wifi);
        }

        MyAdapter adapter = new MyAdapter(this,R.layout.list_item,listWifi);
        wifi_list = (ListView) findViewById(R.id.connect_wifi_list);
        wifi_list.setAdapter(adapter);

        //створення діалогово вікна
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.wifi_dialog);

        final EditText ssid_dialog = dialog.findViewById(R.id.wifi_dialog_ssid);
        final EditText pass_dialog = dialog.findViewById(R.id.wifi_dialog_pass);
        final Button buttonOk = dialog.findViewById(R.id.wifi_dialog_buttonOk);
        final Button buttonClose =  dialog.findViewById(R.id.wifi_dialog_buttonClose);
        final TextView textcapabilities = dialog.findViewById(R.id.wifi_dialog_capabilities);
        final ProgressBar loading = dialog.findViewById(R.id.wifi_dialog_progressbar);

        wifi_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                List<ScanResult> wifiManagerScanResults = wifiManager.getScanResults();
                ScanResult scan = wifiManagerScanResults.get(position);
                String s1 ="",s;
                s = wifiManager.getConnectionInfo().getSSID().toString();
                for(int i = 1;i<s.length()-1;i++){
                    s1= s1+ s.charAt(i);
                }
                if(s1.equals(scan.SSID.toString())){
                    Toast.makeText(context,"Вже підключено",Toast.LENGTH_LONG).show();
                }else {
                    ssid_dialog.setText(scan.SSID);
                    textcapabilities.setText(scan.capabilities);
                    if (!scan.capabilities.toUpperCase().contains("WPA")) {
                        pass_dialog.setVisibility(View.INVISIBLE);
                    } else {
                        pass_dialog.setVisibility(View.VISIBLE);
                    }

                    DisplayMetrics displayMetrics = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                    int displayHeight = displayMetrics.heightPixels;

                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();

                    lp.copyFrom(dialog.getWindow().getAttributes());
                    lp.height = (int) (displayHeight * 0.5f);
                    lp.width = WindowManager.LayoutParams.FILL_PARENT;
                    dialog.show();
                    dialog.getWindow().setAttributes(lp);
                }
            } });

        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                pass_dialog.setText("");
            } });
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Info_arduino info_arduino;
                String ssid, pass = "", capabilities = "";
                pass = pass_dialog.getText().toString();
                ssid = ssid_dialog.getText().toString();

                //треба удалити!!!
                capabilities = textcapabilities.getText().toString();

                info_arduino = new Info_arduino();
                //ssid
                info_arduino.setA(ssid);
                //password
                info_arduino.setB(pass);
                //ip_server
                info_arduino.setC("34.76.99.94");
                //port_server
                info_arduino.setD("8080");
                //_server_addres
                info_arduino.setE("/server-1.0/endpoint");



                Connect_HUB(info_arduino);

                //Затрамка
               /* loading.setVisibility(View.VISIBLE);
                buttonOk.setEnabled(false);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        loading.setVisibility(View.INVISIBLE);
                        buttonOk.setEnabled(true);
                        dialog.dismiss();
                    }
                }, 5000);*/
            }
        });
    }

    private void Connect_to_wifi(String SSID, String password,String Security){
        String TAG  = "TAGG";
        try {
            Log.d(TAG, "Item clicked, SSID " + SSID + " Security : " + Security);

            String networkSSID = SSID;
            String networkPass = password;

            WifiConfiguration conf = new WifiConfiguration();
            conf.SSID = "\"" + networkSSID + "\"";   // Please note the quotes. String should contain ssid in quotes
            conf.status = WifiConfiguration.Status.ENABLED;
            conf.priority = 40;

            if (Security.toUpperCase().contains("WEP")) {
                Log.v("rht", "Configuring WEP");
                conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                conf.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
                conf.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
                conf.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
                conf.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
                conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);

                if (networkPass.matches("^[0-9a-fA-F]+$")) {
                    conf.wepKeys[0] = networkPass;
                } else {
                    conf.wepKeys[0] = "\"".concat(networkPass).concat("\"");
                }

                conf.wepTxKeyIndex = 0;

            } else if (Security.toUpperCase().contains("WPA")) {
                Log.v(TAG, "Configuring WPA");

                conf.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
                conf.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
                conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
                conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);

                conf.preSharedKey = "\"" + networkPass + "\"";

            } else {
                Log.v(TAG, "Configuring OPEN network");
                conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                conf.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
                conf.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
                conf.allowedAuthAlgorithms.clear();
                conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            }

            WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            int networkId = wifiManager.addNetwork(conf);

            Log.v(TAG, "Add result " + networkId);

            List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
            for (WifiConfiguration i : list) {
                if (i.SSID != null && i.SSID.equals("\"" + networkSSID + "\"")) {
                    Log.v(TAG, "WifiConfiguration SSID " + i.SSID);

                    boolean isDisconnected = wifiManager.disconnect();
                    Log.v(TAG, "isDisconnected : " + isDisconnected);

                    boolean isEnabled = wifiManager.enableNetwork(i.networkId, true);
                    Log.v(TAG, "isEnabled : " + isEnabled);

                    boolean isReconnected = wifiManager.reconnect();
                    Log.v(TAG, "isReconnected : " + isReconnected);

                    break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
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

    private void Connect_HUB(Info_arduino info){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.1")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonApi jsonApi = retrofit.create(JsonApi.class);

        Call<Void> call = jsonApi.setHub(info.getA(),info.getB(),info.getC(),info.getD(),info.getE());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                int code = response.code();
                if(code == 200)
                    Toast.makeText(context,"Дані успішно передані, подивіться на екран",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context,"Помилка: "+ t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

}
