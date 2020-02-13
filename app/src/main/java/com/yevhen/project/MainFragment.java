package com.yevhen.project;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.LruCache;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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
    Realm realm;
    ImageView a;
    ImageView image;
    View view;
    Bitmap bitmap;


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
        button_set = (Button) view.findViewById(R.id.tmp_button_set);
        button_get = (Button) view.findViewById(R.id.tmp_button_get);
        button_photo = (Button) view.findViewById(R.id.tmp_button_photo);
        image = (ImageView) view.findViewById(R.id.tmp_image);
        layoutParams = new FrameLayout.LayoutParams(60, 60);
        realm = Realm.getDefaultInstance();

        final Switch switch1 = (Switch) view.findViewById(R.id.tmp_switch);
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                create_mode = isChecked;
            }
        });

        frameLayout = (FrameLayout) view.findViewById(R.id.layout_tmp);

        //пошук фото
        button_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
            }
        });

        button_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image.setImageBitmap(new File(getContext()).get_room_uri());
                //save_data();
            }
        });

        button_get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get_object();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FrameLayout.LayoutParams img_l = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                a = new ImageView(getContext());
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(100,100);
                switch(spinner.getSelectedItemPosition()){
                    case 0:
                        a.setImageResource(R.drawable.icon_lamp);
                        a.setTag(R.drawable.icon_lamp);
                        break;
                    case 1:
                        a.setImageResource(R.drawable.icon_plant);
                        a.setTag(R.drawable.icon_plant);
                        break;
                }
                a.setLayoutParams(img_l);
                a.setClickable(true);
                a.setLongClickable(true);
                a.setOnClickListener(new DoubleClickListener(){
                    @Override
                    public void onSingleClick(View v) {
                        tmp.setText("Single");
                    }

                    @Override
                    public void onDoubleClick(View v) {
                        tmp.setText("LONG");
                        //openFragment();

                    }
                });
                a.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        BottomSheetDialog bottom = new BottomSheetDialog(getContext(),R.style.BottomSheetDialogTheme);
                        bottom.setContentView(R.layout.bottom_sheet_layout);
                        bottom.setCanceledOnTouchOutside(false);
                        //TextView text = bottom.findViewById(R.id.sheet_text);
                       // text.setText(v.toString());
                        EditText edit1 = (EditText) bottom.findViewById(R.id.sheet_edit_x);
                        EditText edit2 = (EditText) bottom.findViewById(R.id.sheet_edit_y);
                        Button bu = (Button) bottom.findViewById(R.id.sheet_button);
                        ImageView img1 = (ImageView)bottom.findViewById(R.id.sheet_image);
                        bu.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                               // img1.setBackgroundColor(R.color.colorFon);
                            }
                        });
                        bottom.show();
                        return false;
                    }
                });

                a.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent event) {

                        if(create_mode) {
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

                a.setX(100f);
                a.setY(0f);

                frameLayout.addView(a,layoutParams);
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
                MyObject myObject = bgRealm.createObject(MyObject.class);
                myObject.setName("" + data_i++);
                myObject.setCord_x(a.getX());
                myObject.setCord_y(a.getY());
                int img_id = 1;

                switch ((int)a.getTag()){
                    case R.drawable.icon_lamp: img_id = 1; break;
                    case R.drawable.icon_plant: img_id = 2; break;
                }
                myObject.setImage_id(img_id);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Toast.makeText(getContext(),"+",Toast.LENGTH_SHORT).show();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Toast.makeText(getContext(),"-",Toast.LENGTH_SHORT).show();
            }
        });
    }

    RealmResults<MyObject> myObjects;

    private void get_object(){
        myObjects = realm.where(MyObject.class).findAll();
        FrameLayout.LayoutParams idsa = new FrameLayout.LayoutParams(15,15);
        ImageView img;
        for(MyObject myObject: myObjects){
            img = new ImageView(getContext());
            switch (myObject.getImage_id()) {
                case 1:
                    img.setImageResource(R.drawable.icon_lamp); break;
                case 2:
                    img.setImageResource(R.drawable.icon_plant); break;
            }
            img.setLayoutParams(idsa);
            img.setClickable(true);
            img.setOnClickListener(new ImageClickLIstener(getContext(),Function.toint(myObject.getName()),tmp));
            img.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(create_mode) {

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
                                break;
                            default:
                                return false;
                        }
                    }
                    return true;
                }});

            img.setX(myObject.getCord_x());
            img.setY(myObject.getCord_y());
            frameLayout.addView(img,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
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


}
