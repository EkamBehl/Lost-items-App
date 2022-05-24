package com.example.findandlost;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class allItemspage extends AppCompatActivity {
    DatabaseHelper dbhelper;
    private ListView listView;
    String name,date,phone, location,description,type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_itemspage);
        listView=findViewById(R.id.listView);
        dbhelper=new DatabaseHelper(this);
        addValsToList();
    }
    ArrayList<String> fullList=new ArrayList<>();
    ArrayList<ArrayList<String>> finalList=new ArrayList<ArrayList<String>>();
    private void addValsToList() {
        Cursor data= dbhelper.getData();
        ArrayList<String> list=new ArrayList<>();


        while(data.moveToNext()){

            type=data.getString(1);
            name=data.getString(0);
            date=data.getString(2);
            phone=data.getString(3);
            description=data.getString(4);

            location=data.getString(5);
            ArrayList<String> newlist= new ArrayList<>(Arrays.asList(name,type,date,phone,description,location));
            list.add(type+" "+name);
            fullList.add(type+" "+name+"\n"+description+"\n" +date+"\n"+location);
            finalList.add(newlist);
        }
        ListAdapter adapter=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String text= fullList.get(i).toString();
                Intent itemIntent=new Intent(allItemspage.this,itemInfo.class);
                itemIntent.putExtra("name",finalList.get(i).get(0));

                itemIntent.putExtra("type",finalList.get(i).get(1));
                itemIntent.putExtra("date",finalList.get(i).get(2));
                itemIntent.putExtra("phone",finalList.get(i).get(3));
                itemIntent.putExtra("description",finalList.get(i).get(4));
                itemIntent.putExtra("location",finalList.get(i).get(5));

                itemIntent.putExtra("item",text);
                startActivity(itemIntent);
            }
        });

    }
    public void makeMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}