package com.example.application.pages;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.application.R;
import com.example.application.data.ApiClient;
import com.example.application.data.CommonCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CategoriesActivity extends AppCompatActivity {

    private LinearLayout categoryLayout;
    private Map<String, List<String>> categoryMap;
    private String currentCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);


        categoryLayout = findViewById(R.id.categoryLayout);
        categoryMap = new HashMap<>();

        loadCategoriesFromApi();
    }

    private void loadCategoriesFromApi() {
        ApiClient.shared.getListCategories(new CommonCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    parseCategories(result);
                    runOnUiThread(() -> addCategoryButtons());

                } catch(Exception exp) {
                    runOnUiThread(() -> {
                        Toast.makeText(CategoriesActivity.this,
                                "Could not parse data",
                                Toast.LENGTH_LONG).show();
                    });
                }
            }

            @Override
            public void onError(String err) {
                runOnUiThread(() -> {
                    Toast.makeText(CategoriesActivity.this,
                            err,
                            Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    private void parseCategories(String json) throws JSONException {
        JSONArray categoriesArray = new JSONArray(json);

        for (int i = 0; i < categoriesArray.length(); i++) {
            JSONObject categoryObject = categoriesArray.getJSONObject(i);
            String categoryName = categoryObject.getString("name");

            JSONArray subCategoriesArray = categoryObject.optJSONArray("subCategories");
            List<String> subCategories = new ArrayList<>();
            if (subCategoriesArray != null) {
                for (int j = 0; j < subCategoriesArray.length(); j++) {
                    subCategories.add(subCategoriesArray.getString(j));
                }
            }

            categoryMap.put(categoryName, subCategories);
        }
    }

    private void addCategoryButtons() {
        categoryLayout.removeAllViews();
        currentCategory = null;

        for (String category : categoryMap.keySet()) {
            Button categoryButton = new Button(this);
            categoryButton.setText(category);

            categoryButton.setOnClickListener(view -> showSubCategories(category));
            categoryLayout.addView(categoryButton);
        }
    }

    private void showSubCategories(String category) {
        currentCategory = category;
        categoryLayout.removeAllViews();

        List<String> subCategories = categoryMap.get(category);
        for (String subCategory : subCategories) {
            Button subCategoryButton = new Button(this);
            subCategoryButton.setText(subCategory);
            categoryLayout.addView(subCategoryButton);
        }

        Button backButton = new Button(this);
        backButton.setText("Retour");
        backButton.setOnClickListener(view -> addCategoryButtons());
        categoryLayout.addView(backButton);
    }
}
