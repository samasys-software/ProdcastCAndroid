package com.samayu.prodcastc.ui;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.samayu.prodcastc.R;
import com.samayu.prodcastc.businessObjects.GlobalUsage;
import com.samayu.prodcastc.businessObjects.SessionInfo;
import com.samayu.prodcastc.businessObjects.connect.ProdcastServiceManager;
import com.samayu.prodcastc.businessObjects.domain.EmployeeDetails;
import com.samayu.prodcastc.businessObjects.domain.Order;
import com.samayu.prodcastc.businessObjects.dto.OrderDTO;

import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.List;

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
    ProgressDialog progressDialog;
    NumberFormat numberFormat= GlobalUsage.getNumberFormat();
    String currencySymbol;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_details);
        progressDialog=getProgressDialog(this);

        currencySymbol= SessionInfo.getInstance().getEmployee().getDistributor().getCurrencySymbol();
        progressDialog=getProgressDialog(this);
        expandableListView=(ExpandableListView) findViewById(R.id.expandableBillDetailsEntryList);
        context = this;
        getServerResponse();
    }

    public void getServerResponse(){
        Intent data = getIntent();
        String billId = data.getStringExtra("billId");
        EmployeeDetails employeeDetails = SessionInfo.getInstance().getEmployee();
        long employeeId = employeeDetails.getEmployeeId();
        //String userRole = employeeDetails.getUserRole();
        String userRole = "D";
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

        TextView orderNoText = (TextView) findViewById(R.id.orderNoText);
        TextView billDateText = (TextView) findViewById(R.id.billDateText);
        TextView totalText = (TextView) findViewById(R.id.totalText);
        TextView balanceText = (TextView) findViewById(R.id.balanceText);
        TextView discountText = (TextView) findViewById(R.id.discountText);

        TextView orderNo = (TextView) findViewById(R.id.orderNo);
        TextView billDate = (TextView) findViewById(R.id.billDate);
        TextView total = (TextView) findViewById(R.id.total);
        TextView balance = (TextView) findViewById(R.id.balance);
        TextView discount = (TextView) findViewById(R.id.discount);


        TextView deliveryType = (TextView) findViewById(R.id.deliveryType);
        TextView type = (TextView) findViewById(R.id.type);

        distName.setText(order.getDistributorName());
        distAddress1.setText(order.getDistributor().getAddress1() + " " + order.getDistributor().getAddress2() + " " + order.getDistributor().getAddress3());
        distAddress3.setText(order.getDistributor().getState() + " " + order.getDistributor().getPostalCode());
        distAddress2.setText(order.getDistributor().getCity() );
        distPhonenumber.setText(order.getDistributor().getHomePhone());
         orderNo.setText(String.valueOf(order.getBillNumber()));

        billDate.setText(String.valueOf(order.getBillDate()));
        total.setText(currencySymbol+""+numberFormat.format(order.getTotalAmount()));
        balance.setText(currencySymbol+""+numberFormat.format(order.getOutstandingBalance()));
        discount.setText(String.valueOf(order.getDiscount()));

        String distType = order.getFulfillmentType();

        if(distType.equals("1")){
            deliveryType.setVisibility(View.VISIBLE);
            type.setVisibility(View.VISIBLE);
            type.setText("Delivery");

        }
        else if (distType.equals("2")){
            deliveryType.setVisibility(View.VISIBLE);
            type.setVisibility(View.VISIBLE);
            type.setText("Pick Up");
        }




    }
   
}