package com.ventruxinformatics.prodcast;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import businessObjects.GlobalUsage;
import businessObjects.SessionInformations;
import businessObjects.connect.ProdcastServiceManager;
import businessObjects.domain.Distributor;
import businessObjects.domain.EmployeeDetails;
import businessObjects.domain.Order;
import businessObjects.dto.CustomerReportDTO;
import businessObjects.font_design.NewTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderHistroyActivity extends ProdcastCBaseActivity implements AdapterView.OnItemSelectedListener
{
    Context context;

    Spinner store;
    Button report,reset;
    ListView billDetailsList;
    NumberFormat numberFormat= GlobalUsage.getNumberFormat();


    String cusReportType = "SummaryReport";

    private static final String TAG = "OrderHistoryActivity";

    private NewTextView mDisplayDate1;
    NewTextView totalAmount,totalPaid,totalBalance,orderTotal,orderBalance;
    private NewTextView mDisplayDate2;
    private DatePickerDialog.OnDateSetListener mDateSetLisyener1;
    private DatePickerDialog.OnDateSetListener mDateSetLisyener2;

    @Override
    public String getProdcastTitle(){
        return "Order History";
    }



    @Override
    public boolean getCompanyName(){
        return false;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_histroy);
        billDetailsList = (ListView) findViewById(R.id.billDetailsList);

        report = (Button) findViewById(R.id.report);
        reset = (Button) findViewById(R.id.reset);
        store = (Spinner) findViewById(R.id.storeSpinner);
        totalAmount=(NewTextView) findViewById(R.id.billAmt);
        totalPaid=(NewTextView) findViewById(R.id.totalPaid);
        totalBalance=(NewTextView) findViewById(R.id.totBalance);
        orderTotal=(NewTextView) findViewById(R.id.orderTotal);
        orderBalance=(NewTextView) findViewById(R.id.orderBalance);


        context=this;
        final ProgressDialog progressDialog=getProgressDialog(context);

        Distributor distributor= SessionInformations.getInstance().getEmployee().getDistributor();

        String dist = distributor.getCompanyName();
        List<String> allDist = new ArrayList<String>();
        allDist.add(dist);

        ArrayAdapter storeDistributors = new ArrayAdapter(this,R.layout.drop_down_list,allDist);
        storeDistributors.setDropDownViewResource(R.layout.drop_down_list);
        final String currencySymbol=distributor.getCurrencySymbol();
        totalAmount.setText("Total Bill Amount("+currencySymbol+")");
        totalPaid.setText("Total Amount Paid("+currencySymbol+")");
        totalBalance.setText("Balance("+currencySymbol+")");
        orderTotal.setText("Total("+currencySymbol+")");
        orderBalance.setText("Balance("+currencySymbol+")");

        //Setting the ArrayAdapter data on the Spinner
        store.setAdapter(storeDistributors);

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
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.drop_down_list, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(R.layout.drop_down_list);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        //Datepicker
        mDisplayDate1 = (NewTextView) findViewById(R.id.startDate);

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

                int month1 = smonth + 1;
                String formattedMonth1 = "" + month1;
                String formattedDayOfMonth1 = "" + sday;

                if(month1 < 10)
                {
                    formattedMonth1 = "0" + month1;
                }
                if(sday < 10)
                {
                    formattedDayOfMonth1 = "0" + sday;
                }

                smonth = smonth + 1;
                Log.d(TAG, "onDateSet: date: "+ formattedDayOfMonth1 + "/" + formattedMonth1 + "/" + syear);

                String date1 = formattedDayOfMonth1 + "/" + formattedMonth1 + "/" + syear;
                mDisplayDate1.setText(date1);

                Toast.makeText(context, date1, Toast.LENGTH_LONG).show();
            }
        };

        mDisplayDate2 = (NewTextView) findViewById(R.id.endDate);

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

                int month2 = emonth + 1;
                String formattedMonth2 = "" + month2;
                String formattedDayOfMonth2 = "" + eday;

                if(month2 < 10)
                {
                    formattedMonth2 = "0" + month2;
                }
                if(eday < 10)
                {
                    formattedDayOfMonth2 = "0" + eday;
                }

                //emonth = emonth + 1;
                Log.d(TAG, "onDateSet: date: "+ formattedDayOfMonth2+ "/" + formattedMonth2  + "/" + eyear);

                String date2 = formattedDayOfMonth2 + "/" + formattedMonth2 + "/" + eyear;
                mDisplayDate2.setText(date2);

                Toast.makeText(context, date2, Toast.LENGTH_LONG).show();
            }
        };

        //Calendar cal  = Calendar.getInstance();

        //Toast.makeText(context, "Selected: " + thisDate, Toast.LENGTH_LONG).show();

        report.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String reportType =spinner.getSelectedItem().toString();
                String reportId = cusReportType;

                EmployeeDetails emp = SessionInformations.getInstance().getEmployee();
                Distributor distributor = emp.getDistributor();

                long distributorId = distributor.getDistributorId();
                long accessId= SessionInformations.getInstance().getCustomerDetails().getAccessId();

                String startDate;
                String endDate;

                if (reportType.equals("custom"))
                {
                    startDate = mDisplayDate1.getText().toString();
                    endDate = mDisplayDate2.getText().toString();
                }
                else
                {
                    startDate = null;
                    endDate = null;
                }

                if (reportType.equals("custom") && (startDate.equals("Start Date") || endDate.equals("End Date")))
                {
                    Toast.makeText(context, "Please fill mandatory fields", Toast.LENGTH_LONG).show();
                }
                else if(reportType.equals("today") || reportType.equals("yesterday") || reportType.equals("week"))
                {
                    progressDialog.show();
                    Call<CustomerReportDTO> reportDTO = new ProdcastServiceManager().getClient().reportForCustomers(reportType,accessId,startDate,endDate,distributorId,reportId);
                    reportDTO.enqueue(new Callback<CustomerReportDTO>()
                    {
                        @Override
                        public void onResponse(Call<CustomerReportDTO> call, Response<CustomerReportDTO> response)
                        {
                            if(response.isSuccessful()) {
                                CustomerReportDTO dto = response.body();


                                if (dto.isError()) {
                                    getErrorBox(context,dto.getErrorMessage());
                                    progressDialog.dismiss();
                                } else {
                                    List<Order> customerReport = dto.getResult();

                                    if (customerReport.size() > 0) {
                                        billDetailsList.setAdapter(new CustomerReportAdaptor(OrderHistroyActivity.this, customerReport));
                                        setSummaryDetails(dto);

                                        RelativeLayout txtView1 = (RelativeLayout) findViewById(R.id.summaryTable);
                                        RelativeLayout txtView = (RelativeLayout) findViewById(R.id.billDetailsTable);

                                        txtView.setVisibility(View.VISIBLE);
                                        txtView1.setVisibility(View.VISIBLE);

                                        billDetailsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                NewTextView billNumber = (NewTextView) findViewById(R.id.cusBillNo);
                                                String selectedBillIndex = billNumber.getText().toString();

                                                Intent intent = new Intent(OrderHistroyActivity.this, BillDetailsActivity.class);
                                                intent.putExtra("billId", selectedBillIndex);
                                                startActivity(intent);
                                            }
                                        });
                                    } else {
                                        Toast.makeText(context, "No Reports available Please try again..!", Toast.LENGTH_LONG).show();
                                        //billDetailsList.setEmptyView(findViewById(R.id.billDetailsList));

                                        RelativeLayout txtView1 = (RelativeLayout) findViewById(R.id.summaryTable);
                                        RelativeLayout txtView = (RelativeLayout) findViewById(R.id.billDetailsTable);

                                        txtView.setVisibility(View.INVISIBLE);
                                        txtView1.setVisibility(View.INVISIBLE);
                                    }
                                    progressDialog.dismiss();
                                }
                            }
                        }
                        @Override
                        public void onFailure(Call<CustomerReportDTO> call, Throwable t)
                        {
                            t.printStackTrace();
                            getAlertBox(context);
                            progressDialog.dismiss();
                        }
                    });
                }
                else
                {

                    SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
                    Date todayDate = new Date();
                    String thisDate = currentDate.format(todayDate);

                    /*try
                    {
                        Date sDate = currentDate.parse(startDate);
                        Date eDate = currentDate.parse(endDate);

                        if (sDate.after(eDate))
                        {
                            Toast.makeText(context, "Please select valid dates", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Toast.makeText(context, "Please select valid dates", Toast.LENGTH_LONG).show();
                        }
                    }
                    catch (ParseException e)
                    {
                        e.printStackTrace();
                    }*/

                    //int sd = Integer.parseInt(startDate);
                    //int ed = Integer.parseInt(endDate);
                    //int td = Integer.parseInt(thisDate);

                    //if (sd > ed || sd > td  || ed > td)
                    if (startDate.compareTo(endDate) > 0 || startDate.compareTo(thisDate) > 0 || endDate.compareTo(thisDate) > 0)
                    {
                        Toast.makeText(context, "Please select valid dates", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        progressDialog.show();;
                        Call<CustomerReportDTO> reportDTO = new ProdcastServiceManager().getClient().reportForCustomers(reportType,accessId,startDate,endDate,distributorId,reportId);
                        reportDTO.enqueue(new Callback<CustomerReportDTO>()
                        {
                            @Override
                            public void onResponse(Call<CustomerReportDTO> call, Response<CustomerReportDTO> response)
                            {
                                if(response.isSuccessful()) {
                                    CustomerReportDTO dto = response.body();

                                    if (dto.isError()) {
                                        //Toast.makeText(context, dto.getErrorMessage(), Toast.LENGTH_LONG).show();
                                        getErrorBox(context,dto.getErrorMessage());
                                        progressDialog.dismiss();
                                    } else {
                                        List<Order> customerReport = dto.getResult();

                                        if (customerReport.size() > 0) {
                                            billDetailsList.setAdapter(new CustomerReportAdaptor(OrderHistroyActivity.this, customerReport));
                                            setSummaryDetails(dto);

                                            RelativeLayout txtView1 = (RelativeLayout) findViewById(R.id.summaryTable);
                                            RelativeLayout txtView = (RelativeLayout) findViewById(R.id.billDetailsTable);

                                            txtView.setVisibility(View.VISIBLE);
                                            txtView1.setVisibility(View.VISIBLE);

                                            billDetailsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                                @Override
                                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                    NewTextView billNumber = (NewTextView) findViewById(R.id.cusBillNo);

                                                    String selectedBillIndex = billNumber.getText().toString();

                                                    Intent intent = new Intent(OrderHistroyActivity.this, BillDetailsActivity.class);
                                                    intent.putExtra("billId", selectedBillIndex);
                                                    startActivity(intent);
                                                }
                                            });
                                        } else {
                                            Toast.makeText(context, "No Reports available Please try again..!", Toast.LENGTH_LONG).show();
                                            //billDetailsList.setEmptyView(findViewById(R.id.billDetailsList));

                                            RelativeLayout txtView1 = (RelativeLayout) findViewById(R.id.summaryTable);
                                            RelativeLayout txtView = (RelativeLayout) findViewById(R.id.billDetailsTable);

                                            txtView.setVisibility(View.INVISIBLE);
                                            txtView1.setVisibility(View.INVISIBLE);
                                        }
                                        progressDialog.dismiss();
                                    }
                                }
                            }
                            @Override
                            public void onFailure(Call<CustomerReportDTO> call, Throwable t)
                            {
                                t.printStackTrace();
                                getAlertBox(context);
                                progressDialog.dismiss();
                            }
                        });
                    }
                }
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
        NewTextView tv = (NewTextView) findViewById(R.id.totBillAmt);
        NewTextView tv1 = (NewTextView) findViewById(R.id.totAmtPait);
        NewTextView tv2 = (NewTextView) findViewById(R.id.sumBalance);

        tv.setText(numberFormat.format(customerReportDTO.getAmount()));
        tv1.setText(numberFormat.format(customerReportDTO.getAmountPaid()));
        tv2.setText(numberFormat.format(customerReportDTO.getOutstandingBalance()));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        if(item.equals("custom"))
        {
            LinearLayout txtView = (LinearLayout)findViewById(R.id.selectDates);
            txtView.setVisibility(View.VISIBLE);
        }
        else
        {
            LinearLayout txtView = (LinearLayout)findViewById(R.id.selectDates);
            txtView.setVisibility(View.INVISIBLE);
        }
        //Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {
        // TODO Auto-generated method stub
    }
}