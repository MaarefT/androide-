package com.example.application.pages;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.application.R;

import java.util.ArrayList;

public class ContactActivity extends AppCompatActivity {

    private TextView txtContactDetails;
    private ArrayList<String> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        txtContactDetails = findViewById(R.id.txtContactDetails);
        contactList = new ArrayList<>();

        Button btnAdd = findViewById(R.id.btnAdd);
        Button btnModify = findViewById(R.id.btnModify);
        Button btnDelete = findViewById(R.id.btnDelete);

        // Ajouter un contact
        btnAdd.setOnClickListener(v -> showAddDialog());

        // Modifier un contact
        btnModify.setOnClickListener(v -> showModifyDialog());

        // Supprimer un contact
        btnDelete.setOnClickListener(v -> showDeleteDialog());
    }

    private void showAddDialog() {
        final String[] options = {"Numéro", "Email"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Ajouter un contact");
        builder.setItems(options, (dialog, which) -> {
            if (which == 0) {
                addPhoneNumber();
            } else {
                addEmail();
            }
        });
        builder.show();
    }

    private void showModifyDialog() {
        if (contactList.isEmpty()) {
            Toast.makeText(this, "Aucun contact à modifier.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Afficher une liste des contacts pour sélectionner celui à modifier
        String[] contactsArray = contactList.toArray(new String[0]);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Modifier un contact");
        builder.setItems(contactsArray, (dialog, which) -> {
            String selectedContact = contactList.get(which);
            showEditDialog(selectedContact);
        });
        builder.show();
    }

    private void showEditDialog(String contact) {
        boolean isPhoneNumber = contact.startsWith("Numéro :");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Modifier " + (isPhoneNumber ? "Numéro" : "Email"));

        final EditText input = new EditText(this);
        input.setInputType(isPhoneNumber ? InputType.TYPE_CLASS_PHONE : InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        input.setText(contact.split(": ")[1]); // Pré-remplir avec le contact existant
        builder.setView(input);

        builder.setPositiveButton("Modifier", (dialog, which) -> {
            String newValue = input.getText().toString().trim();
            if (!newValue.isEmpty()) {
                if (!isPhoneNumber && !isValidEmail(newValue)) {
                    Toast.makeText(this, "Adresse email invalide.", Toast.LENGTH_SHORT).show();
                } else {
                    // Supprimer l'ancien contact et ajouter le nouveau
                    contactList.remove(contact);
                    contactList.add((isPhoneNumber ? "Numéro :" : "Email :") + " " + newValue);
                    updateContactDetails();
                    Toast.makeText(this, "Contact modifié avec succès.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Le champ ne peut pas être vide.", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Annuler", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void showDeleteDialog() {
        if (contactList.isEmpty()) {
            Toast.makeText(this, "Aucun contact à supprimer.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Afficher une liste des contacts pour sélectionner celui à supprimer
        String[] contactsArray = contactList.toArray(new String[0]);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Supprimer un contact");
        builder.setItems(contactsArray, (dialog, which) -> {
            String selectedContact = contactList.get(which);
            contactList.remove(selectedContact);
            updateContactDetails();
            Toast.makeText(this, "Contact supprimé avec succès.", Toast.LENGTH_SHORT).show();
        });
        builder.show();
    }

    private void addPhoneNumber() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Ajouter un Numéro");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_PHONE);
        builder.setView(input);

        builder.setPositiveButton("Ajouter", (dialog, which) -> {
            String phoneNumber = input.getText().toString().trim();
            if (!phoneNumber.isEmpty()) {
                contactList.add("Numéro : " + phoneNumber);
                updateContactDetails();
                Toast.makeText(this, "Numéro ajouté : " + phoneNumber, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Le numéro ne peut pas être vide.", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Annuler", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void addEmail() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Ajouter un Email");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        builder.setView(input);

        builder.setPositiveButton("Ajouter", (dialog, which) -> {
            String email = input.getText().toString().trim();
            if (isValidEmail(email)) {
                contactList.add("Email : " + email);
                updateContactDetails();
                Toast.makeText(this, "Email ajouté : " + email, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Adresse email invalide.", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Annuler", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void updateContactDetails() {
        StringBuilder details = new StringBuilder();
        for (String contact : contactList) {
            details.append(contact).append("\n");
        }
        txtContactDetails.setText(details.toString());
    }

    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
