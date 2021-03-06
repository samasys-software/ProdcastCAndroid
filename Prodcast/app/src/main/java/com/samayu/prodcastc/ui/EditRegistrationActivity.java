package com.samayu.prodcastc.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.samayu.prodcastc.R;
import com.samayu.prodcastc.businessObjects.SessionInfo;
import com.samayu.prodcastc.businessObjects.connect.ProdcastServiceManager;
import com.samayu.prodcastc.businessObjects.domain.Country;
import com.samayu.prodcastc.businessObjects.domain.NewCustomerRegistrationDetails;
import com.samayu.prodcastc.businessObjects.dto.CustomerListDTO;
import com.samayu.prodcastc.businessObjects.dto.ProdcastDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditRegistrationActivity extends ProdcastCBaseActivity {
    EditText firstName,lastName,emailAddress,billingAddress1,billingAddress2,billingAddress3,homePhoneNumber,city,state,postalCode;
    Button reportIssues;
    Spinner country;
    CheckBox smsAllowed;
    Button skip;
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
        skip=(Button) findViewById(R.id.skipRegisteration);
        smsAllowed=(CheckBox)findViewById(R.id.smsAllowed);
        edit=(Button)findViewById(R.id.edit);
        cancel=(Button)findViewById(R.id.reset);

        reportIssues = (Button) findViewById(R.id.reportIssues);

        List<Country> countryList= SessionInfo.getInstance().getCountries();
        final ArrayAdapter<Country> adapter = new ArrayAdapter<Country>(EditRegistrationActivity.this, R.layout.drop_down_list, countryList);
        country.setAdapter(adapter);

        Bundle bundle = intent.getExtras();

        long accessId= SessionInfo.getInstance().getCustomerDetails().getAccessId();
        progressDialog.show();
        Call<CustomerListDTO<NewCustomerRegistrationDetails>> getNewCustomerDTO = new ProdcastServiceManager().getClient().getNewCustomerRegistrationDetails(accessId);
        getNewCustomerDTO.enqueue(new Callback<CustomerListDTO<NewCustomerRegistrationDetails>>() {
            @Override
            public void onResponse(Call<CustomerListDTO<NewCustomerRegistrationDetails>> call, Response<CustomerListDTO<NewCustomerRegistrationDetails>> response) {
                if(response.isSuccessful()) {
                    CustomerListDTO<NewCustomerRegistrationDetails> dto = response.body();
                    if (dto.isError()) {
                        getErrorBox(context,dto.getErrorMessage()).show();

                        //  Toast.makeText(context, dto.getErrorMessage(), Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    } else {

                        NewCustomerRegistrationDetails newCustomerRegistrationDetails = dto.getResult();
                        if (newCustomerRegistrationDetails != null) {
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
                            ArrayAdapter<Country> adapter = (ArrayAdapter<Country>) country.getAdapter();
                            int count = adapter.getCount();
                            for (int i = 0; i < count; i++) {
                                Country aCountry = adapter.getItem(i);

                                if (aCountry.getCountryId().equals(newCustomerRegistrationDetails.getCountry()))
                                //if(aCountry.getCountryName().equalsIgnoreCase(String.valueOf(state)))
                                {
                                    country.setSelection(i);
                                    break;
                                }
                            }

                            // country.setText(newCustomerRegistrationDetails.getCountry());
                            //country.(newCustomerRegistrationDetails.getFirstName());
                            //skip.setText(newCustomerRegistrationDetails.getFirstName());
                            if(newCustomerRegistrationDetails.getSmsAllowed()!=null) {
                                boolean checked = false;

                                if (newCustomerRegistrationDetails.getSmsAllowed().equals("1")) {
                                    checked = true;
                                }
                                smsAllowed.setChecked(checked);
                            }
                            customerId = String.valueOf(newCustomerRegistrationDetails.getCustomerId());


                            progressDialog.dismiss();
                        } else {
                            customerId = null;
                            progressDialog.dismiss();

                        }
                        // Toast.makeText(context, "customerId distributorId", Toast.LENGTH_LONG).show();
                    }
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

        reportIssues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context,R.style.AlertTheme);
                alert.setTitle("Report an Issue");

                LayoutInflater layoutInflater = EditRegistrationActivity.this.getLayoutInflater();
                View layoutView = layoutInflater.inflate(R.layout.report_issue_dialogue,null);
                alert.setView(layoutView);


                final EditText issue = (EditText) layoutView.findViewById(R.id.issue);



                alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });



                alert.setPositiveButton("SUBMIT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //alert.show();

                        /*ServiceSupportDTO serviceSupportDTO = new ServiceSupportDTO();
                        List<ServiceTicket> serviceTickets = new ArrayList<ServiceTicket>();
                       ServiceTicket serviceTicket = new ServiceTicket();
                        serviceTicket.setPhoneNumber(customerPhoneNumber.getText().toString());
                        serviceTicket.setIssue(issue.getText().toString());
                        serviceTicket.setCountryId(customerCountryId.getText().toString());
                        serviceTickets.add(serviceTicket);*/



                        int ctry = country.getSelectedItemPosition();

                        Country selectedCountry = (Country) country.getSelectedItem();
                        String selectedCountryId = selectedCountry.getCountryId();

                        String cellPhone = SessionInfo.getInstance().getCustomerDetails().getUsername();




                        Call<ProdcastDTO> serviceSupportDTOCall = new ProdcastServiceManager().
                                getClient().raiseRequest(cellPhone,
                                issue.getText().toString(), selectedCountryId);
                        serviceSupportDTOCall.enqueue(new Callback<ProdcastDTO>() {
                            @Override
                            public void onResponse(Call<ProdcastDTO> call, Response<ProdcastDTO> response) {
                               /* if(response.isSuccessful()){*/
                                System.out.println("Service response"+"-"+response.toString());
                                ProdcastDTO dto = response.body();
                                System.out.println("service response body"+dto.toString());
                                if(!dto.isError()){
                                    Toast.makeText(context, "Report submitted successfully", Toast.LENGTH_LONG).show();
                                }

                                else
                                {
                                    Toast.makeText(context, "Report not submitted ", Toast.LENGTH_LONG).show();
                                }

                                // }
                            }

                            @Override
                            public void onFailure(Call<ProdcastDTO> call, Throwable t) {
                                t.printStackTrace();
                                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                                alert.setTitle("Oops! Something went Wrong.");
                                alert.setMessage("Connection Timeout.please try again later");


                            }
                        });

                    }
                });
                alert.show();


            }
        });


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
      String cellPhone= SessionInfo.getInstance().getCustomerDetails().getUsername();
        Call<CustomerListDTO> saveCustomerDTO = new ProdcastServiceManager().getClient().saveNewCustomer(customerId,firstName1,
                lastName1,email,cellPhone,phone,address1,address2,address3,city1,state1,selectedCountryId,code,sms);
        saveCustomerDTO.enqueue(new Callback<CustomerListDTO>() {
            @Override
            public void onResponse(Call<CustomerListDTO> call, Response<CustomerListDTO> response) {
                if(response.isSuccessful()) {
                    CustomerListDTO dto = response.body();
                    if (dto.isError()) {
                        getErrorBox(context,dto.getErrorMessage()).show();

                        // Toast.makeText(context, dto.getErrorMessage(), Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    } else {

                        Toast.makeText(context, "Customer Had Registered Successfully", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
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
            TextView errorText = (TextView)country.getSelectedView();
            errorText.setError(getString(R.string.error_field_required));
            Toast.makeText(this, "This field is Reqiured", Toast.LENGTH_SHORT).show();
            focusView=errorText;
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
        country.setSelection(0);
    }
    @Override
    public boolean getCompanyName() {
        return companyName;
    }
}