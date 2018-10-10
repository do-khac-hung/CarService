package com.khachungbg97gmail.carservice;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class HotLine extends AppCompatActivity {
   TextView txtCall;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hot_line);
        txtCall=(TextView)findViewById(R.id.txtCall);
        txtCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callUs= new Intent();
                callUs.setAction(Intent.ACTION_CALL);
                callUs.setData(Uri.parse("tel:1800-588888"));
                startActivity(callUs);
            }
        });

    }
}
