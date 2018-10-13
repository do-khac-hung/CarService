package com.khachungbg97gmail.carservice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.khachungbg97gmail.carservice.Model.ChatUser;

import java.util.HashMap;
import java.util.Map;

public class Chat extends AppCompatActivity {
    ImageView btnSend;
    EditText edtMessage;
    ScrollView scrollView;
    LinearLayout layout;
    Firebase reference1, reference2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        layout = (LinearLayout)findViewById(R.id.layout1);
        btnSend = (ImageView)findViewById(R.id.sendButton);
        edtMessage = (EditText)findViewById(R.id.messageArea);
        scrollView = (ScrollView)findViewById(R.id.scrollView);

        Firebase.setAndroidContext(this);
        reference1 = new Firebase("https://carservice-47a9f.firebaseio.com/Message/" + ChatUser.username + "_" + ChatUser.chatWith);
        reference2 = new Firebase("https://carservice-47a9f.firebaseio.com/Message/" + ChatUser.chatWith + "_" + ChatUser.username);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textMessage=edtMessage.getText().toString();
                if(!textMessage.equals("")){
                    Map<String,String> map=new HashMap<String, String>();
                    map.put("Message",textMessage);
                    map.put("User",ChatUser.username);
                    reference1.push().setValue(map);
                    reference2.push().setValue(map);


                }
            }
        });
        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map=dataSnapshot.getValue(Map.class);
                String message=map.get("Message").toString();
                String user=map.get("User").toString();
                if(user.equals(ChatUser.username)){
                    addMessage("You:-\n" + message, 0);
                }
                else{
                    addMessage(ChatUser.chatWith + ":-\n" + message, 1);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
    public void addMessage(String message,int type){
        TextView textView = new TextView(Chat.this);
        textView.setText(message);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, 10);
        textView.setLayoutParams(lp);
        if(type==0){
            textView.setBackgroundResource(R.drawable.corner);
        }
        else{
            textView.setBackgroundResource(R.drawable.corner2);
        }
        layout.addView(textView);
        scrollView.fullScroll(View.FOCUS_DOWN);

    }
}
