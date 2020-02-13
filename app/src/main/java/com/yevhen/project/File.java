package com.yevhen.project;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

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
    private static String PLAN_ROOM = "PLAN_ROOM";
    private static String ROOM_URI = "ROOM_URI";
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

    //BITMAP
    private static String encodeTobase64(Bitmap image) {
        Bitmap immage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.PNG,40 ,baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        return imageEncoded;
    }
    private static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }


    public void set_room_uri(Bitmap uri){
        myPreferences = context.getSharedPreferences(PLAN_ROOM,MODE_PRIVATE);
        SharedPreferences.Editor editor = myPreferences.edit();
        editor.putString(ROOM_URI, encodeTobase64(uri));
        editor.apply();
    }
    public Bitmap get_room_uri(){
        myPreferences = context.getSharedPreferences(PLAN_ROOM,MODE_PRIVATE);
        String a = myPreferences.getString(ROOM_URI,"");
        return decodeBase64(myPreferences.getString(ROOM_URI,""));
    }

}
