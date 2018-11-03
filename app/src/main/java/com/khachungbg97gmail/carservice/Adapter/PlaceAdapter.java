package com.khachungbg97gmail.carservice.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.khachungbg97gmail.carservice.Map;
import com.khachungbg97gmail.carservice.Model.ServiceAddress;
import com.khachungbg97gmail.carservice.PlaceOnMapFragment;
import com.khachungbg97gmail.carservice.R;

import java.util.List;

/**
 * Created by ASUS on 10/31/2018.
 */

public class PlaceAdapter  extends
        RecyclerView.Adapter<PlaceAdapter.ViewHolder> {

    private List<ServiceAddress> placesList;
    private Context context;

    public PlaceAdapter(List<ServiceAddress> list, Context ctx) {
        placesList = list;
        context = ctx;
    }
    @Override
    public int getItemCount() {
        return placesList.size();
    }

    @Override
    public PlaceAdapter.ViewHolder
    onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.places_item, parent, false);

        PlaceAdapter.ViewHolder viewHolder =
                new PlaceAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final int itemPos = position;
        final ServiceAddress place = placesList.get(position);
        holder.name.setText(place.getName());
        holder.address.setText(place.getFormatted_address());
        holder.phone.setText(place.getId());
//        if(place.getWebsiteUri() != null){
//            holder.website.setText(place.getWebsiteUri().toString());
//        }
//
        if(place.getRating() > -1){
            holder.ratingBar.setNumStars(place.getRating().intValue());
            Drawable drawable = holder.ratingBar.getProgressDrawable();
            drawable.setColorFilter(Color.parseColor("#0064A8"), PorterDuff.Mode.SRC_ATOP);
        }else{
            holder.ratingBar.setVisibility(View.GONE);
        }

        holder.viewOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOnMap(place);
            }
        });
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public TextView address;
        public TextView phone;
        public TextView website;
        public RatingBar ratingBar;

        public Button viewOnMap;

        public ViewHolder(View view) {

            super(view);

            name = view.findViewById(R.id.name);
            address = view.findViewById(R.id.address);
            phone = view.findViewById(R.id.phone);
            website = view.findViewById(R.id.website);
            ratingBar = view.findViewById(R.id.rating);

            viewOnMap = view.findViewById(R.id.view_map_b);
        }
    }

    private void showOnMap(ServiceAddress place){
        FragmentManager fm = ((Map)context)
                .getSupportFragmentManager();

        Bundle bundle=new Bundle();
        bundle.putString("name", (String)place.getName());
        bundle.putString("address", (String)place.getFormatted_address());
        bundle.putDouble("lat", place.getLoca().latitude);
        bundle.putDouble("lng", place.getLoca().longitude);

        PlaceOnMapFragment placeFragment = new PlaceOnMapFragment();
        placeFragment.setArguments(bundle);

        fm.beginTransaction().replace(R.id.map_frame, placeFragment).commit();
    }
}