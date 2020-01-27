package com.yevhen.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yevhen.project.Class.Users;
import com.yevhen.project.Class.Users_log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private static String LOGIN_NAME_FILE = "LOGIN";
    private static String LOGIN_EMAIL = "LOGIN_EMAIL";
    private static String LOGIN_PASS = "LOGIN_PASS";
    private static String IS_LOGIN = "LOGIN_SAVE";
    private String email_text, pass_text;


    private Context context = this;
    private TextView registr_text;
    private TextView error_text;
    private Button login_button;
    private EditText email;
    private EditText pass;
    private CheckBox save_check;
    private Retrofit retrofit;
    private JsonApi jsonApi;
    private Users user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(is_login_save()){
            boolean b=false;
            email_text="";
            pass_text="";
            String email_pass = getLOGIN();
            //Розірвання логіну від пароля
            for(int i=0;i<email_pass.length();i++){
                if(email_pass.charAt(i)==';') {
                    b=true;
                    i++;
                }
                if(b==false) {
                    email_text+=email_pass.charAt(i);
                }
                else {
                    pass_text+=email_pass.charAt(i);
                }
            }
            POST_LOGIN(email_text,pass_text);
        }else {
            setContentView(R.layout.activity_login);

            registr_text = (TextView) findViewById(R.id.login_registration);
            login_button = (Button) findViewById(R.id.logib_button);
            email = (EditText) findViewById(R.id.login_email);
            pass = (EditText) findViewById(R.id.login_pass);
            error_text = (TextView) findViewById(R.id.login_error_text);
            save_check = (CheckBox) findViewById(R.id.login_save);
            Function.setEnabled_button(login_button, false);
            email.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    change(email, pass, error_text, 1);
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            pass.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    change(email, pass, error_text, 2);
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            //відкрити реєстрацію
            registr_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                    startActivity(intent);
                }
            });
            login_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Якщо є доступ до інтернету
                    if (new Function().isOnline(context)) {
                        //Затримка, щоб не було багато раз клікало
                        login_button.setClickable(false);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                login_button.setClickable(true);
                            }
                        }, 2000);
                        //зберігаємо дані користувача, якщо ми обрали галочку
                        if (save_check.isChecked()) {
                            saveLogin(email.getText().toString(),pass.getText().toString());
                        }
                        //авторизовуємося
                        email_text = email.getText().toString();
                        pass_text = pass.getText().toString();
                        POST_LOGIN(email_text, pass_text);
                    }
                }
            });
        }
    }

    private void POST_LOGIN(String email,String pass){
        retrofit = new Retrofit.Builder()
                .baseUrl("http://34.76.99.94:5050")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonApi = retrofit.create(JsonApi.class);

        Call<Users> call = jsonApi.getAuthUsers(new Users_log(email, pass));
        call.enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {
                if (!response.isSuccessful()) {
                    int code = response.code();
                    switch (code) {
                        case 400:
                            Toast.makeText(context, "Деякі парами відсутні або були вказані неправильно (400)", Toast.LENGTH_SHORT).show();
                            break;
                        case 401:
                            Toast.makeText(context, "Неправильний пароль", Toast.LENGTH_SHORT).show();
                            break;
                        case 404:
                            Toast.makeText(context, "Помилка 404", Toast.LENGTH_SHORT).show();
                            break;
                        case 500:
                            Toast.makeText(context, "Щось пішло не так на стороні сервера (500)", Toast.LENGTH_SHORT).show();
                            break;
                    }
                } else {
                    if (response.code() == 200) {
                        Toast.makeText(context, "Авторизовано", Toast.LENGTH_SHORT).show();
                        if (response.body() != null) {
                            user = new Users(response.body().getId(),
                                    response.body().getUsername(),
                                    response.body().getEmail(),
                                    response.body().getFcmToken(),
                                    response.body().getHubId(),
                                    response.body().getAccessToken(),
                                    response.body().getRefreshToken());
                            Intent intent = new Intent(LoginActivity.this,Main2Activity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }
            }
            @Override
            public void onFailure(Call<Users> call, Throwable t) {
                Toast.makeText(context, "Помилка\nПеревірте підключення до інтернету", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void change(EditText e1, EditText e2,TextView error,int i) {
        if (e1 != null && e2 != null && error != null){
            if (e1.length() == 0 && e2.length() == 0) error.setText("");
            if(i==1){
                //EMAIL
                if( Function.isValue(e1.getText().toString(),'@') && Function.isValue(e1.getText().toString(),'.'))
                {
                    if(e2.length()!=0) {
                        Function.setEnabled_button(login_button, true);
                        error.setText("");
                    }else{
                        error.setText("Поле \"password\" пусте");
                    }
                }
                else{
                    error.setText("Перевірте email");
                    if(login_button.isEnabled())
                        Function.setEnabled_button(login_button,false);
                }
            }else{
                //PASS
                if(e2.length()==0) {
                    Function.setEnabled_button(login_button,false);
                    error.setText("Поле \"password\" пусте");
                }
                else{
                    if(Function.isValue(e1.getText().toString(),'@') && Function.isValue(e1.getText().toString(),'.')) {
                        Function.setEnabled_button(login_button,true);
                        error.setText("");
                    }else{
                        error.setText("Перевірте email");
                    }
                }
            }
        }else return;
    }

    private boolean is_login_save(){
        SharedPreferences myPreferences = getSharedPreferences(LOGIN_NAME_FILE,MODE_PRIVATE);
        return myPreferences.getBoolean(IS_LOGIN,false);
    }


    private void saveLogin(String email,String pass){
        SharedPreferences myPreferences = getSharedPreferences(LOGIN_NAME_FILE,MODE_PRIVATE);
        SharedPreferences.Editor editor = myPreferences.edit();

        editor.putBoolean(IS_LOGIN,true);
        editor.putString(LOGIN_EMAIL,email);
        editor.putString(LOGIN_PASS,pass);
        editor.apply();
    }

    private String getLOGIN(){
        SharedPreferences myPreferences = getSharedPreferences(LOGIN_NAME_FILE,MODE_PRIVATE);
        if(is_login_save()){
            String e,p;
            e= myPreferences.getString(LOGIN_EMAIL,"");
            p = myPreferences.getString(LOGIN_PASS,"");
          return e+";"+p;
        }else{
           return "EROOR";
        }
    }


}
