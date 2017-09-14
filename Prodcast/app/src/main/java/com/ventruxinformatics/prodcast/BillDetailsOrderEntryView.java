package com.ventruxinformatics.prodcast;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.text.NumberFormat;

import businessObjects.GlobalUsage;
import businessObjects.domain.OrderEntry;

/**
 * Created by fgs on 9/13/2017.
 */

public class BillDetailsOrderEntryView extends FrameLayout {
    private OrderEntry orderDetails;
    LayoutInflater inflater;
    NumberFormat numberFormat= GlobalUsage.getNumberFormat();

    public BillDetailsOrderEntryView(Context context, OrderEntry orders) {
        super(context);
        this.orderDetails=orders;
        inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.billdetails_orderentryview,this);
    }
    public void setData(){
        ((TextView)findViewById(R.id.productName)).setText(String.valueOf( orderDetails.getProductName()));
        ((TextView)findViewById(R.id.qty)).setText(String.valueOf(orderDetails.getQuantity()));
        ((TextView)findViewById(R.id.price)).setText(numberFormat.format(orderDetails.getUnitPrice()));
        ((TextView)findViewById(R.id.subTotal)).setText(numberFormat.format(orderDetails.getSubtotal()));


    }
}
