package com.yevhen.project;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;


public interface JsonApi {
    @POST("users/sendVerificationCode")
    Call<Void> createVercode(@Body Email_reg email);

    @POST("users")
    Call<Void> createUsers(@Body Users_reg users);

    @Headers("Content-Type: application/json")
    @POST("users/auth")
    Call<Users> getAuthUsers(@Body Users_log users);

    @POST
    Call<Void> setHub(@Body String str);
}
