package com.ventruxinformatics.prodcast;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.List;

import businessObjects.GlobalUsage;
import businessObjects.domain.Order;
import businessObjects.font_design.NewTextView;

public class CustomerReportAdaptor extends BaseAdapter {

    List<Order> customerReportList;
    Context context;
    //OrderHistroyActivity activity;
    private static LayoutInflater inflater=null;
    NumberFormat numberFormat= GlobalUsage.getNumberFormat();

    public CustomerReportAdaptor(OrderHistroyActivity orderHistroyActivity, List<Order> reports)
    {

        // TODO Auto-generated constructor stub
        customerReportList=reports;
        //activity=mainActivity;
        context=orderHistroyActivity;

        inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount()
    {
        // TODO Auto-generated method stub
        //return result.length;             //prev. code
        return customerReportList.size();
    }

    @Override
    public Object getItem(int position)
    {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position)
    {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        NewTextView tv,tv1,tv2,tv3;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        CustomerReportAdaptor.Holder holder=new CustomerReportAdaptor.Holder();

            convertView = inflater.inflate(R.layout.activity_customer_report_adaptor, null);
        holder.tv=(NewTextView) convertView.findViewById(R.id.cusBillNo);
        holder.tv1=(NewTextView) convertView.findViewById(R.id.cusBillDate);
        holder.tv2=(NewTextView) convertView.findViewById(R.id.cusTotal);
        holder.tv3=(NewTextView) convertView.findViewById(R.id.cusBalance);

        holder.tv.setText(String.valueOf(customerReportList.get(position).getBillNumber()));
        holder.tv1.setText(String.valueOf(customerReportList.get(position).getBillDate()));
        holder.tv2.setText(numberFormat.format(customerReportList.get(position).getTotalAmount()));
        holder.tv3.setText(numberFormat.format(customerReportList.get(position).getOutstandingBalance()));

        return convertView;
    }
}
