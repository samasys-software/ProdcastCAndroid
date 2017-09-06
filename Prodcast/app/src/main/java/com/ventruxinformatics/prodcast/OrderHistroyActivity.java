package com.ventruxinformatics.prodcast;

import android.app.DatePickerDialog;
import android.content.Context;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import businessObjects.SessionInformations;
import businessObjects.connect.ProdcastServiceManager;
import businessObjects.domain.Distributor;
import businessObjects.domain.Order;
import businessObjects.dto.CustomerReportDTO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderHistroyActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Context context;
    //Spinner reportDates;
    Button report,reset;
    ListView billDetailsList;

    private static final String TAG = "OrderHistoryActivity";

    private TextView mDisplayDate1;
    private TextView mDisplayDate2;
    private DatePickerDialog.OnDateSetListener mDateSetLisyener1;
    private DatePickerDialog.OnDateSetListener mDateSetLisyener2;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_histroy);
        billDetailsList = (ListView) findViewById(R.id.billDetailsList);

        //String[] reportDateList = { "today", "yesterday", "week", "custom"  };

        report = (Button) findViewById(R.id.report);
        reset = (Button) findViewById(R.id.reset);
        //reportDates = (Spinner) findViewById(R.id.reportDateSpinner);
        context=this;

        //ArrayAdapter dates = new ArrayAdapter(this,android.R.layout.simple_spinner_item,reportDateList);
        //dates.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Setting the ArrayAdapter data on the Spinner
        //reportDates.setAdapter(dates);

        // Spinner element
        final Spinner spinner = (Spinner) findViewById(R.id.reportDateSpinner);

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("today");
        categories.add("yesterday");
        categories.add("week");
        categories.add("custom");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        //Datepicker
        mDisplayDate1 = (TextView) findViewById(R.id.startDate);

        mDisplayDate1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Calendar cal1 = Calendar.getInstance();
                int year1 = cal1.get(Calendar.YEAR);
                int month1 = cal1.get(Calendar.MONTH);
                int day1 = cal1.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        OrderHistroyActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetLisyener1,
                        year1,month1,day1);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetLisyener1 = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int syear, int smonth, int sday)
            {
                smonth = smonth + 1;
                Log.d(TAG, "onDateSet: date: "+ sday + "/" + smonth + "/" + syear);

                String date1 = sday + "/" + smonth + "/" + syear;
                mDisplayDate1.setText(date1);
            }
        };

        mDisplayDate2 = (TextView) findViewById(R.id.endDate);

        mDisplayDate2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Calendar cal2 = Calendar.getInstance();
                int year2 = cal2.get(Calendar.YEAR);
                int month2 = cal2.get(Calendar.MONTH);
                int day2 = cal2.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        OrderHistroyActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog,
                        mDateSetLisyener2,
                        year2,month2,day2);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetLisyener2 = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int eyear, int emonth, int eday)
            {
                emonth = emonth + 1;
                Log.d(TAG, "onDateSet: date: "+ eday+ "/" + emonth  + "/" + eyear);

                String date2 = eday + "/" + emonth + "/" + eyear;
                mDisplayDate2.setText(date2);
            }
        };

        report.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String reportType =spinner.getSelectedItem().toString();

                long accessId= SessionInformations.getInstance().getCustomerDetails().getAccessId();

                Call<CustomerReportDTO> reportDTO = new ProdcastServiceManager().getClient().reportForCustomers(reportType,accessId,null,null,249,"SummaryReport");
                reportDTO.enqueue(new Callback<CustomerReportDTO>()
                {
                    @Override
                    public void onResponse(Call<CustomerReportDTO> call, Response<CustomerReportDTO> response)
                    {
                        CustomerReportDTO dto = response.body();

                        if (dto.isError())
                        {
                            Toast.makeText(context, dto.getErrorMessage(), Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            List<Order> customerReport = dto.getResult();

                            if (customerReport.size()>0)
                            {
                                billDetailsList.setAdapter(new CustomerReportAdaptor(OrderHistroyActivity.this, customerReport));
                                setSummaryDetails(dto);

                                RelativeLayout txtView1 = (RelativeLayout) findViewById(R.id.summaryTable);
                                RelativeLayout txtView = (RelativeLayout) findViewById(R.id.billDetailsTable);

                                txtView.setVisibility(View.VISIBLE);
                                txtView1.setVisibility(View.VISIBLE);
                            }
                            else
                            {
                                Toast.makeText(context, "No Reports available Please try again..!", Toast.LENGTH_LONG).show();
                                //billDetailsList.setEmptyView(findViewById(R.id.billDetailsList));

                                RelativeLayout txtView1 = (RelativeLayout) findViewById(R.id.summaryTable);
                                RelativeLayout txtView = (RelativeLayout) findViewById(R.id.billDetailsTable);

                                txtView.setVisibility(View.INVISIBLE);
                                txtView1.setVisibility(View.INVISIBLE);
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<CustomerReportDTO> call, Throwable t)
                    {
                        t.printStackTrace();
                    }
                });
            }
        });

        reset.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //billDetailsList.setEmptyView(findViewById(R.id.billDetailsList));

                RelativeLayout txtView1 = (RelativeLayout) findViewById(R.id.summaryTable);
                RelativeLayout txtView = (RelativeLayout) findViewById(R.id.billDetailsTable);

                txtView.setVisibility(View.INVISIBLE);
                txtView1.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void setSummaryDetails(CustomerReportDTO customerReportDTO)
    {
        TextView tv = (TextView) findViewById(R.id.totBillAmt);
        TextView tv1 = (TextView) findViewById(R.id.totAmtPait);
        TextView tv2 = (TextView) findViewById(R.id.sumBalance);

        tv.setText(String.valueOf(customerReportDTO.getAmount()));
        tv1.setText(String.valueOf(customerReportDTO.getAmountPaid()));
        tv2.setText(String.valueOf(customerReportDTO.getOutstandingBalance()));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        if(item == "custom")
        {
            RelativeLayout txtView = (RelativeLayout)findViewById(R.id.selectDates);
            txtView.setVisibility(View.VISIBLE);
        }

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {
        // TODO Auto-generated method stub
    }
}