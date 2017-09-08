package com.ventruxinformatics.prodcast;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import businessObjects.connect.ProdcastServiceManager;

import businessObjects.SessionInformations;
import businessObjects.domain.Country;
import businessObjects.domain.NewCustomerRegistrationDetails;
import businessObjects.dto.CustomerListDTO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.R.attr.password;

public class EditRegistrationActivity extends ProdcastCBaseActivity {
    EditText firstName,lastName,emailAddress,billingAddress1,billingAddress2,billingAddress3,homePhoneNumber,city,state,postalCode;
    Spinner country;
    CheckBox smsAllowed;
    TextView skip;
    Button edit,cancel;
    boolean valid = false;
    View focusView = null;
    Context context;
    String customerId=null;
    boolean companyName=false;
    ProgressDialog progressDialog;



    @Override
    public String getProdcastTitle() {
        return "Edit Registration";
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_registration);
        context=this;
        progressDialog=getProgressDialog(context);
        Intent intent = getIntent();

        //String[] countries = { "IN", "USA"  };
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
        cancel=(Button)findViewById(R.id.reset);
        List<Country> countryList=SessionInformations.getInstance().getCountries();
        final ArrayAdapter<Country> adapter = new ArrayAdapter<Country>(EditRegistrationActivity.this, android.R.layout.simple_list_item_1, countryList);
        country.setAdapter(adapter);

        Bundle bundle = intent.getExtras();



        long accessId=SessionInformations.getInstance().getCustomerDetails().getAccessId();
        progressDialog.show();
        Call<CustomerListDTO<NewCustomerRegistrationDetails>> getNewCustomerDTO = new ProdcastServiceManager().getClient().getNewCustomerRegistrationDetails(accessId);
        getNewCustomerDTO.enqueue(new Callback<CustomerListDTO<NewCustomerRegistrationDetails>>() {
            @Override
            public void onResponse(Call<CustomerListDTO<NewCustomerRegistrationDetails>> call, Response<CustomerListDTO<NewCustomerRegistrationDetails>> response) {
                String responseString = null;
                CustomerListDTO<NewCustomerRegistrationDetails> dto = response.body();
                if (dto.isError()) {

                  //  Toast.makeText(context, dto.getErrorMessage(), Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                } else {

                    NewCustomerRegistrationDetails newCustomerRegistrationDetails=dto.getResult();
                    if(newCustomerRegistrationDetails!=null){
                        firstName.setText(newCustomerRegistrationDetails.getFirstName());
                        lastName.setText(newCustomerRegistrationDetails.getLastName());
                        emailAddress.setText(newCustomerRegistrationDetails.getEmail());
                        billingAddress1.setText(newCustomerRegistrationDetails.getAddress1());
                        billingAddress2.setText(newCustomerRegistrationDetails.getAddress2());
                        billingAddress3.setText(newCustomerRegistrationDetails.getAddress3());
                        homePhoneNumber.setText(newCustomerRegistrationDetails.getWorkPhone());
                        city.setText(newCustomerRegistrationDetails.getCity());
                        state.setText(newCustomerRegistrationDetails.getState());
                        postalCode.setText(newCustomerRegistrationDetails.getPostalCode());

                       // country.setText(newCustomerRegistrationDetails.getCountry());
                        //country.(newCustomerRegistrationDetails.getFirstName());
                        //skip.setText(newCustomerRegistrationDetails.getFirstName());
                        boolean checked=false;
                        if(newCustomerRegistrationDetails.getSmsAllowed().equals("1")){
                            checked=true;
                        }
                        smsAllowed.setChecked(checked);
                        customerId=String.valueOf(newCustomerRegistrationDetails.getCustomerId());


                        progressDialog.dismiss();
                    }
                    else{
                        customerId=null;
                        progressDialog.dismiss();

                    }

                   // Toast.makeText(context, "customerId distributorId", Toast.LENGTH_LONG).show();



                }

            }

            @Override
            public void onFailure(Call<CustomerListDTO<NewCustomerRegistrationDetails>> call, Throwable t) {
                t.printStackTrace();
                getAlertBox(context).show();
                progressDialog.dismiss();


            }
        });

        // 5. get status value from bundle
        String status = bundle.getString("status");
        if(status.equals("edit")){
            skip.setVisibility(View.GONE);
            cancel.setVisibility(View.GONE);
            companyName=true;
        }
        /*ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,countries);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        country.setAdapter(aa);*/

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

