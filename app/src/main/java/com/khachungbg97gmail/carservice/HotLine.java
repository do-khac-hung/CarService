package com.khachungbg97gmail.carservice;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.khachungbg97gmail.carservice.Common.Common;
public class HotLine extends AppCompatActivity {
   TextView txtCall,txtChat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hot_line);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Hot Line");
        txtCall=(TextView)findViewById(R.id.txtCall);
        txtChat=(TextView)findViewById(R.id.txtSend);
        if(Common.currentUser==null){
            txtChat.setEnabled(false);
        }
        txtCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try
                {
                    if(Build.VERSION.SDK_INT > 22)
                    {
                        if (ActivityCompat.checkSelfPermission(HotLine.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling

                            ActivityCompat.requestPermissions(HotLine.this, new String[]{Manifest.permission.CALL_PHONE}, 101);

                            return;
                        }

                        Intent callUs= new Intent();
                        callUs.setAction(Intent.ACTION_CALL);
                        callUs.setData(Uri.parse("tel:1800-588888"));
                        startActivity(callUs);

                    }
                    else {
                        Intent callUs= new Intent();
                        callUs.setAction(Intent.ACTION_CALL);
                        callUs.setData(Uri.parse("tel:1800-588888"));
                        startActivity(callUs);
                    }
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        });
        txtChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Common.currentUser.getLastName().equals("Hoang")){
                    Intent listUser=new Intent(HotLine.this,ListUser.class);
                    startActivity(listUser);

                }else {
                    Intent sentUs = new Intent(HotLine.this, Chat.class);
                    startActivity(sentUs);
                }
              //  Toast.makeText(HotLine.this, ChatUser.username, Toast.LENGTH_SHORT).show();
            }
        });

    }
}
