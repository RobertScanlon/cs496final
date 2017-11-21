package com.cs496.robertscanlon.cs496final;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

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
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CreatePersonActivity extends AppCompatActivity {

    final String BASE_URL = "https://cs496final-186222.appspot.com/person";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_person);

        Button newPersonButton = (Button) findViewById(R.id.newPersonButton);
        newPersonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final MediaType JSON = MediaType.parse("application/json");
                OkHttpClient mOkHttpClient = new OkHttpClient();
                Request request;

                String fname = ((TextView)findViewById(R.id.newPersonfnameText)).getText().toString();
                String lname = ((TextView)findViewById(R.id.newPersonlnameText)).getText().toString();
                int age = Integer.parseInt(((TextView)findViewById(R.id.newPersonageText)).getText().toString());
                String address = ((TextView)findViewById(R.id.newPersonaddressText)).getText().toString();

                Log.d("First Name: ", fname);
                Log.d("Last Name: " , lname);
                Log.d("Age: ", String.valueOf(age));
                Log.d("Address: ", address);

                // create json string to send in post request
                String body = "{\"fname\": \"" + fname + "\", \"lname\": \"" + lname + "\", " +
                        "\"age\": " + age + ", \"address\": \"" + address + "\"}";
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
                            HashMap<String,String> person;
                            person = new HashMap<String,String>();
                            List<Map<String,String>> people;
                            people = new ArrayList<Map<String,String>>();
                            person.put("fname", "First Name: " + item.getString("fname"));
                            person.put("lname", "Last Name: " + item.getString("lname"));
                            person.put("age", "Age: " + item.getString("age"));
                            person.put("address", "Address: " + item.getString("address"));

                            final SimpleAdapter peopleAdapter;
                            peopleAdapter = new SimpleAdapter(
                                    CreatePersonActivity.this,
                                    people,
                                    R.layout.new_person_layout,
                                    new String[]{"fname","lname","age","address"},
                                    new int[]{R.id.newPersonFname,
                                            R.id.newPersonLname,
                                            R.id.newPersonAge,
                                            R.id.newPersonAddress}
                            );

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ListView allPeopleListView;
                                    allPeopleListView = (ListView)
                                            findViewById(R.id.newPersonListView);
                                    allPeopleListView.setAdapter(peopleAdapter);
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
