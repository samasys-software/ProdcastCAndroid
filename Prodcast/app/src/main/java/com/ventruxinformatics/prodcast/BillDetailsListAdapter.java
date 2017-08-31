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

import businessObjects.domain.OrderEntry;

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
        TextView tv,tv1,tv2,tv3,tv4,tv5,tv6,tv7;

        TextView orderNo,billDate,salesRep,total,balance,discount;

        TextView discountType;
        TextView productName;
        TextView qty;
        TextView salesTax;
        TextView otherTax;
        TextView subTotal;
        TextView price;
        TextView paymentDate;
        TextView paymentReceiver;
        TextView paymentAmount;


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
