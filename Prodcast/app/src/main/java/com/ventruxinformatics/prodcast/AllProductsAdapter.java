package com.ventruxinformatics.prodcast;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import businessObjects.GlobalUsage;
import businessObjects.SessionInformations;
import businessObjects.domain.Bill;
import businessObjects.domain.Product;

public class AllProductsAdapter extends BaseAdapter {


    List<Product> products;
    int count=0;
    Context context;
    LayoutInflater inflater;
    String currencySymbol;
    NumberFormat numberFormat= GlobalUsage.getNumberFormat();
    public AllProductsAdapter(Context mainActivity, List<Product> product){

        // TODO Auto-generated constructor stub
        products=product;

        currencySymbol=SessionInformations.getInstance().getEmployee().getDistributor().getCurrencySymbol();

     //   System.out.println(products.size());


        context=mainActivity;
       // System.out.println(context);

        inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        //return result.length;             //prev. code
       return products.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        //return products.get(position);
        return  position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        //return products.indexOf(getItem(position));
        return position;
    }

    public class Holder
    {
        //TextView tv;
        TextView tv1;
        TextView tv2;
        TextView tv3;
        //TextView tv4;
        int position;

    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=null;
        if (convertView == null) {
            holder=new Holder();
            convertView = inflater.inflate(R.layout.activity_all_products, parent,false);
            //holder.tv = (TextView) convertView.findViewById(R.id.id);
            holder.tv1 = (TextView) convertView.findViewById(R.id.productName);
            holder.tv2 = (TextView) convertView.findViewById(R.id.unitPrice);
            holder.tv3 = (TextView) convertView.findViewById(R.id.productDescription);
            //holder.tv4 = (TextView) convertView.findViewById(R.id.otherTax);
            convertView.setTag(holder);
        }
        else{
            holder=(Holder) convertView.getTag();
        }
        holder.position=position;
        Product product=products.get(holder.position);
        String productName=product.getProductName();
        if(!product.getSalesTax().equals("0")){
            productName=product.getProductName()+" *";

        }
        if(!product.getOtherTax().equals("0"))
        {
            productName=product.getProductName()+" *";
        }
        //holder.tv.setText(String.valueOf(product.getId()));
        float unitPrice=product.getUnitPrice();
        if(SessionInformations.getInstance().getEmployee().getCustomerType().equals("R")){
            unitPrice=product.getRetailPrice();

        }
        holder.tv1.setText(productName+" - "+currencySymbol+""+numberFormat.format(unitPrice));
        //holder.tv2.setText();
        holder.tv3.setText(String.valueOf(product.getProductDesc()));
       // holder.tv4.setText("Other Tax : "+String.valueOf(product.getOtherTax()));



        return convertView;
    }
}

