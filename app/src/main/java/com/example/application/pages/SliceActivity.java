package com.example.application.pages;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.application.ImageAdapter;
import com.example.application.R;

import java.util.ArrayList;

public class SliceActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CAMERA_REQUEST = 2;

    private RecyclerView recyclerView;
    private ImageView selectedImageView;
    private ArrayList<Uri> imageUris = new ArrayList<>();
    private ImageAdapter imageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slice);

        recyclerView = findViewById(R.id.recyclerView_slices);
        selectedImageView = findViewById(R.id.selectedImageView);
        Button btnAdd = findViewById(R.id.btn_add_slice);
        Button btnEdit = findViewById(R.id.btn_edit_slice);
        Button btnDelete = findViewById(R.id.btn_delete_slice);  // Bouton Supprimer

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        imageAdapter = new ImageAdapter(imageUris);
        recyclerView.setAdapter(imageAdapter);

        // Ajouter une photo
        btnAdd.setOnClickListener(v -> showImageOptions());

        // Modifier les photos (Vous pouvez personnaliser cette logique)
        btnEdit.setOnClickListener(v -> {
            Toast.makeText(SliceActivity.this, "Modifier les photos", Toast.LENGTH_SHORT).show();
            // Logique pour modifier les photos
        });

        // Supprimer une photo
        btnDelete.setOnClickListener(v -> showDeleteDialog());
    }

    private void showImageOptions() {
        final String[] options = {"Prendre une photo", "Choisir une photo dans la galerie"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Choisissez une option");
        builder.setItems(options, (dialog, which) -> {
            if (which == 0) {
                openCamera();
            } else {
                openGallery();
            }
        });
        builder.show();
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        } else {
            Toast.makeText(this, "Aucune application de caméra disponible.", Toast.LENGTH_SHORT).show();
        }
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
    }

    // Afficher la boîte de dialogue pour choisir la photo à supprimer
    private void showDeleteDialog() {
        if (imageUris.isEmpty()) {
            Toast.makeText(this, "Aucune photo à supprimer", Toast.LENGTH_SHORT).show();
            return;
        }

        // Boîte de dialogue pour choisir une image à supprimer
        String[] imageNames = new String[imageUris.size()];
        for (int i = 0; i < imageUris.size(); i++) {
            imageNames[i] = "Image " + (i + 1);  // Afficher un nom générique pour chaque image
        }

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Choisir une photo à supprimer")
                .setItems(imageNames, (dialog, which) -> {
                    // Supprimer l'image sélectionnée
                    imageUris.remove(which);
                    imageAdapter.notifyDataSetChanged();  // Mettre à jour la RecyclerView
                    Toast.makeText(SliceActivity.this, "Image supprimée", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Annuler", null)
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE_REQUEST) {
                // Galerie : récupérer l'image sélectionnée
                Uri selectedImageUri = data.getData();
                if (selectedImageUri != null) {
                    // Ajouter l'URI de l'image à la liste
                    imageUris.add(selectedImageUri);
                    // Mettre à jour la RecyclerView sans afficher l'image dans ImageView
                    imageAdapter.notifyDataSetChanged();
                    Toast.makeText(this, "Image sélectionnée", Toast.LENGTH_SHORT).show();
                    // Masquer l'ImageView ici pour ne pas afficher l'image en bas
                    selectedImageView.setVisibility(View.GONE);
                }
            } else if (requestCode == CAMERA_REQUEST) {
                // Caméra : récupérer l'image capturée
                Uri capturedImageUri = data.getData();
                if (capturedImageUri != null) {
                    // Masquer l'ImageView pour ne pas afficher la photo en bas
                    selectedImageView.setVisibility(View.GONE);
                    // Ajouter l'URI de l'image à la liste
                    imageUris.add(capturedImageUri);
                    // Mettre à jour la RecyclerView
                    imageAdapter.notifyDataSetChanged();
                    Toast.makeText(this, "Photo prise", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }



}
