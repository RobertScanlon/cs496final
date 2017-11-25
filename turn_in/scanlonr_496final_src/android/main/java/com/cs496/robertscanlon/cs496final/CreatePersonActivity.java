/****************************************************************************
 * filename:        CreatePersonActivity.java
 *
 * author:          Robert Scanlon
 *
 * description:     CS496 Fall 2017 Final Project
 *
 * last edit:       24 November 2017
 ****************************************************************************/

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
                        final String newfname = item.getString("fname");
                        final String newlname = item.getString("lname");
                        final String newage = item.getString("age");
                        final String newaddress = item.getString("address");

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ((TextView)findViewById(R.id.newPersonFname)).setText(newfname);
                                ((TextView)findViewById(R.id.newPersonLname)).setText(newlname);
                                ((TextView)findViewById(R.id.newPersonAge)).setText(newage);
                                ((TextView)findViewById(R.id.newPersonAddress)).setText(newaddress);
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
