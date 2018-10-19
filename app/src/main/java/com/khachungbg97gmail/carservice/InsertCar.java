package com.khachungbg97gmail.carservice;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.khachungbg97gmail.carservice.Common.Common;
import com.khachungbg97gmail.carservice.Model.ChatUser;
import com.khachungbg97gmail.carservice.Model.NumberRecognition;
import com.khachungbg97gmail.carservice.Model.Vin;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.List;

public class InsertCar extends AppCompatActivity {
    EditText edtCategory,edtVim;
    Switch stTypeCode;
    Spinner spnCategory;
    Button btnInsert;
    List<String> list;

    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference table_vin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_car);
        edtVim = (MaterialEditText) findViewById(R.id.edtCode);
        edtCategory=(MaterialEditText)findViewById(R.id.edtcategory);
        btnInsert=(Button)findViewById(R.id.btnInsertcar);
        stTypeCode = (Switch) findViewById(R.id.switchType);
        spnCategory = (Spinner) findViewById(R.id.spinner1);
        //init Firebase
        database=FirebaseDatabase.getInstance();
        table_vin=database.getReference("Vin");
        auth = FirebaseAuth.getInstance();
        list = new ArrayList<>();
        list.add("FIESTA");
        list.add("NEW FOCUS");
        list.add("TRANSIT");
        list.add("NEW RANGER");
        list.add("ECOSPORT");
        list.add("NEW EVEREST");
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,list);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spnCategory.setAdapter(adapter);
        //event
        spnCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                edtCategory.setText(spnCategory.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        stTypeCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stTypeCode.isChecked() == true) {
                    Intent infor = new Intent(InsertCar.this, NumberRecognition.class);
                    startActivity(infor);
                }
            }
        });
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog mDialog=new ProgressDialog(InsertCar.this);
                mDialog.setMessage("Please waiting ...");
                mDialog.show();
                final String category=edtCategory.getText().toString();
                final String vinCode=edtVim.getText().toString();
                //validate
                if(category.isEmpty()){
                    edtCategory.setError("can't be blank");
                    return;
                }
                if(vinCode.isEmpty()){
                    edtVim.setError("can't be blank");
                    return;
                }
                if(vinCode.length()!=17){
                    edtVim.setError("length of vin code is 17 character");
                }
                 table_vin.addValueEventListener(new ValueEventListener() {
                     @Override
                     public void onDataChange(DataSnapshot dataSnapshot) {
                         if(dataSnapshot.child(vinCode).exists()){
                             mDialog.dismiss();
                             Toast.makeText(InsertCar.this, "Vin code is exists", Toast.LENGTH_SHORT).show();
                         }
                         else {
                             mDialog.dismiss();
                             Vin vin=new Vin(vinCode, ChatUser.id,category);
                             table_vin.child(vinCode).setValue(vin);
                             Common.currentVin=vin;
                             Intent home=new Intent(InsertCar.this,Home.class);
                             startActivity(home);
                             finish();

                         }
                     }

                     @Override
                     public void onCancelled(DatabaseError databaseError) {

                     }
                 });

            }
        });
    }


    }
