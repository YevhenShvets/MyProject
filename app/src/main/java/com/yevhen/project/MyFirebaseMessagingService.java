package com.yevhen.project;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        //Log.i("Firebase", "Message was received!");
    }

    @Override
    public void onNewToken(@NonNull String s) {
      //  Log.i("Firebase", "New token is " + s);
    }

}
