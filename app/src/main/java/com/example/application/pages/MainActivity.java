package com.example.application.pages;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.application.MonOpenHelper;
import com.example.application.R;

public class MainActivity extends AppCompatActivity {

    EditText et_email, et_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et_email=findViewById(R.id.email2);
        et_password=findViewById(R.id.Password2);

        Intent i=getIntent();
        String email=i.getStringExtra("email");
        String password=i.getStringExtra("password");
        et_email.setText(email);
        et_password.setText(password);
    }



    public void goToInscri(View v){
        Intent it = new Intent(this, InscriptionActivity.class);
        startActivity(it);
    }

    public void login(View v){
        if(et_email.getText().toString().equals(""))
            Toast.makeText(this,"Email vide",Toast.LENGTH_SHORT).show();
        else if(et_password.getText().toString().equals(""))
            Toast.makeText(this,"Password vide",Toast.LENGTH_SHORT).show();
        else {
            MonOpenHelper MH = new MonOpenHelper(this);
            SQLiteDatabase db = MH.getReadableDatabase();
            Cursor C = db.query ("Users", new String[] {"Firstname" ,"Lastname"}, "Email=? and PASSWORD=?", new String[]{et_email.getText().toString(),et_password.getText().toString()}, null, null, null, null);
            if(C.getCount()==0)
                Toast.makeText(this,"no such user",Toast.LENGTH_SHORT).show();

            else {
                C.moveToFirst();
                String Firstname=C.getString(0);
                String Lastname=C.getString(1);
                Intent i=new Intent(this,HomeActivity.class);
                i.putExtra("fn",Firstname);
                i.putExtra("ln",Lastname);
                startActivity(i);
            }
        }
    }


}



