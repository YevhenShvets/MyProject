package com.yevhen.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.yevhen.project.Class.Users;
import com.yevhen.project.Class.Users_log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    Context context = this;
    TextView registr_text;
    Button login_button;
    Retrofit retrofit;
    JsonApi jsonApi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        retrofit = new Retrofit.Builder()
                .baseUrl("http://34.76.99.94:5050")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jsonApi = retrofit.create(JsonApi.class);

        registr_text = (TextView)findViewById(R.id.login_registration);
        login_button = (Button) findViewById(R.id.logib_button);

        registr_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegistrationActivity.class);
                startActivity(intent);
            }
        });
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText email = (EditText) findViewById(R.id.login_email);
                EditText pass = (EditText) findViewById(R.id.login_pass);

                Call<Users> call = jsonApi.getAuthUsers( new Users_log(email.getText().toString(),pass.getText().toString()));
                call.enqueue(new Callback<Users>() {
                    @Override
                    public void onResponse(Call<Users> call, Response<Users> response) {
                        if(!response.isSuccessful()){
                            Toast.makeText(context,"Помилка",Toast.LENGTH_SHORT).show();
                        }
                        int code = response.code();
                        if(response.body()!=null) {
                            Users user = response.body();
                            //String bod = response.body().toString();
                        }
                        switch (code){
                            case 200: Toast.makeText(context,"Авторизовані",Toast.LENGTH_SHORT).show();break;
                            case 400: Toast.makeText(context,"400",Toast.LENGTH_SHORT).show();break;
                            case 401: Toast.makeText(context,"401 no password",Toast.LENGTH_SHORT).show();break;
                            case 404: Toast.makeText(context,"404",Toast.LENGTH_SHORT).show();break;
                            case 500: Toast.makeText(context,"500",Toast.LENGTH_SHORT).show();break;
                        }
                       // Users users = response.body();
                    }

                    @Override
                    public void onFailure(Call<Users> call, Throwable t) {

                    }
                });

            }
        });
    }


}
