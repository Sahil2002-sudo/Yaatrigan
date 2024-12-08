package com.example.carpooling;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

public class home extends AppCompatActivity {


    Button main_login;
    EditText main_user_name, main_passward;
    TextView user_register;
    public SharedPreferences sharedpreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        main_user_name=(EditText)findViewById(R.id.main_user_name);
        main_passward=(EditText)findViewById(R.id.main_password);
        main_login=(Button)findViewById(R.id.main_login);
        user_register=(TextView)findViewById(R.id.register);
        user_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1 =new Intent(home.this,register_user.class);
                startActivity(i1);
            }
        });

        main_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(main_user_name.length()>0) {
                    if (main_passward.length()>0) {
                        String Username = main_user_name.getText().toString();
                        String Password = main_passward.getText().toString();
                        LedOnOff gettrans = new LedOnOff();
                        String url1 = "http://mahavidyalay.in/AcademicDevelopment/carpooling/login123.php?username=" + Username + "&pass=" + Password;
                        gettrans.execute(url1);
                    }else {
                        main_passward.setError("Enter valid password");
                        main_passward.requestFocus(20);
                    }
                }else{
                    main_user_name.setError("Enter valid username");
                    main_user_name.requestFocus(20);

                }
            }
        });
        sharedpreferences = getSharedPreferences("MYPREF", Context.MODE_PRIVATE);

    }
    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    private class LedOnOff extends AsyncTask<String, Integer, String> {
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
            progress = ProgressDialog.show(home.this, null, "Login...");

            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            //Toast.makeText(home.this, ""+result, Toast.LENGTH_LONG).show();
            if(result.contentEquals("fail")){
                Toast.makeText(home.this,"please enter valid username and password",Toast.LENGTH_LONG).show();
                progress.dismiss();
            }else{
                String result1=result.substring(0,8);
                //Toast.makeText(home.this,""+result1,Toast.LENGTH_LONG).show();
                progress.dismiss();
                if(result1.contentEquals("success1")){

                    SharedPreferences shr= PreferenceManager.getDefaultSharedPreferences(home.this);
                   // String islog=shr.getString("islogin","na");
                   // if(islog.contentEquals("1")){
                    //    Intent i1 = new Intent(home.this, book_ride.class);
                     ///   startActivity(i1);
                //    }else {
                     //   Toast.makeText(home.this,"UID"+ result.substring(8),Toast.LENGTH_LONG).show();

                        Intent verify_i1 = new Intent(home.this, HomePage.class);
                        startActivity(verify_i1);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("ID", result.substring(8));
                        editor.commit();
                        SharedPreferences shr11=PreferenceManager.getDefaultSharedPreferences(home.this);
                        SharedPreferences.Editor ed1=shr11.edit();
                        ed1.putString("uid",result.substring(8));
                        ed1.commit();


                    //    Toast.makeText(home.this, result.substring(8), Toast.LENGTH_LONG).show();
                        finish();
                   // }
                }
                else{
                    Toast.makeText(home.this,"please enter valid username and password",Toast.LENGTH_LONG).show();
                }
            }


        }



    }
}
