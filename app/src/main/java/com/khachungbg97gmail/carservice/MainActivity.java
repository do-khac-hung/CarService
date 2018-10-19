package com.khachungbg97gmail.carservice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Button btnSignUp, btnSignIn;
    TextView txtNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        txtNext=(TextView)findViewById(R.id.txtNext);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent SignIn = new Intent(MainActivity.this, SignIn.class);
                startActivity(SignIn);
            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent SignUp = new Intent(MainActivity.this, SignUp.class);
                startActivity(SignUp);
            }
        });
        txtNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home=new Intent(MainActivity.this,Home.class);
                startActivity(home);
            }
        });
    }
}