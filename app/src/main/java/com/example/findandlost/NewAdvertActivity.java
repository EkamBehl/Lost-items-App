package com.example.findandlost;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

//import com.google.android.gms.location.LocationRequest;
public class NewAdvertActivity extends AppCompatActivity {
    EditText name, phone, description, date, location;
    RadioButton found, lost;
    FusedLocationProviderClient fusedLocationProviderClient;
    Button save, getCurrentLoc;
    DatabaseHelper dbHelper;
    LocationManager mLocationManager;


    public void makeMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    String type;
    Location currentLocation;
    ArrayList<LatLng> locList = new ArrayList<>();

    public void addData(String name, String type, String date, String phone, String description, String location) {
        boolean insertData = dbHelper.insertData(name, type, date, phone, description, location);
        if (insertData) {
            makeMessage("Successful entry!!");
        } else {
            makeMessage("Unsuccessful entry!!");
        }
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_advert);

        name = findViewById(R.id.name);
        date = findViewById(R.id.date);
        phone = findViewById(R.id.phone);
        description = findViewById(R.id.description);
        location = findViewById(R.id.location);
        long intervalMillis=90000;

        found = findViewById(R.id.found);
        lost = findViewById(R.id.lost);

        save = findViewById(R.id.Save);
        getCurrentLoc = findViewById(R.id.getCurrentLocation);
        dbHelper = new DatabaseHelper(this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        Places.initialize(getApplicationContext(),"AIzaSyDr40rQpS6SsZ7IfW21OZi9SF_uF2u6a6s");
        //location.setFocusable(false);




        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Objects.equals(name.length(), 0)) {
                    name.setText("");

                }
                if (Objects.equals(date.length(), 0)) {
                    date.setText("");

                }
                if (Objects.equals(phone.length(), 0)) {
                    phone.setText("");

                }
                if (Objects.equals(description.length(), 0)) {
                    description.setText("");

                }
                if (Objects.equals(location.length(), 0)) {
                    location.setText("");

                }
                if (found.isChecked()) {
                    type = "Found";

                } else {
                    type = "Lost";
                }

                addData(name.getText().toString(), type, date.getText().toString(), phone.getText().toString(), description.getText().toString(), location.getText().toString());
                Intent openMain=new Intent(getBaseContext(),MainActivity.class);

                startActivity(openMain);

            }
        });
        ActivityResultLauncher<Intent> resultLauncher =registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result !=null && result.getResultCode() == RESULT_OK){
                    if(result.getData() != null){
                        Place place=Autocomplete.getPlaceFromIntent(result.getData());
                        location.setText(String.valueOf(place.getLatLng().latitude)+","+String.valueOf(place.getLatLng().longitude));
                    }
                }
            }
        });
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Place.Field> fields= Arrays.asList(Place.Field.ADDRESS,Place.Field.LAT_LNG,Place.Field.NAME);
                Intent addLoc=new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY,fields).build(NewAdvertActivity.this);
                startActivityForResult(addLoc,100);

            }
        });
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getCurrentLoc.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                if(ActivityCompat.checkSelfPermission(NewAdvertActivity.this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                    getCurrentLocation();
                    //makeMessage("working");
                }else{
                    makeMessage("Not working");
                    ActivityCompat.requestPermissions(NewAdvertActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);

                }





            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100 && resultCode==RESULT_OK){
            Place place =Autocomplete.getPlaceFromIntent(data);
            location.setText(String.valueOf(place.getLatLng().latitude)+","+ String.valueOf(place.getLatLng().longitude));
            //dbHelper.insertLats(String.valueOf(place.getLatLng().latitude)+","+ String.valueOf(place.getLatLng().longitude),type.toString()+" "+ name.getText().toString());

        }
        else if(resultCode== AutocompleteActivity.RESULT_ERROR){
            makeMessage("wtf happened");
            Status status=Autocomplete.getStatusFromIntent(data);
        }
    }

    private void getCurrentLocation() {

        if(ActivityCompat.checkSelfPermission(NewAdvertActivity.this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED&& ActivityCompat.checkSelfPermission(NewAdvertActivity.this,Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){

            return;

        }
        mLocationManager=(LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location locations) {
                Geocoder geocoder= new Geocoder(NewAdvertActivity.this,Locale.getDefault());
                try {
                    List<Address> addr=geocoder.getFromLocation(locations.getLatitude(),locations.getLongitude(),1);
                    String address=String.valueOf(addr.get(0).getLatitude())+","+String.valueOf(addr.get(0).getLongitude());
                    location.setText(address);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location locations=task.getResult();
                if(locations != null){
                    Toast.makeText(NewAdvertActivity.this, "what is this", Toast.LENGTH_SHORT).show();

                    try {
                        Geocoder geocoder=new Geocoder(NewAdvertActivity.this, Locale.getDefault());
                        List<Address> addressList=geocoder.getFromLocation(locations.getLatitude(),locations.getLongitude(),1);
                        String text= String.valueOf(addressList.get(0).getLatitude())+","+String.valueOf(addressList.get(0).getLongitude());
                        location.setText(text);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }else{

                }

            }
        });



    }


}