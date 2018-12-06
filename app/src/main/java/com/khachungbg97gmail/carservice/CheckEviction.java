package com.khachungbg97gmail.carservice;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.khachungbg97gmail.carservice.Decode.VIN;
import com.khachungbg97gmail.carservice.Interface.ItemClickListener;
import com.khachungbg97gmail.carservice.Model.ChatUser;
import com.khachungbg97gmail.carservice.Model.Vin;
import com.khachungbg97gmail.carservice.SQL.ConnectSQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.khachungbg97gmail.carservice.Common.Common.currentVin;

public class CheckEviction extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Vin,ViewHolder> adapter;

    FirebaseDatabase database;
    Query mReference;
    ConnectSQL connect;
    String Year;
    String Category;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_eviction);
        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        database=FirebaseDatabase.getInstance();
        mReference=database.getReference().child("Vins").orderByChild("idUser").equalTo(ChatUser.id);
        loadListVin(ChatUser.id);
        recyclerView.setAdapter(adapter);

        connect = new ConnectSQL();

    }
    private void loadListVin(String id) {
        adapter=new FirebaseRecyclerAdapter<Vin, ViewHolder>(
                Vin.class,
                R.layout.row_checkvin,
                ViewHolder.class,
                mReference
            )
        {
            @Override
            protected void populateViewHolder(ViewHolder viewHolder, final Vin model, int position) {
                viewHolder.txtVinCode.setText(model.getVinCode());
                final Vin localVin=model;
                currentVin=model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        VIN vin=new VIN(model.getVinCode());
                        Year= String.valueOf(vin.getYear());
                        Category=model.getCategory();
                        CheckElement checkElement=new CheckElement();
                        checkElement.execute();

                    }
                });
            }
        };

    }
    public static class ViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{
        public TextView txtVinCode;
        private ItemClickListener itemClickListener;

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }
        public ViewHolder(View itemView) {
            super(itemView);
            txtVinCode=(TextView)itemView.findViewById(R.id.txtCheckVin);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v,getAdapterPosition(),false);
        }
    }
    public  class CheckElement extends AsyncTask<String,String,String>{
        String z="";
        Boolean isSuccess = false;
        @Override
        protected String doInBackground(String... params) {
            Connection connection = connect.CONN();
            if(connection==null){
                z="Error in connect with SQL server";
            }
            else{
                String query="Select ID from Vehicles where Model='"+Category+"'and Year='"+Year+"'";
                //String query="Select ModifiedDate,Subject,MainContent,FeatureImage from Posts";
                try {
                    PreparedStatement ps = connection.prepareStatement(query);
                    ResultSet rs=ps.executeQuery();
                    if(rs.next()==false){
                        z="true";
                    }
                    else{
                        z="false";
                    }
                    isSuccess = true;
                } catch (SQLException e) {
                    e.printStackTrace();
                    z = "Error retrieving data from table";
                }
            }
            return z;
        }

        @Override
        protected void onPostExecute(String s) {
           // Toast.makeText(CheckEviction.this, ""+s, Toast.LENGTH_SHORT).show();
            if(s.equals("true")){

                AlertDialog.Builder builder = new AlertDialog.Builder(CheckEviction.this);
                builder.setTitle("Check the eviction");
                builder.setMessage("Your car is not in the summon list");
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
            else {
                AlertDialog.Builder builder = new AlertDialog.Builder(CheckEviction.this);
                builder.setTitle("Check the eviction");
                builder.setMessage("Your car is in the summon list");
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
            super.onPostExecute(s);
        }
    }
}
