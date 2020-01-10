package com.yevhen.project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

class MyAdapter extends ArrayAdapter<Wifi> {
    Context context;
    int resource;
    List<Wifi> wifiList;

    public MyAdapter(Context context,int resource,List<Wifi> wifiList){
        super(context,resource,wifiList);
        this.context = context;
        this.resource = resource;
        this.wifiList = wifiList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(resource,null);
        ImageView img = view.findViewById(R.id.wifi_item_img);
        TextView ssid = view.findViewById(R.id.wifi_item_ssid);
        Wifi wifi = wifiList.get(position);
        img.setImageDrawable(context.getResources().getDrawable(wifi.getImg(),null));
        ssid.setText(wifi.getSsid());

        return view;
    }
}