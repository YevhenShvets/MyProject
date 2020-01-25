package com.yevhen.project;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.widget.Button;
import android.widget.EditText;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;

public final class Function extends Application {

    public boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public final static String getApIpAddr(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        DhcpInfo dhcpInfo = wifiManager.getDhcpInfo();
        byte[] ipAddress = convert2Bytes(dhcpInfo.serverAddress);
        try {
            String apIpAddr = InetAddress.getByAddress(ipAddress).getHostAddress();
            return apIpAddr;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }

    public final static String getIp(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        byte[] ipAddress = convert2Bytes(wifiInfo.getIpAddress());
        try {
            String apIpAddr = InetAddress.getByAddress(ipAddress).getHostAddress();
            return apIpAddr;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static byte[] convert2Bytes(int hostAddress) {
        byte[] addressBytes = { (byte)(0xff & hostAddress),
                (byte)(0xff & (hostAddress >> 8)),
                (byte)(0xff & (hostAddress >> 16)),
                (byte)(0xff & (hostAddress >> 24)) };
        return addressBytes;
    }

    private String getIPv4Address(int ipAddress) {
        // convert integer ip to a byte array
        byte[] tempAddress = BigInteger.valueOf(ipAddress).toByteArray();
        int size = tempAddress.length;
        // reverse the content of the byte array
        for(int i = 0; i < size/2; i++) {
            byte temp = tempAddress[size-1-i];
            tempAddress[size-1-i] = tempAddress[i];
            tempAddress[i] = temp;
        }
        try {
            // get the IPv4 formatted ip from the reversed byte array
            InetAddress inetIP = InetAddress.getByAddress(tempAddress);
            return inetIP.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return "";
    }

    static boolean isValue(String str, char ch){
        for(int i=0;i<str.length();i++){
            if(str.charAt(i)==ch) return true;
        }
        return false;
    }

    static void setEnabled_button(Button button, Boolean bool){
        if(button ==null) return;
        else{
            if(bool == true) {
                button.setEnabled(true);
                button.setAlpha(1f);
            }else{
                button.setEnabled(false);
                button.setAlpha(0.6f);
            }
        }
    }
    static boolean isText(EditText editText){
        if(editText==null) return false;
        else{
            if(editText.length()>0) return true;
        }
        return false;
    }

    static boolean notSpec(char a){
        switch (a){
            case '1':
            case '2':
                case '3':
            case '4':
                case '5':
            case '6':
                case '7':
            case '8':
                case '9':
            case '0': return false;
        }
        return true;
    }

}
