package com.example.civilizedtribes.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.civilizedtribes.R;
import com.example.civilizedtribes.adapter.SliderPagerAdapter;
import com.example.civilizedtribes.interfaces.IApiService;
import com.example.civilizedtribes.objects.Images;
import com.example.civilizedtribes.objects.MainResponse;
import com.example.civilizedtribes.remote.RetrofitInstance;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    List<Images> imagesArrayList = new ArrayList<>();
    IApiService iApiService;
    private static final String DEBUG_TAG = "MainActivity ";
    private ViewPager viewPager;
    private LinearLayout ll_dots;
    SliderPagerAdapter sliderPagerAdapter;
    private TextView[] dots;
    int imagePosition = 0;
    private static final int MY_CAMERA_REQUEST_CODE = 300;
    private static final int MY_STORAGE_PERMISSION = 400;

    LinearLayout ivParkingSlot, ivPassbook, ivComplaint, ivRequest, ivPhotoGallary, ivUpcomingEvent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        init();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA},
                        MY_CAMERA_REQUEST_CODE);
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_STORAGE_PERMISSION);
            }
        }
        iApiService = RetrofitInstance.getRetrofitInstance().create(IApiService.class);
        iApiService.getImageResponse().enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
                imagesArrayList = response.body().getImages();
                setImageAdapter();
                addBottomDots(0);
                setImageAnimation();
            }
            @Override
            public void onFailure(Call<MainResponse> call, Throwable t) {                                                   }
        }
        );

ivPhotoGallary.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this, GalleryActivity.class);
        startActivity(intent);
    }
});
        ivPassbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PassbookActivity.class);
                startActivity(intent);
            }
        });
    }

    private void init() {
        getSupportActionBar().hide();
        viewPager = (ViewPager) findViewById(R.id.vp_slider);
        ll_dots = (LinearLayout) findViewById(R.id.ll_dots);
        ivParkingSlot = findViewById(R.id.ivParkingSlot);
        ivComplaint = findViewById(R.id.ivComplaint);
        ivPassbook = findViewById(R.id.ivPassBook);
        ivPhotoGallary = findViewById(R.id.ivPhotoGallary);
        ivRequest = findViewById(R.id.ivRequest);
        ivUpcomingEvent = findViewById(R.id.ivUpcomingEvents);
    }

    private void setImageAnimation() {
        final Handler handler = new Handler();
        final Runnable update = new Runnable() {
            public void run() {
                if (imagePosition == imagesArrayList.size()) {
                    imagePosition = 0;
                } else {
                    imagePosition = imagePosition + 1;
                }
                viewPager.setCurrentItem(imagePosition, true);
            }
        };

        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                handler.post(update);
            }
        }, 100, 5000);
    }

    private void setImageAdapter() {
        sliderPagerAdapter = new SliderPagerAdapter(MainActivity.this, new ArrayList<Images>(imagesArrayList));
        viewPager.setAdapter(sliderPagerAdapter);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                addBottomDots(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[imagesArrayList.size()];

        ll_dots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(Color.parseColor("#000000"));
            ll_dots.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(Color.parseColor("#FFFFFF"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivPhotoGallary:

                break;
        }
    }

    @Override

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_CAMERA_REQUEST_CODE) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();

            } else {

                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();

            }

        }

        if (requestCode == MY_STORAGE_PERMISSION) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(this, "storage permission granted", Toast.LENGTH_LONG).show();

            } else {

                Toast.makeText(this, "storage permission denied", Toast.LENGTH_LONG).show();

            }

        }
    }
}