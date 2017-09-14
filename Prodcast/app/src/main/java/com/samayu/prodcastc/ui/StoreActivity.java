package com.samayu.prodcastc.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.samayu.prodcastc.businessObjects.SessionInfo;
import com.samayu.prodcastc.businessObjects.connect.ProdcastServiceManager;
import com.samayu.prodcastc.businessObjects.domain.Distributor;
import com.samayu.prodcastc.businessObjects.domain.EmployeeDetails;

import java.util.ArrayList;
import java.util.List;

import com.samayu.prodcastc.businessObjects.dto.AdminDTO;
import com.samayu.prodcastc.businessObjects.dto.CustomerLoginDTO;
import com.ventruxinformatics.prodcast.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoreActivity extends ProdcastCBaseActivity {

    ListView listhistory;
    Context context;
    List<Distributor> distributors = new ArrayList<>();
    List<Distributor> dist=null;
    List<Distributor> distOpen=null;
    ProgressDialog mProgressDialog;


    @Override
    public  String getProdcastTitle(){
        return "Change Store";
    }


    @Override
    public  boolean getCompanyName(){
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        context = this;
        listhistory = (ListView) findViewById(R.id.listOfStores);
        final long accessId = SessionInfo.getInstance().getCustomerDetails().getAccessId();
      //  System.out.println(accessId);

        mProgressDialog=getProgressDialog(context);
        mProgressDialog.show();
        Call<CustomerLoginDTO> distributorDTO = new ProdcastServiceManager().getClient().getAllDistributors(accessId);
        distributorDTO.enqueue(new Callback<CustomerLoginDTO>() {
            @Override
            public void onResponse(Call<CustomerLoginDTO> call, Response<CustomerLoginDTO> response) {
                try {


                    CustomerLoginDTO dto = response.body();
                //    System.out.println(dto.toString());
                    if (dto.isError()) {
                     //   Toast.makeText(context, "Nothing To show", Toast.LENGTH_LONG).show()

                        getErrorBox(context,dto.getErrorMessage()).show();
                        mProgressDialog.dismiss();
                    } else {


                        Distributor distributor;
                        int count = -1;
                        dist = dto.getDistributors();

                        distOpen = dto.getDistributorsPublic();
                        distributors.addAll(dist);
                        distributors.addAll(distOpen);

                        SessionInfo.getInstance().setAllDistributors(distributors);
                        listhistory.setAdapter(new CustomStoreAdapter(StoreActivity.this, distributors));
                        mProgressDialog.dismiss();
                    }


                    //if (mProgressDialog.isShowing())
                       // mProgressDialog.dismiss();

                    listhistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                          long distributorId = SessionInfo.getInstance().getAllDistributors().get(position).getDistributorId();
                            mProgressDialog.show();

                            Call<AdminDTO<EmployeeDetails>> getCustomerDTO = new ProdcastServiceManager().getClient().getCustomerDetails(accessId, distributorId);
                            getCustomerDTO.enqueue(new Callback<AdminDTO<EmployeeDetails>>() {
                                @Override
                                public void onResponse(Call<AdminDTO<EmployeeDetails>> call, Response<AdminDTO<EmployeeDetails>> response) {
                                    String responseString = null;
                                    AdminDTO<EmployeeDetails> dto = response.body();
                                    if (dto.isError()) {

                                      //  Toast.makeText(context, dto.getErrorMessage(), Toast.LENGTH_LONG).show();
                                        getErrorBox(context,dto.getErrorMessage()).show();
                                        mProgressDialog.dismiss();
                                        getErrorBox(context,dto.getErrorMessage());
                                    } else {
                                        SessionInfo.getInstance().setEmployee(dto.getResult());
                                        EmployeeDetails emp = SessionInfo.getInstance().getEmployee();
                                     //   Toast.makeText(context, "customerId " + emp.getCustomerId() + " distributorId" + emp.getDistributor().getDistributorId(), Toast.LENGTH_LONG).show();


                                        Intent intent = new Intent(StoreActivity.this, HomeActivity.class);
                                        startActivity(intent);

                                        mProgressDialog.dismiss();


                                    }

                                }

                                @Override
                                public void onFailure(Call<AdminDTO<EmployeeDetails>> call, Throwable t) {
                                    t.printStackTrace();
                                    mProgressDialog.dismiss();
                                    getAlertBox(context).show();

                                }
                            });
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }

            @Override
            public void onFailure(Call<CustomerLoginDTO> call, Throwable t) {
                t.printStackTrace();
                t.printStackTrace();
                mProgressDialog.dismiss();
                getAlertBox(context).show();

            }
        });


    }
}


