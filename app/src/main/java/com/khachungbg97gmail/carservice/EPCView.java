package com.khachungbg97gmail.carservice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class EPCView extends AppCompatActivity {
    WebView epcView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_epcview);
        epcView =(WebView)findViewById(R.id.epcView);
        epcView.loadUrl("http://carexp.net/Account/Login?ReturnUrl=%2f");
        WebSettings webSettings=epcView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        epcView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView web, String url) {
                // TODO Auto-generated method stub
                String uname = "admin";
                String pass = "1234567";
//                web.loadUrl("javascript:(function(){document.getElementsByName('Username')[0].value='"
//                        + uname
//                        + "';document.getElementsByName('Password')[0].value='"
//                        + pass + "';document.getElementsByTagName('form')[0].submit();})()");
                web.loadUrl("javascript:(function(){document.getElementsByName('Username')[0].value='"
                        + uname
                        + "';document.getElementsByName('Password')[0].value='"
                        + pass + "';})()");
            }
        });
    }
}
