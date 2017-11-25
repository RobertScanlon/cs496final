/****************************************************************************
 * filename:        RemovePersonsPetActivity.java
 *
 * author:          Robert Scanlon
 *
 * description:     CS496 Fall 2017 Final Project
 *
 * last edit:       24 November 2017
 ****************************************************************************/

package com.cs496.robertscanlon.cs496final;

import android.app.DownloadManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RemovePersonsPetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_persons_pet);
        TextView removePetTextView = (TextView) findViewById(R.id.removePetTextView);
        removePetTextView.setText("Remove Pet: " + getIntent().getStringExtra("name") +
                " " + getIntent().getStringExtra("species"));

        Button removeButton = (Button) findViewById(R.id.confirmRemovePetButton);
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removePet(getIntent().getStringExtra("url"));
            }
        });
    }

    private void removePet(String url) {
        final MediaType JSON = MediaType.parse("application/json");
        String body = "";
        Request req = new Request.Builder()
                .url(url + "/caretaker")
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    if (response.code() == 200) {
                        ((TextView)findViewById(R.id.removePetTextView)).setText("Pet Removed");
                    } else {
                        ((TextView)findViewById(R.id.removePetTextView)).setText("Error Removing Pet");
                    }
                    }
                });
            }
        });
    }
}
