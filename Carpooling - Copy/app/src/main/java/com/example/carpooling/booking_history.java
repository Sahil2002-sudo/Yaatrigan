package com.example.carpooling;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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

public class  booking_history extends AppCompatActivity {
    ListView listView;
    public static String[] ride_id,name,contact,address,color,time,seats,car_type,start,destination,cost,owner_id,request,book_by,status,status_show,lati,longi;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_history);
        listView=(ListView)findViewById(R.id.listview1);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle b1=new Bundle();
                b1.putString("ride_id",ride_id[position]);

                b1.putString("time",time[position]);
                b1.putString("seats",seats[position]);
                b1.putString("car_type",car_type[position]);
                b1.putString("start",start[position]);
                b1.putString("destination",destination[position]);
                b1.putString("cost",cost[position]);
                b1.putString("owner_id",owner_id[position]);
                b1.putString("request",request[position]);
                b1.putString("book_by",book_by[position]);
                b1.putString("status",status[position]);
                b1.putString("lati",lati[position]);
                b1.putString("longi",longi[position]);

                if(request[position].contentEquals("1")){
                    if(status[position].contentEquals("1")){
                        Toast.makeText(booking_history.this,"You are accepted this request",Toast.LENGTH_LONG).show();
                        Intent i =new Intent(booking_history.this,ShowUserInfo.class);
                        i.putExtras(b1);
                        startActivity(i);
                    }else{
                        Intent i1=new Intent(booking_history.this,approval_request.class);
                        i1.putExtras(b1);
                        startActivity(i1);
                    }

                }else{
                    if(status[position].contentEquals("1")){
                        Toast.makeText(booking_history.this,"You are accepted this request",Toast.LENGTH_LONG).show();
                        Intent i =new Intent(booking_history.this,ShowUserInfo.class);
                        i.putExtras(b1);
                        startActivity(i);
                    }else{
                        Toast.makeText(booking_history.this,"Sorry NO Request For This Ride",Toast.LENGTH_LONG).show();

                    }
                }
            }
        });
        sharedPreferences=getSharedPreferences("MYPREF",MODE_PRIVATE);
        String id=sharedPreferences.getString("ID","");
    //    Toast.makeText(booking_history.this,id,Toast.LENGTH_LONG).show();

        GetData get=new GetData();
        get.execute("http://mahavidyalay.in/AcademicDevelopment/carpooling/ShowRide.php?id="+ URLEncoder.encode(id));
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

            progress = ProgressDialog.show(booking_history.this, null,
                    "Loading...");
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
        //    Toast.makeText(booking_history.this, ""+out, Toast.LENGTH_LONG).show();
            try{

                JSONObject jsonResponse = new JSONObject(out);
                JSONArray jsonArray = jsonResponse.optJSONArray("user_info");
                ride_id = new String[jsonArray.length()];
                name = new String[jsonArray.length()];
                contact = new String[jsonArray.length()];
                address = new String[jsonArray.length()];
                color = new String[jsonArray.length()];
                time = new String[jsonArray.length()];
                seats = new String[jsonArray.length()];
                car_type = new String[jsonArray.length()];
                start = new String[jsonArray.length()];
                destination = new String[jsonArray.length()];
                cost = new String[jsonArray.length()];
                owner_id = new String[jsonArray.length()];
                request = new String[jsonArray.length()];
                book_by = new String[jsonArray.length()];
                status = new String[jsonArray.length()];
                lati = new String[jsonArray.length()];
                longi = new String[jsonArray.length()];
                status_show = new String[jsonArray.length()];



                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonChildNode = jsonArray.getJSONObject(i);
                    ride_id[i] = jsonChildNode.optString("ride_id");
                    time[i] = jsonChildNode.optString("time");
                    seats[i] = jsonChildNode.optString("seats");
                    car_type[i] = jsonChildNode.optString("car_type");
                    start[i] = jsonChildNode.optString("start");
                    destination[i] = jsonChildNode.optString("destination");
                    cost[i] = jsonChildNode.optString("cost");
                    owner_id[i] = jsonChildNode.optString("owner_id");
                    request[i] = jsonChildNode.optString("request");
                    book_by[i] = jsonChildNode.optString("book_by");
                    status[i] = jsonChildNode.optString("status");
                    lati[i] = jsonChildNode.optString("lati");
                    longi[i] = jsonChildNode.optString("longi");
                    if(status[i].contentEquals("1")){
                        status_show[i]="BOOKED";
                    }else{
                        status_show[i]="NOT BOOK";
                    }
                }
                LevelAdapter1 leveladapter=new LevelAdapter1(booking_history.this,start,destination,status_show);
                listView.setAdapter(leveladapter);

            }catch(Exception e){
                //Toast.makeText(search_ride_result.this,"Exception : "+e, Toast.LENGTH_LONG).show();
            }
            progress.dismiss();
        }
    }
}
