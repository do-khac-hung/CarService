package com.khachungbg97gmail.carservice;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.khachungbg97gmail.carservice.Common.Common;
import com.khachungbg97gmail.carservice.Model.ChatUser;
import com.khachungbg97gmail.carservice.Model.User;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignUp extends AppCompatActivity {
   MaterialEditText edtPhone,edtName,edtPass,edtFirstName,edtLastName,edtEmail;
    Button btnSignUp;
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference table_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        edtPhone=(MaterialEditText)findViewById(R.id.Phone);
        edtName=(MaterialEditText)findViewById(R.id.Name);
        edtPass=(MaterialEditText)findViewById(R.id.Password);
        edtFirstName=(MaterialEditText)findViewById(R.id.FirstName);
        edtLastName=(MaterialEditText)findViewById(R.id.LastName);
        edtEmail=(MaterialEditText)findViewById(R.id.Email);
        btnSignUp=(Button)findViewById(R.id.btnSignUp);
        //init Firebase
         database=FirebaseDatabase.getInstance();
         table_user=database.getReference("User");
         auth = FirebaseAuth.getInstance();
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog mDialog=new ProgressDialog(SignUp.this);
                mDialog.setMessage("Please waiting ...");
                mDialog.show();
                final String name = edtName.getText().toString().trim();
                final String email = edtEmail.getText().toString().trim();
                final String password = edtPass.getText().toString().trim();
                final String phone = edtPhone.getText().toString().trim();
                final String firstName=edtFirstName.getText().toString().trim();
                final String lastName=edtLastName.getText().toString().trim();
                //validate
                if (name.isEmpty()) {
                    edtName.setError("can't be blank");
                    return;
                }

                if (email.isEmpty()) {
                    edtEmail.setError("can't be blank");
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    edtEmail.setError("email is invalid ");
                    return;
                }

                if (password.isEmpty()) {
                    edtPass.setError("can't be blank");
                    return;
                }

                if (password.length() < 6) {
                    edtPass.setError("Password with a character count greater than 5");
                    return;
                }

                if (phone.isEmpty()) {
                    edtPhone.setError("can't be blank");
                    return;
                }



                auth.createUserWithEmailAndPassword(email,password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    mDialog.dismiss();
                                    final User user = new User(phone, name, password, email, firstName, lastName);
                                    FirebaseDatabase.getInstance().getReference("User")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(SignUp.this,"Sign Up is successfully", Toast.LENGTH_LONG).show();
                                                Intent insertCar= new Intent(SignUp.this,InsertCar.class);
                                                Common.currentUser=user;
                                                ChatUser.username=Common.currentUser.getName();
                                                ChatUser.id=auth.getCurrentUser().getUid();
                                                startActivity(insertCar);
                                                finish();
                                            } else {
                                                Toast.makeText(SignUp.this, "Sign up is fails", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                } else {
                                    Toast.makeText(SignUp.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }


                            }
                        });
                };

        });

    }
}
