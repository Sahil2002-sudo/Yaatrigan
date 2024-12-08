package com.example.carpooling;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class Search extends AppCompatActivity {
    EditText start,destination;
    Button search_ride,login,reg,swap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        start = (EditText) findViewById(R.id.editTextLeavingFrom);// Create an ArrayAdapter using the string array and a default spinner layout
        destination = (EditText) findViewById(R.id.editTextGoingTo);
        search_ride=(Button)findViewById(R.id.search);
        swap=(Button)findViewById(R.id.swap);
        swap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str=start.getText().toString();
                String dest=destination.getText().toString();
                destination.setText(str);
                start.setText(dest);

            }
        });
        search_ride.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1=new Intent(Search.this,search_ride_result.class);
                Bundle b1= new Bundle();
                b1.putString("start",start.getText().toString());
                b1.putString("destination",destination.getText().toString());
                i1.putExtras(b1);
                startActivity(i1);


            }
        });

        login=(Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i1= new Intent(Search.this,home.class);
                startActivity(i1);
            }
        });

       reg=(Button) findViewById(R.id.register);
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i1= new Intent(Search.this,register_user.class);
                startActivity(i1);
            }
        });

    }
}
