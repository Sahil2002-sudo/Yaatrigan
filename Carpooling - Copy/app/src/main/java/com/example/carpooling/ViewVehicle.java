package com.example.carpooling;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

public class ViewVehicle extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    ListView List;
    String[] number,color,type,vehicleId;
    int pos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_vehicle);
        List=(ListView)findViewById(R.id.listview);
        List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                 pos=position;


                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                String veid=vehicleId[pos];
                                DeleteData add=new DeleteData();
                                add.execute("http://mahavidyalay.in/AcademicDevelopment/carpooling/DeleteVehicle.php?vid="
                                        +URLEncoder.encode(veid));
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                Intent i1 = new Intent(ViewVehicle.this,VehicleInfo.class);
                                Bundle b1=new Bundle();
                                b1.putString("number",number[pos]);
                                b1.putString("color",color[pos]);
                                b1.putString("vehicleId",vehicleId[pos]);
                                b1.putString("type",type[pos]);
                                i1.putExtras(b1);
                                startActivity(i1);
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(ViewVehicle.this);
                builder.setMessage("Select Action?").setPositiveButton("Delete", dialogClickListener)
                        .setNegativeButton("View", dialogClickListener).show();






            }
        });

        sharedPreferences=getSharedPreferences("MYPREF",MODE_PRIVATE);
        String id=sharedPreferences.getString("ID","");

        GetData get=new GetData();
        get.execute("http://mahavidyalay.in/AcademicDevelopment/carpooling/ViewVehicle.php?id="+id);
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
            progress = ProgressDialog.show(ViewVehicle.this, null,
                    "Add Vehicle...");

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
                number =new String[arraylength];
                color =new String[arraylength];
                type =new String[arraylength];
                vehicleId =new String[arraylength];


                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    number[i]=jsonChildNode.optString("Number");
                    color[i]=jsonChildNode.optString("Color");
                    type[i]=jsonChildNode.optString("Type");
                    vehicleId[i]=jsonChildNode.optString("Id");

                }
                LevelAdapter2 leveladapter=new LevelAdapter2(ViewVehicle.this,number,color,type);
                List.setAdapter(leveladapter);

            }catch(Exception e){
                //Toast.makeText(ViewDataset.this,"Exception : "+e, Toast.LENGTH_LONG).show();
            }
            progress.dismiss();
        }
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
            progress = ProgressDialog.show(ViewVehicle.this, null,
                    "Add Vehicle...");

            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            //Toast.makeText(MainActivity.this, ""+out, Toast.LENGTH_LONG).show();

            progress.dismiss();
        }
    }
}
