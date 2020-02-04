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

    MyResponse(Context context, String url){
        this.context = context;
        this.intent = intent;
    }

    public Users getUser() {
        return user;
    }

    public void POST_LOGIN(String email, String pass){

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
