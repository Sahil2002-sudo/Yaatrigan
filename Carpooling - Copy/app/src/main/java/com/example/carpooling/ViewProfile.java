package com.example.carpooling;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
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

public class ViewProfile extends AppCompatActivity {
    TextView Name,Contact,Email;
    ImageView im1;
    String name[],id1[],contact[],email[];
    String gender="";
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        Name=(TextView)findViewById(R.id.contact);
        Contact=(TextView)findViewById(R.id.name);
        Email=(TextView)findViewById(R.id.email);
        im1=(ImageView)findViewById(R.id.img);
        sharedPreferences=getSharedPreferences("MYPREF",MODE_PRIVATE);
        String id=sharedPreferences.getString("ID","");

        GetData get=new GetData();
        get.execute("http://mahavidyalay.in/AcademicDevelopment/carpooling/ViewUser.php?id="+id);
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

            progress = ProgressDialog.show(ViewProfile.this, null,
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
                name =new String[arraylength];
                id1 =new String[arraylength];
                contact =new String[arraylength];
                email =new String[arraylength];


                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    name[i]=jsonChildNode.optString("name");
                    contact[i]=jsonChildNode.optString("contact");
                    email[i]=jsonChildNode.optString("email");
                    id1[i]=jsonChildNode.optString("user_id");
                    Name.setText(jsonChildNode.optString("name"));
                    Contact.setText(jsonChildNode.optString("contact"));
                    Email.setText(jsonChildNode.optString("email"));
                    gender=jsonChildNode.optString("gender");
                    if(gender.contentEquals("Male")){
                        im1.setImageResource(R.drawable.male);
                    }else{
                        im1.setImageResource(R.drawable.female);
                    }


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
