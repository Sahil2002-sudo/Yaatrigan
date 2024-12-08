package com.example.carpooling;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

public class book_ride extends AppCompatActivity {
    TextView name,contact,address,color,time,seats,car_typ,start,destination,cost;
    Button book_ride;
    String Name[],id1[],Contact[],Email[],owner_id;
    Bundle b1 ;
    String ride_id = "";

    SharedPreferences sharedPreferences;
    Spinner sp1;
    String avlseats="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_ride);

        name=(TextView)findViewById(R.id.name);
        contact=(TextView)findViewById(R.id.contact);
        address=(TextView)findViewById(R.id.address);
        //color=(TextView)findViewById(R.id.color);
        time=(TextView)findViewById(R.id.time);
        seats=(TextView)findViewById(R.id.seats);
        car_typ=(TextView)findViewById(R.id.car_type);
        start=(TextView)findViewById(R.id.start);
        destination=(TextView)findViewById(R.id.destination);
        cost=(TextView)findViewById(R.id.cost);
        book_ride=(Button)findViewById(R.id.book_ride_button2);
        sp1=(Spinner)findViewById(R.id.sp1);


        SharedPreferences shr= PreferenceManager.getDefaultSharedPreferences(book_ride.this);
        String islog=shr.getString("islogin","na");
      /* if(islog.contentEquals("1")){

            avlseats=shr.getString("seats","na");
            time.setText(shr.getString("time","na"));
            seats.setText(shr.getString("seats","na"));
            car_typ.setText(shr.getString("car_type","na"));
            start.setText(shr.getString("start","na"));
            destination.setText(shr.getString("destination","na"));
            cost.setText(shr.getString("cost","na"));
            owner_id = shr.getString("owner_id","na");
            ride_id =shr.getString("ride_id","na");
            Profile pro = new Profile();
            pro.execute("http://mahavidyalay.in/AcademicDevelopment/carpooling/ViewUser.php?id=" + owner_id);


        }else {*/


             b1 = getIntent().getExtras();
           // name.setText(b1.getString("name"));
           // contact.setText(b1.getString("contact"));
          //  address.setText(b1.getString("address"));
            //color.setText(b1.getString("color"));
            avlseats=b1.getString("seats");

        int avlseat2=Integer.parseInt(avlseats);

        ArrayList<String> arrayList = new ArrayList<>();
        for(int i=1;i<=avlseat2;i++){
            arrayList.add(""+i);
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,                         android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp1.setAdapter(arrayAdapter);



            ride_id =b1.getString("ride_id").toString();
            time.setText(b1.getString("time"));
            seats.setText(b1.getString("seats"));
            car_typ.setText(b1.getString("car_type"));
            start.setText(b1.getString("start"));
            destination.setText(b1.getString("destination"));
            cost.setText(b1.getString("cost"));
            owner_id = b1.getString("owner_id");
            Profile pro = new Profile();
            pro.execute("http://mahavidyalay.in/AcademicDevelopment/carpooling/ViewUser.php?id=" + owner_id);
       // }

        book_ride.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               try {


                   String seats=sp1.getSelectedItem().toString();
                   int seatwant=Integer.parseInt(seats);




                    //   sharedPreferences = getSharedPreferences("MYPREF", MODE_PRIVATE);

                       SharedPreferences shr = PreferenceManager.getDefaultSharedPreferences(book_ride.this);
                       String id = shr.getString("uid","na");
                   //    Toast.makeText(book_ride.this, "ride id"+ride_id+"uid "+id, Toast.LENGTH_SHORT).show();
//

                       String islog = shr.getString("islogin", "na");
                   //    Toast.makeText(book_ride.this, "is log"+islog+"uid "+id, Toast.LENGTH_SHORT).show();

                      // if (islog.contentEquals("1")) {
                        //   ride_id = sharedPreferences.getString("ride_id", "na");
                     //  } else {
                           //ride_id = "84";//b1.getString("ride_id");
                      // }
                      //Toast.makeText(book_ride.this, "ride id"+ride_id+"uid "+id, Toast.LENGTH_SHORT).show();
                       BookRide gettrans = new BookRide();
                       String url1 = "http://mahavidyalay.in/AcademicDevelopment/carpooling/book_ride.php?ride_id="
                               + URLEncoder.encode(ride_id)
                               + "&id=" + URLEncoder.encode(id)
                               + "&seats=" + URLEncoder.encode(seats);
                       gettrans.execute(url1);



                //   }

               }catch (Exception e){
                  // Toast.makeText(book_ride.this,""+e ,Toast.LENGTH_LONG).show();
               }



            }
        });

    }
    //code for send data to the server
    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    private class BookRide extends AsyncTask<String, Integer, String> {
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
            progress = ProgressDialog.show(book_ride.this, null,
                    "Sending Request...");
            super.onPreExecute();
        }
        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            //.makeText(book_ride.this, ""+result, Toast.LENGTH_LONG).show();
            progress.dismiss();
            SmsManager smgr = SmsManager.getDefault();
            smgr.sendTextMessage(contact.getText().toString(),null,"Dear "+name.getText().toString()+" you have request for ride please check in your app.",null,null);
            Intent register_i1 = new Intent(book_ride.this, HomePage.class);
            startActivity(register_i1);
            finish();
        }
    }


    private class Profile extends AsyncTask<String, Integer, String> {
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
            progress = ProgressDialog.show(book_ride.this, null,
                    "Loading...");

            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
           // Toast.makeText(book_ride.this, ""+out, Toast.LENGTH_LONG).show();
            try{

                JSONObject jsonResponse = new JSONObject(out);
                JSONArray jsonMainNode = jsonResponse.optJSONArray("user_info");
                int arraylength=jsonMainNode.length();
                Name =new String[arraylength];
                id1 =new String[arraylength];
                Contact =new String[arraylength];
                Email =new String[arraylength];


                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    Name[i]=jsonChildNode.optString("name");
                    Contact[i]=jsonChildNode.optString("contact");
                    Email[i]=jsonChildNode.optString("email");
                    id1[i]=jsonChildNode.optString("user_id");
                    name.setText(jsonChildNode.optString("name"));
                    contact.setText(jsonChildNode.optString("contact"));
                    address.setText(jsonChildNode.optString("email"));

                }
                //LevelAdapter levelAdapter=new LevelAdapter(view_user.this,name,contact,name);
                //listView.setAdapter(levelAdapter);

            }catch(Exception e){
                //Toast.makeText(ViewDataset.this,"Exception : "+e, Toast.LENGTH_LONG).show();
            }
            progress.dismiss();
        }
    }
}
