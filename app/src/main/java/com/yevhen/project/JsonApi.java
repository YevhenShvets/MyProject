package com.yevhen.project;

import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.gson.JsonObject;
import com.yevhen.project.Class.Email_reg;
import com.yevhen.project.Class.Users;
import com.yevhen.project.Class.Users_log;
import com.yevhen.project.Class.Users_reg;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
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
    //@PUT
    @GET("users/{id}/fcmToken")
    Call<JsonObject> getToken(@Path("id") String id, @Query("access_token") String access_token);


    @PUT("users/{id}/fcmToken")
    Call<Void> putToken(@Path("id") String id, @Query("access_token") String access_token, @Query("fcmToken") JsonObject token);

}
