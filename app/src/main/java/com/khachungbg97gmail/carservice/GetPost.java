package com.khachungbg97gmail.carservice;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.khachungbg97gmail.carservice.Common.Common;
import com.khachungbg97gmail.carservice.SQL.ConnectSQL;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
                String query="Select ID,ModifiedDate,Subject,MainContent from Posts";
                try {
                    PreparedStatement ps=connection.prepareStatement(query);
                    ResultSet rs=ps.executeQuery();
                   // ArrayList data = new ArrayList();
                    while(rs.next()){
                        Map<String,String>datanum=new HashMap<String,String>();
                        datanum.put("ModifiedDate", String.valueOf(rs.getDate("ModifiedDate")));
                        datanum.put("Subject",rs.getString("Subject"));
                        datanum.put("MainContent",rs.getString("MainContent"));
                        datanum.put("ID",rs.getString("ID"));
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
            //Toast.makeText(GetPost.this, s, Toast.LENGTH_SHORT).show();
            String[] from={"ModifiedDate","Subject","MainContent"};
            int[] views={R.id.ModifiedDate,R.id.Subject,R.id.MainContent};
            final SimpleAdapter adapter=new SimpleAdapter(GetPost.this,list,R.layout.row_post,from,views);
            listPost.setAdapter(adapter);
            Common.countNotify=list.size();
            listPost.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    HashMap<String,Object>obj= (HashMap<String, Object>) adapter.getItem(position);
                    String ID =(String)obj.get("ID");
                    String ModifiedDate=(String)obj.get("ModifiedDate");
                    String Subject=(String)obj.get("Subject");
                    String MainContent=(String)obj.get("MainContent");
                    AlertDialog.Builder builder = new AlertDialog.Builder(GetPost.this);
                            builder.setTitle(Subject);
                            builder.setMessage(MainContent);
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                }
            });
            super.onPostExecute(s);
        }
    }
    private void SaveID(String ID){
        String fileName="IDPost.txt";
        String data=ID+"\n";
        File file=new File(Environment.getExternalStorageDirectory().getAbsolutePath(),fileName);
        try {
            FileOutputStream fileOutputStream=new FileOutputStream(file);
                fileOutputStream.write(data.getBytes());
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this,"file not found",Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this,"Error Saving",Toast.LENGTH_SHORT).show();
        }
    }
}
