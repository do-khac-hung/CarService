package com.khachungbg97gmail.carservice;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.khachungbg97gmail.carservice.Model.ChatUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class ListUser extends AppCompatActivity {
    ListView userList;
    TextView txtNoUser;
    ArrayList<String> arrayList=new ArrayList<>();
    int totalUser=0;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_user);

        userList = (ListView)findViewById(R.id.usersList);
        txtNoUser = (TextView)findViewById(R.id.noUsersText);

        pd = new ProgressDialog(ListUser.this);
        pd.setMessage("Loading...");
        pd.show();

        String url = "https://carservice-47a9f.firebaseio.com/User.json";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                doOnSuccess(s);
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError);
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(ListUser.this);
        rQueue.add(request);

        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ChatUser.chatWith = arrayList.get(position);
                startActivity(new Intent(ListUser.this, Chat.class));
            }
        });
    }

    public void doOnSuccess(String s){
        try {
            JSONObject obj = new JSONObject(s);

            Iterator i = obj.keys();
            String key = "";

            while(i.hasNext()){
                key = i.next().toString();

                if(!key.equals(ChatUser.username)) {
                    arrayList.add(key);
                }

                totalUser++;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(totalUser <=1){
            txtNoUser.setVisibility(View.VISIBLE);
            userList.setVisibility(View.GONE);
        }
        else{
            txtNoUser.setVisibility(View.GONE);
            userList.setVisibility(View.VISIBLE);
            userList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList));
        }

        pd.dismiss();
    }
}
