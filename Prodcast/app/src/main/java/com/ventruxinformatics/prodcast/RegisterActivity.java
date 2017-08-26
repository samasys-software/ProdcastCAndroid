package com.ventruxinformatics.prodcast;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import businessObjects.connect.ProdcastServiceManager;
import businessObjects.domain.CustomerRegistration;

import businessObjects.SessionInformations;
import businessObjects.dto.CustomerLoginDTO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    Spinner spin;
    Context context;
    EditText mobilePhone,pinNumber,confirmPin;
    Button register,clear;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        context=this;
        String[] country = { "IN", "USA"  };
        spin = (Spinner) findViewById(R.id.spinner3);
        mobilePhone = (EditText) findViewById(R.id.MobileNumber);
        pinNumber = (EditText) findViewById(R.id.pin);
        confirmPin = (EditText) findViewById(R.id.confirmPin);
        register = (Button) findViewById(R.id.register);
        clear = (Button) findViewById(R.id.clear);

        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,country);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptClear();
            }
        });
    }
    public void attemptRegister(){
        String country=spin.getSelectedItem().toString();
        String mobileNumber=mobilePhone.getText().toString();
        String pin=pinNumber.getText().toString();
        String confirmPinNumber=confirmPin.getText().toString();
        Call<CustomerLoginDTO<CustomerRegistration>> registrationDTO = new ProdcastServiceManager().getClient().register( country,mobileNumber,pin );

        registrationDTO.enqueue(new Callback<CustomerLoginDTO<CustomerRegistration>>() {
                @Override
                public void onResponse(Call<CustomerLoginDTO<CustomerRegistration>> call, Response<CustomerLoginDTO<CustomerRegistration>> response) {
                    String responseString = null;
                    CustomerLoginDTO<CustomerRegistration> dto = response.body();
                    if(dto.isError())
                    {
                        System.out.println(dto.getErrorMessage());
                    }
                    else{

                        Toast.makeText(context,"Successfully Registered",Toast.LENGTH_LONG).show();
                        CustomerRegistration cust1 = dto.getResult();
                        SessionInformations.getInstance().setRegisteredCustomer(cust1);
                        Intent intent=new Intent(RegisterActivity.this,VerifyPinActivity.class);
                        startActivity(intent);

                    }

                }

                @Override
                public void onFailure(Call<CustomerLoginDTO<CustomerRegistration>> call, Throwable t) {
                    t.printStackTrace();

                }
            });


    }



    public void attemptClear(){

    }


}
