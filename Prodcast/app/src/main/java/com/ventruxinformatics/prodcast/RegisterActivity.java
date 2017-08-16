package com.ventruxinformatics.prodcast;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.ventruxinformatics.prodcast.connect.ProdcastServiceManager;
import com.ventruxinformatics.prodcast.domain.AdminDTO;
import com.ventruxinformatics.prodcast.domain.CustomerLoginDTO;
import com.ventruxinformatics.prodcast.domain.CustomerRegistration;

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

                    }
                    else{
                        Toast.makeText(context,"Successfully Registered",Toast.LENGTH_LONG).show();
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
