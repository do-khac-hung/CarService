package com.khachungbg97gmail.carservice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import com.khachungbg97gmail.carservice.Model.NumberRecognition;

public class InsertCar extends AppCompatActivity {
    Button btnScan;
    EditText edtVim;
    Switch stTypeCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_car);
        btnScan = (Button) findViewById(R.id.btnScan);
        edtVim = (EditText) findViewById(R.id.edtCode);
        stTypeCode=(Switch)findViewById(R.id.switchType);
        stTypeCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(stTypeCode.isChecked()==true){
                    Intent infor=new Intent(InsertCar.this,NumberRecognition.class);
                    startActivity(infor);
                }
            }
        });
    }
}

