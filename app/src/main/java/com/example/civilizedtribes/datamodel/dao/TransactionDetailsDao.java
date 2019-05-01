package com.example.civilizedtribes.datamodel.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.civilizedtribes.datamodel.entity.TransactionDetails;

import java.util.List;

@Dao
public interface TransactionDetailsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(TransactionDetails obj);

    @Query("Select * from transactiondetails")
    List<TransactionDetails> getAllTransactioList();
//    @Query("Select * from transactiondetails where CustomerName=:CustomerName")
//    List<TransactionDetails> getTransactionByCustomer(String CustomerName);
@Query("Select * from transactiondetails where CustomerName=:CustomerName")
List<TransactionDetails> getTransactionByCustomer(String CustomerName);

    @Query("Select SUM(cashReceived) from transactiondetails where CustomerName=:customerName")
    int  gettotalReceivedAmountForCustomer(String customerName);

    @Query("Select SUM(cashPending) from transactiondetails where CustomerName=:customerName")
    int getPendingAmountForCustomer(String customerName);


    @Query("Select * from transactiondetails  Where paymentDateForDb=:todayDate ORDER BY id DESC")
    List<TransactionDetails> getAllTransactioListDesc(String todayDate);
}
