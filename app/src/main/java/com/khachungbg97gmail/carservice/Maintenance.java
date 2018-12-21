package com.khachungbg97gmail.carservice;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.khachungbg97gmail.carservice.Model.ChatUser;
import com.khachungbg97gmail.carservice.Model.Schedule;
import com.khachungbg97gmail.carservice.Notification.MyReceiver;

import java.util.Calendar;
import java.util.Date;

public class Maintenance extends AppCompatActivity {
    EditText edtNote;
    Button btnSet;
    DatePicker datePicker;
    TimePicker time;
    Calendar now;
    FirebaseDatabase database;
    DatabaseReference table_schedule;
    String Vin;
    int progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance);
        datePicker=(DatePicker)findViewById(R.id.datePicker);
        datePicker.setSpinnersShown(false);
        btnSet=(Button)findViewById(R.id.btnSet);
        edtNote=(EditText)findViewById(R.id.note);
        time=(TimePicker)findViewById(R.id.timePicker);
        //init Firebase
        database=FirebaseDatabase.getInstance();
        table_schedule=database.getReference("Schedules");
        Intent intent=getIntent();
        Vin=intent.getStringExtra("Vin");
        progress=intent.getIntExtra("Level",0);
        now=Calendar.getInstance();
        final Calendar current = Calendar.getInstance();
        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                now.set(Calendar.YEAR, datePicker.getYear());
                now.set(Calendar.MONTH, datePicker.getMonth());
                now.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
                now.set(Calendar.HOUR_OF_DAY, time.getCurrentHour());
                now.set(Calendar.MINUTE, time.getCurrentMinute());
                now.set(Calendar.SECOND, 0);
               // Toast.makeText(Maintenance.this,datePicker.getMonth()+1, Toast.LENGTH_SHORT).show();
                if(now.compareTo(current)<=0){
                    Toast.makeText(Maintenance.this,"invalid time",Toast.LENGTH_LONG).show();
                }else{
                    Date Date=now.getTime();
                    Toast.makeText(Maintenance.this, ""+Date, Toast.LENGTH_LONG).show();
                    String note=edtNote.getText().toString();
                    int month=datePicker.getMonth()+1;
                    String date =datePicker.getYear()+"-"+month+"-"+datePicker.getDayOfMonth();
                    String accessories= String.valueOf(progress);
                    String idUser= ChatUser.id;
                    String timeSchedule=time.getCurrentHour()+":"+time.getCurrentMinute();
                    //String vinCode= currentVin.getVinCode();
                    Schedule schedule=new Schedule(date,note,idUser,Vin,accessories,timeSchedule);
                    table_schedule.child(timeSchedule+"_"+date+"_"+idUser).setValue(schedule);
                    Intent notificationIntent = new Intent(Maintenance.this, MyReceiver.class);
                    notificationIntent.putExtra("Vin",Vin);
                    notificationIntent.putExtra("Level",accessories);

                    PendingIntent pendingIntent = PendingIntent.getBroadcast
                            (Maintenance.this,100 , notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.YEAR,datePicker.getYear()-current.get(Calendar.YEAR));
                    cal.add(Calendar.MONTH,datePicker.getMonth()-current.get(Calendar.MONTH));
                    cal.add(Calendar.DAY_OF_MONTH,datePicker.getDayOfMonth()-current.get(Calendar.DAY_OF_MONTH));
                    cal.add(Calendar.HOUR_OF_DAY,time.getCurrentHour()-current.get(Calendar.HOUR_OF_DAY));
                    cal.add(Calendar.MINUTE,time.getCurrentMinute()-current.get(Calendar.MINUTE));
                    cal.add(Calendar.SECOND,0);
                    Log.d("ACD",""+
                            (time.getCurrentHour())+"_"+
                            (time.getCurrentMinute())+"_"+
                            current.get(Calendar.MINUTE)
                    );
                    AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,cal.getTimeInMillis(), pendingIntent);
                    Intent home=new Intent(Maintenance.this,Home.class);
                    startActivity(home);
                }

            }
        });

    }
}
