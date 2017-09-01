package com.ventruxinformatics.prodcast;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import businessObjects.domain.EmployeeDetails;
import businessObjects.dto.AdminDTO;
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

public class OutstandingBillsActivity extends AppCompatActivity {
    Button newOrderPin;
    ListView listHistroy;
    Context context;


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
                Intent intent = new Intent(OutstandingBillsActivity.this, ProductListActivity.class);
                startActivity(intent);
            }
        });
        Intent intent=getIntent();
        boolean useCache=intent.getBooleanExtra("useCache",false);
        if(useCache)
        {
            Customer customer=SessionInformations.getInstance().getBillsForCustomer();
            setBills(customer);
        }
        else {
            EmployeeDetails employeeDetails=SessionInformations.getInstance().getEmployee();
            long customerId = employeeDetails.getCustomerId();
            long employeeId = employeeDetails.getEmployeeId();
            Call<CustomerDTO> custDTO = new ProdcastServiceManager().getClient().getBills(customerId, employeeId);
            custDTO.enqueue(new Callback<CustomerDTO>() {
                @Override
                public void onResponse(Call<CustomerDTO> call, Response<CustomerDTO> response) {
                    CustomerDTO dto = response.body();
                    if (dto.isError()) {
                        Toast.makeText(context, dto.getErrorMessage(), Toast.LENGTH_LONG).show();
                    } else {
                        Customer customer = dto.getCustomer();
                        setBills(customer);

                    }

                }

                @Override
                public void onFailure(Call<CustomerDTO> call, Throwable t) {
                    t.printStackTrace();

                }
            });

        }

    }
    private void setBills(Customer customer)
    {
        List<Bill> bills=customer.getOutstandingBill();
        if(bills.size()>0) {
            listHistroy.setAdapter(new OutstandingBillsDetailsAdapter(OutstandingBillsActivity.this, bills));

            listHistroy.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    TextView c = (TextView) view.findViewById(R.id.billNo);
                    String selectedBillIndex = c.getText().toString();
                    System.out.println(selectedBillIndex);
                    Intent intent = new Intent(OutstandingBillsActivity.this, BillDetailsActivity.class);
                    intent.putExtra("billId",selectedBillIndex);
                    startActivity(intent);

                }
            });

        }
        else{
            Intent intent=new Intent(OutstandingBillsActivity.this,ProductListActivity.class);
            startActivity(intent);
        }

    }



}