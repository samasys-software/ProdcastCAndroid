package com.ventruxinformatics.prodcast;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

import businessObjects.connect.ProdcastServiceManager;
import businessObjects.domain.Country;
import businessObjects.domain.CustomerRegistration;

import businessObjects.SessionInformations;
import businessObjects.dto.CustomerLoginDTO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends ProdcastCBaseActivity {
    Spinner country;
    Context context;
    boolean cancel=false;
    EditText mobilePhone,pinNumber,confirmPin;
    Button register,clear;
    View focusView = null;

    @Override
    public String getProdcastTitle(){
        return "Registration";
    }



    @Override
    public boolean getCompanyName(){
        return false;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_cust);
        context=this;
       // String[] country = { "IN", "USA"  };
        country = (Spinner) findViewById(R.id.countryRegister);
        mobilePhone = (EditText) findViewById(R.id.MobileNumber);
        pinNumber = (EditText) findViewById(R.id.pin);
        confirmPin = (EditText) findViewById(R.id.confirmPin);
        register = (Button) findViewById(R.id.registeration);
        clear = (Button) findViewById(R.id.clear);
        List<Country> countryList=SessionInformations.getInstance().getCountries();
        Country defaultCountry = new Country();
        defaultCountry.setCountryId("");
        defaultCountry.setCountryName("Select Country");
        countryList.add(0, defaultCountry  );
        ArrayAdapter<Country> adapter = new ArrayAdapter<Country>(RegisterActivity.this, android.R.layout.simple_list_item_1, countryList);
        country.setAdapter(adapter);


  /*
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,country);

        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);
*/


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
        int ctry = country.getSelectedItemPosition();
        Country selectedCountry = (Country) country.getSelectedItem();
        String selectedCountryId = selectedCountry.getCountryId();

        String mobileNumber=mobilePhone.getText().toString();
        String pin=pinNumber.getText().toString();
        String confirmPinNumber=confirmPin.getText().toString();
        boolean cancelled=checkValid(ctry,mobileNumber,pin,confirmPinNumber);
        if(cancelled){
            focusView.requestFocus();
            return;
        }
        else {
            Call<CustomerLoginDTO<CustomerRegistration>> registrationDTO = new ProdcastServiceManager().getClient().register(selectedCountryId, mobileNumber, pin);

            registrationDTO.enqueue(new Callback<CustomerLoginDTO<CustomerRegistration>>() {
                @Override
                public void onResponse(Call<CustomerLoginDTO<CustomerRegistration>> call, Response<CustomerLoginDTO<CustomerRegistration>> response) {
                    String responseString = null;
                    CustomerLoginDTO<CustomerRegistration> dto = response.body();
                    if (dto.isError()) {
                        System.out.println(dto.getErrorMessage());
                    } else {

                        Toast.makeText(context, "Successfully Registered", Toast.LENGTH_LONG).show();
                        //CustomerRegistration cust1 = dto.getResult();
                        SessionInformations.getInstance().setRegisteredCustomer(dto.getResult());
                        Intent intent = new Intent(RegisterActivity.this, VerifyPinActivity.class);
                        startActivity(intent);

                    }

                }

                @Override
                public void onFailure(Call<CustomerLoginDTO<CustomerRegistration>> call, Throwable t) {
                    t.printStackTrace();

                }
            });
        }


    }



    public void attemptClear(){

    }

    public boolean checkValid(int ctry,String mobileNumber,String pin,String confirmPinNumber){
        //cancel=false;
        if(ctry<=0){
            focusView=country;
            cancel=true;
            return cancel;
        }
        if(TextUtils.isEmpty(mobileNumber)){
            mobilePhone.setError(getString(R.string.error_field_required));
            focusView=mobilePhone;
            cancel=true;
            return cancel;
        }
        if(TextUtils.isEmpty(pin)){
            pinNumber.setError(getString(R.string.error_field_required));
            focusView=pinNumber;
            cancel=true;
            return cancel;
        }
        if (!LoginActivity.isPasswordValid(pin)) {
            pinNumber.setError(getString(R.string.error_invalid_password));
            focusView = pinNumber;
            cancel = true;
            return cancel;
        }
        if(confirmPinNumber.equals(pin))
        {
            confirmPin.setError(getString(R.string.error_field_required));
            focusView=confirmPin;
            cancel=true;
            return cancel;
        }
        return cancel;


    }



}
