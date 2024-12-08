package com.example.carpooling;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

public class ShowUserInfo extends AppCompatActivity {
    String Name[],id1[],Contact[],Email[],owner_id;
    Button view_location;

    TextView name,contact,address,color,time,seats,car_typ,start,destination,cost;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_user_info);
        name=(TextView)findViewById(R.id.name);
        contact=(TextView)findViewById(R.id.contact);
        address=(TextView)findViewById(R.id.address);
        color=(TextView)findViewById(R.id.color);
        time=(TextView)findViewById(R.id.time);
        seats=(TextView)findViewById(R.id.seats);
        car_typ=(TextView)findViewById(R.id.car_type);
        start=(TextView)findViewById(R.id.start);
        destination=(TextView)findViewById(R.id.destination);
        cost=(TextView)findViewById(R.id.cost);
        view_location=(Button)findViewById(R.id.book_ride_button2);
        view_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Bundle b1=getIntent().getExtras();
                Bundle b=new Bundle();
                b.putString("lati",b1.getString("lati"));
                b.putString("longi",b1.getString("longi"));

                Intent i1=new Intent(ShowUserInfo.this,ViewLocationWeb.class);
                i1.putExtras(b);
                startActivity(i1);


            }
        });
        final Bundle b1=getIntent().getExtras();
        name.setText(b1.getString("name"));
        contact.setText(b1.getString("contact"));
        address.setText(b1.getString("address"));
        //color.setText(b1.getString("color"));
        time.setText(b1.getString("time"));
        seats.setText(b1.getString("seats"));
        car_typ.setText(b1.getString("car_type"));
        start.setText(b1.getString("start"));
        destination.setText(b1.getString("destination"));
        cost.setText(b1.getString("cost"));
        owner_id=b1.getString("book_by");
        Profile pro=new Profile();
        pro.execute("http://mahavidyalay.in/AcademicDevelopment/carpooling/ViewUser.php?id="+owner_id);

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
            progress = ProgressDialog.show(ShowUserInfo.this, null,
                    "Loading...");

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
