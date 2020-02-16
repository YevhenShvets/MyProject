package com.yevhen.project;


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
    TextView tv1;
    Button b1;
    ImageView logo;

    TextView tmp;
    Spinner spinner;
    Button button_set;
    Button button_get;
    Button button_photo;
    FrameLayout frameLayout;
    FrameLayout.LayoutParams layoutParams;
    FrameLayout.LayoutParams img_l;
    Realm realm;
    ImageView a;
    ImageView image;
    Switch switch1;
    View view;
    String temp;

    private static final int GALLERY_REQUEST = 1;

    float mPrevX,mPrevY;
    boolean create_mode = false;
    int i=0;
    int data_i = 0;

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.build_layout, container, false);

        Button button  = (Button) view.findViewById(R.id.b1);
        tmp = (TextView) view.findViewById(R.id.tmp_text);
        spinner = (Spinner) view.findViewById(R.id.tmp_spinner);
        switch1 = (Switch) view.findViewById(R.id.tmp_switch);
        button_set = (Button) view.findViewById(R.id.tmp_button_set);
        button_get = (Button) view.findViewById(R.id.tmp_button_get);
        button_photo = (Button) view.findViewById(R.id.tmp_button_photo);
        image = (ImageView) view.findViewById(R.id.tmp_image);
        frameLayout = (FrameLayout) view.findViewById(R.id.layout_tmp);
        layoutParams = new FrameLayout.LayoutParams(100, 100);
        img_l = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

       // Realm.deleteRealm(Realm.getDefaultConfiguration());
        try {
            realm = Realm.getDefaultInstance();
        }catch (Exception ex){
            Log.d("ERROR",ex.getMessage());
        }

        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                create_mode = isChecked;
            }
        });
        //пошук фото
        button_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete_all_object();
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
            }
        });
        button_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image.setImageBitmap(new File(getContext()).get_room_uri());
            }
        });
        button_get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get_object();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
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
        return view;
    }

    private void save_data(){
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                Number id_ = bgRealm.where(MyObject.class).max("id");
                int id= (id_==null)? 1:id_.intValue()+1;
                MyObject myObject = bgRealm.createObject(MyObject.class,id);
                int img_id = 1;
                switch ((int)a.getTag()){
                    case R.drawable.icon_lamp: img_id = 1; break;
                    case R.drawable.icon_plant: img_id = 2; break;
                }
                myObject.setImage_id(img_id);
                a.setId(myObject.getId());
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Toast.makeText(getContext(),"+",Toast.LENGTH_SHORT).show();
                get_object(a.getId());
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Toast.makeText(getContext(),"-"+ error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
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
    private void add_params(ImageView image){
        if(image!=null) {
            image.setOnClickListener(new DoubleClickListener() {
                @Override
                public void onSingleClick(View v) {
                    v.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#CBE8E8EC")));
                    //міняти розмір працює
                    //v.setLayoutParams(new FrameLayout.LayoutParams(50,50));
                    tmp.setText("Single" + getI());
                }

                @Override
                public void onDoubleClick(View v) {
                    v.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#E2FDC15D")));
                    tmp.setText("Double");
                    delete_object(v);
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
                            default:
                                return false;
                        }
                    }
                    return false;
                }
            });
            frameLayout.addView(image, layoutParams);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
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
                        image.setImageBitmap(selectedImage);

                        File file = new File(getContext());
                        file.set_room_uri(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    private void BottomSheet_Open(boolean create_mode,View v){
        if(!create_mode) {
            BottomSheetDialog bottom = new MyBottomSheetDialog(getContext(), R.style.BottomSheetDialogTheme, v,frameLayout);
            bottom.show();
        }
    }


    //DELETE REALM OBJECT
    private void delete_object(final View v){
        MyObject obj=((ImageIcon)v).getObject();
        final int id = obj.getId();

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<MyObject> result = realm.where(MyObject.class).equalTo("id", id).findAll();
                result.deleteAllFromRealm();
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                frameLayout.removeView(v);
            }
        });
    }
    private void delete_all_object(){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.delete(MyObject.class);
            }
        });
    }

}
