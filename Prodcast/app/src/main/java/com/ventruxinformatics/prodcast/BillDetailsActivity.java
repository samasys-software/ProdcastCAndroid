package com.ventruxinformatics.prodcast;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.util.DiffUtil;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ventruxinformatics.prodcast.BillDetailsListAdapter;
import com.ventruxinformatics.prodcast.R;

import java.text.NumberFormat;

import businessObjects.GlobalUsage;
import businessObjects.SessionInformations;
import businessObjects.connect.ProdcastServiceManager;
import businessObjects.domain.EmployeeDetails;
import businessObjects.domain.Order;
import businessObjects.dto.OrderDTO;
import businessObjects.font_design.NewTextView;
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


    ListView orderListView;
    ListView paymentListView;
    Context context;
    ImageView refresh;
    NewTextView subTotal,paymentAmount;
    Button Close;
    ProgressDialog progressDialog;
    NumberFormat numberFormat= GlobalUsage.getNumberFormat();
    String currencySymbol;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_details);
        progressDialog=getProgressDialog(this);
        //   Close=(Button) findViewById(R.id.close);
        currencySymbol=SessionInformations.getInstance().getEmployee().getDistributor().getCurrencySymbol();
        progressDialog=getProgressDialog(this);
        paymentListView = (ListView) findViewById(R.id.paymentEntriesAdapter);
        subTotal=(NewTextView) findViewById(R.id.subTotal);
        paymentAmount=(NewTextView) findViewById(R.id.paymentAmount);
        subTotal.setText("Sub Total("+currencySymbol+")");
        paymentAmount.setText("Amount("+currencySymbol+")");
        orderListView = (ListView) findViewById(R.id.orderEntriesAdapter);
        refresh = (ImageView) findViewById(R.id.refresh);
        context = this;
        getServerResponse();
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getServerResponse();
            }

        });

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
                        //  Toast.makeText(context, "Welcome", Toast.LENGTH_LONG).show();


                        if (order.getCollectionEntries().size() > 0) {
                            paymentListView.setAdapter(new PaymentDetailsListAdapter(BillDetailsActivity.this, order.getCollectionEntries()));

                        } else {

                            LinearLayout txView = (LinearLayout) findViewById(R.id.llpayment);
                            LinearLayout txView1 = (LinearLayout) findViewById(R.id.paymentDetailsInvisible);

                            txView.setVisibility(View.INVISIBLE);
                            txView1.setVisibility(View.INVISIBLE);
                        }
                        if (order.getOrderEntries().size() > 0) {
                            orderListView.setAdapter(new BillDetailsListAdapter(BillDetailsActivity.this, order.getOrderEntries()));
                        }
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
//        mProgressDialog.cancel();


    }

    /*  Close.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              Intent intent=new Intent(BillDetailsActivity.this,OutstandingBillsActivity.class);
              startActivity(intent);

          }
      });*/
    public void setBillDetails(Order order){
        NewTextView tv = (NewTextView) findViewById(R.id.distName);
        NewTextView tv1 = (NewTextView) findViewById(R.id.custName);
        NewTextView tv2 = (NewTextView) findViewById(R.id.distAddress);
        NewTextView tv3 = (NewTextView) findViewById(R.id.custAddress);
        NewTextView tv4 = (NewTextView) findViewById(R.id.distAddress1);
        NewTextView tv5 = (NewTextView) findViewById(R.id.custAddress1);
        NewTextView tv6 = (NewTextView) findViewById(R.id.distPhonenumber);
        NewTextView tv7 = (NewTextView) findViewById(R.id.custPhonenumber);
        NewTextView orderNo = (NewTextView) findViewById(R.id.orderNo);
        NewTextView billDate = (NewTextView) findViewById(R.id.billDate);
        NewTextView total = (NewTextView) findViewById(R.id.total);
        NewTextView salesRep = (NewTextView) findViewById(R.id.salesRep);
        NewTextView balance = (NewTextView) findViewById(R.id.balance);
        NewTextView discount = (NewTextView) findViewById(R.id.discount);
        tv.setText(order.getDistributorName());
        tv2.setText(order.getDistributor().getAddress1() + " " + order.getDistributor().getAddress2() + " " + order.getDistributor().getAddress3());
        tv4.setText(order.getDistributor().getCity() + " " + order.getDistributor().getState() + " " + order.getDistributor().getPostalCode());
        tv6.setText(order.getDistributor().getHomePhone());
        tv1.setText(order.getCustomerName());
        tv3.setText(order.getCustomer().getBillingAddress1() + " " + order.getCustomer().getBillingAddress2() + " " + order.getCustomer().getBillingAddress3());
        tv5.setText(order.getCustomer().getCity() + " " + order.getCustomer().getState() + " " + order.getCustomer().getPostalCode());
        tv7.setText(order.getCustomer().getCellPhone());
        orderNo.setText(String.valueOf(order.getBillNumber()));
        billDate.setText(String.valueOf(order.getBillDate()));
        salesRep.setText(order.getEmployeeName());
        total.setText(currencySymbol+""+numberFormat.format(order.getTotalAmount()));
        balance.setText(currencySymbol+""+numberFormat.format(order.getOutstandingBalance()));
        discount.setText(String.valueOf(order.getDiscount()));

    }

}