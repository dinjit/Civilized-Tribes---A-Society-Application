package com.example.civilizedtribes.adapter;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.civilizedtribes.BuildConfig;
import com.example.civilizedtribes.R;
import com.example.civilizedtribes.datamodel.entity.PhotoGallery;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GalleryImageAdapter extends BaseAdapter
{
    private Context mContext;
    List<PhotoGallery> photoGalleryArrayList;

    public GalleryImageAdapter(Context context, List<PhotoGallery>photoGalleryArrayList)
    {
        mContext = context;
        this.photoGalleryArrayList=photoGalleryArrayList;
    }

    public int getCount() {
        return photoGalleryArrayList.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if(convertView==null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.gallery_item, parent, false);
            holder.ivGallery = (ImageView) convertView.findViewById(R.id.imgGallery);
            holder.tvImageName = (TextView) convertView.findViewById(R.id.tvImageName);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(photoGalleryArrayList.get(position).imagePath,bmOptions);
        holder.ivGallery.setImageBitmap(bitmap);
        holder.tvImageName.setText(""+photoGalleryArrayList.get(position).imageName);
        return convertView;
    }


    class ViewHolder {
        ImageView ivGallery;
        TextView tvImageName;
    }
}