package com.yevhen.project;

import com.google.gson.annotations.SerializedName;

public class Users_log {
    @SerializedName("email")
    private String email;
    @SerializedName("password")
    private String pass;

    public Users_log(String email, String pass) {
        this.email = email;
        this.pass = pass;
    }

    public String getEmail() {
        return email;
    }

    public String getPass() {
        return pass;
    }
}

