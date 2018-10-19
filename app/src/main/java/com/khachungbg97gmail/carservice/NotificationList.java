package com.khachungbg97gmail.carservice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.khachungbg97gmail.carservice.Adapter.NotificationAdapter;
import com.khachungbg97gmail.carservice.Common.Common;

public class NotificationList extends AppCompatActivity {
    ListView listView;
    NotificationAdapter notificationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_list);
        listView=(ListView)findViewById(R.id.listMessage);
        notificationAdapter=new NotificationAdapter(this,R.layout.row_notification, Common.notificationList);
        notificationAdapter.notifyDataSetChanged();
        listView.setAdapter(notificationAdapter);
    }

}
