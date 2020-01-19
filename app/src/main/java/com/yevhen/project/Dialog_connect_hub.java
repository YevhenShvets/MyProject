package com.yevhen.project;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.yevhen.project.Class.Info_arduino;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Dialog_connect_hub extends Dialog implements View.OnClickListener {

    private Activity c;
    private Dialog d;
    private EditText ssid_dialog;
    private EditText pass_dialog;
    private Button buttonOk;
    private TextView textcapabilities;
    private Button buttonClose;
    private ProgressBar loading;
    private ImageView show_pass;
    private ImageView pass_icon;
    private String ssid,capa;
    private Boolean ispass;

    public Dialog_connect_hub(@NonNull Context context, String ssid, String capa) {
        super(context);
        this.ssid = ssid;
        this.capa = capa;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.wifi_dialog);
        WindowManager.LayoutParams a = new WindowManager.LayoutParams();
        a.copyFrom(getWindow().getAttributes());
        a.width = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(a);

        buttonOk = (Button) findViewById(R.id.wifi_dialog_buttonOk);
        buttonClose = (Button) findViewById(R.id.wifi_dialog_buttonClose);
        pass_dialog = (EditText) findViewById(R.id.wifi_dialog_pass);
        ssid_dialog = (EditText) findViewById(R.id.wifi_dialog_ssid);
        textcapabilities = (TextView) findViewById(R.id.wifi_dialog_capabilities);
        loading = (ProgressBar) findViewById(R.id.wifi_dialog_progressbar);
        show_pass = (ImageView) findViewById(R.id.wifi_dialog_show_wifi_icon);
        pass_icon = (ImageView) findViewById(R.id.wifi_dialog_pass_icon);

        ssid_dialog.setText(ssid);
        textcapabilities.setText(capa);
        ispass = false;

        buttonClose.setOnClickListener(this);
        buttonOk.setOnClickListener(this);
        show_pass.setOnClickListener(this);

        pass_dialog.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(pass_dialog.length()>0){
                    pass_icon.setAlpha(1f);
                    show_pass.setAlpha(1f);
                }else{
                    pass_icon.setAlpha(0.5f);
                    show_pass.setAlpha(0.5f);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.wifi_dialog_buttonClose:{ dismiss(); pass_dialog.setText(""); break;}
            case R.id.wifi_dialog_buttonOk: {
                Info_arduino info_arduino;
                String ssid, pass = "", capabilities = "";
                pass = pass_dialog.getText().toString();
                ssid = ssid_dialog.getText().toString();
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
                break;
            }
            case R.id.wifi_dialog_show_wifi_icon: {
                if(show_pass.getAlpha()<1f) break;
                else {
                    if (ispass) {
                        pass_dialog.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        show_pass.setImageResource(R.drawable.wifi_dialog_pass_show_icon);
                        ispass = false;
                        break;
                    }
                    if (!ispass) {
                        pass_dialog.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        show_pass.setImageResource(R.drawable.wifi_dialog_pass_noshow_icon);
                        ispass = true;
                        break;
                    }
                }

            }
            default:
                dismiss(); break;
        }
    }
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
                    Toast.makeText(getContext(),"Дані успішно передані, подивіться на екран",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(),"Помилка: "+ t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}
