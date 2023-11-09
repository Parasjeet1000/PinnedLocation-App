package com.example.assignment2;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NewLocation extends AppCompatActivity {


    private Button add, cancel;
    private EditText address, longitudein, latitudein;
    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newlocation);
        //textView = findViewById(R.id.textView3);
        address = findViewById(R.id.addresslocation);
        longitudein = findViewById(R.id.longitudein);
        latitudein = findViewById(R.id.latitudein);
        add = findViewById(R.id.add_button);
        cancel = findViewById(R.id.cancel);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             DatabaseHelper dbHelper = new DatabaseHelper(NewLocation.this);
             SQLiteDatabase db = dbHelper.getWritableDatabase();
             double latitude = Double.parseDouble(latitudein.getText().toString());
             double longitude = Double.parseDouble(longitudein.getText().toString());
             String addressString = address.getText().toString();
             //Toast.makeText(test.this, addressString, Toast.LENGTH_SHORT).show();
                // Toast.makeText(test.this, Double.toString(latitude), Toast.LENGTH_SHORT).show();

                    // Insert the address into the database
                    Boolean checkinsertdata = dbHelper.insert(addressString, latitude, longitude);
                    if(checkinsertdata) {
                        Toast.makeText(NewLocation.this, "location has been saved :)", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(NewLocation.this, MainActivity.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(NewLocation.this, "failed to save", Toast.LENGTH_SHORT).show();
                    }
                }




         });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                DatabaseHelper dbHelper = new DatabaseHelper(test.this);
//                SQLiteDatabase db = dbHelper.getWritableDatabase();
//                double latitude = Double.parseDouble(latitudein.getText().toString());
//                double longitude = Double.parseDouble(longitudein.getText().toString());
//                String addressString = address.getText().toString();
//                Toast.makeText(test.this, addressString, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(NewLocation.this, MainActivity.class);
                startActivity(intent);
            }
        });


    }
}