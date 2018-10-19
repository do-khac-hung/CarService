package com.khachungbg97gmail.carservice;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.khachungbg97gmail.carservice.Common.Common;

public class HotLine extends AppCompatActivity {
   TextView txtCall,txtChat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hot_line);
        txtCall=(TextView)findViewById(R.id.txtCall);
        txtChat=(TextView)findViewById(R.id.txtSend);
        if(Common.currentUser==null){
            txtChat.setEnabled(false);
        }
        txtCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callUs= new Intent();
                callUs.setAction(Intent.ACTION_CALL);
                callUs.setData(Uri.parse("tel:1800-588888"));
                startActivity(callUs);
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
