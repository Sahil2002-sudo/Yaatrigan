package com.example.carpooling;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLEncoder;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

public class DeleteRide extends AppCompatActivity {
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

                String rideid=ride_id[position];

                DeleteData add=new DeleteData();
                add.execute("http://mahavidyalay.in/AcademicDevelopment/carpooling/DeleteRide.php?rid="
                        +URLEncoder.encode(rideid));

            }
        });
        sharedPreferences=getSharedPreferences("MYPREF",MODE_PRIVATE);
        String id=sharedPreferences.getString("ID","");
    //    Toast.makeText(DeleteRide.this,id,Toast.LENGTH_LONG).show();

        GetData get=new GetData();
        get.execute("http://mahavidyalay.in/AcademicDevelopment/carpooling/ShowRide.php?id="+ URLEncoder.encode(id));
    }

    private class DeleteData extends AsyncTask<String, Integer, String> {
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
            progress = ProgressDialog.show(DeleteRide.this, null,
                    "Deleteing Ride...");

            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
           Toast.makeText(DeleteRide.this, "Ride Deleted Sucessfully", Toast.LENGTH_LONG).show();
           finish();

            progress.dismiss();
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

            progress = ProgressDialog.show(DeleteRide.this, null,
                    "Loading...");
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            //Toast.makeText(DeleteRide.this, ""+out, Toast.LENGTH_LONG).show();
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
                LevelAdapter1 leveladapter=new LevelAdapter1(DeleteRide.this,start,destination,status_show);
                listView.setAdapter(leveladapter);


            }catch(Exception e){
                //Toast.makeText(search_ride_result.this,"Exception : "+e, Toast.LENGTH_LONG).show();
            }
            progress.dismiss();
        }
    }
}
