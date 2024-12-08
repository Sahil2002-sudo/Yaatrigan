package com.example.carpooling;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLEncoder;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

public class AddVehicle extends AppCompatActivity {
    EditText color,vehicleNo;
    Spinner type;
    Button addVehicle;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle);
        color=(EditText)findViewById(R.id.Color);
        vehicleNo=(EditText)findViewById(R.id.VehicleNo);
        type=(Spinner)findViewById(R.id.car_type);
        addVehicle=(Button)findViewById(R.id.AddVehicle);
        addVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences=getSharedPreferences("MYPREF",MODE_PRIVATE);
                String id=sharedPreferences.getString("ID","");
                Add add=new Add();
                add.execute("http://mahavidyalay.in/AcademicDevelopment/carpooling/AddVehicle.php?number="
                        + URLEncoder.encode(vehicleNo.getText().toString())
                        +"&color="+URLEncoder.encode(color.getText().toString())
                        +"&type="+URLEncoder.encode(type.getSelectedItem().toString())
                        +"&owner="+URLEncoder.encode(id));
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    private class Add extends AsyncTask<String, Integer, String> {
        private ProgressDialog progress = null;
        String out="";
        int count=0;
        @Override
        protected String doInBackground(String... geturl) {


            try{
                HttpClient http=new DefaultHttpClient();
                HttpPost http_get= new HttpPost(geturl[0]);
                HttpResponse response=http.execute(http_get);
                HttpEntity http_entity=response.getEntity();
                BufferedReader br= new BufferedReader(new InputStreamReader(http_entity.getContent()));
                out = br.readLine();

            }catch (Exception e){

                out= e.toString();
            }
            return out;
        }

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(AddVehicle.this, null,
                    "Add Vehicle...");

            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            Toast.makeText(AddVehicle.this, "Vehicle Added Sucessfully"+result, Toast.LENGTH_LONG).show();
            progress.dismiss();
            finish();
        }



    }
}
