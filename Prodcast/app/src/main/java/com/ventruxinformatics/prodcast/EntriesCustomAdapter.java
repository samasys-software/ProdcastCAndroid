package com.ventruxinformatics.prodcast;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
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

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import businessObjects.GlobalUsage;
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
    NumberFormat numberFormat= GlobalUsage.getNumberFormat();


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
        //if (convertView == null)
        {
            holder=new Holder();
            convertView = inflater.inflate(R.layout.entry_list, null);
            holder.img= (ImageButton) convertView.findViewById(R.id.removebtn);
            holder.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setTitle("Prodcast Notification");
                    alert.setMessage("This Order Has Been Deleted Permanently...Do You Want To Continue?");
                    alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    alert.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            //alert.show();

                            orderEntries.remove(position);


                           notifyDataSetChanged();//notify();
                            Toast.makeText(context,"Your Order has Been Deleted Successfully",Toast.LENGTH_LONG);
                            //SessionInformations.getInstance().setEntry(orderEntries);

                        }
                    });
                    alert.show();
                }
            });

            holder.tv = (TextView) convertView.findViewById(R.id.productName);
            holder.tv1 = (TextView) convertView.findViewById(R.id.unitPrice);
            holder.qty=(EditText) convertView.findViewById(R.id.orderQuantity);
            holder.tv2= (TextView) convertView.findViewById(R.id.subTotal);

            convertView.setTag(holder);
            //img.setTag(holder);

            final OrderDetails orders=orderEntries.get(position);
            holder.tv.setText(orders.getProduct().getProductName());
            holder.tv1.setText(numberFormat.format(orders.getProduct().getUnitPrice()));
            holder.qty.setText(String.valueOf(orders.getQuantity()));
            holder.tv2.setText(ProductDetailFragment.calculateTotal(orders.getProduct(),Integer.parseInt(holder.qty.getText().toString())));




            holder.qty.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    OrderDetails orders=orderEntries.get(position);
                    String newValue = s.toString();
                    if( newValue.trim().length()==0) newValue="0";
                    int newQuantity = Integer.parseInt(newValue);
                    orders.setQuantity( newQuantity );
                    holder.tv2.setText( String.valueOf( orders.getQuantity()*orders.getProduct().getUnitPrice()));

                }
            });
        }
        /*else{
            holder=(Holder) convertView.getTag();
        }*/



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
