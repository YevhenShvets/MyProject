package com.yevhen.project;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().name("MyFile.realm").build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }
}
