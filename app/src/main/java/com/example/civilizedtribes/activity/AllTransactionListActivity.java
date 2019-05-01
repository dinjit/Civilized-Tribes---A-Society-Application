package com.example.civilizedtribes.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.widget.ListView;

import com.example.civilizedtribes.datamodel.DatabaseClient;
import com.example.civilizedtribes.R;
import com.example.civilizedtribes.adapter.PaymentListAdapter;
import com.example.civilizedtribes.datamodel.entity.TransactionDetails;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AllTransactionListActivity extends AppCompatActivity {
ListView allTransactionDetailList;
    List<TransactionDetails> alltransactionDetailsArrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_transaction_list);
        allTransactionDetailList = findViewById(R.id.payementDetailList);
        AllTransactionAsynTask allTransactionAsynTask = new AllTransactionAsynTask();
        allTransactionAsynTask.execute();
    }

    class AllTransactionAsynTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            Date currentTime = Calendar.getInstance().getTime();
            String dateFormat = String.valueOf(DateFormat.format("dd/MM/yyyy", currentTime));
            alltransactionDetailsArrayList = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                    .transactionDetailsDao()
                    .getAllTransactioListDesc(dateFormat);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            PaymentListAdapter paymentListAdapter = new PaymentListAdapter(AllTransactionListActivity.this, alltransactionDetailsArrayList, 2);
            allTransactionDetailList.setAdapter(paymentListAdapter);


        }
    }
}
