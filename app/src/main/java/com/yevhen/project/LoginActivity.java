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

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.yevhen.project.Class.Token;
import com.yevhen.project.Class.Users;
import com.yevhen.project.Class.Users_log;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    private Context context = this;

    private TextView registr_text;
    private TextView error_text;
    private Button login_button;
    private EditText email;
    private EditText pass;
    private CheckBox save_check;

    private Retrofit retrofit;
    private JsonApi jsonApi;

    private String email_text,pass_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        //кнопка авторизації
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
                        File file = new File(context);
                        file.saveLogin(email.getText().toString(),pass.getText().toString());
                    }

                    //авторизовуємося
                    email_text = email.getText().toString();
                    pass_text = pass.getText().toString();
                    retrofit = new Retrofit.Builder()
                            .baseUrl("http://34.76.99.94:5050")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    jsonApi = retrofit.create(JsonApi.class);

                    Call<Users> call = jsonApi.getAuthUsers(new Users_log(email_text, pass_text));
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
                                        MyResponse.user = response.body();
                                        start_main_activity();
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
            }
        });

    }

    private void start_main_activity(){
        Intent intent = new Intent(LoginActivity.this,Main2Activity.class);
        startActivity(intent);
        finish();
    }


    //робить аткивність кнопок перевіряючи едіти
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

}
