package com.example.civilizedtribes.activity;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.civilizedtribes.datamodel.DatabaseClient;
import com.example.civilizedtribes.R;
import com.example.civilizedtribes.adapter.PaymentListAdapter;
import com.example.civilizedtribes.datamodel.entity.TransactionDetails;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TenantDetailActivity extends AppCompatActivity {
    ListView payementDetailList;
    List<TransactionDetails> transactionDetailsArrayList = new ArrayList<>();
    String CustomerName = "No Records Found";
    int pendingAmountForCustomer, totalReceivedAmountForCustomer;
    EditText settlementAmount;
    TextView tvCustomerName, customerAmountPending, customerAmountReceived, settlePayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tenant_detail);
        getSupportActionBar().hide();
        payementDetailList = findViewById(R.id.payementDetailList);
        customerAmountPending = findViewById(R.id.customerAmountPending);
        customerAmountReceived = findViewById(R.id.customerAmountReceived);
        settlePayment = findViewById(R.id.settlePayment);
        tvCustomerName = findViewById(R.id.CustomerName);
        try {
            CustomerName = getIntent().getStringExtra("CustomerName");
        } catch (Exception e) {
            CustomerName = "No Records Found";
            e.printStackTrace();
        }
        tvCustomerName.setText("" + CustomerName);
        LoadList st = new LoadList();
        st.execute();

        settlePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(TenantDetailActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.settlement_layout);
                settlementAmount = dialog.findViewById(R.id.settlementAmount);
                TextView tvPendingAmount = dialog.findViewById(R.id.tvPendingAmount);
                tvPendingAmount.setText("Pending Amount :"+pendingAmountForCustomer);
                settlementAmount.setText(""+pendingAmountForCustomer);
                TextView btnSaveSettlement = dialog.findViewById(R.id.btnSaveSettlement);
                btnSaveSettlement.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SettlementTransaction settlementTransaction= new SettlementTransaction();
                        settlementTransaction.execute();
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }


    class LoadList extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {


            //adding to database
            transactionDetailsArrayList = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                    .transactionDetailsDao()
                    .getTransactionByCustomer(CustomerName);
            pendingAmountForCustomer = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                    .transactionDetailsDao()
                    .getPendingAmountForCustomer(CustomerName);
            totalReceivedAmountForCustomer = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                    .transactionDetailsDao()
                    .gettotalReceivedAmountForCustomer(CustomerName);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            customerAmountReceived.setText("" + totalReceivedAmountForCustomer);
            customerAmountPending.setText("" + pendingAmountForCustomer);
            PaymentListAdapter paymentListAdapter = new PaymentListAdapter(TenantDetailActivity.this, transactionDetailsArrayList, 1);
            payementDetailList.setAdapter(paymentListAdapter);
            super.onPostExecute(aVoid);

        }
    }

    class SettlementTransaction extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            TransactionDetails transactionDetails = new TransactionDetails();
            Date currentTime = Calendar.getInstance().getTime();
            String dateTimeFormat = String.valueOf(DateFormat.format("dd/MM/yyyy hh:mm a", currentTime));
            String dateFormat = String.valueOf(DateFormat.format("dd/MM/yyyy", currentTime));
            transactionDetails.paymentDate = dateTimeFormat;
            transactionDetails.paymentDateForDb = dateFormat;
            transactionDetails.CustomerName= CustomerName;
            transactionDetails.cashReceived = 0;
            transactionDetails.cashPending = Integer.parseInt("-"+settlementAmount.getText().toString());
            //adding to database
            DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                    .transactionDetailsDao()
                    .insert(transactionDetails);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(TenantDetailActivity.this, "Transaction Successful", Toast.LENGTH_SHORT).show();
            super.onPostExecute(aVoid);
            LoadList st = new LoadList();
            st.execute();
        }
    }


}
