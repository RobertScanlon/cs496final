package com.cs496.robertscanlon.cs496final;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ViewFreePetsActivity extends AppCompatActivity {

    final String URL = "https://cs496final-186222.appspot.com/pet/free";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_free_pets);
        showFreePets(getIntent().getStringExtra("person_id"));
    }

    private void showFreePets(final String person_id) {
        Request req = new Request.Builder()
                .url(URL)
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

                final String r = response.body().string();

                try {
                    JSONArray items = new JSONArray(r);
                    ArrayList<HashMap<String,String>> pets;
                    pets = new ArrayList<HashMap<String,String>>();

                    for (int i = 0; i < items.length(); i++) {
                        HashMap<String,String> p;
                        p = new HashMap<String,String>();
                        p.put("person_id",
                                person_id);
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

                    final AddPersonsPetAdapter petsAdapter;
                    petsAdapter = new AddPersonsPetAdapter(
                            ViewFreePetsActivity.this,
                            pets,
                            R.layout.free_pets_layout,
                            new String[]{"name","species","age","weight"},
                            new int[]{R.id.freePetName,
                                    R.id.freePetSpecies,
                                    R.id.freePetAge,
                                    R.id.freePetWeight}
                    );

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ListView allPetsListView;
                            allPetsListView = (ListView)
                                    findViewById(R.id.freePetsListView);
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
