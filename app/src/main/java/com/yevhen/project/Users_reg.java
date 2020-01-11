package com.yevhen.project;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Users_reg {
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("verification_code")
    @Expose
    private String vercode;

    public Users_reg(String username, String email, String vercode, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.vercode = vercode;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getVercode() {
        return vercode;
    }
}


