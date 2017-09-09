package com.ventruxinformatics.prodcast;

import android.view.View;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.List;

import businessObjects.GlobalUsage;
import businessObjects.domain.OrderEntry;

/**
 * Created by nandhini on 01/09/17.
 */

public class BillDetailsListAdapter extends BaseAdapter {

    List<OrderEntry> orderDetails;



    Context context;
    public static LayoutInflater inflater = null;
    NumberFormat numberFormat= GlobalUsage.getNumberFormat();
    //String currencySymbol;

    public BillDetailsListAdapter(BillDetailsActivity billdetailsActivity, List<OrderEntry> orders) {
        orderDetails = orders;
        context = billdetailsActivity;
        //currencySymbol= SessionInformations.getInstance().getEmployee().getDistributor().getCurrencySymbol();
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
       TextView productName,qty,salesTax,otherTax,subTotal,price;
    }

    @Override
    public View getView(final int position, View convertview, ViewGroup viewGroup) {
        BillDetailsListAdapter.Holder holder = new BillDetailsListAdapter.Holder();
        if (convertview == null) {
            convertview = inflater.inflate(R.layout.activity_bill_details_list_adapter, null);
            holder.productName=(TextView) convertview.findViewById(R.id.productName);
            holder.qty=(TextView) convertview.findViewById(R.id.qty);
            holder.price=(TextView) convertview.findViewById(R.id.price);
            //holder.salesTax=(TextView) convertview.findViewById(R.id.salesTax);
            //holder.otherTax=(TextView) convertview.findViewById(R.id.otherTax);
            holder.subTotal=(TextView) convertview.findViewById(R.id.subTotal);

            //order details
            holder.productName.setText(orderDetails.get(position).getProductName());
            holder.qty.setText(String.valueOf(orderDetails.get(position).getQuantity()));

            holder.price.setText(numberFormat.format(orderDetails.get(position).getUnitPrice()));
            //holder.salesTax.setText(String.valueOf(orderDetails.get(position).getSalesTax()));
            //holder.otherTax.setText(String.valueOf(orderDetails.get(position).getOtherTax()));
            holder.subTotal.setText(numberFormat.format(orderDetails.get(position).getSubtotal()));
        }
        return convertview;
    }
}


