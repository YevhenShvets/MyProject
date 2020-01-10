package com.yevhen.project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.yevhen.project.Class.Email_reg;
import com.yevhen.project.Class.Users_reg;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.List;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reg_layout);

        post_email_edit = (EditText) findViewById(R.id.reg_email_post_edit);
        post_vercode_button = (Button) findViewById(R.id.reg_email_post_button);

        post_vercode_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = post_email_edit.getText().toString();
                openFragment(email);
            }
        });

    }

    void Reg(){
        retrofit = new Retrofit.Builder()
                .baseUrl("http://34.76.99.94:5050")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jsonApi = retrofit.create(JsonApi.class);

        post_vercode_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = post_email_edit.getText().toString();
                Call<Void> call = jsonApi.createVercode(new Email_reg(email));
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(!response.isSuccessful()) {
                            Toast.makeText(context,"Помилка",Toast.LENGTH_SHORT).show();
                        }
                        int code = response.code();
                        switch (code){
                            case 202: Toast.makeText(context,"Успішно надіслано, перевірте пошту",Toast.LENGTH_LONG).show(); break;
                            case 400:Toast.makeText(context,"400",Toast.LENGTH_LONG).show(); break;
                            case 409: Toast.makeText(context,"409",Toast.LENGTH_LONG).show(); break;
                            case 500: Toast.makeText(context,"500",Toast.LENGTH_LONG).show(); break;
                        }

                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        // Toast.makeText(context,"",Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        //РЕЄСРАЦІЯ
       /* reg_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                        if(!response.isSuccessful()){
                            Toast.makeText(context,"Помилка",Toast.LENGTH_LONG).show();
                        }
                        int code = response.code();
                        switch (code){
                            case 201: Toast.makeText(context,"Успішно зареєстровано",Toast.LENGTH_LONG).show(); break;
                            case 400: Toast.makeText(context,"400",Toast.LENGTH_LONG).show(); break;
                            case 401: Toast.makeText(context,"Email not vercode 401",Toast.LENGTH_LONG).show(); break;
                            case 500: Toast.makeText(context,"500",Toast.LENGTH_LONG).show(); break;
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                    }
                });
            }
        });*/
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
}
