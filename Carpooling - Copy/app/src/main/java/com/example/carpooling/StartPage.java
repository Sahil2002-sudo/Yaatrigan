package com.example.carpooling;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

public class StartPage extends Activity {
Button search,login,reg;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dummy);
        search=(Button) findViewById(R.id.button2);
        login=(Button) findViewById(R.id.button3);
        reg=(Button) findViewById(R.id.button4);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
                
                
                
                
            }
        });


    }
}
