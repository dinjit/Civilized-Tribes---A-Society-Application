package com.example.civilizedtribes.datamodel;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.civilizedtribes.datamodel.dao.PhotoGalleryDao;
import com.example.civilizedtribes.datamodel.dao.TransactionDetailsDao;
import com.example.civilizedtribes.datamodel.entity.PhotoGallery;
import com.example.civilizedtribes.datamodel.entity.TransactionDetails;

@Database(entities =  {PhotoGallery.class,  TransactionDetails.class}, version = 1,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public  abstract PhotoGalleryDao photoGalleryDao();
    public abstract TransactionDetailsDao transactionDetailsDao();

}
