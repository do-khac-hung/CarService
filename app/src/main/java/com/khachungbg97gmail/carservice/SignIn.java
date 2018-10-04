package com.khachungbg97gmail.carservice;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.khachungbg97gmail.carservice.Model.User;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignIn extends AppCompatActivity {
    EditText edtPhone,edtPass;
    Button btnSignIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        edtPhone=(MaterialEditText)findViewById(R.id.Phone);
        edtPass=(MaterialEditText)findViewById(R.id.Password);
        btnSignIn=(Button)findViewById(R.id.btnSignIn);
        //init Firebase
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        final DatabaseReference table_user=database.getReference("User");
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog mDialog=new ProgressDialog(SignIn.this);
                mDialog.setMessage("Please waiting ...");
                mDialog.show();
                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //check if user not exits in database
                        if (dataSnapshot.child(edtPhone.getText().toString()).exists()) {
                            mDialog.dismiss();
                            //get user information
                            User user = dataSnapshot.child(edtPhone.getText().toString()).getValue(User.class);
                            if (user.getPassword().equals(edtPass.getText().toString())) {
                                Toast.makeText(SignIn.this, "Sign in success", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(SignIn.this, "Sign in failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            mDialog.dismiss();
                            Toast.makeText(SignIn.this, "User not exist in database", Toast.LENGTH_SHORT).show();
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