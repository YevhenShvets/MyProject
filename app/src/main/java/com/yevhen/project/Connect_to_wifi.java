package com.yevhen.project;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;

import android.net.ConnectivityManager;
import android.net.MacAddress;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.net.wifi.ScanResult;

import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiEnterpriseConfig;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import android.net.wifi.WifiNetworkSpecifier;
import android.net.wifi.WifiNetworkSuggestion;

import android.net.wifi.hotspot2.PasspointConfiguration;
import android.os.Build;
import android.os.Bundle;

import android.os.Handler;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class Connect_to_wifi extends AppCompatActivity {
    WifiManager wifiManager;
    Context context = this;
    List<Wifi> listWifi;
    ListView wifi_list;
    Button upg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_1);


        ConnectWifi();
        upg = (Button)findViewById(R.id.connect_wifi_upg);
        upg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectWifi();
            }
        });
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
        for (ScanResult scanResult : a) {
            wifi = new Wifi(R.drawable.wifi_image,scanResult.SSID);
            listWifi.add(wifi);
        }
        //ваіа

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
            } });
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ssid, pass = "", capabilities = "";
                pass = pass_dialog.getText().toString();
                ssid = ssid_dialog.getText().toString();
                capabilities = textcapabilities.getText().toString();
                Connect_to_wifi(ssid, pass, capabilities);
                SystemClock.sleep(5000);
                String s1 ="",s;
                s = wifiManager.getConnectionInfo().getSSID().toString();
                for(int i = 1;i<s.length()-1;i++){
                    s1= s1+ s.charAt(i);
                }
                 if(s1.equals(ssid_dialog.getText().toString())){
                     dialog.dismiss();
                     Toast.makeText(context, "Підключено", Toast.LENGTH_LONG).show();
                 } else {
                     Toast.makeText(context, "Перевірте введені дані", Toast.LENGTH_LONG).show();
                 }
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
                   /* Wifi wifi = new Wifi(1,"");
                    tv1 = findViewById(R.id.tv1);
                    List<ScanResult>  a = wifiManager.getScanResults();
                    for (ScanResult scanResult : a) {
                        wifi = new Wifi(R.mipmap.ic_launcher,scanResult.SSID);
                        listWifi.add(wifi);
                    }*/
            }
            else {
                Toast.makeText(c,"Помилка  ", Toast.LENGTH_LONG).show();
            }

        }
    };
}
