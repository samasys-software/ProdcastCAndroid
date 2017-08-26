package com.ventruxinformatics.prodcast;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import businessObjects.dto.CustomerDTO;

import businessObjects.connect.ProdcastServiceManager;

import businessObjects.domain.Bill;
import businessObjects.domain.Customer;

import java.util.ArrayList;
import java.util.List;

import businessObjects.SessionInformations;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BillActivity extends AppCompatActivity {
    Button newOrderPin;
    ListView listHistroy;
    Context context;
    List<Bill> bills=new ArrayList<Bill>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);
        context = this;
        listHistroy=(ListView) findViewById(R.id.billsListView);
        newOrderPin = (Button) findViewById(R.id.newOrderPin);
        newOrderPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BillActivity.this, ProductListActivity.class);
                startActivity(intent);
            }
        });


        long customerId = SessionInformations.getInstance().getEmployee().getCustomerId();
        long employeeId = SessionInformations.getInstance().getEmployee().getEmployeeId();
        Call<CustomerDTO> custDTO = new ProdcastServiceManager().getClient().getBills(customerId, employeeId);
        custDTO.enqueue(new Callback<CustomerDTO>() {
            @Override
            public void onResponse(Call<CustomerDTO> call, Response<CustomerDTO> response) {
                String responseString = null;
                CustomerDTO dto = response.body();
                if (dto.isError()) {
                    Toast.makeText(context, dto.getErrorMessage(), Toast.LENGTH_LONG).show();
                } else {
                    Customer customer = dto.getCustomer();
                    bills.addAll(customer.getOutstandingBill());
                    if(bills.size()>0) {
                        listHistroy.setAdapter(new BillDetailsList(BillActivity.this, bills));
                    }
                    else{
                        Intent intent=new Intent(BillActivity.this,HomeActivity.class);
                        startActivity(intent);
                    }

                }

            }

            @Override
            public void onFailure(Call<CustomerDTO> call, Throwable t) {
                t.printStackTrace();

            }
        });

    }

}