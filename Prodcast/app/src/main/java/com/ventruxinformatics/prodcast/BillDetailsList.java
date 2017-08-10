package com.ventruxinformatics.prodcast;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class BillDetailsList extends BaseAdapter {


        ArrayList<String> billno;
        ArrayList<String> orderStatus;
        ArrayList<String> billDate;
        ArrayList<String> totalAmount;
        ArrayList<String> outstandingBalance;
        Context context;
        //int [] imageId;           //prev. code
        ArrayList<Bitmap> imageId;
        private static LayoutInflater inflater=null;
        //public CustomStoreList(StoreActivity mainActivity,String[] prgmNameList, int[] prgmImages) {           //prev. code
        public BillDetailsList(BillActivity mainActivity,ArrayList<String> billNumber, ArrayList<String> status, ArrayList<String> billDates, ArrayList<String> total,ArrayList<String> balance) {
            // TODO Auto-generated constructor stub
            billno=billNumber;
            orderStatus=status;
            billDate=billDates;
            totalAmount=total;
            outstandingBalance=balance;
            context=mainActivity;

            inflater = ( LayoutInflater )context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            //return result.length;             //prev. code
            return billno.size();
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
            // TODO Auto-generated method stub
            com.ventruxinformatics.prodcast.BillDetailsList.Holder holder=new com.ventruxinformatics.prodcast.BillDetailsList.Holder();
            View rowView;
            rowView = inflater.inflate(R.layout.activity_bill_details_list, null);
            holder.tv=(TextView) rowView.findViewById(R.id.billNo);
            holder.tv1=(TextView) rowView.findViewById(R.id.status);
            holder.tv2=(TextView) rowView.findViewById(R.id.billDate);
            holder.tv3=(TextView) rowView.findViewById(R.id.total);
            holder.tv4=(TextView) rowView.findViewById(R.id.outstandingBalance);
            //holder.img=(ImageView) rowView.findViewById(R.id.companyLogo);
            //  holder.tv.setText(result[position]);                  //prev. code
            holder.tv.setText(billno.get(position));
            holder.tv1.setText(orderStatus.get(position));
            holder.tv2.setText(billDate.get(position));
            holder.tv3.setText(totalAmount.get(position));
            holder.tv4.setText(outstandingBalance.get(position));
            //  holder.img.setImageResource(imageId[position]);            //prev. code
          //  holder.img.setImageBitmap(imageId.get(position));


            //prev. code
      /*  if (position % 2 == 0) {
            rowView.setBackgroundColor(Color.parseColor("#eae9e9"));
        } else {
            rowView.setBackgroundColor(Color.parseColor("#ffffff"));
        }*/
        /*rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(context, "You Clicked "+result[position], Toast.LENGTH_LONG).show();
            }
        });*/
            return rowView;
        }
    }


   /* @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_details_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }*/


