package com.cs496.robertscanlon.cs496final;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

/**
 * Created by robert on 11/24/17.
 */

public class ViewPersonsPetsActivity  extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_persons_pets);
        showPersonsPets(getIntent().getStringExtra("url"));
    }

    private void showPersonsPets(String url) {
        Request req = new Request.Builder()
                .url(url + "/pets")
                .build();

        OkHttpClient mOkHttpClient = new OkHttpClient();
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

                    final PersonsPetsAdapter petsAdapter;
                    petsAdapter = new PersonsPetsAdapter(
                            ViewPersonsPetsActivity.this,
                            pets,
                            R.layout.persons_pets_layout,
                            new String[]{"name","species","age","weight"},
                            new int[]{R.id.personsPetName,
                                    R.id.personsPetSpecies,
                                    R.id.personsPetAge,
                                    R.id.personsPetWeight}
                    );

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ListView allPetsListView;
                            allPetsListView = (ListView)
                                    findViewById(R.id.personsPetsListView);
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
