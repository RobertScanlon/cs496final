/****************************************************************************
 * filename:        EditPersonActivity.java
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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EditPersonActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_person);

        // set the person attributes
        final TextView oldFname = (TextView)findViewById(R.id.editPersonFname);
        final TextView oldLname = (TextView)findViewById(R.id.editPersonLname);
        final TextView oldAge = (TextView)findViewById(R.id.editPersonAge);
        final TextView oldAddress = (TextView)findViewById(R.id.editPersonAddress);

        final String oldFnameText = getIntent().getStringExtra("fname");
        final String oldLnameText = getIntent().getStringExtra("lname");
        final String oldAgeText = getIntent().getStringExtra("age");
        final String oldAddressText = getIntent().getStringExtra("address");

        oldFname.setText(oldFnameText);
        oldLname.setText(oldLnameText);
        oldAge.setText(oldAgeText);
        oldAddress.setText(oldAddressText);

        // set hints
        final EditText newAgeTextEdit = (EditText)findViewById(R.id.newPersonAgeEditText);
        final EditText newAddressEditText = (EditText)findViewById(R.id.newAddressEditText);

        newAgeTextEdit.setHint(oldAgeText);
        newAddressEditText.setHint(oldAddressText);

        Button editPersonButton = (Button)findViewById(R.id.submitPersonEditButton);
        editPersonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // new values
                String newAge = newAgeTextEdit.getText().toString();
                if (newAge == "") {
                    newAge = oldAgeText;
                }
                int newAgeInt = Integer.parseInt(newAge);
                String newAddress = newAddressEditText.getText().toString();

                // url
                String edit_url = getIntent().getStringExtra("url");
                editPerson(edit_url, newAgeInt, newAddress);
            }
        });
    }

    private void editPerson(String delUrl, int age, String addr) {

        final MediaType JSON = MediaType.parse("application/json");
        String body = "{\"age\": " + age + ", \"address\": \"" + addr + "\"}";

        Request req = new Request.Builder()
                .url(delUrl)
                .patch(RequestBody.create(JSON, body))
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

                String r = response.body().string();

            }
        });
    }
}
