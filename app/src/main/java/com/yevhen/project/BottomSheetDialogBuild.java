package com.yevhen.project;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.yevhen.project.Class.MyObject;

import java.io.FileNotFoundException;
import java.io.InputStream;

import io.realm.Realm;
import io.realm.RealmResults;

import static android.app.Activity.RESULT_OK;

public class BottomSheetDialogBuild extends BottomSheetDialog {
    Button button_create;
    Button button_photo;
    Spinner spinner;
    Switch switch1;

    Realm realm;
    FrameLayout frameLayout;
    FrameLayout.LayoutParams img_l;
    MainFragment mainFragment;
    ImageView a;
    Animation animation;

   float mPrevX,mPrevY;


    public BottomSheetDialogBuild(@NonNull Context context, int theme,Realm realm,FrameLayout frameLayout,MainFragment mainFragment) {
        super(context, theme);
        this.realm = realm;
        this.frameLayout = frameLayout;
        this.mainFragment = mainFragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.build_layout);

       //setCanceledOnTouchOutside(false);
        button_create = (Button) findViewById(R.id.build_create_button);
        spinner = (Spinner) findViewById(R.id.build_spinner);
        switch1 = (Switch) findViewById(R.id.build_switch);
        button_photo = (Button) findViewById(R.id.build_button_photo);
        animation = AnimationUtils.loadAnimation(getContext(), R.anim.scaling);
        img_l = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        switch1.setChecked(MainFragment.create_mode);
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MainFragment.create_mode = isChecked;
                if(isChecked == true){
                    mainFragment.frameCreateMode.setVisibility(View.VISIBLE);
                    dismiss();
                }else{
                    mainFragment.frameCreateMode.setVisibility(View.INVISIBLE);
                }
            }
        });
        //пошук фото
        button_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainFragment.setPlan_img();
                dismiss();

            }
        });

        button_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a = new ImageIcon(getContext(),new MyObject());
                switch(spinner.getSelectedItemPosition()){
                    case 0:
                        a.setTag(R.drawable.icon_lamp);
                        break;
                    case 1:
                        a.setTag(R.drawable.icon_plant);
                        break;
                }
                save_data();
            }
        });
    }

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
                    Toast.makeText(getContext(), "+", Toast.LENGTH_SHORT).show();
                    get_object(a.getId());
                }
            }, new Realm.Transaction.OnError() {
                @Override
                public void onError(Throwable error) {
                    Toast.makeText(getContext(), "-" + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception ex){
            Toast.makeText(getContext(),ex.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    private void get_object(){
        RealmResults<MyObject> myObjects;
        myObjects = realm.where(MyObject.class).findAll();
        for(MyObject myObject: myObjects){
            a = new ImageIcon(getContext(),myObject);
            add_params(a);
        }
    }
    private void get_object(int id){
        RealmResults<MyObject> myObjects;
        myObjects = realm.where(MyObject.class).equalTo("id",id).findAll();
        MyObject myObject = myObjects.get(0);
        a = new ImageIcon(getContext(),myObject);
        add_params(a);
    }
    private void add_params(final ImageView image){
        if(image!=null) {
            image.setOnClickListener(new DoubleClickListener() {
                @Override
                public void onSingleClick(View v) {
                    if(v.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#CBE8E8EC")))
                        v.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#E2FDC15D")));
                    else
                        v.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#CBE8E8EC")));
                    //міняти розмір працює
                    //v.setLayoutParams(new FrameLayout.LayoutParams(50,50));
                    v.startAnimation(animation);
                }

                @Override
                public void onDoubleClick(View v) {
                   //delete_object(v);

                }
            });
            if(((ImageIcon)image).getObject().isStatus())  image.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#E2FDC15D")));
            else
                image.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#CBE8E8EC")));

            image.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    BottomSheet_Open(MainFragment.create_mode, v);
                    return false;
                }
            });

            image.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent event) {
                    if (MainFragment.create_mode) {
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

    }
    private void BottomSheet_Open(boolean create_mode,View v){
        if(!create_mode) {
            BottomSheetDialog bottom = new MyBottomSheetDialog(getContext(), R.style.BottomSheetDialogTheme, v,frameLayout);
            bottom.show();
        }
    }

}
