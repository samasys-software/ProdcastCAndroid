package com.ventruxinformatics.prodcast;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import businessObjects.connect.ProdcastServiceManager;

import businessObjects.SessionInformations;
import businessObjects.dto.CustomerListDTO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditRegistrationActivity extends AppCompatActivity {
    EditText firstName,lastName,emailAddress,billingAddress1,billingAddress2,billingAddress3,homePhoneNumber,city,state,postalCode;
    Spinner country;
    CheckBox smsAllowed;
    TextView skip;
    Button edit,cancel;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_registration);
        context=this;
        Intent intent = getIntent();

        String[] countries = { "IN", "USA"  };
        firstName=(EditText) findViewById(R.id.firstName);
        lastName=(EditText) findViewById(R.id.latName);
        emailAddress=(EditText) findViewById(R.id.emailAddress);
        billingAddress1=(EditText) findViewById(R.id.billingAddress1);
        billingAddress2=(EditText) findViewById(R.id.billingAddress2);
        billingAddress3=(EditText) findViewById(R.id.billingAddress3);
        homePhoneNumber=(EditText) findViewById(R.id.homePhoneNumber);
        city=(EditText) findViewById(R.id.city);
        state=(EditText) findViewById(R.id.state);
        postalCode=(EditText) findViewById(R.id.postalCode);
        country=(Spinner)findViewById(R.id.editCountry);
        skip=(TextView)findViewById(R.id.skipRegisteration);
        smsAllowed=(CheckBox)findViewById(R.id.smsAllowed);
        edit=(Button)findViewById(R.id.edit);
        cancel=(Button)findViewById(R.id.cancel);
        Bundle bundle = intent.getExtras();

        // 5. get status value from bundle
        String status = bundle.getString("status");
        if(status.equals("edit")){
            skip.setVisibility(View.INVISIBLE);
            edit.setText("Edit");
        }
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,countries);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        country.setAdapter(aa);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(EditRegistrationActivity.this,StoreActivity.class);
                startActivity(intent);
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                editRegistration();
            }
        });


    }
    public void editRegistration(){
        String customerId=null;
        String firstName1=firstName.getText().toString();
        String lastName1=lastName.getText().toString();
        String email=emailAddress.getText().toString();
        String address1=billingAddress1.getText().toString();
        String address2=billingAddress2.getText().toString();
        String address3=billingAddress3.getText().toString();
        String phone=firstName.getText().toString();
        String city1=firstName.getText().toString();
        String state1=firstName.getText().toString();
        String code=firstName.getText().toString();
        Boolean sms;
        String ctry=country.getSelectedItem().toString();
         if(smsAllowed.isChecked()){
            sms=true;
        }
        else{
            sms=false;
        }

      /*  if(TextUtils.isEmpty(firstName1)) {

            firstName.setError(getString(R.string.error_field_required));

        }
        if(TextUtils.isEmpty(lastName1)){
            lastName.setError(getString(R.string.error_field_required));


        }
        if(TextUtils.isEmpty(email)){
            emailAddress.setError(getString(R.string.error_field_required));

        }
        if(TextUtils.isEmpty(address1)){
            billingAddress1.setError(getString(R.string.error_field_required));

        }
        if(TextUtils.isEmpty(address2)){
            billingAddress2.setError(getString(R.string.error_field_required));

        }
        if(TextUtils.isEmpty(phone)){
            homePhoneNumber.setError(getString(R.string.error_field_required));

        }
        if(TextUtils.isEmpty(city1)){
            city.setError(getString(R.string.error_field_required));

        }
        if(TextUtils.isEmpty(state1)) {
            state.setError(getString(R.string.error_field_required));

        }
        if(TextUtils.isEmpty(code)){
            postalCode.setError(getString(R.string.error_field_required));

        }
        */
      String cellPhone=SessionInformations.getInstance().getCustomerDetails().getUsername();
        Call<CustomerListDTO> saveCustomerDTO = new ProdcastServiceManager().getClient().saveNewCustomer(customerId,firstName1,lastName1,email,cellPhone,phone,address1,address2,address3,city1,state1,ctry,code,sms);
        saveCustomerDTO.enqueue(new Callback<CustomerListDTO>() {
            @Override
            public void onResponse(Call<CustomerListDTO> call, Response<CustomerListDTO> response) {
                String responseString = null;
                CustomerListDTO dto = response.body();
                if (dto.isError()) {

                    Toast.makeText(context, dto.getErrorMessage(), Toast.LENGTH_LONG).show();
                } else {

                    Toast.makeText(context, "customerId distributorId", Toast.LENGTH_LONG).show();



                }

            }

            @Override
            public void onFailure(Call<CustomerListDTO> call, Throwable t) {
                t.printStackTrace();

            }
        });
    }
}