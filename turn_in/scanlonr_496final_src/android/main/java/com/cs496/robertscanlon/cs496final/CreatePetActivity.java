/****************************************************************************
 * filename:        CreatePetActivity.java
 *
 * author:          Robert Scanlon
 *
 * description:     CS496 Fall 2017 Final Project
 *
 * last edit:       24 November 2017
 ****************************************************************************/

package com.cs496.robertscanlon.cs496final;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONObject;
import org.json.JSONException;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CreatePetActivity extends AppCompatActivity {

    final String BASE_URL = "https://cs496final-186222.appspot.com/pet";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pet);
        Button newPetButton = (Button) findViewById(R.id.newPetButton);
        newPetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final MediaType JSON = MediaType.parse("application/json");
                OkHttpClient mOkHttpClient = new OkHttpClient();
                Request request;

                String name = ((TextView)findViewById(R.id.newPetNameText)).getText().toString();
                String species = ((TextView)findViewById(R.id.newPetSpeciesText)).getText().toString();
                int age = Integer.parseInt(((TextView)findViewById(R.id.newPetAgeText)).getText().toString());
                int weight = Integer.parseInt(((TextView)findViewById(R.id.newPetWeightText)).getText().toString());

                Log.d("Name: ", name);
                Log.d("Species: " , species);
                Log.d("Age: ", String.valueOf(age));
                Log.d("Weight: ", String.valueOf(weight));

                // create json string to send in post request
                String body = "{\"name\": \"" + name + "\", \"species\": \"" + species + "\", " +
                        "\"age\": " + age + ", \"weight\": " + weight + "}";
                Log.d("body string: ", body);

                request = new Request.Builder()
                        .url(BASE_URL)
                        .post(RequestBody.create(JSON, body))
                        .build();

                mOkHttpClient = new OkHttpClient();
                mOkHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String r = response.body().string();

                        try {
                            JSONObject item = new JSONObject(r);
                            final String newname = item.getString("name");
                            final String newspecies = item.getString("species");
                            final String newage = item.getString("age");
                            final String newweight = item.getString("weight");

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ((TextView)findViewById(R.id.newPetName)).setText(newname);
                                    ((TextView)findViewById(R.id.newPetSpecies)).setText(newspecies);
                                    ((TextView)findViewById(R.id.newPetAge)).setText(newage);
                                    ((TextView)findViewById(R.id.newPetWeight)).setText(newweight);
                                }
                            });
                        } catch (JSONException je) {
                            je.printStackTrace();
                        }
                    }
                });
            }
        });
    }
}
