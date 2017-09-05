package com.ventruxinformatics.prodcast;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import businessObjects.domain.OrderEntry;

/**
 * Created by nandhini on 01/09/17.
 */

public class BillDetailsListAdapter extends BaseAdapter {

    List<OrderEntry> orderDetails;



    Context context;
    public static LayoutInflater inflater = null;

    public BillDetailsListAdapter(BillDetailsActivity billdetailsActivity, List<OrderEntry> orders) {
        orderDetails = orders;
        context = billdetailsActivity;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return orderDetails.size();
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
        TextView productName;
        TextView qty;
        TextView salesTax;
        TextView otherTax;
        TextView subTotal;
        TextView price;
    }

    @Override
    public View getView(final int position, View convertview, ViewGroup viewGroup) {
        BillDetailsListAdapter.Holder holder = new BillDetailsListAdapter.Holder();
        if (convertview == null) {
            convertview = inflater.inflate(R.layout.activity_bill_details_list_adapter, null);
            holder.productName=(TextView) convertview.findViewById(R.id.productName);
            holder.qty=(TextView) convertview.findViewById(R.id.qty);
            holder.price=(TextView) convertview.findViewById(R.id.price);
            holder.salesTax=(TextView) convertview.findViewById(R.id.salesTax);
            holder.otherTax=(TextView) convertview.findViewById(R.id.otherTax);
            holder.subTotal=(TextView) convertview.findViewById(R.id.subTotal);

            //order details
            holder.productName.setText(orderDetails.get(position).getProductName());
            holder.qty.setText(String.valueOf(orderDetails.get(position).getQuantity()));
            holder.price.setText(String.valueOf(orderDetails.get(position).getUnitPrice()));
            holder.salesTax.setText(String.valueOf(orderDetails.get(position).getSalesTax()));
            holder.otherTax.setText(String.valueOf(orderDetails.get(position).getOtherTax()));
            holder.subTotal.setText(String.valueOf(orderDetails.get(position).getSubtotal()));
        }
        return convertview;
    }
}


