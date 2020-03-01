package com.yevhen.project;

import android.content.Context;
import android.graphics.Color;
import android.icu.text.UnicodeSetSpanner;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Pass_hub_fragment extends Fragment {

    private Animation animation;
    private Button button_enter;
    private static int index=0;
    private int edit_count =6;
    private EditText[] edits;


    public Pass_hub_fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        animation = AnimationUtils.loadAnimation(getContext(), R.anim.scaling);
        View view = inflater.inflate(R.layout.or_layout, container, false);
        ImageView local_img = view.findViewById(R.id.local_or);
        ImageView global_img = view.findViewById(R.id.global_or);

        local_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animation);
            }
        });
        global_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animation);
            }
        });
        /*index=0;
        edits = new EditText[6];
        button_enter = view.findViewById(R.id.pass_button_enter);

        EditText e1 = view.findViewById(R.id.pass_edit_1);
        EditText e2 = view.findViewById(R.id.pass_edit_2);
        EditText e3 = view.findViewById(R.id.pass_edit_3);
        EditText e4 = view.findViewById(R.id.pass_edit_4);
        EditText e5 = view.findViewById(R.id.pass_edit_5);
        EditText e6 = view.findViewById(R.id.pass_edit_6);
        edits[0] = e1;
        edits[1] = e2;
        edits[2] = e3;
        edits[3] = e4;
        edits[4] = e5;
        edits[5] = e6;

        for(int i=0;i<edit_count;i++) {
            edits[i].addTextChangedListener(textWatcher);
            edits[i].setTag(i);
            edits[i].setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if(keyCode == KeyEvent.KEYCODE_DEL){
                        clear_one((int)v.getTag());
                    }
                    return false;
                }
            });

        }

        e1.setFocusable(true);
        e1.requestFocus();


        button_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get_text();
            }
        });*/
        return view;
    }


    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(s.length() == 1) {
                next();
                anim();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void next(){
        if(index+1<edit_count) {
            edits[++index].setFocusable(true);
            edits[index].requestFocus();
        }
    }
    private void clear_one(int i){
        if(i!=0) {
            edits[i].setText("");
            edits[--i].requestFocus();
            index--;
        }
    }

    private void clear_all(){
        for(int i=0;i<edits.length;i++){
            edits[i].setText("");
        }
        index=0;
        edits[index].requestFocus();
    }

    private void anim(){
        edits[index].startAnimation(animation);
    }

    private void get_text(){
        String str= "";
        for(int i=0;i<edits.length;i++){
            str+=edits[i].getText().toString();
        }
        Toast.makeText(getContext(),str,Toast.LENGTH_LONG).show();
    }

}
