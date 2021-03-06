package com.yevhen.project;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegistrationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RegistrationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegistrationFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String EMAIL = "email";

    // TODO: Rename and change types of parameters
    private String mEmail;

    private OnFragmentInteractionListener mListener;

    private EditText email_fragment;
    private Button button_back_fragment;
    private Button button_registration;
    private TextView error;
    private EditText vercode,password,username;

    public RegistrationFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static RegistrationFragment newInstance(String email) {
        RegistrationFragment fragment = new RegistrationFragment();
        Bundle args = new Bundle();
        args.putString(EMAIL, email);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mEmail = getArguments().getString(EMAIL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_registration, container, false);
       email_fragment = (EditText) view.findViewById(R.id.registration_email_edit);
       button_back_fragment = (Button) view.findViewById(R.id.button_back_fragment);
       button_registration = (Button) view.findViewById(R.id.register_button);
       error = (TextView) view.findViewById(R.id.registration_error_text);
        username = (EditText) view.findViewById(R.id.registration_username_edit);
        vercode = (EditText) view.findViewById(R.id.registration_varcode_edit);
        password = (EditText) view.findViewById(R.id.registration_pass_edit);

        vercode.setFilters(new InputFilter[] {new InputFilter.AllCaps()});

        Function.setEnabled_button(button_registration,false);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(username.length() == 0 && vercode.length()== 0 && password.length() ==0){
                    error.setText("");
                    if(button_registration.isEnabled())
                        Function.setEnabled_button( button_registration,false);
                }else {
                    if(username.length() == 0 || vercode.length() ==0 || password.length() == 0)
                    {
                        error.setText("Перевірте поля");
                        if(button_registration.isEnabled())
                            Function.setEnabled_button( button_registration,false);
                        return;
                    }
                    else {
                        error.setText("");
                        Function.setEnabled_button( button_registration,true);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        username.addTextChangedListener(textWatcher);
        vercode.addTextChangedListener(textWatcher);
        password.addTextChangedListener(textWatcher);


       button_back_fragment.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Back();
           }
       });
       email_fragment.setText(mEmail);

        return view;
    }


    public void Back() {
        if (mListener != null) {
            mListener.onFragmentInteraction();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction();
    }
}
