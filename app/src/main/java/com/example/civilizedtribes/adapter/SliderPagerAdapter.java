package com.example.civilizedtribes.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.civilizedtribes.R;
import com.example.civilizedtribes.objects.Images;

import java.util.ArrayList;

public class SliderPagerAdapter extends PagerAdapter {
    private LayoutInflater layoutInflater;
    Context context;
    ArrayList<Images> imagesArrayList;

    public SliderPagerAdapter(Context context, ArrayList<Images> imagesArrayList) {
        this.context = context;
        this.imagesArrayList = imagesArrayList;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.layout_slider, container, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.im_slider);
        Glide.with(context)
                .load(imagesArrayList.get(position).getImageURL())
                .override(300, 200)
                .into(imageView);

        container.addView(view);

        return view;
    }

    @Override
    public int getCount() {
        return imagesArrayList.size();
    }


    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }
}
