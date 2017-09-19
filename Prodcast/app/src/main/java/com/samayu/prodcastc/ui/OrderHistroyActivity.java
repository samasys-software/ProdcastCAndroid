package com.samayu.prodcastc.ui;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.samayu.prodcastc.businessObjects.GlobalUsage;
import com.samayu.prodcastc.businessObjects.SessionInfo;
import com.samayu.prodcastc.businessObjects.connect.ProdcastServiceManager;
import com.samayu.prodcastc.businessObjects.domain.Distributor;
import com.samayu.prodcastc.businessObjects.domain.EmployeeDetails;
import com.samayu.prodcastc.businessObjects.domain.Order;
import com.samayu.prodcastc.businessObjects.dto.CustomerReportDTO;
import com.samayu.prodcastc.R;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderHistroyActivity extends ProdcastCBaseActivity
{
    Context context;

    Spinner store;
    Button report;
    ListView billDetailsList;
    NumberFormat numberFormat = GlobalUsage.getNumberFormat();

    String cusReportType = "SummaryReport";
    ProgressDialog progressDialog;

    TextView totalAmount, totalPaid, totalBalance,
            orderTotal, orderBalance,startDate,endDate;
    TextView billAmountCurrency, totalPaidCurrency, totalBalanceCurrency,
            orderTotalCurrency,orderBalanceCurrency ;
    RelativeLayout txtView, txtView1;
    LinearLayout selectDates;

    private ImageButton startDatePicker;
    private ImageButton endDatePicker;

    private DatePickerDialog.OnDateSetListener mDateSetListener1;
    private DatePickerDialog.OnDateSetListener mDateSetListener2;

    @Override
    public String getProdcastTitle()
    {
        return "Order History";
    }

    @Override
    public boolean getCompanyName()
    {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_histroy);
        context = this;
        progressDialog = getProgressDialog(this);
        billDetailsList = (ListView) findViewById(R.id.billDetailsList);

        report = (Button) findViewById(R.id.report);
        store = (Spinner) findViewById(R.id.storeSpinner);
        totalAmount = (TextView) findViewById(R.id.billAmt);
        totalPaid = (TextView) findViewById(R.id.totalPaid);
        totalBalance = (TextView) findViewById(R.id.totBalance);
        orderTotal = (TextView) findViewById(R.id.orderTotal);
        orderBalance = (TextView) findViewById(R.id.orderBalance);
        txtView1 = (RelativeLayout) findViewById(R.id.summaryTable);
        txtView = (RelativeLayout) findViewById(R.id.billDetailsTable);
        selectDates = (LinearLayout) findViewById(R.id.selectDates);
        startDate=(TextView)findViewById(R.id.startDate);
        endDate=(TextView)findViewById(R.id.endDate);

        startDatePicker=(ImageButton)findViewById(R.id.startDatePicker);
        endDatePicker=(ImageButton)findViewById(R.id.endDatePicker);

        billAmountCurrency = (TextView) findViewById(R.id.billAmountCurrency);
        totalPaidCurrency = (TextView) findViewById(R.id.totalPaidCurrency);
        totalBalanceCurrency = (TextView) findViewById(R.id.totalBalanceCurrency);

        orderBalanceCurrency = (TextView) findViewById(R.id.orderBalanceCurrency);
        orderTotalCurrency = (TextView) findViewById(R.id.orderTotalCurrency);

        Distributor distributor = SessionInfo.getInstance().getEmployee().getDistributor();

        String dist = distributor.getCompanyName();
        List<String> allDist = new ArrayList<String>();
        allDist.add(dist);

        ArrayAdapter storeDistributors = new ArrayAdapter(this, R.layout.drop_down_list, allDist);
        storeDistributors.setDropDownViewResource(R.layout.drop_down_list);
        final String currencySymbol = distributor.getCurrencySymbol();
        billAmountCurrency.setText("(" + currencySymbol + ")");
        totalPaidCurrency.setText("(" + currencySymbol + ")");
        totalBalanceCurrency.setText("(" + currencySymbol + ")");

        orderTotalCurrency.setText("(" + currencySymbol + ")");
        orderBalanceCurrency.setText("(" + currencySymbol + ")");

        //Setting the ArrayAdapter data on the Spinner
        store.setAdapter(storeDistributors);

        // Spinner element
        final Spinner spinner = (Spinner) findViewById(R.id.reportDateSpinner);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("today");
        categories.add("yesterday");
        categories.add("week");
        categories.add("custom");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.drop_down_list, categories);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        startDatePicker.setOnClickListener(new View.OnClickListener()
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
                        mDateSetListener1,
                        year1,month1,day1);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        endDatePicker.setOnClickListener(new View.OnClickListener()
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
                        mDateSetListener2,
                        year1,month1,day1);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });


        /*startDate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Calendar cal1 = Calendar.getInstance();
                int year1 = cal1.get(Calendar.YEAR);
                int month1 = cal1.get(Calendar.MONTH);
                int day1 = cal1.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        OrderHistroyActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener1,
                        year1,month1,day1);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                // startDatePicker.setVisibility(View.VISIBLE);
            }
        });*/
        //startDatePicker

        /*endDate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Calendar cal2 = Calendar.getInstance();
                int year2 = cal2.get(Calendar.YEAR);
                int month2 = cal2.get(Calendar.MONTH);
                int day2 = cal2.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        OrderHistroyActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener2,
                        year2,month2,day2);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                //endDatePicker.setVisibility(View.VISIBLE);
            }
        });*/

       /* endDatePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
                endDate.setText( datePicker.getDayOfMonth()+"/"+(datePicker.getMonth()+1)+"/"+datePicker.getYear());
                datePicker.setVisibility(View.GONE);
            }
        });*/


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String item = parent.getSelectedItem().toString();

                if (item.equals("custom"))
                {
                    selectDates.setVisibility(View.VISIBLE);
                    report.setVisibility(View.VISIBLE);
                }
                else
                {
                    selectDates.setVisibility(View.GONE);
                    report.setVisibility(View.GONE);
                    serviceResponse(item,null,null);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        mDateSetListener1 = new DatePickerDialog.OnDateSetListener()
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

                String date1 = formattedDayOfMonth1 + "/" + formattedMonth1 + "/" + syear;
                startDate.setText(date1);
            }
        };

        mDateSetListener2 = new DatePickerDialog.OnDateSetListener()
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

                String date2 = formattedDayOfMonth2 + "/" + formattedMonth2 + "/" + eyear;
                endDate.setText(date2);
            }
        };

        report.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String reportType = spinner.getSelectedItem().toString();
                String startDate1=startDate.getText().toString();
                String endDate1=endDate.getText().toString();
                if (reportType.equals("custom") && (startDate1.equals("Start Date") || endDate1.equals("End Date")))
                {
                    Toast.makeText(context, "Please fill mandatory fields", Toast.LENGTH_LONG).show();
                }
                else
                {
                    SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
                    Date todayDate = new Date();
                    String thisDate = currentDate.format(todayDate);

                    if (startDate1.compareTo(endDate1) > 0 || startDate1.compareTo(thisDate) > 0 || endDate1.compareTo(thisDate) > 0)
                    {
                        Toast.makeText(context, "Please select valid dates", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        serviceResponse(reportType,startDate1,endDate1);
                    }
                }
            }
        });
    }

    public void serviceResponse(String reportType, String startDate, String endDate)
    {

        String reportId = cusReportType;

        EmployeeDetails emp = SessionInfo.getInstance().getEmployee();
        Distributor distributor = emp.getDistributor();

        long distributorId = distributor.getDistributorId();
        long accessId = SessionInfo.getInstance().getCustomerDetails().getAccessId();

        progressDialog.show();
        Call<CustomerReportDTO> reportDTO = new ProdcastServiceManager().getClient().reportForCustomers(reportType, accessId, startDate, endDate, distributorId, reportId);
        reportDTO.enqueue(new Callback<CustomerReportDTO>()
        {
            @Override
            public void onResponse(Call<CustomerReportDTO> call, Response<CustomerReportDTO> response)
            {
                if (response.isSuccessful())
                {
                    CustomerReportDTO dto = response.body();

                    if (dto.isError())
                    {
                        getErrorBox(context, dto.getErrorMessage()).show();
                        progressDialog.dismiss();
                    }
                    else
                    {
                        List<Order> customerReport = dto.getResult();

                        if (customerReport.size() > 0)
                        {
                            billDetailsList.setAdapter(new CustomerReportAdaptor(OrderHistroyActivity.this, customerReport));
                            setSummaryDetails(dto);

                            txtView.setVisibility(View.VISIBLE);
                            txtView1.setVisibility(View.VISIBLE);

                            billDetailsList.setOnItemClickListener(new AdapterView.OnItemClickListener()
                            {

                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                                {
                                    TextView billNumber = (TextView) findViewById(R.id.cusBillNo);
                                    String selectedBillIndex = billNumber.getText().toString();

                                    Intent intent = new Intent(OrderHistroyActivity.this, BillDetailsActivity.class);
                                    intent.putExtra("billId", selectedBillIndex);
                                    startActivity(intent);
                                }
                            });
                        }
                        else
                        {
                            Toast.makeText(context, "No order was placed during this time..!", Toast.LENGTH_LONG).show();

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

    public void setSummaryDetails(CustomerReportDTO customerReportDTO)
    {
        TextView tv = (TextView) findViewById(R.id.totBillAmt);
        TextView tv1 = (TextView) findViewById(R.id.totAmtPait);
        TextView tv2 = (TextView) findViewById(R.id.sumBalance);

        tv.setText(numberFormat.format(customerReportDTO.getAmount()));
        tv1.setText(numberFormat.format(customerReportDTO.getAmountPaid()));
        tv2.setText(numberFormat.format(customerReportDTO.getOutstandingBalance()));
    }
}