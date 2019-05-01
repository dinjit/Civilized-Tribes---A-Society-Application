package com.example.civilizedtribes.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.civilizedtribes.R;
import com.example.civilizedtribes.datamodel.entity.TransactionDetails;

import java.util.ArrayList;
import java.util.List;

public class PaymentListAdapter extends BaseAdapter {
    Context context;
    int isFrom;
    ArrayList <TransactionDetails> transactionDetailsArrayList= new ArrayList<>();
    public PaymentListAdapter(Context context , List<TransactionDetails> transactionDetailsArrayList, int isFrom){
        this.context= context;
        this.transactionDetailsArrayList = (ArrayList<TransactionDetails>) transactionDetailsArrayList;
        this.isFrom= isFrom;

    }
    @Override
    public int getCount() {
        return transactionDetailsArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    private class ViewHolder {
        TextView PaymentDate;
        TextView cashReceived;
        TextView cashPending;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.transaction_list_item, null);
            holder = new ViewHolder();
            holder.PaymentDate = (TextView) convertView.findViewById(R.id.paymentDate);
            holder.cashReceived = (TextView) convertView.findViewById(R.id.cashReceived);
            holder.cashPending = (TextView) convertView.findViewById(R.id.cashPending);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        TransactionDetails transactionDetails = transactionDetailsArrayList.get(position);
        if(isFrom==1){
            holder.PaymentDate.setText(transactionDetails.paymentDate);
        }else{
            holder.PaymentDate.setText(transactionDetails.CustomerName);
        }
        try {
            holder.cashPending.setText(""+transactionDetails.cashPending);
        } catch (Exception e) {
            holder.cashPending.setText("0");
            e.printStackTrace();
        }
        try {
            holder.cashReceived.setText(""+transactionDetails.cashReceived);
        } catch (Exception e) {
            holder.cashReceived.setText("0");
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
        if (position % 2 == 0) {
            convertView.setBackgroundColor(context.getResources().getColor(R.color.color_white_gray));
        } else {
            convertView.setBackgroundColor(context.getResources().getColor(R.color.color_very_light_gray));
            }
        }

        return convertView;
    }
}
