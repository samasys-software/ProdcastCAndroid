package com.ventruxinformatics.prodcast;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import businessObjects.connect.ProdcastServiceManager;

import businessObjects.SessionInformations;
import businessObjects.domain.CustomersLogin;
import businessObjects.dto.AdminDTO;
import businessObjects.dto.CustomerLoginDTO;
import businessObjects.font_design.NewEditText;
import businessObjects.font_design.NewTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyPinActivity extends ProdcastCBaseActivity {
    NewEditText verificationCode;
    Button verify;
    NewTextView resendConfirmationCode;
    Context context;

    @Override
    public String getProdcastTitle() {
        return "Verify Code";
    }

    @Override
    public boolean getCompanyName() {
        return false;
    }


    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_pin);
        context=this;
        progressDialog=getProgressDialog(this);
        verificationCode=(NewEditText) findViewById(R.id.verficationCode);
        verify=(Button) findViewById(R.id.verify);
        resendConfirmationCode=(NewTextView)findViewById(R.id.resendConfirmationCode);
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptVerify();
            }
        });
        resendConfirmationCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resendCode();
            }
        });
    }

    public void attemptVerify(){
        String  code=verificationCode.getText().toString();
        if(TextUtils.isEmpty(code)){
            verificationCode.setError(getString(R.string.error_field_required));
            verificationCode.requestFocus();
        }
        else{
            long accessId=SessionInformations.getInstance().getRegisteredCustomer().getConfirmationId();
            progressDialog.show();
            Call<CustomerLoginDTO<CustomersLogin>> verificationDTO = new ProdcastServiceManager().getClient().verify( accessId,code);

            verificationDTO.enqueue(new Callback<CustomerLoginDTO<CustomersLogin>>() {
                @Override
                public void onResponse(Call<CustomerLoginDTO<CustomersLogin>> call, Response<CustomerLoginDTO<CustomersLogin>> response) {
                    String responseString = null;
                    CustomerLoginDTO<CustomersLogin> dto = response.body();
                    if(dto.isError())
                    {
                     //   System.out.println(dto.getErrorMessage());
                        getErrorBox(context,dto.getErrorMessage()).show();
                        progressDialog.dismiss();
                    }
                    else{

                     //  Toast.makeText(context,"Successfully Verified",Toast.LENGTH_LONG).show();
                        CustomersLogin cust1 = dto.getResult();

                        SessionInformations.getInstance().setCustomerDetails(cust1);
                        Intent i = new Intent(VerifyPinActivity.this,EditRegistrationActivity.class);
                        Bundle extras = new Bundle();
                        extras.putString("status", "save");
                        i.putExtras(extras);
                        startActivity(i);
                        progressDialog.dismiss();

                    }

                }

                @Override
                public void onFailure(Call<CustomerLoginDTO<CustomersLogin>> call, Throwable t) {
                    t.printStackTrace();
                    progressDialog.dismiss();
                    getAlertBox(context).show();

                }
            });


        }
    }

    public void resendCode(){
            long accessId=SessionInformations.getInstance().getRegisteredCustomer().getConfirmationId();
        progressDialog.show();
            Call<AdminDTO> verificationDTO = new ProdcastServiceManager().getClient().resendConfirmationCode( accessId);

            verificationDTO.enqueue(new Callback<AdminDTO>() {
                @Override
                public void onResponse(Call<AdminDTO> call, Response<AdminDTO> response) {
                    String responseString = null;
                    AdminDTO dto = response.body();
                    if(dto.isError())
                    {
                      //  System.out.println(dto.getErrorMessage());
                        progressDialog.dismiss();
                        getErrorBox(context,dto.getErrorMessage());
                    }
                    else{

                        Toast.makeText(context,"Code Has Been Send To Your Registered Mobile Number",Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();

                    }

                }

                @Override
                public void onFailure(Call<AdminDTO> call, Throwable t) {
                    t.printStackTrace();
                    progressDialog.dismiss();
                    getAlertBox(context).show();

                }
            });


        }


}
