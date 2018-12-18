package com.khachungbg97gmail.carservice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class EPCView extends AppCompatActivity {
    WebView epcView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_epcview);
        epcView =(WebView)findViewById(R.id.epcView);
        epcView.loadUrl("http://carexp.net/EPC/VehicleType");
        WebSettings webSettings=epcView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);

    }
}
