package com.khachungbg97gmail.carservice;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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
import com.rengwuxian.materialedittext.MaterialEditText;

import io.paperdb.Paper;

import static com.khachungbg97gmail.carservice.R.id.Password;

public class SignIn extends AppCompatActivity {
    EditText edtEmail,edtPass;
    Button btnSignIn,btnReset;
    String email, pass;
    CheckBox chbRemember;
    FirebaseDatabase database;
    DatabaseReference table_user;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        edtEmail = (MaterialEditText) findViewById(R.id.edtEmail);
        edtPass = (MaterialEditText) findViewById(Password);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        btnReset=(Button)findViewById(R.id.btn_reset_password);
        chbRemember=(CheckBox)findViewById(R.id.checkbox);
        //init paper
        Paper.init(this);
       //init firebase
        database = FirebaseDatabase.getInstance();
        table_user = database.getReference("Users");
        auth = FirebaseAuth.getInstance();
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignIn.this, ResetPassword.class));
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email=edtEmail.getText().toString();
                pass=edtPass.getText().toString();
                if(email.equals("")){
                    edtEmail.setError("can't be blank");
                }
                else if(pass.equals("")){
                    edtPass.setError("can't be blank");
                }
                else if(pass.length()<6){
                    edtPass.setError("Password with a character count greater than 5");
                }
                else {
                    if(chbRemember.isChecked()){
                        Paper.book().write(Common.EMAIL_KEY,email);
                        Paper.book().write(Common.PWD_KEY,pass);
                    }
                    final ProgressDialog mDialog = new ProgressDialog(SignIn.this);
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
                                                    Intent homeIntent=new Intent(SignIn.this,Home.class);
                                                    Common.currentUser=dataSnapshot.getValue(User.class);
                                                    ChatUser.username=Common.currentUser.getName();
                                                    ChatUser.id=dataSnapshot.getKey();
                                                    table_user.child(dataSnapshot.getKey()).child("password").setValue(pass);
                                                    startActivity(homeIntent);
                                                    finish();
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {
                                                    mDialog.dismiss();
                                                    Toast.makeText(SignIn.this, "Sign in is fails", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mDialog.dismiss();
                            Toast.makeText(SignIn.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }
  }

