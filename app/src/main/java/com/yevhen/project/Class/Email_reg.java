package com.yevhen.project.Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Email_reg{
    @SerializedName("email")
    @Expose
    private String email;

    public Email_reg(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}