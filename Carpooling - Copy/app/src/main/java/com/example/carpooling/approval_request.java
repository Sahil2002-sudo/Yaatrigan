package com.example.carpooling;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class approval_request extends AppCompatActivity {
    TextView name,contact,email;
    EditText msg;
    Button accept;
    public static String[] Name,Contact,Email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approval_request);
        name=(TextView)findViewById(R.id.name);
        contact=(TextView)findViewById(R.id.contact);
        email=(TextView)findViewById(R.id.email);
        msg=(EditText) findViewById(R.id.massage);
        accept=(Button)findViewById(R.id.accept_button);
        final Bundle b1=getIntent().getExtras();
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Accept gettrans = new Accept();

                String url1 = "http://mahavidyalay.in/AcademicDevelopment/carpooling/accept_request.php?ride_id=" + URLEncoder.encode(b1.getString("ride_id"));
                gettrans.execute(url1);
            }
        });
        GetData get=new GetData();
        String url1 = "http://mahavidyalay.in/AcademicDevelopment/carpooling/ViewUser.php?id=" + URLEncoder.encode(b1.getString("book_by"));
        get.execute(url1);
    }
    //ADD FOLLOWING line in manifest android:usesCleartextTraffic="true"
    private void getJSON(final String urlWebService) {

        class GetJSON extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }


            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                try {
                    loadIntoListView(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                try {
                    URL url = new URL(urlWebService);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }
                    return sb.toString().trim();
                } catch (Exception e) {
                    return null;
                }
            }
        }
        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }
    private void loadIntoListView(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        Name = new String[jsonArray.length()];
        Contact = new String[jsonArray.length()];
        Email = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            name.setText(Name[i] = obj.getString("name"));
            contact.setText(Contact[i] = obj.getString("contact"));
            email.setText(Email[i] = obj.getString("email"));
        }
    }

    //code for send data to the server
    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    private class Accept extends AsyncTask<String, Integer, String> {
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
            progress = ProgressDialog.show(approval_request.this, null,
                    "Add Ride...");

            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
         //
            //   Toast.makeText(approval_request.this, ""+result, Toast.LENGTH_LONG).show();
            progress.dismiss();
            SmsManager smgr = SmsManager.getDefault();
            smgr.sendTextMessage(contact.getText().toString(),null,"Your booking request accepted, Msg From Owner: "+msg.getText().toString(),null,null);
            //transactions.setText("Transactions :"+count);


            Intent register_i1 = new Intent(approval_request.this, HomePage.class);
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

            progress = ProgressDialog.show(approval_request.this, null,
                    "Add Vehicle...");
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
          //  Toast.makeText(approval_request.this, ""+out, Toast.LENGTH_LONG).show();
            try{

                JSONObject jsonResponse = new JSONObject(out);
                JSONArray jsonArray = jsonResponse.optJSONArray("user_info");


                Name = new String[jsonArray.length()];
                Contact = new String[jsonArray.length()];
                Email = new String[jsonArray.length()];



                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonChildNode = jsonArray.getJSONObject(i);
                    name.setText(Name[i] =jsonChildNode.optString("name"));
                    contact.setText(Contact[i] = jsonChildNode.optString("contact"));
                    email.setText(Email[i] = jsonChildNode.optString("email"));
                }

            }catch(Exception e){
                //Toast.makeText(search_ride_result.this,"Exception : "+e, Toast.LENGTH_LONG).show();
            }
            progress.dismiss();
        }
    }

}
