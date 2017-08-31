package com.ventruxinformatics.prodcast;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import businessObjects.SessionInformations;
import businessObjects.domain.Bill;
import businessObjects.domain.OrderDetails;
import businessObjects.domain.Product;
import businessObjects.dto.OrderEntryDTO;

public class EntriesCustomAdapter extends BaseAdapter {


    List<OrderDetails> orderEntries;
    List<OrderEntryDTO> orderEntryDto=new ArrayList<OrderEntryDTO>();
    int count=0;
    Context context;
    LayoutInflater inflater;
    EditText qty;
    TextView subTotal,productName,unitPrice;
    ImageButton rmv;

    public EntriesCustomAdapter(EntryActivity mainActivity, List<OrderDetails> orderDetails){

        // TODO Auto-generated constructor stub
        orderEntries=orderDetails;


        context=mainActivity;
        System.out.println(context);

        inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return orderEntries.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return  position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public  class Holder
    {
        TextView tv,tv1,tv2;
        ImageButton img;
        EditText qty;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
          final Holder holder;
         if (convertView == null) {
            holder=new Holder();
            convertView = inflater.inflate(R.layout.entry_list, null);
            holder.img= (ImageButton) convertView.findViewById(R.id.removebtn);
            holder.tv = (TextView) convertView.findViewById(R.id.productName);
            holder.tv1 = (TextView) convertView.findViewById(R.id.unitPrice);
            holder.qty=(EditText) convertView.findViewById(R.id.orderQuantity);
            holder.tv2= (TextView) convertView.findViewById(R.id.subTotal);
            convertView.setTag(holder);
        }
        else{
            holder=(Holder) convertView.getTag();
        }

        final OrderDetails orders=orderEntries.get(position);
        holder.tv.setText(orders.getProduct().getProductName());
        holder.tv1.setText(String.valueOf(orders.getProduct().getUnitPrice()));
        holder.qty.setText(String.valueOf(orders.getQuantity()));
        holder.tv2.setText(ProductDetailFragment.calculateTotal(orders.getProduct(),Integer.parseInt(holder.qty.getText().toString())));

        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                orderEntries.remove(position);

                notifyDataSetChanged();
               // SessionInformations.getInstance().setEntry(orderEntries);


            }
        });


       /* holder.qty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

                      // holder.tv2.setText(ProductDetailFragment.calculateTotal(orders.getProduct(),Integer.parseInt(holder.qty.getText().toString())));
            }
            @Override
            public void onTextChanged(final CharSequence s, int start, int before,
                                      int count) {

               //holder.tv2.setText(ProductDetailFragment.calculateTotal(orders.getProduct(),Integer.parseInt(holder.qty.getText().toString())));

            }
            @Override
            public void afterTextChanged(final Editable s) {
                orderEntries.get(position).setQuantity(Integer.parseInt(holder.qty.getText().toString()));
                holder.tv2.setText(ProductDetailFragment.calculateTotal(orders.getProduct(),Integer.parseInt(holder.qty.getText().toString())));
                //notifyDataSetChanged();
               // SessionInformations.getInstance().setEntry(orderEntries);
            }
        });



*/
        //  orderEntries.set(position);



        return convertView;
    }
}
