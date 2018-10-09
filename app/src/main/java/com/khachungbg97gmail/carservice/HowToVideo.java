package com.khachungbg97gmail.carservice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.khachungbg97gmail.carservice.Adapter.VideoYouTubeAdapter;
import com.khachungbg97gmail.carservice.Model.VideoYouTube;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HowToVideo extends AppCompatActivity {
     public static String API_KEY="AIzaSyBz6QP1SJk37CLobZ-GEsF895lJfwka1JY";
     String ID_PLAYLIST="PLArwz0-ppPQLXZSENKebpzXv1CaL7qZmu";
     String url="https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId="+ID_PLAYLIST+"&key="+API_KEY+"&maxResults=50";
     ListView lvVideo;
     ArrayList<VideoYouTube> arrayVideo;
     VideoYouTubeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_video);
        lvVideo=(ListView)findViewById(R.id.listVideo);
        arrayVideo=new ArrayList<>();

        adapter=new VideoYouTubeAdapter(this,R.layout.row_video_youtube,arrayVideo);
        lvVideo.setAdapter(adapter);
        getJson(url);
        lvVideo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent playVideo= new Intent(HowToVideo.this,PlayVideoYouTube.class);
                playVideo.putExtra("idVideoPlay",arrayVideo.get(i).getIdVideo());
                startActivity(playVideo);
            }
        });
    }
    private  void getJson(String url){
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonItems=response.getJSONArray("items");
                    String title="";
                    String url="";
                    String idVideo="";
                    for(int i=0;i<jsonItems.length();i++){
                        JSONObject jsonItem=jsonItems.getJSONObject(i);
                        JSONObject jsonSnippet=jsonItem.getJSONObject("snippet");
                        title=jsonSnippet.getString("title");
                        JSONObject jsonThunbnail= jsonSnippet.getJSONObject("thumbnails");
                        JSONObject jsonMedium=jsonThunbnail.getJSONObject("medium");
                        url=jsonMedium.getString("url");
                        JSONObject jsonResourceID=jsonSnippet.getJSONObject("resourceId");
                        idVideo=jsonResourceID.getString("videoId");
                        arrayVideo.add(new VideoYouTube(title,url,idVideo));

                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(HowToVideo.this,response.toString(), Toast.LENGTH_SHORT).show();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(HowToVideo.this, "error", Toast.LENGTH_SHORT).show();

                    }
                });
        requestQueue.add(jsonObjectRequest);

    }
}
