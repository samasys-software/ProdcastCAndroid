package com.ventruxinformatics.prodcast;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.List;

import businessObjects.SessionInformations;
import businessObjects.domain.Collection;
import businessObjects.domain.OrderEntry;

/**
 * Created by fgs on 9/13/2017.
 */

public class BillDetailsExpandableListViewAdapter extends BaseExpandableListAdapter {
    List<String> expandableListTitle;
    Context context;
    List<OrderEntry> orderDetails;
    List<Collection> collections;
    String  currencySymbol= SessionInformations.getInstance().getEmployee().getDistributor().getCurrencySymbol();
    public BillDetailsExpandableListViewAdapter(Context context, List<String> titles, List<OrderEntry> orders, List<Collection> collections){
               this.context = context;
                this.orderDetails = orders;
                this.collections = collections;
                this.expandableListTitle = titles;
            }
    @Override
    public int getGroupCount() {

        return 2;
    }

    @Override
    public int getChildrenCount(int i) {
        if(i==0){
            return orderDetails.size()+1;
        }
       else{
            return collections.size()+1;
        }
    }

    @Override
    public Object getGroup(int i) {

        if(i==0)return orderDetails;
        else return collections;
    }

    @Override
    public Object getChild(int i, int j) {
       if(i==0){
           return orderDetails.get(j);
       }
       else {
           return collections.get(j);
       }
    }

    @Override
    public long getGroupId(int i) {

        return i;
    }

    @Override
    public long getChildId(int i, int j) {
        return j;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded, View view, ViewGroup parent) {
        String title = expandableListTitle.get(listPosition);
                LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.billdetilsnestedheader, null);
                ((TextView)view.findViewById(R.id.expandableBillDetailsHeader)).setText( title );
                return view;
            }


    @Override
    public View getChildView(int i, int j, boolean t, View view, ViewGroup viewGroup) {
        if(i==0){
                 View childView=null;
                // adding orderentry table header
                 if(j==0){
                    LayoutInflater inflater=(LayoutInflater)this.context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
                    childView = inflater.inflate(R.layout.billdetailsorderheader,null);
                    TextView subTotal=(TextView)childView.findViewById(R.id.orderSubtotal);
                     subTotal.setText("("+currencySymbol+")");


                 }
                 else{
                    BillDetailsOrderEntryView entry=new BillDetailsOrderEntryView(context,(OrderEntry) getChild(i,j-1));
                    entry.setData();
                    childView=entry;
                  }
                return childView;
          }
          else{
            View childView = null;
            if (j==0) {
                LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
                childView = inflater.inflate(R.layout.billdetailspaymentheader, null);
               TextView paymentAmount=(TextView)childView.findViewById(R.id.paymentTotalCurrency);
                paymentAmount.setText("("+currencySymbol+")");
            }
            else {
                BillDetailsCollectionEntryView entry=new BillDetailsCollectionEntryView(context,(Collection) getChild(i,j-1));
                entry.setData();
                childView=entry;
            }
            return  childView;

            }
        }



   @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {

       return false;
    }
}
