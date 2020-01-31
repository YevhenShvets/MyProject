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

import com.yevhen.project.Class.Users;
import com.yevhen.project.LoginActivity;

import androidx.fragment.app.Fragment;

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
    float aa =100f;
    float mPrevX,mPrevY;
    boolean create_mode = false;
    int i=0;

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

        final Switch switch1 = (Switch) view.findViewById(R.id.tmp_switch);
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                create_mode = isChecked;
            }
        });

        final FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.layout_tmp);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                //ImageButton a= new ImageButton(getContext());
                ImageView a = new ImageView(getContext());
                FrameLayout.LayoutParams img_l = new FrameLayout.LayoutParams(30,30);

                switch(spinner.getSelectedItemPosition()){
                    case 0:a.setImageResource(R.drawable.icon_lamp); break;
                    case 1:a.setImageResource(R.drawable.icon_plant); break;
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
                                    // v.setY(event.getRawY());
                                    // v.setX(event.getRawX());
                                    break;
                            }
                        }
                        return false;
                    }});
                a.setX(aa);
                a.setY(0f);
                aa+=10f;
                frameLayout.addView(a,layoutParams);
            }
        });
        return view;
    }

}
