package com.cs496.robertscanlon.cs496final;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;

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

public class ViewAllPeopleActivity extends AppCompatActivity {

    final String BASE_URL = "https://cs496final-186222.appspot.com/";
    OkHttpClient httpClient = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_people);
        showAllPeople();
    }

    private void showAllPeople() {

        Request req = new Request.Builder()
                .url(BASE_URL + "person")
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
                    List<Map<String,String>> people;
                    people = new ArrayList<Map<String,String>>();

                    for (int i = 0; i < items.length(); i++) {
                        HashMap<String,String> p;
                        p = new HashMap<String,String>();
                        p.put("fname", "First Name: " +
                                items.getJSONObject(i).getString("fname"));
                        p.put("lname", "Last Name: " +
                                items.getJSONObject(i).getString("lname"));
                        p.put("age", "Age: " +
                                items.getJSONObject(i).getString("age"));
                        p.put("address", "Address: " +
                                items.getJSONObject(i).getString("address"));
                        people.add(p);
                    }

                    final SimpleAdapter peopleAdapter;
                    peopleAdapter = new SimpleAdapter(
                            ViewAllPeopleActivity.this,
                            people,
                            R.layout.all_people_layout,
                            new String[]{"fname","lname","age","address"},
                            new int[]{R.id.allPeopleFname,
                                    R.id.allPeopleLname,
                                    R.id.allPeopleAge,
                                    R.id.allPeopleAddress}
                    );

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ListView allPeopleListView;
                            allPeopleListView = (ListView)
                                    findViewById(R.id.peopleListView);
                            allPeopleListView.setAdapter(peopleAdapter);
                        }
                    });
                } catch (JSONException je) {
                    je.printStackTrace();
                }
            }
        });
    }
}
