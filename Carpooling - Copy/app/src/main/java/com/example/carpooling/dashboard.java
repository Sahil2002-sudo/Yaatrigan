package com.example.carpooling;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class dashboard extends AppCompatActivity {
    Button add_ride,search_ride,history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        add_ride=(Button)findViewById(R.id.add_ride);
        search_ride=(Button)findViewById(R.id.search_ride);
        history=(Button)findViewById(R.id.history);

        add_ride.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1=new Intent(dashboard.this, add_ride.class);
                startActivity(i1);
            }
        });
        search_ride.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1=new Intent(dashboard.this,search_ride.class);
                startActivity(i1);
            }
        });
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1= new Intent(dashboard.this,booking_history.class);
                startActivity(i1);
            }
        });
    }
}
