package com.yevhen.project;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.yevhen.project.Class.MyObject;

@SuppressLint("AppCompatCustomView")
public class ImageIcon extends ImageView {
    private MyObject object;
    private  float mPrevX,mPrevY;
    private FrameLayout.LayoutParams img_l;

    public ImageIcon(Context context, MyObject object) {
        super(context);
        img_l = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.object = object;
        setClickable(true);
        setLongClickable(true);
        setBackgroundResource(R.drawable.background_icon);
        setX(object.getCord_x());
        setY(object.getCord_y());
        switch(object.getImage_id()){
            case 1:
                setImageResource(R.drawable.icon_lamp);
                break;
            case 2:
                setImageResource(R.drawable.icon_plant);
                break;
        }
    }

    public MyObject getObject(){
        return object;
    }

}
