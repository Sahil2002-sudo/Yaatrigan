package com.example.carpooling;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.Calendar;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

public class add_ride extends AppCompatActivity {
    EditText start,destination,via;
    Spinner vehicle,ampm;
    EditText name,contact,address,time,seats,cost;
    SharedPreferences sharedPreferences;
    Button add_ride;
    String[] number,color,type,vehicleId;
    String vId="";
    DatePickerDialog datePickerDialog;

    TextView exp;
    int year;
    int month;
    int dayOfMonth;
    Calendar calendar;

    public double lati = 0.0;
    public double longi = 0.0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ride);
        add_ride=(Button)findViewById(R.id.add_ride_button);
        start = (EditText) findViewById(R.id.start);// Create an ArrayAdapter using the string array and a default spinner layout
        destination = (EditText) findViewById(R.id.destination);
        via = (EditText) findViewById(R.id.via);
        vehicle=(Spinner)findViewById(R.id.vehicle);
        time=(EditText)findViewById(R.id.time);
        seats=(EditText)findViewById(R.id.seats);
        cost=(EditText)findViewById(R.id.cost);
        ampm=(Spinner)findViewById(R.id.ampm);
        exp=(TextView)findViewById(R.id.exp);

        exp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(add_ride.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                exp.setText(day + "/" + (month + 1) + "/" + year);
                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();

            }
        });

        sharedPreferences=getSharedPreferences("MYPREF",MODE_PRIVATE);
        String id=sharedPreferences.getString("ID","");
        FetchCordinates fetchCordinates = new FetchCordinates();
        fetchCordinates.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR) ;


        GetData get=new GetData();
        get.execute("http://mahavidyalay.in/AcademicDevelopment/carpooling/ViewVehicle.php?id="+id);

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(add_ride.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {

                        time.setText(hourOfDay + ":" + minutes);

                    }
                }, 0, 0, false);
                timePickerDialog.show();

            }
        });

        add_ride.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences=getSharedPreferences("MYPREF",MODE_PRIVATE);
                String id=sharedPreferences.getString("ID","");
                boolean isok=true;
                int cost1=Integer.parseInt(cost.getText().toString());
                if(cost.getText().toString().length()==0 ||cost1>10000 ){
                    cost.setError("Please Enter Valid Cost");
                    cost.requestFocus(20);
                    isok=false;
                }
                int seatcount=Integer.parseInt(seats.getText().toString().trim());
                if(seats.getText().toString().length()==0 ||seatcount>7){
                    seats.setError("Please Enter Valid Seats");
                    seats.requestFocus(20);
                    isok=false;
                }

                if(time.getText().toString().length()==0){
                    time.setError("Please Enter Valid Time");
                    time.requestFocus(20);
                    isok=false;
                }

                if(via.getText().toString().length()==0){
                    via.setError("Please Enter Valid address");
                    via.requestFocus(20);
                    isok=false;
                }
                if(start.getText().toString().length()==0){
                    start.setError("Please Enter Valid address");
                    start.requestFocus(20);
                    isok=false;
                }
                if(destination.getText().toString().length()==0){
                    destination.setError("Please Enter Valid address");
                    destination.requestFocus(20);
                    isok=false;
                }


                vId=vehicleId[vehicle.getSelectedItemPosition()-1];
                if(vId=="" || vId=="0"){

                    vehicle.requestFocus(20);
                    isok=false;
                }



                if(isok==true){
                    AddRide gettrans = new AddRide();

                    String url1 = "http://mahavidyalay.in/AcademicDevelopment/carpooling/add_ride.php?via=" + URLEncoder.encode(via.getText().toString())
                            + "&time=" + URLEncoder.encode(time.getText().toString())
                            + "&start=" + URLEncoder.encode(start.getText().toString())
                            + "&destination=" + URLEncoder.encode(destination.getText().toString())
                            + "&car_type=" + URLEncoder.encode(vehicle.getSelectedItem().toString())
                            + "&date=" + URLEncoder.encode(exp.getText().toString())
                            + "&lati=" + URLEncoder.encode(String.valueOf(lati))
                            + "&longi=" + URLEncoder.encode(String.valueOf(longi))
                            + "&time=" + URLEncoder.encode(time.getText().toString()+ampm.getSelectedItem().toString())
                            + "&cost=" + URLEncoder.encode(cost.getText().toString())
                            + "&owner_id=" + URLEncoder.encode(id)
                            + "&vehicleId=" + URLEncoder.encode(vId)
                            + "&seats=" + URLEncoder.encode(seats.getText().toString());
                    gettrans.execute(url1);
                }
            }
        });

    }
    //code for send data to the server
    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    private class AddRide extends AsyncTask<String, Integer, String> {
        private ProgressDialog progress = null;
        String out="";
        int count=0;
        @Override
        protected String doInBackground(String... geturl) {


            try{
                //	String url= ;


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
            progress = ProgressDialog.show(add_ride.this, null,
                    "Add Ride...");

            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            Toast.makeText(add_ride.this, "Ride Added Sucessfully", Toast.LENGTH_LONG).show();
            progress.dismiss(); 

        Intent register_i1 = new Intent(add_ride.this, HomePage.class);
        startActivity(register_i1);
            finish();
        }
    }

    private class GetData extends AsyncTask<String, Integer, String> {
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


            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            //Toast.makeText(MainActivity.this, ""+out, Toast.LENGTH_LONG).show();
            try{

                JSONObject jsonResponse = new JSONObject(out);
                JSONArray jsonMainNode = jsonResponse.optJSONArray("user_info");
                int arraylength=jsonMainNode.length();
                number =new String[arraylength+1];
                color =new String[arraylength];
                type =new String[arraylength];
                vehicleId =new String[arraylength];


                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    number[i+1]=jsonChildNode.optString("Number");
                    color[i]=jsonChildNode.optString("Color");
                    type[i]=jsonChildNode.optString("Type");
                    vehicleId[i]=jsonChildNode.optString("Id");

                }

            }catch(Exception e){
                //Toast.makeText(ViewDataset.this,"Exception : "+e, Toast.LENGTH_LONG).show();
            }
            number[0]="Select Vehicle";
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(add_ride.this, android.R.layout.simple_spinner_item,number);
            //set the view for the Drop down list
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //set the ArrayAdapter to the spinner
            vehicle.setAdapter(dataAdapter);
        }
    }




    //Code for fetch location
    public class FetchCordinates extends AsyncTask<String, Integer, String> {
        ProgressDialog progDailog = null;



        public LocationManager mLocationManager;
        public VeggsterLocationListener mVeggsterLocationListener;

        @Override
        protected void onPreExecute() {
            mVeggsterLocationListener = new VeggsterLocationListener();
            mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            if (ActivityCompat.checkSelfPermission(add_ride.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(add_ride.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mVeggsterLocationListener);
            progDailog = new ProgressDialog(add_ride.this);
            progDailog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    FetchCordinates.this.cancel(true);
                }
            });
            progDailog.setMessage("Getting Your Location...");
            progDailog.setIndeterminate(true);
            progDailog.setCancelable(true);
            progDailog.show();
        }



        @Override
        protected void onPostExecute(String result) {
            progDailog.dismiss();



            Toast.makeText(add_ride.this,"LATITUDE :" + lati + " LONGITUDE :" + longi,Toast.LENGTH_LONG).show();
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            while (lati == 0.0) {

            }
            return null;
        }

        public class VeggsterLocationListener implements LocationListener {

            @Override
            public void onLocationChanged(Location location) {

                int lat = (int) location.getLatitude(); // * 1E6);
                int log = (int) location.getLongitude(); // * 1E6);
                int acc = (int) (location.getAccuracy());

                String info = location.getProvider();
                try {
                    lati = location.getLatitude();
                    longi = location.getLongitude();

                } catch (Exception e) {
                }

            }

            @Override
            public void onProviderDisabled(String provider) {
                Log.i("OnProviderDisabled", "OnProviderDisabled");
            }

            @Override
            public void onProviderEnabled(String provider) {
                Log.i("onProviderEnabled", "onProviderEnabled");
            }

            @Override
            public void onStatusChanged(String provider, int status,
                                        Bundle extras) {
                Log.i("onStatusChanged", "onStatusChanged");

            }

        }

    }


}
