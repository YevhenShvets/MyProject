package com.yevhen.project.Class;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class MyObject extends RealmObject {
    @PrimaryKey
    int id;
    String name;
    float cord_x;
    float cord_y;
    int width;
    int height;
    int image_id;

    public MyObject(MyObject temp){
        this.id = temp.id;
        this.image_id = temp.image_id;
        this.name = temp.name;
        this.cord_y = temp.cord_y;
        this.cord_x = temp.cord_x;
        this.width = temp.width;
        this.height = temp.height;
    }

    public MyObject() {
        this.name = "MyName";
        this.cord_x = 100f;
        this.cord_y = 100f;
        this.width = 100;
        this.height = 100;
        this.image_id = 1;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public float getCord_x() {
        return cord_x;
    }

    public float getCord_y() {
        return cord_y;
    }

    public int getImage_id() {
        return image_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCord_x(float cord_x) {
        this.cord_x = cord_x;
    }

    public void setCord_y(float cord_y) {
        this.cord_y = cord_y;
    }

    public void setImage_id(int image_id) {
        this.image_id = image_id;
    }
}
