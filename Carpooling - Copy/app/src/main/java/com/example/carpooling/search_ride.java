package com.example.carpooling;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class search_ride extends AppCompatActivity {
    EditText start,destination;
    Button search_ride;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_ride);
        start = (EditText) findViewById(R.id.start);// Create an ArrayAdapter using the string array and a default spinner layout
        destination = (EditText) findViewById(R.id.destination);
        search_ride=(Button)findViewById(R.id.search_ride_button);
        search_ride.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1=new Intent(search_ride.this,search_ride_result.class);
                Bundle b1= new Bundle();
                b1.putString("start",start.getText().toString());
                b1.putString("destination",destination.getText().toString());
                i1.putExtras(b1);
                startActivity(i1);


            }
        });



    }
}
