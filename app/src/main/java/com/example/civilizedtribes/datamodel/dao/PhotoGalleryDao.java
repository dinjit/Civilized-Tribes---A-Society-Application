package com.example.civilizedtribes.datamodel.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.civilizedtribes.datamodel.entity.PhotoGallery;

import java.util.List;
@Dao
public interface PhotoGalleryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(PhotoGallery obj);

    @Query("Select * from photogallery")
    List<PhotoGallery> getAllImages();
}
