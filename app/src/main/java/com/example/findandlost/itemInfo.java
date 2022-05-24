package com.example.findandlost;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class itemInfo extends AppCompatActivity {

    Button delete;
    TextView text;
    //String txt;
    String name,type,location,phone,description,date;
    DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_info);
        text=findViewById(R.id.textView5);
        delete=findViewById(R.id.delete);
        db=new DatabaseHelper(this);
        Intent getIntent=getIntent();
        //txt=getIntent.getStringExtra("item");
        name=getIntent.getStringExtra("name");
        date=getIntent.getStringExtra("date");
        type=getIntent.getStringExtra("type");
        location=getIntent.getStringExtra("location");
        phone=getIntent.getStringExtra("phone");
        description=getIntent.getStringExtra("description");

        text.setText(type+" "+name+"\n"+description+"\n" +date+"\n"+location);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.deleteItem(name,type,date,phone,description,location);

                text.setText("");
            }

        });
    }
}