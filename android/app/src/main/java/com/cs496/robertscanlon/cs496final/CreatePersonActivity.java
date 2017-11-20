package com.cs496.robertscanlon.cs496final;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CreatePersonActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_person);

        Button newPersonButton = (Button) findViewById(R.id.newPersonButton);
        newPersonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fname = ((TextView)findViewById(R.id.newPersonfnameText)).toString();
                String lname = ((TextView)findViewById(R.id.newPersonfnameText)).toString();
                int age = Integer.parseInt(((TextView)findViewById(R.id.newPersonfnameText)).toString());
                String address = ((TextView)findViewById(R.id.newPersonfnameText)).toString();

                Log.d("First Name: ", fname);
                Log.d("Last Name: " , lname);
                Log.d("Age: ", String.valueOf(age));
                Log.d("Address: ", address);
            }
        });
    }
}
