package com.yevhen.project;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        //Log.i("Firebase", "Message was received!");
    }

    @Override
    public void onNewToken(@NonNull String s) {
        //MyResponse myResponse = new MyResponse(getApplicationContext(),"");
       // myResponse.putToken(SplashScreenActivity.user.getId(),SplashScreenActivity.user.getAccessToken(),s);
    }



}
