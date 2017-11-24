/****************************************************************************
 * filename:        ViewAllPetsActivity.java
 *
 * author:          Robert Scanlon
 *
 * description:     CS496 Fall 2017 Final Project
 *
 * last edit:       24 November 2017
 ****************************************************************************/

package com.cs496.robertscanlon.cs496final;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;

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

public class ViewAllPetsActivity extends AppCompatActivity {

    final String BASE_URL = "https://cs496final-186222.appspot.com/";
    OkHttpClient httpClient = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_pets);
        showAllPets();
    }

    private void showAllPets() {

        Request req = new Request.Builder()
                .url(BASE_URL + "pet")
                .build();

        OkHttpClient mOkHttpClient;
        mOkHttpClient = new OkHttpClient();
        mOkHttpClient.newCall(req).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String r = response.body().string();

                try {
                    JSONArray items = new JSONArray(r);
                    ArrayList<HashMap<String,String>> pets;
                    pets = new ArrayList<HashMap<String,String>>();

                    for (int i = 0; i < items.length(); i++) {
                        HashMap<String,String> p;
                        p = new HashMap<String,String>();
                        p.put("name", "Name: " +
                                items.getJSONObject(i).getString("name"));
                        p.put("species", "Species: " +
                                items.getJSONObject(i).getString("species"));
                        p.put("age", "Age: " +
                                items.getJSONObject(i).getString("age"));
                        p.put("weight", "Weight: " +
                                items.getJSONObject(i).getString("weight"));
                        p.put("pet_selfUrl",
                                items.getJSONObject(i).getString("self"));
                        pets.add(p);
                    }

                    final PetAdapter petsAdapter;
                    petsAdapter = new PetAdapter(
                            ViewAllPetsActivity.this,
                            pets,
                            R.layout.all_pets_layout,
                            new String[]{"name","species","age","weight"},
                            new int[]{R.id.allPetName,
                                    R.id.allPetSpecies,
                                    R.id.allPetAge,
                                    R.id.allPetWeight}
                    );

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ListView allPetsListView;
                            allPetsListView = (ListView)
                                    findViewById(R.id.petsListView);
                            allPetsListView.setAdapter(petsAdapter);
                        }
                    });
                } catch (JSONException je) {
                    je.printStackTrace();
                }
            }
        });
    }
}

