package com.cs496.robertscanlon.cs496final;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DeletePersonActivity extends AppCompatActivity {

    final String BASE_URL = "https://cs496final-186222.appspot.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_person);
        TextView deletePersonTextView;
        deletePersonTextView = (TextView) findViewById(R.id.deletePersonTextView);
        deletePersonTextView.setText("Delete Person: " + getIntent().getStringExtra("fname") +
        " " + getIntent().getStringExtra("lname"));

        Button deleteButton = (Button) findViewById(R.id.confirmDeletePersonButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletePerson(getIntent().getStringExtra("url"));
            }
        });
    }

    private void deletePerson(String delUrl) {

        Request req = new Request.Builder()
                .url(delUrl)
                .delete()
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
                        if (response.code() == 204) {
                            ((TextView)findViewById(R.id.deletePersonTextView)).setText("Person Deleted");
                        } else {
                            ((TextView)findViewById(R.id.deletePersonTextView)).setText("Error Deleting Person");
                        }
                    }
                });
            }
        });
    }
}
