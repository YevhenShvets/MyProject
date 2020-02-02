package com.yevhen.project;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public final class File extends Application {
    public File(Context context) {
        this.context = context;
    }

    private Context context;
    private static String LOGIN_NAME_FILE = "LOGIN";
    private static String LOGIN_EMAIL = "LOGIN_EMAIL";
    private static String LOGIN_PASS = "LOGIN_PASS";
    private static String IS_LOGIN = "LOGIN_SAVE";
    private SharedPreferences myPreferences;

    public boolean is_login_save(){
        myPreferences = context.getSharedPreferences(LOGIN_NAME_FILE,MODE_PRIVATE);
        return myPreferences.getBoolean(IS_LOGIN,false);
    }


    public void saveLogin(String email,String pass){
        myPreferences = context.getSharedPreferences(LOGIN_NAME_FILE,MODE_PRIVATE);
        SharedPreferences.Editor editor = myPreferences.edit();

        editor.putBoolean(IS_LOGIN,true);
        editor.putString(LOGIN_EMAIL,email);
        editor.putString(LOGIN_PASS,pass);
        editor.apply();
    }

    public String getLOGIN(){
        myPreferences = context.getSharedPreferences(LOGIN_NAME_FILE,MODE_PRIVATE);
        if(is_login_save()){
            String e,p;
            e= myPreferences.getString(LOGIN_EMAIL,"");
            p = myPreferences.getString(LOGIN_PASS,"");
            return e+";"+p;
        }else{
            return "EROOR";
        }
    }

    public void Drop_login() {
        myPreferences = context.getSharedPreferences(LOGIN_NAME_FILE, MODE_PRIVATE);
        SharedPreferences.Editor editor = myPreferences.edit();
        editor.clear();
        editor.commit();
    }

    public static void putToken(){

    }

}
