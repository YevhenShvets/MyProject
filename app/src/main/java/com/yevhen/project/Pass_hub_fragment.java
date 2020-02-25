package com.yevhen.project;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class Pass_hub_fragment extends Fragment {

    private Animation animation;
    private ImageView left_b;
    private ImageView right_b;
    private ImageView center_b;
    private Button button_enter;
    private ImageView button_remove;
    private ImageView[] img;
    private static int index=0;
    private int img_count =10;


    public Pass_hub_fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        index=0;
        animation = AnimationUtils.loadAnimation(getContext(), R.anim.scaling);
        View view = inflater.inflate(R.layout.pass_hub_layout, container, false);
        left_b = view.findViewById(R.id.pass_button1);
        center_b = view.findViewById(R.id.pass_button2);
        right_b = view.findViewById(R.id.pass_button3);
        button_remove = view.findViewById(R.id.pass_remove);
        button_enter = view.findViewById(R.id.pass_button_enter);
        img = new ImageView[img_count];
        ImageView img1 = view.findViewById(R.id.pass_img_1);
        ImageView img2 = view.findViewById(R.id.pass_img_2);
        ImageView img3 = view.findViewById(R.id.pass_img_3);
        ImageView img4 = view.findViewById(R.id.pass_img_4);
        ImageView img5 = view.findViewById(R.id.pass_img_5);
        ImageView img6 = view.findViewById(R.id.pass_img_6);
        ImageView img7 = view.findViewById(R.id.pass_img_7);
        ImageView img8 = view.findViewById(R.id.pass_img_8);
        ImageView img9 = view.findViewById(R.id.pass_img_9);
        ImageView img10 = view.findViewById(R.id.pass_img_10);
        img[0]=img1;
        img[1]=img2;
        img[2]=img3;
        img[3]=img4;
        img[4]=img5;
        img[5]=img6;
        img[6]=img7;
        img[7]=img8;
        img[8]=img9;
        img[9]=img10;

        button_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                to_code();
            }
        });

        button_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear_one();
            }
        });

        button_remove.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                clear_all();
                return false;
            }
        });

        left_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animation);
                click_button(R.drawable.icon_pass_left);
            }
        });
        center_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animation);
                click_button(R.drawable.icon_pass_all);
            }
        });
        right_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animation);
                click_button(R.drawable.icon_pass_right);
            }
        });

        return view;
    }

    private void clear_all(){
        if(index>0) {
            index = 0;
            for (int i = 0; i < img_count; i++) {
                img[i].setTag("");
                img[i].setBackgroundResource(R.drawable.background_icon);
            }
        }
    }
    private void clear_one(){
        if(index>0) {
            index--;
            img[index].setTag("");
            img[index].setBackgroundResource(R.drawable.background_icon);
        }
    }
    private void click_button(int resurse){
        if(index>=0 && index<img_count) {
            img[index].setBackgroundResource(resurse);
            img[index].setTag(resurse);
            img[index].startAnimation(animation);
            index++;
        }
    }

    private void to_code(){
        String code = "";
        for(int i=0;i<img.length;i++){
            switch ((int)img[i].getTag()){
                case R.drawable.icon_pass_left: code+="L";break;
                case R.drawable.icon_pass_all: code+="C";break;
                case R.drawable.icon_pass_right:code+="R";break;
                default: code+=""; break;
            }
        }
        Toast.makeText(getContext(),code,Toast.LENGTH_LONG).show();
    }

}
