package com.example.fgs.stockquotes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.fgs.stockquotes.bussinessobjects.ListData;

import java.util.ArrayList;

/**
 * Created by fgs on 8/23/2017.
 */

public class MyBaseAdapter extends BaseAdapter {
    ArrayList<ListData> myList=new ArrayList<ListData>();
    LayoutInflater inflater;
    Context context;
    public MyBaseAdapter(Context context,ArrayList<ListData> myList){
        this.myList=myList;
        this.context=context;
        inflater=LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return myList.size();
    }

    @Override
    public Object getItem(int position) {
        return myList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        MyViewHolder mViewHolder;
        if(convertView == null){
            convertView=inflater.inflate(R.layout.stockquotes_ticker, viewGroup, false);
            mViewHolder=new MyViewHolder();
            convertView.setTag(mViewHolder);

        }
        else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }

       mViewHolder.ticker= detail(convertView,R.id.ticker,myList.get(position).getStockSymbol());
       mViewHolder.quoteId=detail(convertView,R.id.quoteId, myList.get(position).getQuoteId());
        mViewHolder.date=detail(convertView,R.id.date, myList.get(position).getDate());
        mViewHolder.close=detail(convertView,R.id.close,myList.get(position).getClose());


   return convertView;


    }
                private TextView detail(View v,int resId,String text){
                    TextView tv=(TextView) v.findViewById(resId);
                    tv.setText(text);
                    return tv;
                 }
                    private  class MyViewHolder {
                         TextView ticker,date,close,quoteId;

                    }
}
