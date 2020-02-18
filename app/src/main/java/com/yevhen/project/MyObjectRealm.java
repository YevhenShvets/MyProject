package com.yevhen.project;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.yevhen.project.Class.MyObject;

import io.realm.Realm;
import io.realm.RealmResults;

public class MyObjectRealm  {

    //НУ ЗРОБИ Ж ПО-ЛЮДСЬКИ



   /* Context context;
    Realm realm;
    ImageView a;

    private void save_data(){
        try {
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm bgRealm) {
                    Number id_ = bgRealm.where(MyObject.class).max("id");
                    int id = (id_ == null) ? 1 : id_.intValue() + 1;
                    MyObject myObject = bgRealm.createObject(MyObject.class, id);
                    int img_id = 1;
                    switch ((int) a.getTag()) {
                        case R.drawable.icon_lamp:
                            img_id = 1;
                            break;
                        case R.drawable.icon_plant:
                            img_id = 2;
                            break;
                    }
                    myObject.setImage_id(img_id);
                    a.setId(myObject.getId());
                }
            }, new Realm.Transaction.OnSuccess() {
                @Override
                public void onSuccess() {
                   // Toast.makeText(getContext(), "+", Toast.LENGTH_SHORT).show();
                    get_object(a.getId());
                }
            }, new Realm.Transaction.OnError() {
                @Override
                public void onError(Throwable error) {
                  //  Toast.makeText(getContext(), "-" + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception ex){
          //  Toast.makeText(getContext(),ex.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    private void get_object(){
        RealmResults<MyObject> myObjects;
        myObjects = realm.where(MyObject.class).findAll();
        for(MyObject myObject: myObjects){
            a = new ImageIcon(context,myObject);
            add_params(a);
        }
    }
    private void get_object(int id){
        RealmResults<MyObject> myObjects;
        myObjects = realm.where(MyObject.class).equalTo("id",id).findAll();
        MyObject myObject = myObjects.get(0);
        a = new ImageIcon(context,myObject);
        add_params(a);
    }
    private void add_params(final ImageView image){
        if(image!=null) {
            image.setOnClickListener(new DoubleClickListener() {
                @Override
                public void onSingleClick(View v) {
                    v.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#CBE8E8EC")));
                    //міняти розмір працює
                    //v.setLayoutParams(new FrameLayout.LayoutParams(50,50));
                }

                @Override
                public void onDoubleClick(View v) {
                    v.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#E2FDC15D")));
                    //delete_object(v);
                }
            });
            image.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    BottomSheet_Open(create_mode, v);
                    return false;
                }
            });

            image.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent event) {
                    if (create_mode) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                mPrevX = view.getX() - event.getRawX();
                                mPrevY = view.getY() - event.getRawY();
                                break;
                            case MotionEvent.ACTION_MOVE:
                                view.animate()
                                        .x(event.getRawX() + mPrevX)
                                        .y(event.getRawY() + mPrevY)
                                        .setDuration(0)
                                        .start();
                                break;
                            case MotionEvent.ACTION_UP:
                                ImageIcon img_temp = ((ImageIcon)view);
                                try {
                                    img_temp.setCord(view.getX(),view.getY());
                                }catch (Exception ex){
                                    //Toast.makeText(getContext(),ex.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                                break;

                            default:
                                return false;
                        }
                    }
                    return false;
                }
            });
            frameLayout.addView(image);
        }

    }*/
}
