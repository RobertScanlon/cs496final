package com.cs496.robertscanlon.cs496final;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class EditPetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pet);

        // set the pet attributes
        TextView oldName = (TextView)findViewById(R.id.editPetName);
        TextView oldSpecies = (TextView)findViewById(R.id.editPetSpecies);
        TextView oldAge = (TextView)findViewById(R.id.editPetAge);
        TextView oldWeight = (TextView)findViewById(R.id.editPetWeight);

        String oldNameText = getIntent().getStringExtra("name");
        String oldSpeciesText = getIntent().getStringExtra("species");
        String oldAgeText = getIntent().getStringExtra("age");
        String oldWeightText = getIntent().getStringExtra("weight");

        oldName.setText(oldNameText);
        oldSpecies.setText(oldSpeciesText);
        oldAge.setText(oldAgeText);
        oldWeight.setText(oldWeightText);

        // set hints
        EditText newAgeTextEdit = (EditText)findViewById(R.id.newAgeEditText);
        EditText newWeightEditText = (EditText)findViewById(R.id.newWeightEditText);

        newAgeTextEdit.setHint(oldAgeText);
        newWeightEditText.setHint(oldWeightText);

        // new values
        String newAge = newAgeTextEdit.getText().toString();
        if (newAge == "") {
            newAge = oldAgeText;
        }
        String newWeight = newWeightEditText.getText().toString();
        if (newWeight == "") {
            newWeight = oldWeightText;
        }
        int newAgeInt = Integer.parseInt(newAge);
        int newWeightInt = Integer.parseInt(newWeight);

        // url
        String edit_url = getIntent().getStringExtra("url");
    }

    private void editPet(String delUrl, int age, int weight) {

        final MediaType JSON = MediaType.parse("application/json");
        String body = "{\"age\": " + age + ", \"weight\":" + weight + "}";

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

                try {
                    JSONObject item = new JSONObject(r);
                    final String newname = item.getString("name");
                    final String newspecies = item.getString("species");
                    final String newage = item.getString("age");
                    final String newweight = item.getString("weight");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            ((TextView)findViewById(R.id.newPetName)).setText(newname);
//                            ((TextView)findViewById(R.id.newPetSpecies)).setText(newspecies);
//                            ((TextView)findViewById(R.id.newPetAge)).setText(newage);
//                            ((TextView)findViewById(R.id.newPetWeight)).setText(newweight);
                        }
                    });
                } catch (JSONException je) {
                    je.printStackTrace();
                }
            }
        });
    }
}
