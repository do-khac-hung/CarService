package com.khachungbg97gmail.carservice;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.khachungbg97gmail.carservice.Common.Common;
import com.khachungbg97gmail.carservice.Model.ChatUser;
import com.khachungbg97gmail.carservice.Model.Schedule;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import io.paperdb.Paper;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    FirebaseDatabase database;
    DatabaseReference car;
    TextView txtName,txtScheduleMessage;
    ImageView mAddress,mHotline,mVideo,mSchedule,mEPC,mAdd;
    String url="https://carservice-47a9f.firebaseio.com/Schedules.json";
    Query mReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Menu");
        setSupportActionBar(toolbar);
        mAddress=(ImageView)findViewById(R.id.mAddress);
        mHotline=(ImageView)findViewById(R.id.mHotline);
        mVideo=(ImageView)findViewById(R.id.mVideo);
        mSchedule=(ImageView)findViewById(R.id.mSchedule);
        mEPC=(ImageView)findViewById(R.id.mEPC);
        mAdd=(ImageView)findViewById(R.id.mAdd);
        //init paper
        Paper.init(this);
        database=FirebaseDatabase.getInstance();
        mReference=database.getReference().child("Schedules").orderByChild("idUser").equalTo(ChatUser.id);
        txtScheduleMessage=(TextView)findViewById(R.id.txtScheduleMessage);
//        if(Common.currentUser!=null) {
//            findNearestDate(url);
//        }
        if(Common.currentUser!=null){
            findNearest();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Common.currentUser==null){
                Snackbar.make(view, "Please sign in or sign up", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                }else{
                    Intent sentUs = new Intent(Home.this, Chat.class);
                    startActivity(sentUs);
                }

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Menu menuNav=navigationView.getMenu();
        MenuItem nav_info=menuNav.findItem(R.id.nav_info);
        MenuItem nav_notify=menuNav.findItem(R.id.nav_notify);
        MenuItem nav_manage=menuNav.findItem(R.id.nav_manage);
        MenuItem nav_send=menuNav.findItem(R.id.nav_send);
        MenuItem nav_check=menuNav.findItem(R.id.nav_check);
        if(Common.currentUser==null){
            nav_info.setEnabled(false);
            nav_notify.setEnabled(false);
            nav_manage.setEnabled(false);
            nav_send.setEnabled(false);
            nav_check.setEnabled(false);
            mSchedule.setEnabled(false);
        }
        //set Name for user
        View headerView=navigationView.getHeaderView(0);
        txtName=(TextView)headerView.findViewById(R.id.txtName1);
        if(Common.currentUser!=null) {
            txtName.setText(Common.currentUser.getLastName());
        }
        mAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mMap=new Intent(Home.this,Map.class);
                startActivity(mMap);
            }
        });
        mHotline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mHotLine=new Intent(Home.this,HotLine.class);
                startActivity(mHotLine);
            }
        });
        mVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mYoutube=new Intent(Home.this,HowToVideo.class);
                startActivity(mYoutube);
            }
        });
        mEPC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mEpc=new Intent(Home.this,EPCView.class);
                startActivity(mEpc);
            }
        });
        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.openDrawer(Gravity.START);
            }
        });
        mSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mSchedule=new Intent(Home.this,ChooseService.class);
                startActivity(mSchedule);
            }
        });

    }

    private void findNearest() {
        final long now = System.currentTimeMillis();
        final List<Date> dates = new ArrayList<Date>();
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    Schedule schedule=childSnapshot.getValue(Schedule.class);
                    if(schedule!=null){
                        String schedules=schedule.getDate();
                        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                        try {
                            Date date=df.parse(schedules);
                            if(date.getTime()-now>=0){
                                dates.add(date);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }
                }
                if(!dates.isEmpty()) {
                    Date closest = Collections.min(dates, new Comparator<Date>() {
                        public int compare(Date d1, Date d2) {
                            long diff1 = Math.abs(d1.getTime() - now);
                            long diff2 = Math.abs(d2.getTime() - now);
                            return Long.compare(diff1, diff2);
                        }
                    });
                    txtScheduleMessage.setText(closest.getDate()+"|"+(closest.getMonth()+1)+"|"+(closest.getYear()+1900));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_info) {
            Intent infor=new Intent(Home.this,PersonalInformation.class);
            startActivity(infor);

        } else if (id == R.id.nav_address) {
            Intent mMap=new Intent(Home.this,Map.class);
            startActivity(mMap);

        } else if (id == R.id.nav_manage) {
            Intent choose=new Intent(Home.this,ChooseService.class);
            startActivity(choose);

        }else if(id==R.id.nav_check){
            Intent mCheck=new Intent(Home.this,GetPost.class);
            startActivity(mCheck);

        }else if(id==R.id.nav_details){
            Intent mDetails=new Intent(Home.this,CarDetails.class);
            startActivity(mDetails);

        }
        else if (id == R.id.nav_notify) {
            Intent notify=new Intent(Home.this,Maintenance.class);
            startActivity(notify);

        } else if (id == R.id.nav_send) {
            Intent mHotLine=new Intent(Home.this,HotLine.class);
            startActivity(mHotLine);

        }else if (id == R.id.nav_logout) {
            //delete email ,pass
            Paper.book().destroy();
            Intent SignOut= new Intent(Home.this,MainActivity.class);
            SignOut.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(SignOut);

        }else if(id==R.id.nav_howto){
            Intent mYoutube=new Intent(Home.this,HowToVideo.class);
            startActivity(mYoutube);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void findNearestDate(String url){
        final long now = System.currentTimeMillis();
        final List<Date> dates = new ArrayList<Date>();
        StringRequest request=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj=new JSONObject(response);
                    Iterator i=obj.keys();
                    String schedules="";
                    while (i.hasNext()){
                        schedules=i.next().toString();
                        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                        Date date=df.parse(schedules);
                        if(date.getTime()-now>=0){
                           dates.add(date);
                        }
                    }
                    if(!dates.isEmpty()) {
                        Date closest = Collections.min(dates, new Comparator<Date>() {
                            public int compare(Date d1, Date d2) {
                                long diff1 = Math.abs(d1.getTime() - now);
                                long diff2 = Math.abs(d2.getTime() - now);
                                return Long.compare(diff1, diff2);
                            }
                        });
                        txtScheduleMessage.setText(closest.getDate()+"|"+closest.getMonth()+"|"+(closest.getYear()+1900));
                    }
                    //Toast.makeText(Home.this, ""+closest, Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("" + error);
            }
        });
        RequestQueue rQueue = Volley.newRequestQueue(Home.this);
        rQueue.add(request);
    }
}
