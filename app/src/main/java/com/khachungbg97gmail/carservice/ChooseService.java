package com.khachungbg97gmail.carservice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.khachungbg97gmail.carservice.Model.ChatUser;
import com.khachungbg97gmail.carservice.Model.Vin;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.List;

public class ChooseService extends AppCompatActivity {
    List<String> level1,level2,level3;
    List<String>listVin;
    ImageView ImAdd,ImMinus;
    TextView txtLevel;
    EditText edtCar;
    SeekBar seekbar;
    ListView listView;
    Spinner spinnerCar;
    Button btnNext;
    FirebaseDatabase database;
    Query query;
    int progressValue = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_service);
        level1=new ArrayList<>();
        level1.add("Nhớt động cơ");
        level1.add("Lọc nhớt");
        level2=new ArrayList<>();
        level2.add("Nhớt động cơ");
        level3=new ArrayList<>();
        level3.add("Nhớt động cơ");
        level3.add("Lọc nhớt");
        level3.add("Lọc xăng");
        level3.add("Lọc gió");
        level3.add("Hộp số");
        level3.add("Dầu trơ lực lái");
        level3.add("Dầu thắng");
        level3.add("Nước làm mát");
        level3.add("Bugi");
        level3.add("Hệ thống thắng");
        level3.add("Bạc đạn bánh");
        level3.add("Hệ thống lạnh");
        ImAdd=(ImageView) findViewById(R.id.plus);
        ImMinus=(ImageView) findViewById(R.id.minus);
        txtLevel=(TextView)findViewById(R.id.txtlevel);
        seekbar=(SeekBar)findViewById(R.id.sb);
        edtCar=(MaterialEditText)findViewById(R.id.edtVinCar1);
        listView=(ListView)findViewById(R.id.listbd);
        listVin=new ArrayList<>();
        database= FirebaseDatabase.getInstance();
        query=database.getReference().child("Vins").orderByChild("idUser").equalTo(ChatUser.id);
       // initItem(query);
        //listVin.add(" ");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<String> listE = new ArrayList<String>();
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    //Toast.makeText(ChooseService.this, ""+childSnapshot.getKey(), Toast.LENGTH_SHORT).show();
                    Vin vin=childSnapshot.getValue(Vin.class);
                    if(vin!=null){
                        String vinCode=vin.getVinCode();
                        listE.add(vinCode);
                    }

                }
                spinnerCar=(Spinner)findViewById(R.id.spinnerCar);
                ArrayAdapter<String> adapter = new ArrayAdapter(ChooseService.this, android.R.layout.simple_spinner_item,listE);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerCar.setAdapter(adapter);
                spinnerCar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                edtCar.setText(listE.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                edtCar.setText("");
            }
        });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });

       // spinnerCar=(Spinner)findViewById(R.id.spinnerCar);
        btnNext=(Button)findViewById(R.id.btnDatLich);
        updateListView(progressValue);

//        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,listVin);
//        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
//        spinnerCar.setAdapter(adapter);
//        spinnerCar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                edtCar.setText(listVin.get(position));
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                edtCar.setText("");
//            }
//        });

        ImAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(seekbar.getProgress()<8){
                    seekbar.setProgress(seekbar.getProgress()+1);
                    progressValue=seekbar.getProgress();
                    txtLevel.setText(""+progressValue*5000+"KM");
                    updateListView(progressValue);
                }
            }
        });
        ImMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(seekbar.getProgress()>0){
                    seekbar.setProgress(seekbar.getProgress()-1);
                    progressValue=seekbar.getProgress();
                    txtLevel.setText(""+progressValue*5000+"KM");
                    updateListView(progressValue);
                }
            }
        });
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                progressValue = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(ChooseService.this, "Seek bar progress is :" + progressValue,
                        Toast.LENGTH_SHORT).show();
                txtLevel.setText(""+progressValue*5000+"KM");
                updateListView(progressValue);

            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent schedule=new Intent(ChooseService.this, Schedules.class);
                schedule.putExtra("Vin",edtCar.getText().toString());
                schedule.putExtra("Level",progressValue);
                startActivity(schedule);

            }
        });


    }

    private void initItem(Query query) {
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    // Toast.makeText(ChooseService.this, ""+childSnapshot.getKey(), Toast.LENGTH_SHORT).show();
                    Vin vin=childSnapshot.getValue(Vin.class);
                    if(vin!=null){
                        String vinCode=vin.getVinCode();
                        listVin.add(vinCode);
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }

    public void updateListView(int progress){
          if(progress==0||progress==2||progress==4||progress==6){
              ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, level1);
              listView.setAdapter(adapter);
          }
          if(progress==1||progress==3||progress==5||progress==7){
              ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, level2);
              listView.setAdapter(adapter);
          }
          if(progress==8){
              ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, level3);
              listView.setAdapter(adapter);
          }
    }

}
