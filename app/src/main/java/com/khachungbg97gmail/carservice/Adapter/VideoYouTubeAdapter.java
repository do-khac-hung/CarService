package com.khachungbg97gmail.carservice.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.khachungbg97gmail.carservice.Model.VideoYouTube;
import com.khachungbg97gmail.carservice.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by ASUS on 10/9/2018.
 */

public class VideoYouTubeAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    List<VideoYouTube>videoYouTubeList;
    @Override
    public int getCount() {
        return videoYouTubeList.size();
    }

    public VideoYouTubeAdapter(Context context, int layout, List<VideoYouTube> videoYouTubeList) {
        this.context = context;
        this.layout = layout;
        this.videoYouTubeList = videoYouTubeList;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
     private  class ViewHolder{
         ImageView imgThumbnail;
         TextView txtTitle;
     }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder hoder;
        if(view==null){
            hoder=new ViewHolder();
            LayoutInflater inflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view=inflater.inflate(layout,null);
            hoder.txtTitle=(TextView)view.findViewById(R.id.textTitle);
            hoder.imgThumbnail=(ImageView)view.findViewById(R.id.imageThumbnail);
            view.setTag(hoder);
        }
        else {
            hoder= (ViewHolder) view.getTag();
        }
        VideoYouTube video=videoYouTubeList.get(i);
        hoder.txtTitle.setText(video.getTitle());
        Picasso.with(context).load(video.getThumbnail()).into(hoder.imgThumbnail);

        return view;
    }
}
