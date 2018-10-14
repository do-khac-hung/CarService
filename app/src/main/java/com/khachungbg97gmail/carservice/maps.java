package com.khachungbg97gmail.carservice;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.khachungbg97gmail.carservice.Adapter.ServiceAddressAdapter;
import com.khachungbg97gmail.carservice.Model.ServiceAddress;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class maps extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,OnMapReadyCallback,GoogleMap.OnMyLocationButtonClickListener {
    private Double lat,lng;
    private GoogleMap mMap;
    ArrayList<ServiceAddress> arrayList;
    ServiceAddressAdapter addressAdapter;
    ListView listView;
    private FusedLocationProviderApi client;
    private Location location;
    //Initializing the GoogleApiClient object
    private GoogleApiClient googleApiClient;
    public static final int LOCATION_REQUEST = 101;
    String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        client= LocationServices.FusedLocationApi;
        listView=(ListView)findViewById(R.id.listService);
        arrayList = new ArrayList<>();
        addressAdapter=new ServiceAddressAdapter(this,R.layout.row_address,arrayList);
        listView.setAdapter(addressAdapter);
        //Building a instance of Google Api Client
        googleApiClient=new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .build();
  //      findServiceAddress();
    }



    @Override
    protected void onStart() {
        super.onStart();
        // Initiating the GoogleApiClient Connection when the activity is visible
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        googleApiClient.disconnect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        findServiceAddress();
    }

    private void findServiceAddress() {
        //Checking if the location permission is granted
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED&&
                ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)!=
                PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
            }, LOCATION_REQUEST);
            return;
        }
        //Fetching location using FusedLOcationProviderAPI
        client=LocationServices.FusedLocationApi;
        location=client.getLastLocation(googleApiClient);
        if(location!= null){
            url = "https://maps.googleapis.com/maps/api/place/textsearch/json?location="+location.getLatitude()+","+location.getLongitude()+"&radius=5000&query=ford&key=AIzaSyBz6QP1SJk37CLobZ-GEsF895lJfwka1JY";
            ReadJson(url);
        }

    }
    public void ReadJson(String url){

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            arrayList.clear();
                            JSONArray jsonArray = response.getJSONArray("results");

                            for(int i = 0;i<jsonArray.length();i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                String formatted_address = jsonObject.getString("formatted_address");
                                String name = jsonObject.getString("name");
                                String id = jsonObject.getString("id");
                                String icon = jsonObject.getString("icon");

                                JSONObject geometry = jsonObject.getJSONObject("geometry");
                                JSONObject location = geometry.getJSONObject("location");
                                lat = location.getDouble("lat");
                                lng = location.getDouble("lng");

                                LatLng ltn = new LatLng(lat,lng);

                                arrayList.add(new ServiceAddress(ltn,formatted_address,icon,id,name));

                                mMap.addMarker(new MarkerOptions().position(arrayList.get(i).getLoca()).title(name));

                                Log.d("AAA",arrayList.toString());
                            }
                            addressAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("AAA",e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );

        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "Connection was suspended", Toast.LENGTH_SHORT);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Connection failed", Toast.LENGTH_SHORT);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);

    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }
}
