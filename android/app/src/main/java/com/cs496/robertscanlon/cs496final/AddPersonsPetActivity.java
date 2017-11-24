/****************************************************************************
 * filename:        AddPersonsPetActivity.java
 *
 * author:          Robert Scanlon
 *
 * description:     CS496 Fall 2017 Final Project
 *
 * last edit:       24 November 2017
 ****************************************************************************/

package com.cs496.robertscanlon.cs496final;

import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddPersonsPetActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_persons_pet);
        TextView addPetTextView = (TextView) findViewById(R.id.AddPetTextView);
        addPetTextView.setText("Add Pet: " + getIntent().getStringExtra("name") +
                " " + getIntent().getStringExtra("species"));

        Button addButton = (Button) findViewById(R.id.confirmAddPetButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPet(getIntent().getStringExtra("url"),
                       getIntent().getStringExtra("person_id"));
            }
        });
    }

    private void addPet(String url, String person_id) {
        final MediaType JSON = MediaType.parse("application/json");
        String body = "{\"person_id\": " + "\"" + person_id + "\"}";
        Request req = new Request.Builder()
                .url(url + "/caretaker")
                .put(RequestBody.create(JSON, body))
                .build();


        OkHttpClient mOkHttpClient;
        mOkHttpClient = new OkHttpClient();
        mOkHttpClient.newCall(req).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.code() == 200) {
                            ((TextView)findViewById(R.id.AddPetTextView)).setText("Pet Added");
                        } else {
                            ((TextView)findViewById(R.id.AddPetTextView)).setText("Error Adding Pet");
                        }
                    }
                });
            }
        });
    }
}
