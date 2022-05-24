package com.example.findandlost;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button newAdvert,allitems,showOnMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        newAdvert=findViewById(R.id.newAdvert);
        allitems=findViewById(R.id.allItems);
        showOnMap=findViewById(R.id.showOnMap);
        newAdvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getBaseContext(),NewAdvertActivity.class);
                startActivity(intent);
            }
        });
        allitems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getBaseContext(),allItemspage.class);
                startActivity(intent);
            }
        });
        showOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getBaseContext(),MapsActivity.class);
                startActivity(intent);
            }
        });
    }





}