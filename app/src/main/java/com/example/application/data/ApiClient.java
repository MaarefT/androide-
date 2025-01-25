package com.example.application.data;

import android.widget.Toast;

import com.example.application.pages.CategoriesActivity;

import org.json.JSONException;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ApiClient {
    public static ApiClient shared = new ApiClient();

    private OkHttpClient client;

    private ApiClient() {
        client = new OkHttpClient();
    }

    public void getListCategories(CommonCallback callback) {
        Request request = new Request.Builder()
                .url(Constants.getListCategories)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String errorMsg = "Erreur de connexion : " + e.getMessage();
                callback.onError(errorMsg);
                e.printStackTrace(); // Pour afficher les détails de l'erreur dans les logs
            }


            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    callback.onSuccess(responseBody);
                } else {
                    callback.onError("Erreur API: " + response.code());
                }
            }
        });
    }
}
