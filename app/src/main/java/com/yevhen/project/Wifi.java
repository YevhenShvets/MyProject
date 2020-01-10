package com.yevhen.project;

class Wifi {
    int img;
    String ssid;
    Wifi(int img,String ssid){
        this.img = img;
        this.ssid = ssid;
    }
    int getImg(){return this.img;}

    String getSsid(){return this.ssid;}

    void setImg(int img){
        this.img = img;
    }

    void setSsid(String ssid){
        this.ssid = ssid;
    }
}


