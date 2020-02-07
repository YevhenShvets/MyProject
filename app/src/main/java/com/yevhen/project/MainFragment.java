package com.yevhen.project;


import android.content.SharedPreferences;
import android.os.Bundle;
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
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.yevhen.project.Class.MyObject;
import com.yevhen.project.Class.Users;
import com.yevhen.project.LoginActivity;

import androidx.fragment.app.Fragment;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.internal.RealmObjectProxy;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.view.Gravity.*;


/**
 * A simple {@link Fragment} subclass.
 */

public class MainFragment extends Fragment {
    TextView tv1;
    Button b1;
    ImageView logo;

    TextView tmp;
    Spinner spinner;
    Button button_set;
    Button button_get;
    FrameLayout frameLayout;
    FrameLayout.LayoutParams layoutParams;
    Realm realm;
    ImageView a;


    float mPrevX,mPrevY;
    boolean create_mode = false;
    int i=0;
    int data_i = 0;

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.build_layout, container, false);

        Button button  = (Button) view.findViewById(R.id.b1);
        tmp = (TextView) view.findViewById(R.id.tmp_text);
        spinner = (Spinner) view.findViewById(R.id.tmp_spinner);
        button_set = (Button) view.findViewById(R.id.tmp_button_set);
        button_get = (Button) view.findViewById(R.id.tmp_button_get);
        realm = Realm.getDefaultInstance();

        final Switch switch1 = (Switch) view.findViewById(R.id.tmp_switch);
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                create_mode = isChecked;
            }
        });

        frameLayout = (FrameLayout) view.findViewById(R.id.layout_tmp);

        button_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save_data();
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
                layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                //ImageButton a= new ImageButton(getContext());
                a = new ImageView(getContext());
                FrameLayout.LayoutParams img_l = new FrameLayout.LayoutParams(30,30);

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
                a.setOnClickListener(new ImageClickLIstener(getContext(),i++,tmp));

                a.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (create_mode) {
                            float currX, currY;
                            switch (event.getAction()) {
                                case MotionEvent.ACTION_DOWN:
                                    //tmp.setText("ACTION_DOWN"+ event.getRawX() + " X "+ event.getRawY()+"\n"+v.getX() +" X "+ v.getY());
                                    mPrevX = event.getX();
                                    mPrevY = event.getY();
                                    break;
                                case MotionEvent.ACTION_MOVE:
                                    //tmp.setText("ACTION_MOVE"+ event.getX() + " X "+ event.getY()+"\n"+v.getX() +" X "+ v.getY());
                                    currX = event.getRawX();
                                    currY = event.getRawY();

                                    v.setX(currX - mPrevX);
                                    v.setY(currY - mPrevY);

                                    break;
                                case MotionEvent.ACTION_UP:
                                    //tmp.setText("ACTION_UP"+ event.getX() + " X "+ event.getY()+"\n"+v.getX() +" X "+ v.getY());
                                    break;
                            }
                        }
                        return false;
                    }});
                a.setX(100);
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
/*                Number id_ = bgRealm.where(MyObject.class).max("id");
                int id= (id_==null)? 1:id_.intValue()+1;*/
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
        FrameLayout.LayoutParams idsa = new FrameLayout.LayoutParams(30,30);
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
                    if (create_mode) {
                        float currX, currY;
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                mPrevX = event.getX();
                                mPrevY = event.getY();
                                break;
                            case MotionEvent.ACTION_MOVE:
                                currX = event.getRawX();
                                currY = event.getRawY();

                                v.setX(currX - mPrevX);
                                v.setY(currY - mPrevY);

                                break;
                            case MotionEvent.ACTION_UP:
                                break;
                        }
                    }
                    return false;
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
}
