package com.ventruxinformatics.prodcast;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import businessObjects.domain.Collection;

public class PaymentDetailsListAdapter extends BaseAdapter {
    List<Collection> collections;



    Context context;
    public static LayoutInflater inflater = null;

    public PaymentDetailsListAdapter(BillDetailsActivity billdetailsActivity, List<Collection> orders) {
        collections = orders;
        context = billdetailsActivity;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return collections.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder {

        TextView paymentDate;
        TextView paymentReceiver;
        TextView paymentAmount;


    }

    @Override
    public View getView(final int position, View convertview, ViewGroup viewGroup) {
        PaymentDetailsListAdapter.Holder holder = new PaymentDetailsListAdapter.Holder();
        if (convertview == null) {
            convertview = inflater.inflate(R.layout.activity_payment_details_list_adapter, null);
            holder.paymentDate=(TextView) convertview.findViewById(R.id.paymentDate);
            holder.paymentReceiver=(TextView) convertview.findViewById(R.id.paymentReceiver);
            holder.paymentAmount=(TextView) convertview.findViewById(R.id.paymentAmount);
            holder.paymentDate.setText(String.valueOf(collections.get(position).getPaymentDate()));
            holder.paymentReceiver.setText(collections.get(position).getEmployeeName());
            holder.paymentAmount.setText(String.valueOf(collections.get(position).getAmountPaid()));


        }
        return convertview;
    }


}
