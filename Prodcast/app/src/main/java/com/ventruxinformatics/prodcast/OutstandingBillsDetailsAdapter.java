package com.ventruxinformatics.prodcast;

import android.content.Context;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import businessObjects.domain.Bill;

public class OutstandingBillsDetailsAdapter extends BaseAdapter {


        List<Bill> outstandingBills;
        Context context;
        OutstandingBillsActivity activity;
        private static LayoutInflater inflater=null;
        public OutstandingBillsDetailsAdapter(OutstandingBillsActivity mainActivity, List<Bill> bills){

            // TODO Auto-generated constructor stub
            outstandingBills=bills;
            activity=mainActivity;
            context=mainActivity;

                    inflater = ( LayoutInflater )context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public int getCount() {
                // TODO Auto-generated method stub
                //return result.length;             //prev. code
                return outstandingBills.size();
        }

        @Override
        public Object getItem(int position) {
                // TODO Auto-generated method stub
                return position;
                }

        @Override
        public long getItemId(int position) {
                // TODO Auto-generated method stub
                return position;
                }

        public class Holder
        {
            TextView tv;
            TextView tv1;
            TextView tv2;
            TextView tv3;
            TextView tv4;

        }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        String status;
        // TODO Auto-generated method stub
        OutstandingBillsDetailsAdapter.Holder holder=new OutstandingBillsDetailsAdapter.Holder();
        View rowView;
        if(convertView==null)

            convertView = inflater.inflate(R.layout.activity_bill_details_list, null);
        holder.tv=(TextView) convertView.findViewById(R.id.billNo);
        holder.tv1=(TextView) convertView.findViewById(R.id.status);
        holder.tv2=(TextView) convertView.findViewById(R.id.billDate);
        holder.tv3=(TextView) convertView.findViewById(R.id.total);
        holder.tv4=(TextView) convertView.findViewById(R.id.outstandingBalance);
        //holder.img=(ImageView) rowView.findViewById(R.id.companyLogo);
        //  holder.tv.setText(result[position]);                  //prev. code
        holder.tv.setText(String.valueOf(outstandingBills.get(position).getBillNumber()));
        status="READY";
        if(outstandingBills.get(position).getOrderStatus().equals(("S")))
        {
            status="NEW";
        }
        holder.tv1.setText(status);
        holder.tv2.setText(String.valueOf(outstandingBills.get(position).getBillDate()));
        holder.tv3.setText(String.valueOf(outstandingBills.get(position).getBillAmount()));
        holder.tv4.setText(String.valueOf(outstandingBills.get(position).getOutstandingBalance()));





        return convertView;
    }
}
