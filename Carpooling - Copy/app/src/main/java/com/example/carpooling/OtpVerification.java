package com.example.carpooling;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class OtpVerification extends AppCompatActivity {
    Button sign_in, register;
    EditText et1;
    String otp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otpverification);
        sign_in=(Button)findViewById(R.id.sign_in);
        et1=(EditText) findViewById(R.id.otp);
        Intent i1=getIntent();

        otp=i1.getStringExtra("otp").toString();
        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uotp=et1.getText().toString();
                if(uotp.contentEquals(otp)){

                    Intent i1=new Intent(OtpVerification.this,home.class);
                    startActivity(i1);
                }else{
                    Toast.makeText(OtpVerification.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
