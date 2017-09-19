package com.samayu.prodcastc.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.samayu.prodcastc.BuildConfig;
import com.squareup.picasso.Picasso;

import java.util.List;

import com.samayu.prodcastc.businessObjects.domain.Distributor;
import com.samayu.prodcastc.R;

/**
 * Created by AndroidUser on 12/13/2016.
 */

public class CustomStoreAdapter extends BaseAdapter {

    List<Distributor> result;
      Context context;
     private static LayoutInflater inflater=null;
   public CustomStoreAdapter(StoreActivity mainActivity, List<Distributor> distributors) {
       result=distributors;
       context=mainActivity;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
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
        TextView tv,tv1;
        ImageView img;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        CustomStoreAdapter.Holder holder=new CustomStoreAdapter.Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.store_list_items, null);
        holder.tv=(TextView) rowView.findViewById(R.id.companyDetails);
        holder.tv1=(TextView) rowView.findViewById(R.id.companyAddress);
        holder.img=(ImageView) rowView.findViewById(R.id.companyLogo);
        holder.tv.setText(result.get(position).getCompanyName());
        holder.tv1.setText(result.get(position).getAddress1()+" "+result.get(position).getAddress2()+" "+result.get(position).getAddress3());
        Picasso.with(context).load(BuildConfig.SERVER_URL+"/prodcastcweb/images/" +result.get(position).getLogo()).into(holder.img);
        return rowView;
    }
}
