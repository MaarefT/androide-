package com.example.application.pages;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.application.R;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        try {
            // Récupérer les données de l'intent et vérifier leur validité
            Intent i = getIntent();
            String firstname = i.getStringExtra("fn");
            String lastname = i.getStringExtra("ln");

            // Si les données sont nulles, affecter une valeur par défaut
            if (firstname == null) firstname = "Utilisateur";
            if (lastname == null) lastname = "";

            // Configurer le texte de bienvenue
            TextView tv_txt_wlc = findViewById(R.id.textView5);
            tv_txt_wlc.setText(String.format("%s %s %s", tv_txt_wlc.getText(), firstname, lastname));

            // Configurer les boutons
            Button btnSlice = findViewById(R.id.btn_slice);
            Button btnCategories = findViewById(R.id.btn_categories);
            Button btnContact = findViewById(R.id.btn_contact);

            // Naviguer vers l'activité "Slice"
            btnSlice.setOnClickListener(v -> {
                clearCacheAndNavigate(SliceActivity.class);
            });

            // Naviguer vers l'activité "Categories"
            btnCategories.setOnClickListener(v -> {
                clearCacheAndNavigate(CategoriesActivity.class);
            });

            // Naviguer vers l'activité "Contact"
            btnContact.setOnClickListener(v -> {
                clearCacheAndNavigate(ContactActivity.class);
            });

        } catch (Exception e) {
            // Affichage d'une erreur détaillée dans le log
            Log.e(TAG, "Erreur lors de l'initialisation : ", e);
            e.printStackTrace();  // Afficher la trace de l'exception dans la console
        }
    }

    /**
     * Méthode pour effacer le cache et naviguer vers une nouvelle activité.
     */
    private void clearCacheAndNavigate(Class<?> activityClass) {
        // Effacer le cache ou les données si nécessaire
        getIntent().removeExtra("fn");
        getIntent().removeExtra("ln");

        // Démarrer la nouvelle activité
        Intent intent = new Intent(HomeActivity.this, activityClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
