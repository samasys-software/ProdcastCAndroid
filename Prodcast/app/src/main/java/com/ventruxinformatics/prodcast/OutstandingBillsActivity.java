package com.ventruxinformatics.prodcast;

import android.app.ProgressDialog;
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
import businessObjects.dto.CustomerDTO;

import businessObjects.connect.ProdcastServiceManager;

import businessObjects.domain.Bill;
import businessObjects.domain.Customer;

import java.util.List;

import businessObjects.SessionInformations;
import businessObjects.font_design.NewTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OutstandingBillsActivity extends ProdcastCBaseActivity {
    Button newOrderPin;
    ListView listHistroy;

    Context context;
    ProgressDialog progressDialog;

    @Override
    public String getProdcastTitle() {
        return "Outstanding Bills";
    }

    @Override
    public boolean getCompanyName() {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outstanding_bill);
        context = this;
        progressDialog=getProgressDialog(context);
        listHistroy=(ListView) findViewById(R.id.billsListView);
        newOrderPin = (Button) findViewById(R.id.newOrderPin);
        NewTextView total=(NewTextView) findViewById(R.id.total);
        NewTextView outstandingbalance=(NewTextView) findViewById(R.id.outstandingBalance);
        String currencySymbol=SessionInformations.getInstance().getEmployee().getDistributor().getCurrencySymbol();
        total.setText("Total("+currencySymbol+")");
        outstandingbalance.setText("Balance("+currencySymbol+")");
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
            progressDialog.show();
            EmployeeDetails employeeDetails=SessionInformations.getInstance().getEmployee();
            long customerId = employeeDetails.getCustomerId();
            long employeeId = employeeDetails.getEmployeeId();
            Call<CustomerDTO> custDTO = new ProdcastServiceManager().getClient().getBills(customerId, employeeId);
            custDTO.enqueue(new Callback<CustomerDTO>() {
                @Override
                public void onResponse(Call<CustomerDTO> call, Response<CustomerDTO> response) {
                    if(response.isSuccessful()) {
                        CustomerDTO dto = response.body();
                        if (dto.isError()) {
                            //  Toast.makeText(context, dto.getErrorMessage(), Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                            getErrorBox(context,dto.getErrorMessage()).show();
                        } else {
                            Customer customer = dto.getCustomer();
                            setBills(customer);
                            progressDialog.dismiss();

                        }
                    }

                }

                @Override
                public void onFailure(Call<CustomerDTO> call, Throwable t) {
                    t.printStackTrace();
                    progressDialog.dismiss();
                    getAlertBox(context).show();

                }
            });

        }

    }
    private void setBills(Customer customer)
    {
        List<Bill> bills=customer.getOutstandingBill();
        progressDialog.show();
        if(bills.size()>0) {
            listHistroy.setAdapter(new OutstandingBillsDetailsAdapter(OutstandingBillsActivity.this, bills));
            progressDialog.dismiss();
            listHistroy.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    NewTextView c = (NewTextView) view.findViewById(R.id.billNo);
                    String selectedBillIndex = c.getText().toString();
                  //  System.out.println(selectedBillIndex);
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