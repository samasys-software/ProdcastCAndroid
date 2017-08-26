package com.ventruxinformatics.prodcast;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import businessObjects.connect.ProdcastServiceManager;
import businessObjects.domain.Distributor;
import businessObjects.domain.EmployeeDetails;

import java.util.ArrayList;
import java.util.List;

import businessObjects.SessionInformations;
import businessObjects.dto.AdminDTO;
import businessObjects.dto.CustomerLoginDTO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoreActivity extends AppCompatActivity {

    ListView listhistory;
    Context context;
    List<Distributor> distributors = new ArrayList<>();
    List<Distributor> dist=null;
    List<Distributor> distOpen=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        context = this;
        listhistory = (ListView) findViewById(R.id.listOfStores);
        final long accessId = SessionInformations.getInstance().getCustomerDetails().getAccessId();
        System.out.println(accessId);
        final ProgressDialog mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("One Moment Please");
                mProgressDialog.show();
        Call<CustomerLoginDTO> distributorDTO = new ProdcastServiceManager().getClient().getAllDistributors(accessId);
        distributorDTO.enqueue(new Callback<CustomerLoginDTO>() {
            @Override
            public void onResponse(Call<CustomerLoginDTO> call, Response<CustomerLoginDTO> response) {
                try {
                    String responseString = null;


                    CustomerLoginDTO dto = response.body();
                    System.out.println(dto.toString());
                    if (dto.isError()) {
                        Toast.makeText(context, "Nothing To show", Toast.LENGTH_LONG).show();
                    } else {


                        Distributor distributor;
                        int count = -1;
                        dist = dto.getDistributors();

                        distOpen = dto.getDistributorsPublic();
                        distributors.addAll(dist);
                        distributors.addAll(distOpen);

                        SessionInformations.getInstance().setAllDistributors(distributors);
                    }
                    listhistory.setAdapter(new CustomStoreList(StoreActivity.this, distributors));
                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();

                    listhistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                          long distributorId = SessionInformations.getInstance().getAllDistributors().get(position).getDistributorId();
                            mProgressDialog.show();

                            Call<AdminDTO<EmployeeDetails>> getCustomerDTO = new ProdcastServiceManager().getClient().getCustomerDetails(accessId, distributorId);
                            getCustomerDTO.enqueue(new Callback<AdminDTO<EmployeeDetails>>() {
                                @Override
                                public void onResponse(Call<AdminDTO<EmployeeDetails>> call, Response<AdminDTO<EmployeeDetails>> response) {
                                    String responseString = null;
                                    AdminDTO<EmployeeDetails> dto = response.body();
                                    if (dto.isError()) {

                                        Toast.makeText(context, dto.getErrorMessage(), Toast.LENGTH_LONG).show();
                                    } else {
                                        SessionInformations.getInstance().setEmployee(dto.getResult());
                                        EmployeeDetails emp = SessionInformations.getInstance().getEmployee();
                                        Toast.makeText(context, "customerId " + emp.getCustomerId() + " distributorId" + emp.getDistributor().getDistributorId(), Toast.LENGTH_LONG).show();
                                        mProgressDialog.dismiss();
                                        Intent intent = new Intent(StoreActivity.this, HomeActivity.class);
                                        startActivity(intent);


                                    }

                                }

                                @Override
                                public void onFailure(Call<AdminDTO<EmployeeDetails>> call, Throwable t) {
                                    t.printStackTrace();

                                }
                            });
                        }
                    });
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<CustomerLoginDTO> call, Throwable t) {
                t.printStackTrace();

            }
        });


    }
}


