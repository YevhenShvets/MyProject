package com.yevhen.project;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ImageClickLIstener extends Activity implements View.OnClickListener
{
    Context context;
    int position;
    TextView text;
    ImageClickLIstener(Context context,int position,TextView text)
    {
        this.context = context;
        this.position= position;
        this.text = text;
    }

    @Override
    public void onClick(View v) {
        text.setText("IMAGE "+position);
    }
}