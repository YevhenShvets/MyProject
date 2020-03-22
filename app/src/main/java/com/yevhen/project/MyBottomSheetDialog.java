package com.yevhen.project;

import android.content.Context;
import android.mtp.MtpConstants;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.yevhen.project.Class.MyObject;

import io.realm.Realm;
import io.realm.RealmResults;

public class MyBottomSheetDialog extends BottomSheetDialog {
  private MyObject myObject;
  private Button buttonOk;
  private Button buttonClose;
  private Button buttonDelete;
  private EditText edit_name;
  private EditText edit_width;
  private EditText edit_height;
  private View view;
  private FrameLayout frameLayout;

  public MyBottomSheetDialog(@NonNull Context context, int theme, View myObject, FrameLayout frameLayout) {
    super(context, theme);
    this.myObject = ((ImageIcon)myObject).getObject();
    this.view = myObject;
    this.frameLayout = frameLayout;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.bottom_sheet_layout);
    setCanceledOnTouchOutside(false);

    buttonOk = (Button)findViewById(R.id.sheet_buttonOk);
    buttonClose = (Button)findViewById(R.id.sheet_buttonClose);
    buttonDelete = (Button) findViewById(R.id.sheet_buttonDelete);
    edit_name = (EditText) findViewById(R.id.sheet_edit_name);
    edit_width = (EditText) findViewById(R.id.sheet_edit_x);
    edit_height = (EditText) findViewById(R.id.sheet_edit_y);


    edit_name.setText(myObject.getName());
    edit_height.setText("" + myObject.getHeight());
    edit_width.setText("" + myObject.getWidth());
    buttonClose.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        dismiss();
      }
    });

    buttonOk.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if(edit_height.length()!=0 && edit_name.length()!=0 && edit_width.length()!=0){
          myObject.setName(edit_name.getText().toString());
          myObject.setHeight(Integer.parseInt(edit_height.getText().toString()));
          myObject.setWidth(Integer.parseInt(edit_width.getText().toString()));
          view.setLayoutParams(new FrameLayout.LayoutParams(Integer.parseInt(edit_width.getText().toString()), Integer.parseInt(edit_height.getText().toString())));
          ((ImageIcon)view).setObject(myObject);
          dismiss();
        }
      }
    });

    buttonDelete.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        delete_object(view);
        dismiss();
      }
    });
  }

  private void delete_object(final View v){
    MyObject obj=((ImageIcon)v).getObject();
    final int id = obj.getId();

    Realm realm = Realm.getInstance(Realm.getDefaultConfiguration());
    realm.executeTransactionAsync(new Realm.Transaction() {
      @Override
      public void execute(Realm realm) {
        RealmResults<MyObject> result = realm.where(MyObject.class).equalTo("id", id).findAll();
        result.deleteAllFromRealm();
      }
    }, new Realm.Transaction.OnSuccess() {
      @Override
      public void onSuccess() {
        frameLayout.removeView(v);
      }
    });
  }
}
