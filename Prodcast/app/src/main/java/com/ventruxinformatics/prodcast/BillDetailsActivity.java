package com.ventruxinformatics.prodcast;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import businessObjects.connect.ProdcastServiceManager;
import businessObjects.domain.Order;
import businessObjects.dto.CustomerReportDTO;
import businessObjects.dto.OrderDTO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BillDetailsActivity extends AppCompatActivity {

    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_details);

        context=this;
        // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);

        Call<OrderDTO> billDetailsDTO = new ProdcastServiceManager().getClient().getBillDetails(875,626,"D");
        billDetailsDTO.enqueue(new Callback<OrderDTO>() {
            @Override
            public void onResponse(Call<OrderDTO> call, Response<OrderDTO> response) {
                String responseString = null;
                OrderDTO dto = response.body();
                if (dto.isError()) {

                    Toast.makeText(context, dto.getErrorMessage(), Toast.LENGTH_LONG).show();
                } else {
                    Order order=dto.getOrder();
                    Toast.makeText(context, "vvgfv", Toast.LENGTH_LONG).show();



                }

            }

            @Override
            public void onFailure(Call<OrderDTO> call, Throwable t) {
                t.printStackTrace();

            }
        });
    }

}
