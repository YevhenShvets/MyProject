package com.yevhen.project;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.yevhen.project.Class.Users;
import com.yevhen.project.Class.Users_log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyResponse extends Application {
    private Context context;
    private Intent intent;
    private Retrofit retrofit;
    private JsonApi jsonApi;


    public static Users user;

    MyResponse(Context context, String url, Intent intent){
        this.context = context;
        this.intent = intent;
    }

    public Users getUser() {
        return user;
    }

    public void POST_LOGIN(String email, String pass){
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
                            user = response.body();
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

    public void getToken(String id, String access_token){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://34.76.99.94:5050")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonApi jsonApi = retrofit.create(JsonApi.class);

        Call<JsonObject> call = jsonApi.getToken(id,access_token);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.body()!=null){

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
            }
        });
    }

    public void putToken(String id,String access_token,String token){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://34.76.99.94:5050")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonApi jsonApi = retrofit.create(JsonApi.class);
        JsonObject token_ = new JsonObject();
        token_.addProperty("fcm_token",token);
        Call<Void> call = jsonApi.putToken(id,access_token,token_);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }
}
