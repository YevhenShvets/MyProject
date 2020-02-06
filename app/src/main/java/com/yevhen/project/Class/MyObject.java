package com.yevhen.project.Class;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class MyObject extends RealmObject {
    String name;
    float cord_x;
    float cord_y;
    int image_id;

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
