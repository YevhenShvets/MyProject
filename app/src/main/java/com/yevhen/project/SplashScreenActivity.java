package com.yevhen.project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.yevhen.project.Class.Users;
import com.yevhen.project.Class.Users_log;

import java.lang.reflect.Array;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SplashScreenActivity extends AppCompatActivity {
    private Context context = this;
    private File file;

    public ImageView logo;
    private AnimationDrawable animationDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        logo = (ImageView) findViewById(R.id.splash_logo);
        animation();

    /*//Сповіщення
        createNotificationChannel();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "32")
                .setSmallIcon(R.drawable.icon_settings)
                .setContentTitle("HALLO")
                .setContentText("HOOO dasfsdfsdwfe")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Mfdsfffswwgggegwgewgewegwgewegwegwwgewgewgegwe"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

// notificationId is a unique int for each notification that you must define
        notificationManager.notify(32, builder.build());

        builder = new NotificationCompat.Builder(this,"32")
                .setSmallIcon(R.drawable.icon_settings)
                .setContentTitle("2")
                .setContentText("HOOO 2")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("2"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        notificationManager.notify(31, builder.build());*/
//ENC
        char ch[];
        String st = "V7RZE";
        ch = new char[5];
        for(int i=0;i<5;i++){
            ch[i] = st.charAt(i);
        }
        Log.i("ENC",Encryption.Encrypt("ВАСЯ", ch));
        Log.i("ENC",Encryption.Decrypt("J2J5M6P6", ch));
//enc
        start();
    }


    private void animation() {
        logo.setBackgroundResource(R.drawable.splash_screen_animation);
        animationDrawable = (AnimationDrawable) logo.getBackground();
    }

    private void start_main_activity(){
        Intent intent = new Intent(SplashScreenActivity.this,Main2Activity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        animationDrawable.start();
    }

    private void start(){
        String email_text, pass_text, email_pass;

        //Збережена авторизація, автопідключення
        file = new File(context);
        if(file.is_login_save()) {

            //Розірвання логіну від пароля
            boolean b = false;
            email_text = "";
            pass_text = "";
            email_pass = file.getLOGIN();
            for (int i = 0; i < email_pass.length(); i++) {
                if (email_pass.charAt(i) == ';') {
                    b = true;
                    i++;
                }
                if (b == false) {
                    email_text += email_pass.charAt(i);
                } else {
                    pass_text += email_pass.charAt(i);
                }
            }
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://34.76.99.94:5050")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            JsonApi jsonApi = retrofit.create(JsonApi.class);

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
                                Log.i("USERS",response.body().getFcmToken());
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
        else{
            Intent intent = new Intent(SplashScreenActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("FIREBASE", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        Log.i("FIREBASE",token);

                        // Log and toast
                        String msg = token;
                        Log.d("FIREBASE", msg);
                        //MyFirebaseMessagingService.putToken(user.getId(),user.getAccessToken(),msg);
                    }

                });
    }


    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("32", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
