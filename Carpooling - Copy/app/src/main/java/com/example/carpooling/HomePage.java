package com.example.carpooling;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.telephony.SmsManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
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

public class HomePage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    String Name[],Contact[],Email[];
    SharedPreferences sharedPreferences;
    TextView NameText;
    HandlerThread hThread ;
    GPSTracker gps;
    String currentlatitude,currentlongitude;
    double latitude=0.0;
    double longitude=0.0;
    ListView listView;
    int click =0;
    Button SearchCar,SearchPassenger,safety;
    public static String[] ride_id,name,contact,address,color,time,seats,car_type,start,destination,cost,owner_id,request,book_by,status,status_show;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        safety=(Button)findViewById(R.id.applysafety);

        safety.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click =click+1;
                if(click>4){

                    SmsManager sm=SmsManager.getDefault();
                    sm.sendTextMessage("7719807564",null,"I Need Help ",null,null);

                }
            }
        });

        SearchCar=(Button)findViewById(R.id.searchCar);
        SearchPassenger=(Button)findViewById(R.id.searchPassanger) ;
        SearchCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent I=new Intent(HomePage.this,search_ride.class);
                startActivity(I);
            }
        });
        SearchPassenger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent I=new Intent(HomePage.this,add_ride.class);
                startActivity(I);
            }
        });
        listView=(ListView)findViewById(R.id.history);

        try {
            sharedPreferences = getSharedPreferences("MYPREF", MODE_PRIVATE);
            listView = (ListView) findViewById(R.id.history);
            sharedPreferences = getSharedPreferences("MYPREF", MODE_PRIVATE);
            String id = sharedPreferences.getString("ID", "");
           // Toast.makeText(HomePage.this, id, Toast.LENGTH_LONG).show();
            GetData get = new GetData();
            get.execute("http://mahavidyalay.in/AcademicDevelopment/carpooling/ShowRide.php?id=" + URLEncoder.encode(id));
        }catch(Exception e){
            Toast.makeText(HomePage.this,""+e,Toast.LENGTH_LONG).show();
        }


        //location

        gps= new GPSTracker(HomePage.this);
        hThread  = new HandlerThread("HandlerThread");
        hThread.start();
        final Handler handler = new Handler(hThread.getLooper());
        final long oneMinuteMs = 30 * 500;

        Runnable eachMinute = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, oneMinuteMs);
                handler.postDelayed(this, oneMinuteMs);
                try{
                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();

                    Toast.makeText(HomePage.this, "Your Location is - \nLat: "
                            + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();

                  //  tv1.setText("Lat : "+latitude+" Lang : "+longitude);


                    try{

                        SharedPreferences shr= PreferenceManager.getDefaultSharedPreferences(HomePage.this);
                        String uid1=shr.getString("uid","");

                        UpdateLoc gettrans=new UpdateLoc();
                        DbParameter host=new DbParameter();
                        String url=host.getHostpath();
                        url=url+"/UpdateLocation.php?id="+ URLEncoder.encode(uid1)+"&";
                        url=url+"&lat="+URLEncoder.encode(String.valueOf( latitude));
                        url=url+"&lang="+URLEncoder.encode(String.valueOf(longitude));
                        gettrans.execute(url);
                    }catch(Exception e){

                       // Toast.makeText(HomePage.this, ""+e, Toast.LENGTH_LONG).show();
                    }




                }catch(Exception e){
                  //  Toast.makeText(HomePage.this, ""+e,Toast.LENGTH_LONG).show();
                }
            }
        };
        handler.postDelayed(eachMinute, oneMinuteMs);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.addVehicle) {
            Intent i=new Intent(HomePage.this,AddVehicle.class);
            startActivity(i);
            // Handle the camera action
        }
        else if (id == R.id.view) {
            Intent i=new Intent(HomePage.this,ViewProfile.class);
            startActivity(i);

        } else if (id == R.id.viewVehicle) {
            Intent i=new Intent(HomePage.this,ViewVehicle.class);
            startActivity(i);

        } else if (id == R.id.History) {
            Intent i=new Intent(HomePage.this,booking_history.class);
            startActivity(i);

        }  else if (id == R.id.Help) {
            Intent i=new Intent(HomePage.this,Help.class);
            startActivity(i);


        } else if (id == R.id.Logout) {
            Intent i=new Intent(HomePage.this,home.class);
            startActivity(i);
            finish();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    private class UpdateLoc extends AsyncTask<String, Integer, String> {
        private ProgressDialog progress = null;
        String out="";
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
            // progress = ProgressDialog.show(DriverDashboard.this, null,
            //   "Loading Products...");

            super.onPreExecute();
        }





        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
          //  Toast.makeText(HomePage.this, " "+out, Toast.LENGTH_LONG).show();

            try{




            }catch(Exception e){
                //Toast.makeText(SearchByLocation.this,"Results Not Founds"+e, Toast.LENGTH_LONG).show();
            }

            //    progress.dismiss();
            super.onPostExecute(result);
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
            progress = ProgressDialog.show(HomePage.this, null,
                    "Add Vehicle...");

            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            //Toast.makeText(HomePage.this, ""+out, Toast.LENGTH_LONG).show();
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
                    if(status[i].contentEquals("1")){
                        status_show[i]="BOOKED";


                    }else{
                        status_show[i]="NOT BOOK";
                    }
                }
                LevelAdapter1 leveladapter=new LevelAdapter1(HomePage.this,start,destination,status_show);
                listView.setAdapter(leveladapter);

            }catch(Exception e){
            //    Toast.makeText(HomePage.this,"Exception : "+e, Toast.LENGTH_LONG).show();
            }
            progress.dismiss();
        }
    }

}
