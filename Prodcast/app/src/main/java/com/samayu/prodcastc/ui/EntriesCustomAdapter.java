package com.samayu.prodcastc.ui;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.samayu.prodcastc.R;
import com.samayu.prodcastc.businessObjects.GlobalUsage;
import com.samayu.prodcastc.businessObjects.SessionInfo;
import com.samayu.prodcastc.businessObjects.domain.OrderDetails;
import com.samayu.prodcastc.businessObjects.dto.OrderEntryDTO;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class EntriesCustomAdapter extends BaseAdapter {


    List<OrderDetails> orderEntries;
    List<OrderEntryDTO> orderEntryDto=new ArrayList<OrderEntryDTO>();
    int count=0;
    Context context;
    LayoutInflater inflater;

    ImageButton rmv;
    NumberFormat numberFormat= GlobalUsage.getNumberFormat();


    public EntriesCustomAdapter(EntryActivity mainActivity, List<OrderDetails> orderDetails){

        // TODO Auto-generated constructor stub
        orderEntries=orderDetails;


        context=mainActivity;
      //  System.out.println(context);

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
        TextView tv,tv1,tv2,optionValue,flavorValue;
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

            holder.tv = (TextView) convertView.findViewById(R.id.productName);
            holder.tv1 = (TextView) convertView.findViewById(R.id.unitPrice);
            holder.qty=(EditText) convertView.findViewById(R.id.orderQuantity);
            holder.optionValue=(TextView) convertView.findViewById(R.id.optionValue);
            holder.flavorValue=(TextView) convertView.findViewById(R.id.flavorValue);
            /*holder.tv2= (TextView) convertView.findViewById(R.id.subTotal);*/

            convertView.setTag(holder);
            //img.setTag(holder);

            final OrderDetails orders=orderEntries.get(position);



            holder.tv.setText(orders.getProduct().getProductName());
            if(orders.getProductOptions()!=null) {
                holder.optionValue.setText(orders.getProductOptions().getOptionValue());
            }
            if(orders.getProductFlavors()!=null) {
                holder.flavorValue.setText(orders.getProductFlavors().getFlavorValue());
            }
            float price=0;
            if(SessionInfo.getInstance().getEmployee().getCustomerType().equals("R")){
                price=orders.getProduct().getRetailPrice();
            }
            else{
                price=orders.getProduct().getUnitPrice();
            }
            holder.tv1.setText(numberFormat.format(price));

            holder.qty.setText(String.valueOf(orders.getQuantity()));
            /*holder.tv2.setText(GlobalUsage.getNumberFormat().format(ProductDetailFragment.calculateTotal(orders.getProduct(),Integer.parseInt(holder.qty.getText().toString()))));*/




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
                    /*holder.tv2.setText(GlobalUsage.getNumberFormat().format(ProductDetailFragment.calculateTotal(orders.getProduct(),newQuantity)));*/
                    ((EntryActivity)context).getValueForTotal(orderEntries);

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
               // SessionInfo.getInstance().setEntry(orderEntries);
            }
        });
*/
        //  orderEntries.set(position);



        return convertView;
    }

}
