package com.example.carpooling;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class ViewLocationWeb extends Activity {

WebView wv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewlivelocationweb);

        wv=(WebView)findViewById(R.id.webview);


        Intent i1=getIntent();
        String lat=i1.getStringExtra("lati");
        String lang=i1.getStringExtra("longi");
        String url="http://maps.google.com/maps?q=loc:"+lat+","+lang+"";

        Toast.makeText(ViewLocationWeb.this, "lat "+lat+"lang"+lang, Toast.LENGTH_SHORT).show();


        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);

    }

    public class myWebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
        }
    }
}
