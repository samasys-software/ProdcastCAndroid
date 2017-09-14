package com.ventruxinformatics.prodcast;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.List;

import businessObjects.GlobalUsage;
import businessObjects.SessionInformations;
import businessObjects.connect.ProdcastServiceManager;
import businessObjects.domain.EmployeeDetails;
import businessObjects.domain.Order;
import businessObjects.dto.OrderDTO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BillDetailsActivity extends ProdcastCBaseActivity {


    @Override
    public boolean getCompanyName(){
        return true;
    }

    @Override
     public String getProdcastTitle(){
        return "Bill Details";
    }

    ExpandableListView expandableListView;

    Context context;

    TextView subTotal,paymentAmount;

    ProgressDialog progressDialog;
    NumberFormat numberFormat= GlobalUsage.getNumberFormat();
    String currencySymbol;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_details);
        progressDialog=getProgressDialog(this);

        currencySymbol=SessionInformations.getInstance().getEmployee().getDistributor().getCurrencySymbol();
        progressDialog=getProgressDialog(this);

        expandableListView=(ExpandableListView) findViewById(R.id.expandableBillDetailsEntryList);

        context = this;
        getServerResponse();



    }

    public void getServerResponse(){
        Intent data = getIntent();
        String billId = data.getStringExtra("billId");
        EmployeeDetails employeeDetails = SessionInformations.getInstance().getEmployee();
        long employeeId = employeeDetails.getEmployeeId();
        String userRole = employeeDetails.getUserRole();


        progressDialog.show();
        Call<OrderDTO> billDetailsDTO = new ProdcastServiceManager().getClient().getBillDetails(Long.parseLong(billId), employeeId, userRole);

        billDetailsDTO.enqueue(new Callback<OrderDTO>() {
            @Override
            public void onResponse(Call<OrderDTO> call, Response<OrderDTO> response) {
                if(response.isSuccessful()) {
                    OrderDTO dto = response.body();
                    if (dto.isError()) {
                        getErrorBox(context,dto.getErrorMessage()).show();
                        progressDialog.dismiss();
                    } else {
                        Order order = dto.getOrder();
                        setBillDetails(order);

                       List<String> titles = new LinkedList<String>();
                        titles.add("Order Details");
                        titles.add("Payment Details");
                        expandableListView.setAdapter(
                                new BillDetailsExpandableListViewAdapter(
                                        BillDetailsActivity.this,titles,order.getOrderEntries(),order.getCollectionEntries())
                        );
                        expandableListView.expandGroup(0);
                        expandableListView.expandGroup(1);
                        progressDialog.dismiss();
                    }
                }

            }

            @Override
            public void onFailure(Call<OrderDTO> call, Throwable t) {

                t.printStackTrace();
                progressDialog.dismiss();
                getAlertBox(context).show();


            }

        });
    }



    public void setBillDetails(Order order){
        TextView distName = (TextView) findViewById(R.id.distName);
        TextView distAddress1 = (TextView) findViewById(R.id.distAddress1);
        TextView distAddress2 = (TextView) findViewById(R.id.distAddress2);
        TextView distAddress3 = (TextView) findViewById(R.id.distAddress3);
        TextView distPhonenumber = (TextView) findViewById(R.id.distPhonenumber);
        TextView orderNo = (TextView) findViewById(R.id.orderNo);
        TextView billDate = (TextView) findViewById(R.id.billDate);
        TextView total = (TextView) findViewById(R.id.total);
        TextView balance = (TextView) findViewById(R.id.balance);
        TextView discount = (TextView) findViewById(R.id.discount);
        distName.setText(order.getDistributorName());
        distAddress1.setText(order.getDistributor().getAddress1() + " " + order.getDistributor().getAddress2() + " " + order.getDistributor().getAddress3());
        distAddress3.setText(order.getDistributor().getState() + " " + order.getDistributor().getPostalCode());
        distAddress2.setText(order.getDistributor().getCity() );
        distPhonenumber.setText(order.getDistributor().getHomePhone());
         orderNo.setText("Order No:"+" "+String.valueOf(order.getBillNumber())+"");

        billDate.setText("BillDate:"+" "+String.valueOf(order.getBillDate())+"");
        total.setText("Total:"+" "+"("+currencySymbol+")"+numberFormat.format(order.getTotalAmount())+"");
        balance.setText("Balance:"+" "+"("+currencySymbol+")"+numberFormat.format(order.getOutstandingBalance())+"");
        discount.setText("Discount:"+" "+String.valueOf(order.getDiscount())+"");

    }
   
}