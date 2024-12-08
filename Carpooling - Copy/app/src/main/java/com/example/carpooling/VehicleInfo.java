package com.example.carpooling;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class VehicleInfo extends AppCompatActivity {

    TextView Number,Color,Type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_info);
        Bundle b1=getIntent().getExtras();
        Number=(TextView)findViewById(R.id.number);
        Color=(TextView)findViewById(R.id.color);
        Type=(TextView)findViewById(R.id.type);
        Number.setText(b1.getString("number"));
        Color.setText(b1.getString("color"));
        Type.setText(b1.getString("type"));

    }
}
