package com.yevhen.project;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.yevhen.project.LoginActivity;

import androidx.fragment.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 */

public class Settings_fragment extends Fragment {


    public Settings_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.setting_fragment, container, false);
        Button button = (Button) view.findViewById(R.id.setting_button);
        TextView textView = (TextView) view.findViewById(R.id.setting_text);
        textView.setText(new File(getContext()).getLOGIN());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               File a= new File(getContext());
               a.Drop_login();
            }
        });
        return view;
    }

}
