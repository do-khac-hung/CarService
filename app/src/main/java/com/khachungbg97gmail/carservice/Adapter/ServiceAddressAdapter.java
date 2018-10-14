package com.khachungbg97gmail.carservice.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.khachungbg97gmail.carservice.Model.ServiceAddress;
import com.khachungbg97gmail.carservice.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by ASUS on 10/14/2018.
 */

public class ServiceAddressAdapter extends BaseAdapter {
    private Context context;
    private  int  layout;
    List<ServiceAddress> addressList;
    @Override
    public int getCount() {
        return addressList.size();
    }

    public ServiceAddressAdapter(Context context, int layout, List<ServiceAddress> addressList) {
        this.context = context;
        this.layout = layout;
        this.addressList = addressList;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    private class ViewHolder{
        ImageView icon;
        TextView txtName,txtAddress;

    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if(view==null){
            viewHolder=new ViewHolder();
            LayoutInflater inflater=(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view=inflater.inflate(layout,null);
            viewHolder.icon=(ImageView)view.findViewById(R.id.imgicon);
            viewHolder.txtName=(TextView)view.findViewById(R.id.txtNameAddress);
            viewHolder.txtAddress=(TextView)view.findViewById(R.id.formatted_address);
            view.setTag(viewHolder);
        }
        else{
            viewHolder=(ViewHolder)view.getTag();
        }
         ServiceAddress address=addressList.get(i);
        viewHolder.txtName.setText(address.getName());
        viewHolder.txtAddress.setText(address.getFormatted_address());
        Picasso.with(context).load(address.getIcon()).into(viewHolder.icon);
        return view;
    }
}
