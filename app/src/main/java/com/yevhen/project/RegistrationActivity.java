package com.yevhen.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.yevhen.*;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegistrationActivity extends AppCompatActivity implements RegistrationFragment.OnFragmentInteractionListener {
    Context context = this;
    FrameLayout fragment_framelayout;
    Button post_vercode_button;
    EditText post_email_edit;
    JsonApi jsonApi;
    Retrofit retrofit;
    String email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reg_layout);

        post_email_edit = (EditText) findViewById(R.id.reg_email_post_edit);
        post_vercode_button = (Button) findViewById(R.id.reg_email_post_button);

        retrofit = new Retrofit.Builder()
                .baseUrl("http://34.76.99.94:5050")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jsonApi = retrofit.create(JsonApi.class);

        post_vercode_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(new Function().isOnline(context)) {
                    //Затримка, щоб на кнопку не можна було нажати багато разів
                    post_vercode_button.setClickable(false);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            post_vercode_button.setClickable(true);
                        }
                    }, 2000);

                    email = post_email_edit.getText().toString();
                    Call<Void> call = jsonApi.createVercode(new Email_reg(email));
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (!response.isSuccessful()) {
                                int code = response.code();
                                switch (code) {
                                    case 400:
                                        Toast.makeText(context, "Деякі парами відсутні або були вказані неправильно (400)", Toast.LENGTH_LONG).show();
                                        break;
                                    case 409:
                                        Toast.makeText(context, "На даний email вже створений акаунт", Toast.LENGTH_LONG).show();
                                        break;
                                    case 500:
                                        Toast.makeText(context, "Щось пішло не так на стороні сервера (500)", Toast.LENGTH_LONG).show();
                                        break;
                                }
                            } else {
                                if(response.code() == 202) {
                                    Toast.makeText(context, "Повідомлення надіслано, перевірте пошту", Toast.LENGTH_LONG).show();
                                    openFragment(email);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(context, "Помилка\nПеревірте підключення до інтернету", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    Toast.makeText(context, "Перевірте підключення до інтернету", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void openFragment(String email){
        RegistrationFragment fragment = RegistrationFragment.newInstance(email);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_right,R.anim.enter_from_right,R.anim.exit_to_right);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.add(R.id.registration_framelayout,fragment,"Reg_END").commit();
    }

    @Override
    public void onFragmentInteraction() {
        post_email_edit.setText("");
        onBackPressed();
    }


    public void REGISTER_ONCLICK(View view) {
        if(new Function().isOnline(context)) {
            EditText username = (EditText) findViewById(R.id.registration_username_edit);
            EditText email = (EditText) findViewById(R.id.registration_email_edit);
            EditText varcode = (EditText) findViewById(R.id.registration_varcode_edit);
            EditText password = (EditText) findViewById(R.id.registration_pass_edit);

            Users_reg user = new Users_reg(username.getText().toString(),
                    email.getText().toString(),
                    varcode.getText().toString(),
                    password.getText().toString());
            Call<Void> call = jsonApi.createUsers(user);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (!response.isSuccessful()) {
                        int code = response.code();
                        switch (code) {
                            case 400:
                                Toast.makeText(context, "Деякі парами відсутні або були вказані неправильно (400)", Toast.LENGTH_LONG).show();
                                break;
                            case 401:
                                Toast.makeText(context, "Перевірте дані (код)", Toast.LENGTH_LONG).show();
                                break;
                            case 500:
                                Toast.makeText(context, "Щось пішло не так на стороні сервера (500)", Toast.LENGTH_LONG).show();
                                break;
                        }
                    }else {
                        if(response.code() == 201)
                            Toast.makeText(context, "Зареєстровано", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {

                }
            });
        }else{
            Toast.makeText(context, "Перевірте підключення до інтернету", Toast.LENGTH_SHORT).show();
        }
    }
}
