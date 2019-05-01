package com.example.civilizedtribes.datamodel.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "TransactionDetails")
public class TransactionDetails {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    public  int id;
   public String CustomerName;
    public int cashReceived, cashPending;
    public  String paymentDate;
    public String paymentDateForDb;
}
