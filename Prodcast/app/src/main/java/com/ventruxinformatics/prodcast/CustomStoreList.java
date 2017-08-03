package com.ventruxinformatics.prodcast;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;

/**
 * Created by AndroidUser on 12/13/2016.
 */

public class CustomStoreList extends BaseAdapter {

   // String [] result;             //prev. code
    ArrayList<String> result;
    ArrayList<String> address;
    Context context;
    //int [] imageId;           //prev. code
    ArrayList<Bitmap> imageId;
    private static LayoutInflater inflater=null;
   //public CustomStoreList(StoreActivity mainActivity,String[] prgmNameList, int[] prgmImages) {           //prev. code
   public CustomStoreList(StoreActivity mainActivity,ArrayList<String> prgmNameList, ArrayList<String> prgmAddressList,ArrayList<Bitmap> prgmImages) {
       // TODO Auto-generated constructor stub
        result=prgmNameList;
       address=prgmAddressList;
        context=mainActivity;
        imageId=prgmImages;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        //return result.length;             //prev. code
        return result.size();
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
        ImageView img;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        CustomStoreList.Holder holder=new CustomStoreList.Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.store_list_items, null);
        holder.tv=(TextView) rowView.findViewById(R.id.companyDetails);
        holder.tv1=(TextView) rowView.findViewById(R.id.companyAddress);
        holder.img=(ImageView) rowView.findViewById(R.id.companyLogo);
      //  holder.tv.setText(result[position]);                  //prev. code
      holder.tv.setText(result.get(position));
      holder.tv1.setText(address.get(position));
      //  holder.img.setImageResource(imageId[position]);            //prev. code
      holder.img.setImageBitmap(imageId.get(position));


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
