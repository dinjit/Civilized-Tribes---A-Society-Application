package com.example.civilizedtribes.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.civilizedtribes.datamodel.DatabaseClient;
import com.example.civilizedtribes.R;
import com.example.civilizedtribes.datamodel.entity.TransactionDetails;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PassbookActivity extends AppCompatActivity {
    LinearLayout newTransactionLayout, settingLayout;
    Dialog dialog;
    AutoCompleteTextView CustomerName ;
    EditText cashPending,cashRecieved;
    List<TransactionDetails> transactionDetailsArrayList = new ArrayList<>();
    AutoCompleteTextView etsearchCustomer;
    ArrayList<String> AllCustomerNameArrayList = new ArrayList<>();
    ImageView ivSearchCustomer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passbook);
        getSupportActionBar().hide();
        newTransactionLayout = findViewById(R.id.newTransactionLayout);
        etsearchCustomer = findViewById(R.id.etSearchCustomer);
        settingLayout = findViewById(R.id.settingLayout);
        ivSearchCustomer = findViewById(R.id.ivSearchCustomer);
        ivSearchCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etsearchCustomer.getText().length() != 0) {
                    Intent intent = new Intent(PassbookActivity.this, TenantDetailActivity.class);
                    String str = etsearchCustomer.getText().toString();
                    String CustomerName = str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
                    intent.putExtra("CustomerName", CustomerName);
                    startActivity(intent);
                } else {
                    Toast.makeText(PassbookActivity.this, "Enter Customer Name", Toast.LENGTH_SHORT).show();
                }
            }
        });
        settingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PassbookActivity.this,AllTransactionListActivity.class);
                startActivity(intent);
            }
        });
        newTransactionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, NewTransactionActivity.class);
//                startActivity(intent);
                dialog = new Dialog(PassbookActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.activity_new_transaction);
                CustomerName = dialog.findViewById(R.id.CustomerName);
                cashPending = dialog.findViewById(R.id.cashPending);
                cashRecieved = dialog.findViewById(R.id.cashReceived);
                etsearchCustomer = findViewById(R.id.etSearchCustomer);
                SetCustomerNameDailogAuto autoCompleteList = new SetCustomerNameDailogAuto();
                autoCompleteList.execute();
                TextView btnSaveTransaction = dialog.findViewById(R.id.btnSaveTransaction);


                btnSaveTransaction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(CustomerName.getText().toString().length()!=0) {


                            TransactionDetails  transactionDetails = new TransactionDetails();
                            Date currentTime = Calendar.getInstance().getTime();
                            String dateTimeFormat = String.valueOf(DateFormat.format("dd/MM/yyyy hh:mm a", currentTime));
                            String dateFormat = String.valueOf(DateFormat.format("dd/MM/yyyy", currentTime));
                            transactionDetails.paymentDate = dateTimeFormat;
                            transactionDetails.paymentDateForDb = dateFormat;
                            if (cashPending.getText().toString().length() != 0) {

                                try {
                                    transactionDetails.cashPending = Integer.parseInt(cashPending.getText().toString());
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                transactionDetails.cashPending = 0;
                            }
                            if (cashRecieved.getText().toString().length() != 0) {

                                try {
                                    transactionDetails.cashReceived = Integer.parseInt(cashRecieved.getText().toString());
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                transactionDetails.cashReceived = 0;
                            }
                            String str = CustomerName.getText().toString();
                            String CustomerName = str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
                            transactionDetails.CustomerName = CustomerName;

                            AddTransaction addTransaction = new AddTransaction(transactionDetails);
                            addTransaction.execute();
                        } else{
                            Toast.makeText(PassbookActivity.this, "Enter Tenant Name", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                dialog.show();
            }
        });
    }


    class SetAutoCompleteList extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            Date currentTime = Calendar.getInstance().getTime();
            transactionDetailsArrayList = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                    .transactionDetailsDao()
                    .getAllTransactioList();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            for (int i = 0; i < transactionDetailsArrayList.size(); i++) {
                if (!AllCustomerNameArrayList.contains(transactionDetailsArrayList.get(i).CustomerName)) {
                    AllCustomerNameArrayList.add(transactionDetailsArrayList.get(i).CustomerName);
                }
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(PassbookActivity.this, R.layout.auto_suggestion_list_item, R.id.tvSuggestionText, AllCustomerNameArrayList);
            etsearchCustomer.setThreshold(1);
            etsearchCustomer.setAdapter(adapter);
            super.onPostExecute(aVoid);

        }

    }

    class AddTransaction extends AsyncTask<TransactionDetails, Void, Void> {
        TransactionDetails transactionDetails;
        public AddTransaction(TransactionDetails transactionDetails) {
            this.transactionDetails=transactionDetails;
        }

        @Override
        protected Void doInBackground(TransactionDetails... voids) {
            DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                    .transactionDetailsDao()
                    .insert(transactionDetails);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(PassbookActivity.this, "Transaction Successfully", Toast.LENGTH_SHORT).show();
            super.onPostExecute(aVoid);
            dialog.dismiss();
            SetAutoCompleteList autoCompleteList = new SetAutoCompleteList();
            autoCompleteList.execute();
        }
    }


    class SetCustomerNameDailogAuto extends AsyncTask<Void, Void, Void> {
        List<TransactionDetails> allCustomerNameList = new ArrayList<>();
        ArrayList<String> dialiogCustomerNameList = new ArrayList<>();
        @Override
        protected Void doInBackground(Void... voids) {
            allCustomerNameList = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                    .transactionDetailsDao()
                    .getAllTransactioList();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            for (int i = 0; i < allCustomerNameList.size(); i++) {
                if (!dialiogCustomerNameList.contains(allCustomerNameList.get(i).CustomerName)) {
                    dialiogCustomerNameList.add(allCustomerNameList.get(i).CustomerName);
                }
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(PassbookActivity.this, R.layout.auto_suggestion_list_item, R.id.tvSuggestionText, dialiogCustomerNameList);
            CustomerName.setThreshold(1);
            CustomerName.setAdapter(adapter);
            super.onPostExecute(aVoid);
        }

    }
}
