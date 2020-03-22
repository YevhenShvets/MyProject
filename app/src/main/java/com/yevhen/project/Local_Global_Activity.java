package com.yevhen.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

public class Local_Global_Activity extends AppCompatActivity {

    private Animation animation;
    private ImageView local_img;
    private ImageView global_img;
    private View local_view;
    private View global_view;
    private Activity activity = this;
    public static boolean open_bollean;
    private static final int LOCATION_PERMISSION_CODE = 100;
    private static final int STORAGE_PERMISSION_CODE = 101;
    float mx,my;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.or_layout);


        checkPermission(Manifest.permission.ACCESS_FINE_LOCATION,LOCATION_PERMISSION_CODE);
        checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,STORAGE_PERMISSION_CODE);

        animation = AnimationUtils.loadAnimation(this, R.anim.scaling);
        local_img = (ImageView) findViewById(R.id.local_or);
        global_img = (ImageView) findViewById(R.id.global_or);
        local_view = (View) findViewById(R.id.local_or_view);
        global_view = (View)findViewById(R.id.global_or_view);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        mx = dm.widthPixels;
        my = dm.heightPixels;
        Log.i("Metrix",mx+"  "+my);

        local_view.setLayoutParams(new ConstraintLayout.LayoutParams((int)mx,(int)(my/2)));
        ViewGroup.LayoutParams layoutParams = global_view.getLayoutParams();
        layoutParams.height =(int)(my/2);
        global_view.setLayoutParams(layoutParams);
        local_view.setOnClickListener(onClickListener);
        global_view.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(!open_bollean) {
                if (v == global_view || v == global_img) {
                    changeViewColor(global_view, R.color.global_or_code);
                    global_img.startAnimation(animation);
                }
                if (v == local_img || v == local_view) {
                    changeViewColor(local_view, R.color.local_or_color);
                    local_img.startAnimation(animation);
                    OpenFragment();
                    open_bollean = true;
                }
            }
        }
    };

    private void OpenFragment(){
        Pass_hub_fragment fragment = new Pass_hub_fragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_right,R.anim.enter_from_right,R.anim.exit_to_right);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.add(R.id.hub_frame_layout,fragment,"Pass_hub").commit();
    }

    private void changeViewColor(final View view,int color) {
        // Load initial and final colors.
        final int initialColor = getResources().getColor(R.color.gray_2);
        final int finalColor = getResources().getColor(color);

        ValueAnimator anim = ValueAnimator.ofFloat(0, 1);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // Use animation position to blend colors.
                float position = animation.getAnimatedFraction();
                int blended = blendColors(initialColor, finalColor, position);

                // Apply blended color to the view.
                view.setBackgroundColor(blended);
            }
        });

        anim.setDuration(300).start();
    }
    private int blendColors(int from, int to, float ratio) {
        final float inverseRatio = 1f - ratio;

        final float r = Color.red(to) * ratio + Color.red(from) * inverseRatio;
        final float g = Color.green(to) * ratio + Color.green(from) * inverseRatio;
        final float b = Color.blue(to) * ratio + Color.blue(from) * inverseRatio;

        return Color.rgb((int) r, (int) g, (int) b);
    }

    // Function to check and request permission.
    public void checkPermission(String permission, int requestCode)
    {
        if (ContextCompat.checkSelfPermission(this, permission)
                == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(this,
                    new String[] { permission },
                    requestCode);
        }
        else {
          /*  Toast.makeText(this,
                    "Permission already granted",
                    Toast.LENGTH_SHORT)
                    .show();*/
        }
    }

    // This function is called when the user accepts or decline the permission.
    // Request Code is used to check which permission called this function.
    // This request code is provided when the user is prompt for permission.

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,
                        permissions,
                        grantResults);

        if (requestCode == LOCATION_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
             /*   Toast.makeText(this,
                        "Location Permission Granted",
                        Toast.LENGTH_SHORT)
                        .show();*/
            }
            else {
              /*  Toast.makeText(this,
                        "Location Permission Denied",
                        Toast.LENGTH_SHORT)
                        .show();*/
            }
        }
        else if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
               /* Toast.makeText(this,
                        "Storage Permission Granted",
                        Toast.LENGTH_SHORT)
                        .show();*/
            }
            else {
            /*    Toast.makeText(this,
                        "Storage Permission Denied",
                        Toast.LENGTH_SHORT)
                        .show();*/
            }
        }
    }

}
