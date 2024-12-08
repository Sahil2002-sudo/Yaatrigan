package com.example.carpooling;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

public class search_ride_result extends AppCompatActivity {
    ListView listView;
    public static String[] ride_id,time,seats,car_type,start,destination,cost,owner_id,VehicleId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_ride_result);
        listView=(ListView)findViewById(R.id.lst11);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        SharedPreferences shr= PreferenceManager.getDefaultSharedPreferences(search_ride_result.this);
        String islogin=shr.getString("islogin","na");
      /*  if(islogin.contentEquals("0")){

            Intent i1= new Intent(search_ride_result.this,home.class);
            startActivity(i1);
            SharedPreferences.Editor edit=shr.edit();
            edit.putString("ride_id",ride_id[position]);
            edit.putString("time",time[position]);
            edit.putString("seats",seats[position]);
            edit.putString("car_type",car_type[position]);
            edit.putString("start",start[position]);
            edit.putString("destination",destination[position]);
            edit.putString("cost",cost[position]);
            edit.putString("owner_id",owner_id[position]);
            edit.putString("VehicleId",VehicleId[position]);
            edit.putString("islogin","1");
            edit.commit();



        }else{*/
            Bundle b1 = new Bundle();
            b1.putString("ride_id", ride_id[position]);
            b1.putString("time", time[position]);
            b1.putString("seats", seats[position]);
            b1.putString("car_type", car_type[position]);
            b1.putString("start", start[position]);
            b1.putString("destination", destination[position]);
            b1.putString("cost", cost[position]);
            b1.putString("owner_id", owner_id[position]);
            b1.putString("VehicleId", VehicleId[position]);
            Intent i1 = new Intent(search_ride_result.this, book_ride.class);
            i1.putExtras(b1);
            startActivity(i1);

     //   }
            }
        });

        Bundle b1=getIntent().getExtras();
       // Toast.makeText(search_ride_result.this, "source "+b1.getString("start"), Toast.LENGTH_SHORT).show();

        try {
            GetData get = new GetData();
            get.execute("http://mahavidyalay.in/AcademicDevelopment/carpooling/SearchRide.php?start="
                    + URLEncoder.encode(b1.getString("start"))
                    + "&destination=" + URLEncoder.encode(b1.getString("destination")));
        }catch(Exception e){
            Toast.makeText(search_ride_result.this, "Ride Not Found", Toast.LENGTH_SHORT).show();
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
            progress = ProgressDialog.show(search_ride_result.this, null,
                    "Loading...");

            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
          //  Toast.makeText(search_ride_result.this, ""+out, Toast.LENGTH_LONG).show();


            try{

                JSONObject jsonResponse = new JSONObject(out);
                JSONArray jsonMainNode = jsonResponse.optJSONArray("user_info");
                int arraylength=jsonMainNode.length();
        //        Toast.makeText(search_ride_result.this, "Array Lenght"+arraylength, Toast.LENGTH_SHORT).show();
                ride_id =new String[arraylength];
                start =new String[arraylength];
                destination =new String[arraylength];
                time =new String[arraylength];
                seats =new String[arraylength];
                cost =new String[arraylength];
                owner_id =new String[arraylength];
                VehicleId =new String[arraylength];
                car_type =new String[arraylength];



                SharedPreferences shr=PreferenceManager.getDefaultSharedPreferences(search_ride_result.this);
                String uid=shr.getString("ID","na");
            //    Toast.makeText(search_ride_result.this, "uid"+uid, Toast.LENGTH_SHORT).show();
                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                    if(uid.contentEquals(jsonChildNode.optString("owner_id"))){

                    }else {


                        ride_id[i] = jsonChildNode.optString("ride_id");
                        start[i] = jsonChildNode.optString("start");
                        destination[i] = jsonChildNode.optString("destination");
                        time[i] = jsonChildNode.optString("time");
                        seats[i] = jsonChildNode.optString("seats");
                        cost[i] = jsonChildNode.optString("cost");
                        owner_id[i] = jsonChildNode.optString("owner_id");
                        VehicleId[i] = jsonChildNode.optString("VehicleId");
                        car_type[i] = jsonChildNode.optString("car_type");
                    }
                }

               LevelAdapter3 leveladapter=new LevelAdapter3(search_ride_result.this,start,destination,cost);
                listView.setAdapter(leveladapter);

            }catch(Exception e){
                Toast.makeText(search_ride_result.this,"Ride Not Found", Toast.LENGTH_LONG).show();
            }
            progress.dismiss();
        }
    }
}
