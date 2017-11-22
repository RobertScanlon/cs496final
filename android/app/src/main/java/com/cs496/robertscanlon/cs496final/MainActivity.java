package com.cs496.robertscanlon.cs496final;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Show all People
        Button allPeopleButton = (Button) findViewById(R.id.viewAllPeopleButton);
        allPeopleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent allPeopleIntent = new Intent(MainActivity.this, ViewAllPeopleActivity.class);
                startActivity(allPeopleIntent);
            }
        });

        // Show all Pets
        Button allPetsButton = (Button) findViewById(R.id.viewAllPetsButton);
        allPetsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent allPetsIntent = new Intent(MainActivity.this, ViewAllPetsActivity.class);
                startActivity(allPetsIntent);
            }
        });

        // Create a new Person
        Button newPersonButton = (Button) findViewById(R.id.addPersonButton);
        newPersonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newPersonIntent = new Intent(MainActivity.this, CreatePersonActivity.class);
                startActivity(newPersonIntent);
            }
        });

        // Create a new Pet
        Button newPetButton = (Button) findViewById(R.id.addPetButton);
        newPetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newPetIntent = new Intent(MainActivity.this, CreatePetActivity.class);
                startActivity(newPetIntent);
            }
        });
    }
}
