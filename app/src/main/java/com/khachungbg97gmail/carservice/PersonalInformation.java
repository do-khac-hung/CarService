package com.khachungbg97gmail.carservice;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.khachungbg97gmail.carservice.Common.Common;
import com.khachungbg97gmail.carservice.Model.ChatUser;
import com.khachungbg97gmail.carservice.Model.User;
import com.khachungbg97gmail.carservice.Model.Vin;
import com.rengwuxian.materialedittext.MaterialEditText;

public class PersonalInformation extends TabActivity {
    MaterialEditText edtPhone,edtName,edtPass,edtFirstName,edtLastName,edtEmail;
    Button btnSave,btnAdd;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Vin,VinViewHolder>adapter;

    FirebaseDatabase database;
    DatabaseReference table_user;
    DatabaseReference table_vin;
    Query mReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information);
        edtPhone=(MaterialEditText)findViewById(R.id.Phone);
        edtName=(MaterialEditText)findViewById(R.id.Name);
        edtPass=(MaterialEditText)findViewById(R.id.Password);
        edtFirstName=(MaterialEditText)findViewById(R.id.FirstName);
        edtLastName=(MaterialEditText)findViewById(R.id.LastName);
        edtEmail=(MaterialEditText)findViewById(R.id.Email);
        btnSave=(Button)findViewById(R.id.btnSave);
        btnAdd=(Button)findViewById(R.id.btnAdd);
        recyclerView=(RecyclerView)findViewById(R.id.list);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        //init Firebase
        database=FirebaseDatabase.getInstance();
        table_user=database.getReference("User");
        table_vin=database.getReference("Vin");
        mReference=database.getReference().child("Vin").orderByChild("idUser").equalTo(ChatUser.id);



        TabHost.TabSpec spec=getTabHost().newTabSpec("tag1");

        spec.setContent(R.id.listCar);
        spec.setIndicator("ListCar");
        getTabHost().addTab(spec);

        spec=getTabHost().newTabSpec("tag2");
        spec.setContent(R.id.personal);
        spec.setIndicator("Personal");
        getTabHost().addTab(spec);

        getTabHost().setCurrentTab(0);
        //load list Vin with idUser;
        loadListVin(ChatUser.id);
        Log.d("Tag1",ChatUser.id);
        Log.d("Tag",""+adapter.getItemCount());
        recyclerView.setAdapter(adapter);
        //set data into editText
        setData(Common.currentUser);
        //event
        btnSave.setOnClickListener(onSave);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PersonalInformation.this,InsertCar.class);
                startActivity(intent);
            }
        });
    }

    private void loadListVin(String id) {
        adapter=new FirebaseRecyclerAdapter<Vin, VinViewHolder>(
                Vin.class,
                R.layout.row_vin,
                VinViewHolder.class,
                mReference
                 )
        {
            @Override
            protected void populateViewHolder(VinViewHolder viewHolder, Vin model, int position) {
                  viewHolder.txtVinCode.setText("Vin Code:"+model.getVinCode());
                  viewHolder.txtCategory.setText("Category:"+model.getCategory());
            }
        };

    }

    private void setData(User currentUser) {
        edtName.setText(currentUser.getName());
        edtPhone.setText(currentUser.getPhone());
        edtPass.setText(currentUser.getPassword());
        edtLastName.setText(currentUser.getLastName());
        edtFirstName.setText(currentUser.getFirstName());
        edtEmail.setText(currentUser.getEmail());
    }

    private View.OnClickListener onSave=new View.OnClickListener() {
        public void onClick(View v) {
            String name=edtName.getText().toString();
            String phone=edtPhone.getText().toString();
            String pass=edtPass.getText().toString();
            String lastName=edtLastName.getText().toString();
            String firstName=edtFirstName.getText().toString();
            String email=edtEmail.getText().toString();
            User user=new User(phone,name,pass,email,firstName,lastName);
            table_user.child(ChatUser.id).setValue(user);
            Toast.makeText(PersonalInformation.this, "Update successful", Toast.LENGTH_SHORT).show();
            Common.currentUser=user;

        }
        };
        public static class VinViewHolder extends RecyclerView.ViewHolder {
           public TextView txtVinCode,txtCategory;
            View view;
            public VinViewHolder(View itemView) {
                super(itemView);
                txtVinCode=(TextView)itemView.findViewById(R.id.txtVin);
                txtCategory=(TextView)itemView.findViewById(R.id.txtCategory);
                view=itemView;
            }


        }
    }

