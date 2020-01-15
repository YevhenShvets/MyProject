package com.yevhen.project;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface JsonApi {
    @POST("users/sendVerificationCode")
    Call<Void> createVercode(@Body Email_reg email);

    @POST("users")
    Call<Void> createUsers(@Body Users_reg users);

    @Headers("Content-Type: application/json")
    @POST("users/auth")
    Call<Users> getAuthUsers(@Body Users_log users);

    @POST("options")
    Call<Void> setHub(@Query("a")String a,
                      @Query("b")String b,
                      @Query("c")String c,
                      @Query("d")String d,
                      @Query("e")String e);
}
