package com.ventruxinformatics.prodcast;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import businessObjects.SessionInformations;
import businessObjects.connect.ProdcastServiceManager;
import businessObjects.domain.Customer;
import businessObjects.domain.CustomersLogin;
import businessObjects.domain.EmployeeDetails;
import businessObjects.domain.OrderDetails;
import businessObjects.dto.CustomerDTO;
import businessObjects.dto.CustomerLoginDTO;
import businessObjects.dto.OrderDetailDTO;
import businessObjects.dto.OrderEntryDTO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EntryActivity extends ProdcastCBaseActivity {

    Inflater inflater;
    Button order,backButton;
    ProgressDialog progressDialog;
    Context context;

    @Override
    public String getProdcastTitle() {
        return "Orders Screen";
    }

    @Override
    public boolean getCompanyName() {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        context=this;
        progressDialog=getProgressDialog(this);

         List<OrderDetails> entries = SessionInformations.getInstance().getEntry();
        if (entries.size() != 0) {
            final EmployeeDetails emp = SessionInformations.getInstance().getEmployee();


            //View convertView = (View) inflater.inflate(R.layout.activity_all_products, null);
            //alertDialog.setView(convertView);
            // alertDialog.setTitle("List");
            final ListView listView = (ListView) findViewById(R.id.listofentries);
            final TextView total=(TextView) findViewById(R.id.total);

            order = (Button) findViewById(R.id.order);
            backButton = (Button) findViewById(R.id.backButton);
            final EntriesCustomAdapter adapter = new EntriesCustomAdapter(EntryActivity.this, entries);
            progressDialog.show();
            listView.setAdapter(adapter);
            progressDialog.dismiss();







            //total.setText("Total:"+getOrdersEntry());
            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();


                }
            });


            order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OrderDetailDTO dto = new OrderDetailDTO();
                    List<OrderEntryDTO> orderEntries=new ArrayList<OrderEntryDTO>();

                    List<OrderDetails> orderDetails = SessionInformations.getInstance().getEntry();
                    System.out.println("Size:" + orderDetails.size());
                    for (OrderDetails d1 : orderDetails) {
                        OrderEntryDTO orderEntry=new OrderEntryDTO();
                        orderEntry.setProductId(String.valueOf(d1.getProduct().getId()));
                        orderEntry.setQuantity(String.valueOf(d1.getQuantity()));
                        //subtotal=subtotal+d1.getSubTotal();
                        orderEntries.add(orderEntry);


                    }
                    //return subtotal;

                    dto.setEntries(orderEntries);
                    dto.setCustomerId(String.valueOf(emp.getCustomerId()));
                    dto.setDiscountType(null);
                    dto.setDiscountValue("0");
                    dto.setEmployeeId(String.valueOf(emp.getEmployeeId()));
                    dto.setOrderStatus("S");
                    dto.setPaymentAmount("0");
                    dto.setPaymentType(null);
                    dto.setRefDetail(null);
                    dto.setRefNO(null);
                    progressDialog.show();

                    Call<CustomerDTO> saveOrderDTO = new ProdcastServiceManager().getClient().saveOrder(dto);

                    saveOrderDTO.enqueue(new Callback<CustomerDTO>() {
                        @Override
                        public void onResponse(Call<CustomerDTO> call, Response<CustomerDTO> response) {
                            String responseString = null;
                            CustomerDTO dto = response.body();
                            if (dto.isError()) {
                                progressDialog.dismiss();

                            } else {
                                System.out.println("success");
                                SessionInformations.getInstance().setEntry(null);
                                Customer customer=dto.getCustomer();
                                SessionInformations.getInstance().setBillsForCustomer(customer);
                                Intent i=new Intent(EntryActivity.this,OutstandingBillsActivity.class);
                                i.putExtra("useCache",true);
                                startActivity(i);
                                progressDialog.dismiss();


                            }

                        }

                        @Override
                        public void onFailure(Call<CustomerDTO> call, Throwable t) {
                            t.printStackTrace();
                            getAlertBox(context).show();
                            progressDialog.dismiss();

                        }
                    });


                }
            });


        }
    }
}
