package com.example.application.pages;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.application.MonOpenHelper;
import com.example.application.R;
import com.example.application.pages.MainActivity;

public class InscriptionActivity extends AppCompatActivity {


    EditText et_firstmane,et_lastname,et_email,et_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscritactiv2);
        et_firstmane=findViewById(R.id.Name);
        et_lastname=findViewById(R.id.Lastname);
        et_email=findViewById(R.id.email);
        et_password=findViewById(R.id.Password);

    }
    public void addUser(View v) {
        if (et_firstmane.getText().toString().isEmpty()) {
            Toast.makeText(this, "First name is required", Toast.LENGTH_LONG).show();
        } else if (et_lastname.getText().toString().isEmpty()) {
            Toast.makeText(this, "Last name is required", Toast.LENGTH_LONG).show();
        } else if (et_email.getText().toString().isEmpty()) {
            Toast.makeText(this, "Email is required", Toast.LENGTH_LONG).show();
        } else if (et_password.getText().toString().isEmpty()) {
            Toast.makeText(this, "Password is required", Toast.LENGTH_LONG).show();
        } else {
            // Afficher une boîte de dialogue de confirmation
            new AlertDialog.Builder(this)
                    .setTitle("Confirmation")
                    .setMessage("Êtes-vous sûr de vouloir enregistrer ces données ?")
                    .setPositiveButton("Oui", (dialog, which) -> {
                        // Enregistrer les données dans la base
                        MonOpenHelper MH = new MonOpenHelper(this);
                        SQLiteDatabase db = MH.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.put("Email", et_email.getText().toString());
                        values.put("Password", et_password.getText().toString());
                        values.put("Firstname", et_firstmane.getText().toString());
                        values.put("Lastname", et_lastname.getText().toString());
                        long rslt = db.insert("Users", null, values);
                        db.close();

                        if (rslt == -1) {
                            Toast.makeText(this, "Erreur lors de l'inscription", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Inscription réussie", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(this, MainActivity.class);
                            i.putExtra("email", et_email.getText().toString());
                            i.putExtra("password", et_password.getText().toString());
                            startActivity(i);
                        }
                    })
                    .setNegativeButton("Non", (dialog, which) -> {
                        // Effacer les champs de saisie
                        et_firstmane.setText("");
                        et_lastname.setText("");
                        et_email.setText("");
                        et_password.setText("");
                        Toast.makeText(this, "Veuillez saisir à nouveau vos données.", Toast.LENGTH_SHORT).show();
                    })
                    .show();
        }

    }

}

