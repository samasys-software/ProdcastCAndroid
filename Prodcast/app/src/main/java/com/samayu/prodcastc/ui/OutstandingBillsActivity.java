package com.samayu.prodcastc.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import com.samayu.prodcastc.businessObjects.SessionInfo;
import com.samayu.prodcastc.businessObjects.connect.ProdcastServiceManager;
import com.samayu.prodcastc.businessObjects.domain.Bill;
import com.samayu.prodcastc.businessObjects.domain.Customer;
import com.samayu.prodcastc.businessObjects.domain.EmployeeDetails;
import com.samayu.prodcastc.businessObjects.dto.CustomerDTO;
import com.ventruxinformatics.prodcast.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OutstandingBillsActivity extends ProdcastCBaseActivity {
    FloatingActionButton newOrderPin;
    ListView listHistroy;
    TextView outstandingBillText;
    TextView outstandingBillCount;
    TextView outstandingBillTotalText, outstandingBillBalanceText;
    TextView readyLegend, newLegend;
    ImageView readyLegendImage, newLegendImage;
    Context context;
    ProgressDialog progressDialog;

    @Override
    public String getProdcastTitle() {
        return "Open Orders";
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
        newOrderPin = (FloatingActionButton) findViewById(R.id.newOrderPin);
        outstandingBillText = (TextView) findViewById(R.id.outstandingBillText);
        outstandingBillCount = (TextView) findViewById(R.id.outstandingBillCount);

        readyLegendImage = (ImageView) findViewById(R.id.readyLegendImage);
        newLegendImage = (ImageView) findViewById(R.id.newLegendImage);

        readyLegend = (TextView) findViewById(R.id.readyLegend);
        newLegend = (TextView) findViewById(R.id.newLegend);

        TextView total=(TextView) findViewById(R.id.total);
        TextView outstandingbalance=(TextView) findViewById(R.id.outstandingBalance);


        TextView outstandingBillTotalText = (TextView)findViewById(R.id.outstandingBillTotalText);
        TextView outstandingBillBalanceText = (TextView)findViewById(R.id.outstandingBillBalanceText);

        String currencySymbol= SessionInfo.getInstance().getEmployee().getDistributor().getCurrencySymbol();

        outstandingBillTotalText.setText("("+currencySymbol+")");
        outstandingBillBalanceText.setText("("+currencySymbol+")");

        outstandingBillText.setText("Open Orders");

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
            Customer customer= SessionInfo.getInstance().getBillsForCustomer();
            setBills(customer);
        }
        else {
            progressDialog.show();
            EmployeeDetails employeeDetails= SessionInfo.getInstance().getEmployee();
            long customerId = employeeDetails.getCustomer().getId();
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
                            setCount();
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
    @Override
    public void onResume(){
        super.onResume();
        setCount();
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

                    TextView c = (TextView) view.findViewById(R.id.billNo);
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

    private void setCount(){
        TextView billSize = (TextView) findViewById(R.id.outstandingBillText);
        TextView billCount = (TextView) findViewById(R.id.outstandingBillCount);
        if( billSize != null && listHistroy.getAdapter() != null )
        //billSize.setText("Outstanding Bills("+listHistroy.getAdapter().getCount()+")");
        billCount.setText(" " +"("+listHistroy.getAdapter().getCount()+")");
    }



}