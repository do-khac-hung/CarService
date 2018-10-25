package com.khachungbg97gmail.carservice;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.khachungbg97gmail.carservice.SQL.ConnectSQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetPost extends AppCompatActivity {
    ConnectSQL connect;
    //EditText edtID,edtStatus,edtVin,edtModel;
    ListView listPost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_post);
        connect = new ConnectSQL();
        listPost=(ListView)findViewById(R.id.lst);

        FillList fillList = new FillList();
        fillList.execute("");
    }

    public class FillList extends AsyncTask<String, String, String> {
        String z="";
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();

        @Override
        protected String doInBackground(String... params) {
            Connection connection = connect.CONN();
            if(connection==null){
                z="Error in connect with SQL server";
            }
            else{
                String query="Select * from Posts";
                try {
                    PreparedStatement ps=connection.prepareStatement(query);
                    ResultSet rs=ps.executeQuery();
                   // ArrayList data = new ArrayList();
                    while(rs.next()){
                        Map<String,String>datanum=new HashMap<String,String>();
                        datanum.put("ID",rs.getString("ID"));
                        datanum.put("Status",rs.getString("Status"));
                        datanum.put("VIN",rs.getString("VIN"));
                        datanum.put("Model",rs.getString("Model"));
                        list.add(datanum);

                    }
                    z = "Success";
                } catch (SQLException e) {
                    e.printStackTrace();
                    z = "Error retrieving data from table";
                }
            }
            return z;
        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(GetPost.this, s, Toast.LENGTH_SHORT).show();
            String[] from={"ID","Status","VIN","Model"};
            int[] views={R.id.txtID,R.id.txtStatus,R.id.txtVin,R.id.txtmodel};
            final SimpleAdapter adapter=new SimpleAdapter(GetPost.this,list,R.layout.row_post,from,views);
            listPost.setAdapter(adapter);
            listPost.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    HashMap<String,Object>obj= (HashMap<String, Object>) adapter.getItem(position);
                    String ID=(String)obj.get("ID");
                    String Status=(String)obj.get("Status");
                    String VIN=(String)obj.get("VIN");
                    String Model=(String)obj.get("Model");

                }
            });
            super.onPostExecute(s);
        }
    }
}
