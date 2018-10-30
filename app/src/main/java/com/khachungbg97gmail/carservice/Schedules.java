package com.khachungbg97gmail.carservice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.khachungbg97gmail.carservice.Model.ChatUser;
import com.khachungbg97gmail.carservice.Model.Schedule;

public class Schedules extends AppCompatActivity {
    ImageView ImCreate;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Schedule,ScheduleViewHolder>adapter;
    FirebaseDatabase database;
    Query mReference;
    String Vin;
    int progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedules);
        ImCreate=(ImageView) findViewById(R.id.imCalendar);
        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        database=FirebaseDatabase.getInstance();
        mReference=database.getReference().child("Schedules").orderByChild("idUser").equalTo(ChatUser.id);
        loadListVin(ChatUser.id);
        recyclerView.setAdapter(adapter);
        Intent intent=getIntent();
        Vin=intent.getStringExtra("Vin");
        progress=intent.getIntExtra("Level",0);
        ImCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent maintenance=new Intent(Schedules.this,Maintenance.class);
                maintenance.putExtra("Vin",Vin);
                maintenance.putExtra("Level",progress);
                startActivity(maintenance);


            }
        });
    }
    private void loadListVin(String id) {
        adapter=new FirebaseRecyclerAdapter<Schedule, ScheduleViewHolder>(
                Schedule.class,
                R.layout.row_schedule,
                ScheduleViewHolder.class,
                mReference
        ) {
            @Override
            protected void populateViewHolder(ScheduleViewHolder viewHolder, Schedule model, int position) {
                viewHolder.txtTitle.setText("Cấp bảo dưỡng:"+model.getAccessory());
                viewHolder.txtVin.setText("Xe:"+model.getIdVin());
                viewHolder.txtNote.setText("Note:"+model.getNote());
                viewHolder.txtTime.setText(model.getTime());
                viewHolder.txtDate.setText(model.getDate());
            }
        };

    }
    public static class ScheduleViewHolder extends RecyclerView.ViewHolder {
        public TextView txtTitle,txtVin,txtNote,txtTime,txtDate;
        View view;
        public ScheduleViewHolder(View itemView) {
            super(itemView);
            txtTitle=(TextView)itemView.findViewById(R.id.title);
            txtVin=(TextView)itemView.findViewById(R.id.vin);
            txtNote=(TextView)itemView.findViewById(R.id.Note);
            txtDate=(TextView)itemView.findViewById(R.id.date);
            txtTime=(TextView)itemView.findViewById(R.id.time);
            view=itemView;
        }


    }
}
