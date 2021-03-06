package com.khachungbg97gmail.carservice;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.khachungbg97gmail.carservice.Adapter.PlaceAdapter;
import com.khachungbg97gmail.carservice.Model.ServiceAddress;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.khachungbg97gmail.carservice.Common.Common.placesListCommon;

public class Map extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    public static final String TAG = "CurrentLocNearByPlaces";
    private static final int LOC_REQ_CODE = 1;
    protected RecyclerView recyclerView;
    LocationManager manager;
    private FusedLocationProviderApi client;
    private Location location;
    private Double lat, lng;
    String url;
    FragmentManager fm;
    PlaceOnMapFragment placeFragment;
    //Initializing the GoogleApiClient object
    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Toolbar tb = findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        tb.setSubtitle("Map");
        fm= getSupportFragmentManager();
        placeFragment = new PlaceOnMapFragment();

        client= LocationServices.FusedLocationApi;
        recyclerView = findViewById(R.id.places_lst);
        if (!isGpsOn()) {
            showSettingsAlert();
        }

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .build();
        googleApiClient.connect();

        LinearLayoutManager recyclerLayoutManager =
                new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerLayoutManager);

        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(recyclerView.getContext(),
                        recyclerLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        getCurrentPlaceItems();
    }

    private boolean isGpsOn() {
        manager = (LocationManager) getSystemService(LOCATION_SERVICE);
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }


    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);


        alertDialog.setTitle("GPS is not Enabled!");

        alertDialog.setMessage("Do you want to turn on GPS?");


        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });


        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });


        alertDialog.show();
    }

    private boolean isLocationAccessPermitted() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        } else {
            return true;
        }
    }

    private void requestLocationAccessPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                LOC_REQ_CODE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LOC_REQ_CODE) {
            if (resultCode == RESULT_OK) {
                getCurrentPlaceData();
            }
        }
    }

    private void getCurrentPlaceItems() {
        if (isLocationAccessPermitted()) {
            getCurrentPlaceData();
        } else {
            requestLocationAccessPermission();
        }
    }

    @SuppressLint("MissingPermission")
    private void getCurrentPlaceData() {
        client = LocationServices.FusedLocationApi;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        location = client.getLastLocation(googleApiClient);
      //  mMap.setMyLocationEnabled(true);
        if (location != null) {
            Bundle bundle=new Bundle();
            bundle.putString("name", "Vị trí hiện tại");
            bundle.putString("address", "Địa điểm");
            bundle.putDouble("lat", location.getLatitude());
            bundle.putDouble("lng", location.getLongitude());
            placeFragment.setArguments(bundle);
            fm.beginTransaction().replace(R.id.map_frame, placeFragment).commit();
            url = "https://maps.googleapis.com/maps/api/place/textsearch/json?location=" + location.getLatitude() + "," + location.getLongitude() + "&radius=5000&query=ford&key=AIzaSyBz6QP1SJk37CLobZ-GEsF895lJfwka1JY";
            ReadJson(url);
        }
    }

    public void ReadJson(String url) {

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            placesListCommon=new ArrayList<ServiceAddress>();
                            List<ServiceAddress> placesList = new ArrayList<ServiceAddress>();
                            JSONArray jsonArray = response.getJSONArray("results");

                            for (int i=0;i<jsonArray.length();i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                String formatted_address = jsonObject.getString("formatted_address");
                                String name = jsonObject.getString("name");
                                String id = jsonObject.getString("id");
                                String icon = jsonObject.getString("icon");
                                Double rating = jsonObject.getDouble("rating");

                                JSONObject geometry = jsonObject.getJSONObject("geometry");
                                JSONObject location = geometry.getJSONObject("location");
                                lat = location.getDouble("lat");
                                lng = location.getDouble("lng");

                                LatLng ltn = new LatLng(lat,lng);
                                ServiceAddress serviceAddress=new ServiceAddress(ltn,formatted_address,icon,id,name,rating);
                                placesList.add(serviceAddress);
                             //   placeFragment.mMap.addMarker(new MarkerOptions().position(serviceAddress.getLoca()).title(serviceAddress.getName()));

                               // mMap.addMarker(new MarkerOptions().position(placesList.get(i).getLoca()).title(name));

                                Log.d("AAA",placesList.toString());
                            }
                            PlaceAdapter placeAdapter = new PlaceAdapter(placesList,Map.this);
                            recyclerView.setAdapter(placeAdapter);
                            placesListCommon.addAll(placesList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("AAA",e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        placesListCommon=new ArrayList<ServiceAddress>();
                        String message = null;
                        if (volleyError instanceof NetworkError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                        } else if (volleyError instanceof ServerError) {
                            message = "The server could not be found. Please try again after some time!!";
                        } else if (volleyError instanceof AuthFailureError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                        } else if (volleyError instanceof ParseError) {
                            message = "Parsing error! Please try again after some time!!";
                        } else if (volleyError instanceof NoConnectionError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                        } else if (volleyError instanceof TimeoutError) {
                            message = "Connection TimeOut! Please check your internet connection.";
                        }
                        Toast.makeText(Map.this, ""+message, Toast.LENGTH_LONG).show();
                    }
                }
        );

        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        getCurrentPlaceData();
    }

    @Override
    public void onConnectionSuspended(int i) {
        googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @Override
    public void onLocationChanged(Location location) {

    }
    @Override
    protected void onResume() {
        placeFragment.onResume();
        super.onResume();
//        googleApiClient.connect();
//        getCurrentPlaceItems();
    }


    @Override
    protected void onPause() {
        placeFragment.onPause();
        super.onPause();

    }


    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        placeFragment.onStop();
        super.onStop();
        //googleApiClient.disconnect();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }



}
