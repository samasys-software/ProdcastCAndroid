package com.ventruxinformatics.prodcast;

import android.content.Context;
import android.os.Bundle;


import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import businessObjects.connect.ProdcastServiceManager;
import businessObjects.dto.CustomerListDTO;
import businessObjects.dto.CustomerReportDTO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderHistroyActivity extends AppCompatActivity {
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_histroy);
        context=this;
       // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);

        Call<CustomerReportDTO> reportDTO = new ProdcastServiceManager().getClient().reportForCustomers("today",261,null,null,249,"SummaryReport");
        reportDTO.enqueue(new Callback<CustomerReportDTO>() {
            @Override
            public void onResponse(Call<CustomerReportDTO> call, Response<CustomerReportDTO> response) {
                String responseString = null;
                CustomerReportDTO dto = response.body();
                if (dto.isError()) {

                    Toast.makeText(context, dto.getErrorMessage(), Toast.LENGTH_LONG).show();
                } else {

                    Toast.makeText(context, "", Toast.LENGTH_LONG).show();



                }

            }

            @Override
            public void onFailure(Call<CustomerReportDTO> call, Throwable t) {
                t.printStackTrace();

            }
        });

    }

}
