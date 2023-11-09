package com.example.assignment2;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private Button load, getcoor, newLocation;
    private EditText input;
    private TextView address;
    private ImageView delete;

    double longitude;
    double latitude;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        load = findViewById(R.id.load);
        delete = findViewById(R.id.delete);
        getcoor = findViewById(R.id.getcordinates);
        delete.setVisibility(View.GONE);
        newLocation= findViewById(R.id.newlocation);
        input = findViewById(R.id.addressinput);
        address = findViewById(R.id.address);
        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                geocodeLatLongPairs(MainActivity.this, "pairs.txt");
            }
        });

        getcoor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userinput = input.getText().toString();
                double [] latlng = getLatLongForAddress(userinput);
                delete.setVisibility(View.VISIBLE);
                if (latlng != null) {
                    latitude = latlng[0];
                    longitude = latlng[1];
                    // Display latitude and longitude to the user
                    String coordinates = "(" + Double.toString(latitude) + ", " + Double.toString(longitude)+")";
                    address.setText(coordinates);

                } else {
                    Toast.makeText(MainActivity.this, "not found", Toast.LENGTH_SHORT).show();
                }

            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper dbHelper = new DatabaseHelper(MainActivity.this);
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                Boolean checkdelete = dbHelper.deleteLocation(latitude, longitude);
                if(checkdelete){
                    Toast.makeText(MainActivity.this, "Note has been Deleted", Toast.LENGTH_SHORT).show();
                    address.setText("");
                    input.setText("");
                }
            }
        });



        newLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (MainActivity.this, NewLocation.class);
                startActivity(intent);
                //Toast.makeText(MainActivity.this, "this works", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public List<String> geocodeLatLongPairs(Context context, String assetFileName) {
        List<String> addresses = new ArrayList<>();
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());

        DatabaseHelper dbHelper = new DatabaseHelper(MainActivity.this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            // Open and read the file using an AssetManager
            AssetManager assetManager = context.getAssets();
            InputStream inputStream = assetManager.open(assetFileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            while ((line = reader.readLine()) != null) {
                // Split the line into latitude and longitude values
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    double latitude = Double.parseDouble(parts[0].trim());
                    double longitude = Double.parseDouble(parts[1].trim());
                    List<Address> addressesList = geocoder.getFromLocation(latitude, longitude, 1);

                    if (!addressesList.isEmpty()) {
                        Address address = addressesList.get(0);
                        String addressString = address.getAddressLine(0);
                        addresses.add(addressString);

                        //Toast.makeText(this, addressString, Toast.LENGTH_SHORT).show();

                        // Insert the address into the database
                        Boolean checkinsertdata = dbHelper.insert(addressString, latitude, longitude);
                        if(checkinsertdata) {
                            Toast.makeText(MainActivity.this, "Address has been saved :)", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(MainActivity.this, "failed to save", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        addresses.add("No address found for coordinates: " + latitude + ", " + longitude);
                        //Toast.makeText(MainActivity.this, "no address", Toast.LENGTH_SHORT).show();
                        //dbHelper.insert(null, latitude, longitude);
                    }
                }
            }

            // Close the file and the database
            reader.close();
            ///db.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return addresses;
    }

    public double[] getLatLongForAddress(String address) {
        DatabaseHelper dbHelper = new DatabaseHelper(MainActivity.this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {DatabaseHelper.LAT_COL, DatabaseHelper.LONG_COL};
        String selection = DatabaseHelper.ADDRESS_COL + " LIKE ?";
        String[] selectionArgs = new String[]{"%" + address + "%"};

        Cursor cursor = db.query(DatabaseHelper.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        double[] latLng = new double[2]; // Index 0 for latitude, Index 1 for longitude

        if (cursor != null) {
            //Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();
            if (cursor.moveToFirst()) {
                //Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();
                latLng[0] = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.LAT_COL));
                latLng[1] = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.LONG_COL));

                cursor.close();
            } else {
                // Handle the case where the address is not found
                latLng = null; // Set to null or handle accordingly
            }
            cursor.close();
        } else {
            // Handle the case where the cursor is null
            latLng = null; // Set to null or handle accordingly
        }
        db.close();
        return latLng;
    }






}