                editRegistration(customerId);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset();
            }
        });

    }


    public void editRegistration(String customerId){
        //String customerId=null;
        String firstName1=firstName.getText().toString();
        String lastName1=lastName.getText().toString();
        String email=emailAddress.getText().toString();
        String address1=billingAddress1.getText().toString();
        String address2=billingAddress2.getText().toString();
        String address3=billingAddress3.getText().toString();
        String phone=homePhoneNumber.getText().toString();
        String city1=city.getText().toString();
        String state1=state.getText().toString();
        String code=postalCode.getText().toString();
        Boolean sms;
        int ctry = country.getSelectedItemPosition();
        Country selectedCountry = (Country) country.getSelectedItem();
        String selectedCountryId = selectedCountry.getCountryId();

        if(smsAllowed.isChecked()){
            sms=true;
        }
        else{
            sms=false;
        }
        boolean validation=  validation(firstName1,lastName1,email,address1,address2,phone,city1,state1,code,ctry);
        if (validation) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
            return;
        }
        validation(firstName1,lastName1,email,address1,address2,phone,code,city1,state1,ctry);

        progressDialog.show();
      String cellPhone=SessionInformations.getInstance().getCustomerDetails().getUsername();
        Call<CustomerListDTO> saveCustomerDTO = new ProdcastServiceManager().getClient().saveNewCustomer(customerId,firstName1,
                lastName1,email,cellPhone,phone,address1,address2,address3,city1,state1,selectedCountryId,code,sms);
        saveCustomerDTO.enqueue(new Callback<CustomerListDTO>() {
            @Override
            public void onResponse(Call<CustomerListDTO> call, Response<CustomerListDTO> response) {
                String responseString = null;
                CustomerListDTO dto = response.body();
                if (dto.isError()) {

                   // Toast.makeText(context, dto.getErrorMessage(), Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                } else {

                   // Toast.makeText(context, "customerId distributorId", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();



                }

            }

            @Override
            public void onFailure(Call<CustomerListDTO> call, Throwable t) {
                t.printStackTrace();
                getAlertBox(context).show();
                progressDialog.dismiss();

            }
        });
    }
    public boolean validation(String firstName1,String lastName1,String email,String address1,String address2,String phone,String city1,String state1,String code,int ctry){
      //  public boolean validation(){

            // Reset errors.
        valid = false;
        firstName.setError(null);
        lastName.setError(null);
        emailAddress.setError(null);
        billingAddress1.setError(null);
        billingAddress2.setError(null);
        billingAddress3.setError(null);
        homePhoneNumber.setError(null);
        city.setError(null);
        state.setError(null);
        postalCode.setError(null);
        if(TextUtils.isEmpty(firstName1)){
            firstName.setError("This Field is Required");
            focusView = firstName;
            valid = true;
            return  valid;
        }
        if(TextUtils.isEmpty(lastName1)){
            lastName.setError("This Field is Required");
            focusView = lastName;
            valid = true;
            return  valid;
        }
        if(TextUtils.isEmpty(email)){
            emailAddress.setError("This Field is Required");
            focusView = emailAddress;
            valid = true;
            return  valid;
        }
       /* if (!isValidEmail(email)) {
            emailAddress.setError("Invalid Email");
            valid = true;
            return  valid;
        }*/
        if(TextUtils.isEmpty(address1)){
            billingAddress1.setError("This Field is Required");
            focusView = billingAddress1;
            valid = true;
            return  valid;
        }
        if(TextUtils.isEmpty(address2)){
            billingAddress2.setError("This Field is Required");
            focusView = billingAddress2;
            valid = true;
            return  valid;
        }
        if(TextUtils.isEmpty(city1)){
            city.setError("This Field is Required");
            focusView = city;
            valid = true;
            return  valid;
        }
        if(TextUtils.isEmpty(state1)){
            state.setError("This Field is Required");
            focusView = state;
            valid = true;
            return  valid;
        }

        if(TextUtils.isEmpty(phone)){
            homePhoneNumber.setError("This Field is Required");
            focusView = homePhoneNumber;
            valid = true;
            return  valid;
        }
        if(TextUtils.isEmpty(code)){
            postalCode.setError("This Field is Required");
            focusView = postalCode;
            valid = true;
            return  valid;
        }
        if (ctry<= 0) {
         //   country.setError("This Field is Required");
            focusView=country;
            valid = true;
            return  valid;
        }
        return valid;
    }

    // validating email id
   /* private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }*/
    public void reset(){
        firstName.setText("");
        lastName.setText("");
        emailAddress.setText("");
        billingAddress1.setText("");
        billingAddress2.setText("");
        billingAddress3.setText("");
        homePhoneNumber.setText("");
        city.setText("");
        state.setText("");
        postalCode.setText("");

    }
    @Override
    public boolean getCompanyName() {
        return companyName;
    }


}