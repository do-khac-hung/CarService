package com.khachungbg97gmail.carservice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.khachungbg97gmail.carservice.Decode.VIN;
import com.khachungbg97gmail.carservice.Interface.ItemClickListener;
import com.khachungbg97gmail.carservice.Model.ChatUser;
import com.khachungbg97gmail.carservice.Model.NumberRecognition;
import com.khachungbg97gmail.carservice.Model.Vin;
import com.rengwuxian.materialedittext.MaterialEditText;

public class CarDetails extends AppCompatActivity {
    Button btnScan,btnProcess;
    EditText edtVinCode;
    ScrollView scView;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Vin,ViewHolder> adapter;

    FirebaseDatabase database;
    Query mReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_details);
        btnScan=(Button)findViewById(R.id.btnScanDetail);
        btnProcess=(Button)findViewById(R.id.btnprocess);
        edtVinCode=(MaterialEditText)findViewById(R.id.edtVinCode);
        scView=(ScrollView)findViewById(R.id.scView);
        scView.fullScroll(View.FOCUS_DOWN);
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CarDetails.this,NumberRecognition.class);
                startActivityForResult(intent,0);
            }
        });
        btnProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String vinCode=edtVinCode.getText().toString();
                //validate

                //xu ly
                getData(vinCode);

            }
        });
        recyclerView=(RecyclerView)findViewById(R.id.listVin);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        database=FirebaseDatabase.getInstance();
        mReference=database.getReference().child("Vins").orderByChild("idUser").equalTo(ChatUser.id);
        loadListVin(ChatUser.id);
        recyclerView.setAdapter(adapter);

        //init Firebase


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
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                         getData(localVin.getVinCode());
                    }
                });
            }
        };

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==0){
            if(resultCode== CommonStatusCodes.SUCCESS){
                if(data!=null){
                    String text=data.getStringExtra("TextBlock");
                   // Toast.makeText(this, ""+text, Toast.LENGTH_SHORT).show();
                    edtVinCode.setText(text);

                }else{
                    edtVinCode.setText("NULL");
                }
            }

        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void getData(String vinCode){
        VIN vin=new VIN(vinCode);
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(vinCode);
        dialog.setMessage("Thông tin chi tiết của xe");
        LayoutInflater inflater = LayoutInflater.from(this);
        View detail_dialog = inflater.inflate(R.layout.detail_dialog,null);
        TextView txtManufacter=detail_dialog.findViewById(R.id.txtManufacter);
        TextView txtCountry=detail_dialog.findViewById(R.id.txtCountry);
        TextView txtYear=detail_dialog.findViewById(R.id.txtYear);
        TextView txtSeri=detail_dialog.findViewById(R.id.txtSeri);
        TextView txtweightDigit=detail_dialog.findViewById(R.id.txtweightDigit);
        TextView txtPlant=detail_dialog.findViewById(R.id.txtPlant);
        TextView txtdescription=detail_dialog.findViewById(R.id.description);
        txtManufacter.setText(vin.getManufacturer());
        txtCountry.setText(vin.getCountry());
        txtYear.setText(""+vin.getYear());
        txtSeri.setText(vin.getSerialNumber());
        txtPlant.setText(vin.getPlantCode());
        txtweightDigit.setText(String.valueOf(vin.getWeightDigit()));
        txtdescription.setText(vin.getVdsCode());
        dialog.setView(detail_dialog);
        dialog.show();

    }
    public void RestAPI(String url){

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
}
