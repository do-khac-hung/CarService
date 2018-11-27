package com.khachungbg97gmail.carservice;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.khachungbg97gmail.carservice.Common.Common;
import com.khachungbg97gmail.carservice.Model.ChatUser;
import com.khachungbg97gmail.carservice.Model.User;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {
    Button btnSignUp, btnSignIn;
    TextView txtNext;
    FirebaseDatabase database;
    DatabaseReference table_user;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        txtNext=(TextView)findViewById(R.id.txtNext);
        //init Paper
        Paper.init(this);
        database = FirebaseDatabase.getInstance();
        table_user = database.getReference("Users");
        auth = FirebaseAuth.getInstance();
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
        //check remember
        String Email=Paper.book().read(Common.EMAIL_KEY);
        String Pass=Paper.book().read(Common.PWD_KEY);
        if(Email!=null && Pass!=null){
            if(!Email.isEmpty() && !Pass.isEmpty()){
                Login(Email,Pass);
            }
        }
    }

    private void Login(String email, String pass) {
        final ProgressDialog mDialog = new ProgressDialog(MainActivity.this);
        mDialog.setMessage("Please waiting ...");
        mDialog.show();
        auth.signInWithEmailAndPassword(email,pass)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        mDialog.dismiss();
                        FirebaseDatabase.getInstance().getReference("Users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        Intent homeIntent=new Intent(MainActivity.this,Home.class);
                                        Common.currentUser=dataSnapshot.getValue(User.class);
                                        ChatUser.username=Common.currentUser.getName();
                                        ChatUser.id=dataSnapshot.getKey();
                                        startActivity(homeIntent);
                                        finish();
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        mDialog.dismiss();
                                        Toast.makeText(MainActivity.this, "Sign in is fails", Toast.LENGTH_SHORT).show();
                                    }
                                });

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mDialog.dismiss();
                Toast.makeText(MainActivity.this,e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}