package com.ventruxinformatics.prodcast;

import android.app.Dialog;
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
import android.widget.Button;
import android.widget.ListView;

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

public class EntryActivity extends AppCompatActivity {

    Inflater inflater;
    Button order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);

         List<OrderDetails> entries = SessionInformations.getInstance().getEntry();
        if (entries.size() != 0) {
            final EmployeeDetails emp = SessionInformations.getInstance().getEmployee();


            //View convertView = (View) inflater.inflate(R.layout.activity_all_products, null);
            //alertDialog.setView(convertView);
            // alertDialog.setTitle("List");
            ListView listView = (ListView) findViewById(R.id.listofentries);
            order = (Button) findViewById(R.id.order);
            EntriesCustomAdapter adapter = new EntriesCustomAdapter(EntryActivity.this, entries);

            listView.setAdapter(adapter);


            order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<OrderEntryDTO> orderEntries=new ArrayList<OrderEntryDTO>();

                    List<OrderDetails> orderDetails = SessionInformations.getInstance().getEntry();
                    System.out.println("Size:" + orderDetails.size());
                    for (OrderDetails d1 : orderDetails) {
                        OrderEntryDTO orderEntry=new OrderEntryDTO();
                        orderEntry.setProductId(String.valueOf(d1.getProduct().getId()));
                        orderEntry.setQuantity(String.valueOf(d1.getQuantity()));
                        orderEntries.add(orderEntry);

                    }
                    OrderDetailDTO dto = new OrderDetailDTO();

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


                    Call<CustomerDTO> saveOrderDTO = new ProdcastServiceManager().getClient().saveOrder(dto);

                    saveOrderDTO.enqueue(new Callback<CustomerDTO>() {
                        @Override
                        public void onResponse(Call<CustomerDTO> call, Response<CustomerDTO> response) {
                            String responseString = null;
                            CustomerDTO dto = response.body();
                            if (dto.isError()) {

                            } else {
                                System.out.println("success");
                                SessionInformations.getInstance().setEntry(null);
                                Customer customer=dto.getCustomer();
                                SessionInformations.getInstance().setBillsForCustomer(customer);
                                Intent i=new Intent(EntryActivity.this,OutstandingBillsActivity.class);
                                i.putExtra("useCache",true);
                                startActivity(i);


                            }

                        }

                        @Override
                        public void onFailure(Call<CustomerDTO> call, Throwable t) {
                            t.printStackTrace();

                        }
                    });


                }
            });


        }
    }

}