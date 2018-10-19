package com.khachungbg97gmail.carservice.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.khachungbg97gmail.carservice.Model.Notification;
import com.khachungbg97gmail.carservice.R;

import java.util.ArrayList;

/**
 * Created by ASUS on 10/17/2018.
 */

public class NotificationAdapter extends BaseAdapter {
    private Context context;
    private  int  layout;
    ArrayList<Notification> notificationList;

    public NotificationAdapter(Context context, int layout, ArrayList<Notification> notificationList) {
        this.context = context;
        this.layout = layout;
        this.notificationList = notificationList;
    }

    @Override
    public int getCount() {
        return notificationList.size();
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
        TextView txtDate,txtTitle,txtBody;

    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if(view==null){
            viewHolder=new ViewHolder();
            LayoutInflater inflater=(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view=inflater.inflate(layout,null);
            viewHolder.txtDate=(TextView) view.findViewById(R.id.date);
            viewHolder.txtTitle=(TextView)view.findViewById(R.id.title);
            viewHolder.txtBody=(TextView)view.findViewById(R.id.body);
            view.setTag(viewHolder);
        }
        else{
            viewHolder=(ViewHolder)view.getTag();
        }
        Notification notification=notificationList.get(i);
        viewHolder.txtDate.setText("date");
        viewHolder.txtTitle.setText(notification.getTitle());
        viewHolder.txtBody.setText(notification.getBody());

        return view;
    }
}
