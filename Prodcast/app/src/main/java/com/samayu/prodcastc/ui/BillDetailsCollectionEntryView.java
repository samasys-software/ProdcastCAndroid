package com.samayu.prodcastc.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.text.NumberFormat;

import com.samayu.prodcastc.businessObjects.GlobalUsage;
import com.samayu.prodcastc.businessObjects.domain.Collection;
import com.ventruxinformatics.prodcast.R;

/**
 * Created by fgs on 9/13/2017.
 */

public class BillDetailsCollectionEntryView extends FrameLayout {
    private Collection collection;
    LayoutInflater inflater;
    NumberFormat numberFormat= GlobalUsage.getNumberFormat();
    public BillDetailsCollectionEntryView(Context context, Collection collection) {
        super(context);
        this.collection=collection;
        inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.billdetails_paymententryview,this);

    }
    public void setData(){
        ((TextView)findViewById(R.id.paymentDate)).setText(String.valueOf(collection.getPaymentDate()));
        ((TextView)findViewById(R.id.paymentReceiver)).setText(collection.getEmployeeName());
        ((TextView)findViewById(R.id.paymentAmount)).setText(numberFormat.format(collection.getAmountPaid()));

    }
}
