package com.yevhen.project.Class;

public class Wifi {
    private int img;
    private String ssid;
    public Wifi(int img,String ssid){
        this.img = img;
        this.ssid = ssid;
    }
    public int getImg(){return this.img;}

    public String getSsid(){return this.ssid;}

    public void setImg(int img){
        this.img = img;
    }

    public void setSsid(String ssid){
        this.ssid = ssid;
    }
}


