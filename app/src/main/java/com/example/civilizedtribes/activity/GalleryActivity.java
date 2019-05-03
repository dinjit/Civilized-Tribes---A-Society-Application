package com.example.civilizedtribes.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;


import com.example.civilizedtribes.R;
import com.example.civilizedtribes.adapter.GalleryImageAdapter;
import com.example.civilizedtribes.datamodel.DatabaseClient;
import com.example.civilizedtribes.datamodel.entity.PhotoGallery;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GalleryActivity extends AppCompatActivity {
    TextView uploadImage, captureImage;
    String imageFileName;
    GridView gallery;
    List<PhotoGallery> photoGalleryList= new ArrayList<>();
    File file;
    private int CAMERA_PIC_REQUEST = 1001;
    GalleryImageAdapter galleryImageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        getSupportActionBar().hide();
        uploadImage= findViewById(R.id.uploadImage);
        captureImage = findViewById(R.id.captureImage);
         gallery = (GridView) findViewById(R.id.gallery);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());



        captureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                imageFileName = String.valueOf(System.currentTimeMillis())+".png";
                File f = new File(Environment.getExternalStorageDirectory(), imageFileName);
                camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                startActivityForResult(camera_intent, CAMERA_PIC_REQUEST);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode== Activity.RESULT_OK){
            file = new File(Environment.getExternalStorageDirectory().toString());
            for (File temp : file.listFiles()) {
                if (temp.getName().equals(imageFileName)) {
                    file = temp;

                    saveImageData();
                    FetchImageList fetchImageList = new FetchImageList();
                    fetchImageList.execute();
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        FetchImageList fetchImageList = new FetchImageList();
        fetchImageList.execute();

    }

    class FetchImageList extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            photoGalleryList = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                    .photoGalleryDao()
                    .getAllImages();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            galleryImageAdapter = new GalleryImageAdapter(GalleryActivity.this,photoGalleryList);
            gallery.setAdapter(galleryImageAdapter);

        }
    }
    void saveImageData(){
        new Thread(() -> {
            PhotoGallery photoGallery = new PhotoGallery();
            photoGallery.imagePath = file.getAbsolutePath();
            photoGallery.imageName = imageFileName;
            //adding to database
            DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                    .photoGalleryDao()
                    .insert(photoGallery);

        }).start();
    }
}
