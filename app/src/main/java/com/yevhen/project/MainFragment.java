package com.yevhen.project;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.util.LruCache;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.yevhen.project.Class.MyObject;
import com.yevhen.project.Class.Users;
import com.yevhen.project.LoginActivity;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Timer;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.exceptions.RealmException;
import io.realm.exceptions.RealmFileException;
import io.realm.internal.RealmObjectProxy;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.app.Activity.RESULT_OK;
import static android.view.Gravity.*;


public class MainFragment extends Fragment {

    FrameLayout frameLayout;
    FrameLayout frameCreateMode;
    Realm realm;
    ImageView a;
    ImageView plan_img;
    View view;
    Animation animation;

    private static final int GALLERY_REQUEST = 1;

    float mPrevX,mPrevY;
    public static boolean create_mode = false;

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.plan_layout, container, false);
        animation = AnimationUtils.loadAnimation(getContext(), R.anim.scaling);
        //Realm.deleteRealm(Realm.getDefaultConfiguration());
        try {
            realm = Realm.getDefaultInstance();
            get_object();
        }catch (Exception ex){
            Log.d("ERROR",ex.getMessage());
        }
        plan_img = (ImageView) view.findViewById(R.id.plan_img_img);
        plan_img.setImageBitmap(new File(getContext()).get_room_uri());
        frameLayout = (FrameLayout) view.findViewById(R.id.frame_layout);
        frameCreateMode = (FrameLayout) view.findViewById(R.id.frame_create_mode);
        ImageView button = (ImageView) view.findViewById(R.id.plan_settings_img);
        get_object();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialogBuild build = new BottomSheetDialogBuild(getContext(),R.style.BottomSheetDialogTheme1,realm,frameLayout,MainFragment.this);
                build.show();
            }
        });

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        upgrade_object();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    private void BottomSheet_Open(boolean create_mode,View v){
        if(!create_mode) {
            BottomSheetDialog bottom = new MyBottomSheetDialog(getContext(), R.style.BottomSheetDialogTheme, v,frameLayout);
            bottom.show();
        }
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
                   // v.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#E2FDC15D")));
                    //delete_object(v);
                }
            });
                       if(((ImageIcon)image).getObject().isStatus())  image.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#E2FDC15D")));
            else
                image.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#CBE8E8EC")));

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

    }

    //REALM
    //UPGRADE
    private void upgrade_object(){
        if(frameLayout!=null&& realm!=null){
            View view;
            for(int i=0;i<frameLayout.getChildCount();i++){
                view = frameLayout.getChildAt(i);
                MyObject myObject = ((ImageIcon)view).getObject();
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(myObject);
                realm.commitTransaction();
            }
        }
    }

    //DELETE REALM OBJECT
    private void delete_all_object(){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.delete(MyObject.class);
            }
        });
    }



    public void setPlan_img(){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case GALLERY_REQUEST:
                if (resultCode == RESULT_OK) {
                    try {
                        final Uri imageUri = data.getData();
                        final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        plan_img.setImageBitmap(selectedImage);

                        File file = new File(getContext());
                        file.set_room_uri(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

}
