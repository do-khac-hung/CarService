package com.khachungbg97gmail.carservice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.khachungbg97gmail.carservice.Decode.VIN;
import com.khachungbg97gmail.carservice.Interface.ItemClickListener;
import com.khachungbg97gmail.carservice.Model.ChatUser;
import com.khachungbg97gmail.carservice.Model.DecodeValue;
import com.khachungbg97gmail.carservice.Model.NumberRecognition;
import com.khachungbg97gmail.carservice.Model.Vin;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CarDetails extends AppCompatActivity {
    Button btnScan,btnProcess;
    EditText edtVinCode;
    ScrollView scView;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Vin,ViewHolder> adapter;
    ListView listDetail;
    List<DecodeValue>listDecode;
    ArrayList<HashMap<String,String>> arrayList;
    FirebaseDatabase database;
    Query mReference;
    public static final String apiPrefix="https://api.vindecoder.eu/2.0";
    public static final String apikey="6d1f7d648988";
    public static final String secretkey="8ab8cf7aa0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_details);
        btnScan=(Button)findViewById(R.id.btnScanDetail);
        btnProcess=(Button)findViewById(R.id.btnprocess);
        edtVinCode=(MaterialEditText)findViewById(R.id.edtVinCode);
        listDetail=new ListView(this);
        listDecode=new ArrayList<DecodeValue>();
        arrayList=new ArrayList<HashMap<String, String>>();
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
                         RestAPI(localVin.getVinCode());
                        // getData(localVin.getVinCode());
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
    public void RestAPI(String vinCode){
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(vinCode);
        dialog.setMessage("Thông tin chi tiết của xe");
        dialog.setView(listDetail);
        String[] from={"label","value"};
        int[] views={R.id.txtLabel,R.id.txtValue};
        final SimpleAdapter adapter=new SimpleAdapter(CarDetails.this,arrayList,R.layout.row_vin_detail,from,views);
        listDetail.setAdapter(adapter);
        String sum=vinCode+"|"+apikey+"|"+secretkey;
        String controlSum=getSHAHash(sum);
        String url=apiPrefix+"/"+apikey+"/"+controlSum.substring(0,10)+"/decode/"+vinCode+".json";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Toast.makeText(CarDetails.this, "vào on response", Toast.LENGTH_SHORT).show();
                        try {

                            JSONArray jsonArray = response.getJSONArray("decode");
                            for (int i=0;i<jsonArray.length();i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String label=jsonObject.getString("label");
                                String value=jsonObject.getString("value");
                                DecodeValue decodeValue=new DecodeValue(label,value);
                                listDecode.add(decodeValue);
                                HashMap<String,String> hashMap=new HashMap<String, String>();
                                hashMap.put("label",label);
                                hashMap.put("value",value);
                                arrayList.add(hashMap);
                               // Toast.makeText(CarDetails.this, ""+value, Toast.LENGTH_SHORT).show();
                            }

                             adapter.notifyDataSetChanged();
                             dialog.show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("AAA",e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CarDetails.this, error.getMessage(), Toast.LENGTH_LONG).show();
                        Log.d("TAG1",error.getMessage());
                    }
                }
        );

        requestQueue.add(jsonObjectRequest);
    }


    public String getSHAHash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] messageDigest = md.digest(input.getBytes());
            return convertByteToHex(messageDigest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public String convertByteToHex(byte[] data) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            sb.append(Integer.toString((data[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
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
