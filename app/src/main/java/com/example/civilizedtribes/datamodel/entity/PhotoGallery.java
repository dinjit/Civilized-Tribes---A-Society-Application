package com.example.civilizedtribes.datamodel.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.File;

@Entity(tableName = "PhotoGallery")
public class PhotoGallery {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String imagePath, imageName;
}
