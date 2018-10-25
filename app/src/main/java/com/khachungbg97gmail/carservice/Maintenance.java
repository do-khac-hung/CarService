package com.khachungbg97gmail.carservice;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.khachungbg97gmail.carservice.Model.ChatUser;
import com.khachungbg97gmail.carservice.Model.Schedule;
import com.khachungbg97gmail.carservice.Notification.MyReceiver;

import java.util.Calendar;

import static com.khachungbg97gmail.carservice.Common.Common.currentVin;

public class Maintenance extends AppCompatActivity {
    EditText edtNote;
    Button btnSet;
    DatePicker datePicker;
    Calendar now;
    FirebaseDatabase database;
    DatabaseReference table_schedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance);
        datePicker=(DatePicker)findViewById(R.id.datePicker);
        datePicker.setSpinnersShown(false);
        btnSet=(Button)findViewById(R.id.btnSet);
        edtNote=(EditText)findViewById(R.id.note);
        //init Firebase
        database=FirebaseDatabase.getInstance();
        table_schedule=database.getReference("Schedules");
        now=Calendar.getInstance();
        final Calendar current = Calendar.getInstance();
        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour=7;
                int minute=0;
                now.set(datePicker.getYear(),datePicker.getMonth(),datePicker.getDayOfMonth(),hour,minute);
                if(now.compareTo(current)<=0){
                    Toast.makeText(Maintenance.this,"invalid time",Toast.LENGTH_LONG).show();
                }else{
                    String note=edtNote.getText().toString();
                    String date =datePicker.getYear()+"-"+datePicker.getMonth()+"-"+datePicker.getDayOfMonth();
                    String accessories="bugi";
                    String idUser= ChatUser.id;
                    String vinCode=currentVin.getVinCode();
                    Schedule schedule=new Schedule(date,note,idUser,vinCode,accessories);
                    table_schedule.child(date).setValue(schedule);
                    Toast.makeText(Maintenance.this, "Successfully!!", Toast.LENGTH_SHORT).show();
                    Intent notifyIntent = new Intent(Maintenance.this, MyReceiver.class);
                  //  notifyIntent.putExtra("title",item.getText());
                    PendingIntent pendingIntent = PendingIntent.getBroadcast
                            (Maintenance.this,0 , notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                    alarmManager.set(AlarmManager.RTC,now.getTimeInMillis(), pendingIntent);
                }

            }
        });

    }
}
