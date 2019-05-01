package com.example.civilizedtribes.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.civilizedtribes.R;
import com.example.civilizedtribes.adapter.GalleryImageAdapter;
import com.example.civilizedtribes.datamodel.DatabaseClient;
import com.example.civilizedtribes.datamodel.entity.PhotoGallery;


import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class GalleryActivity extends AppCompatActivity {
    TextView uploadImage, captureImage;
    File storeImageFile;
    public static final int CAPTURE_IMAGE = 100;
    int i=0;
    GridView gallery;
    List<PhotoGallery> photoGalleryList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        getSupportActionBar().hide();
        uploadImage= findViewById(R.id.uploadImage);
        captureImage = findViewById(R.id.captureImage);
         gallery = (GridView) findViewById(R.id.gallery);

        captureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i++;
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    File photoFile = null;
                    photoFile = storeImage();
                    if (photoFile != null) {
                        Uri photoURI = Uri.fromFile(photoFile);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                        startActivityForResult(takePictureIntent, CAPTURE_IMAGE);
                    }
                }
            }
        });
    }

    private File storeImage() {
        storeImageFile = null;
        storeImageFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),
                i+ ".jpg");


        if (storeImageFile.exists()) {
            storeImageFile.delete();
        }
        try {
            storeImageFile.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(storeImageFile);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return storeImageFile;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE) {
            if (resultCode == RESULT_OK) {
                UploadImageData uploadData = new UploadImageData();
                uploadData.execute();
                //  saveImageDataToDatabase();
            }
        }
    }

    class UploadImageData extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            PhotoGallery photoGallery = new PhotoGallery();
            photoGallery.imagePath=""+Environment.getExternalStorageDirectory().getAbsolutePath()+i+ ".jpg";
            photoGallery.imageName=""+i;

            //adding to database
            DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                    .photoGalleryDao()
                    .insert(photoGallery);
            photoGalleryList =  DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                    .photoGalleryDao()
                    .getAllImages();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(GalleryActivity.this, "Added Successsfully", Toast.LENGTH_SHORT).show();
            super.onPostExecute(aVoid);

            GalleryImageAdapter galleryImageAdapter = new GalleryImageAdapter(GalleryActivity.this,new ArrayList<PhotoGallery>(photoGalleryList));
       gallery.setAdapter(galleryImageAdapter);
        }
    }
}
