package com.samayu.prodcastc.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.samayu.prodcastc.R;
import com.samayu.prodcastc.businessObjects.SessionInfo;
import com.samayu.prodcastc.businessObjects.connect.ProdcastServiceManager;
import com.samayu.prodcastc.businessObjects.domain.Country;
import com.samayu.prodcastc.businessObjects.domain.CustomersLogin;
import com.samayu.prodcastc.businessObjects.dto.AdminDTO;
import com.samayu.prodcastc.businessObjects.dto.CountryDTO;
import com.samayu.prodcastc.businessObjects.dto.CustomerLoginDTO;
import com.samayu.prodcastc.businessObjects.dto.ProdcastDTO;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    //private UserLoginTask mAuthTask = null;
    String[] countries= {"IN", "USA"};
    public static final String PREFS_NAME = "MyPrefsFile";


    EditText mobileNumber, pinNumber;

    Button signInButton, clearButton;
    TextView forgotPin, register, reportIssue;
    Spinner country;

    boolean cancel = false;
    View focusView = null;
    Context context;
    public static final String FILE_NAME = "prodcastCustomerLogin.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        final LayoutInflater inflater = this.getLayoutInflater();
        mobileNumber = (EditText) findViewById(R.id.loginMobileNumber);
        context = this;
        pinNumber = (EditText) findViewById(R.id.loginPinNumber);
        signInButton = (Button) findViewById(R.id.logIn);
        clearButton = (Button) findViewById(R.id.logClear);
        forgotPin = (TextView) findViewById(R.id.forgotPin);
        register = (TextView) findViewById(R.id.register);
        reportIssue = (TextView) findViewById(R.id.reportIssue);

        //Getting the instance of Spinner and applying OnItemSelectedListener on it
        country = (Spinner) findViewById(R.id.country);


        Call<CountryDTO> countryDTOCall = new ProdcastServiceManager().getClient().getCountries();
        countryDTOCall.enqueue(new Callback<CountryDTO>() {
            @Override
            public void onResponse(Call<CountryDTO> call, Response<CountryDTO> response) {
                if (response.isSuccessful()){
                    CountryDTO countryDTO = response.body();
                    List<Country> countryList= countryDTO.getResult();
                    SessionInfo.getInstance().setCountries(countryList);


                    Country defaultCountry = new Country();
                    defaultCountry.setCountryId("");
                    defaultCountry.setCountryName("Select Country");
                    countryList.add(0, defaultCountry  );

                    ArrayAdapter<Country> adapter = new ArrayAdapter<Country>(LoginActivity.this,R.layout.drop_down_list, countryList);
                    country.setAdapter(adapter);
                    country.setSelection(2);
                }
            }

            @Override
            public void onFailure(Call<CountryDTO> call, Throwable t) {

                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Oops! Something went Wrong.");
                alert.setMessage("Connection Timeout.please try again later");
                alert.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                alert.show();
            }
        });


        CustomersLogin custLogin = loginRetrive();
        if (custLogin != null) {
            SessionInfo.getInstance().setCustomerDetails(custLogin);
            Intent intent = new Intent(LoginActivity.this, StoreActivity.class);
            startActivity(intent);
        }

        //  spin.setOnItemSelectedListener(this);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clear();
            }
        });

        forgotPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                attemptRetrive();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        reportIssue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context,R.style.AlertTheme);
                alert.setTitle("Report an Issue");

                LayoutInflater layoutInflater = LoginActivity.this.getLayoutInflater();
                View layoutView = layoutInflater.inflate(R.layout.report_issue_dialogue,null);
                alert.setView(layoutView);


                final EditText customerPhoneNumber = (EditText) layoutView.findViewById(R.id.customerPhoneNumber);
                customerPhoneNumber.setVisibility(View.VISIBLE);
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


                        Call<ProdcastDTO> serviceSupportDTOCall = new ProdcastServiceManager().
                                getClient().raiseRequest(customerPhoneNumber.getText().toString(),
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

/*
        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, countries);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        country.setAdapter(aa);
 */
    }





    private void clear()
    {
        mobileNumber.setText("");
        pinNumber.setText("");
        country.setSelection(0);
    }

    public boolean checkValue(String username, String password, int ctry) {
        // Reset errors.
        cancel = false;
        mobileNumber.setError(null);
        pinNumber.setError(null);

        // Store values at the time of the login attempt.

        //check for a valid country
        if (ctry <= 0) {
            TextView errorText = (TextView)country.getSelectedView();
            errorText.setError(getString(R.string.error_field_required));
            Toast.makeText(this, "This field is Required", Toast.LENGTH_SHORT).show();
            //errorText.setText(getString(R.string.error_field_required));
            focusView=errorText;
            cancel = true;
            return  cancel;

            /*TextView errorText = (TextView)country.getSelectedView();
            errorText.setError(getString(R.string.error_field_required));
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("This field is Reqiured");//changes the selected item text to this
            focusView=errorText;
            cancel = true;
            return  cancel;*/

            /*((TextView)country.getSelectedView()).setError("This field is Reqiured");
            focusView = country;
            cancel = true;
            return  cancel;*/
        }
        // Check for a valid username
        if (TextUtils.isEmpty(username)) {
            mobileNumber.setError(getString(R.string.error_field_required));
            focusView = mobileNumber;
            cancel = true;
            return  cancel;
        }
        //check for valid password
        if(password!=null) {
            // Check for a valid password, if the user entered one.
            if (TextUtils.isEmpty(password)) {
                pinNumber.setError(getString(R.string.error_field_required));
                focusView = pinNumber;
                cancel = true;
                return  cancel;
            }
            if (!isPasswordValid(password)) {
                pinNumber.setError(getString(R.string.error_invalid_password));
                focusView = pinNumber;
                cancel = true;
                return  cancel;
            }
            return  cancel;
        }
        return  cancel;
    }

    private void attemptLogin() {
       /* ArrayAdapter<Country> adapter =(ArrayAdapter<Country>) country.getAdapter();
        int count =  adapter.getCount();*/

        String username = mobileNumber.getText().toString();
        String password = pinNumber.getText().toString();
        int ctry = country.getSelectedItemPosition();

        Country selectedCountry = (Country) country.getSelectedItem();
        String selectedCountryId = selectedCountry.getCountryId();

        // String ctry = country.getSelectedItem().toString();
        boolean cancelled=checkValue(username, password, ctry);

        if (cancelled) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
            return;
        }
        else {

            Call<CustomerLoginDTO<CustomersLogin>> loginDTO = new ProdcastServiceManager().getClient().login(username, password, selectedCountryId);

            loginDTO.enqueue(new Callback<CustomerLoginDTO<CustomersLogin>>() {
                @Override
                public void onResponse(Call<CustomerLoginDTO<CustomersLogin>> call, Response<CustomerLoginDTO<CustomersLogin>> response) {
                    String responseString = null;
                    Log.d("Response",response.toString());
                    if(response.isSuccessful()) {
                        CustomerLoginDTO<CustomersLogin> dto = response.body();
                        if (dto.isError()) {
                            mobileNumber.setError(getString(R.string.error_incorrect_password));
                            focusView = mobileNumber;
                            return;
                        } else {
                            if (dto.isVerified()) {
                                CustomersLogin cust1 = dto.getResult();
                                SessionInfo.getInstance().setCustomerDetails(cust1);
                                loginToFile(cust1);
                                Intent intent = new Intent(LoginActivity.this, StoreActivity.class);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(LoginActivity.this, VerifyPinActivity.class);
                                startActivity(intent);
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<CustomerLoginDTO<CustomersLogin>> call, Throwable t) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setTitle("Oops! Something went Wrong.");
                    alert.setMessage("Connection Timeout.please try again later");
                    alert.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                    alert.show();
                }
            });
           // System.out.println("successfully Login");
        }
    }

    private void attemptRetrive() {
        String username = mobileNumber.getText().toString();
        int ctry = country.getSelectedItemPosition();
        boolean cancelled=checkValue(username,null,ctry);

        Country selectedCountry = (Country) country.getSelectedItem();
        String selectedCountryId = selectedCountry.getCountryId();
        if(cancelled) {
            focusView.requestFocus();
            return;
        }
        else
        {
            Call<AdminDTO> retriveDTO = new ProdcastServiceManager().getClient().retrieve(username, selectedCountryId);

            retriveDTO.enqueue(new Callback<AdminDTO>() {
                @Override
                public void onResponse(Call<AdminDTO> call, Response<AdminDTO> response) {
                   if(response.isSuccessful()) {
                       AdminDTO dto = response.body();
                       if (dto.isError()) {
                           mobileNumber.setError(dto.getErrorMessage());
                           focusView = mobileNumber;

                       } else {
                           Toast.makeText(context, "Message Has Been Sent To The Mobile Number with the pin", Toast.LENGTH_LONG).show();
                       }
                   }
                }

                @Override
                public void onFailure(Call<AdminDTO> call, Throwable t) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setTitle("Oops! Something went Wrong.");
                    alert.setMessage("Connection Timeout.please try again later");
                    alert.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                    alert.show();
                }
            });
        }
    }

    public static boolean isPasswordValid(String password) {
        // logic
        return password.length() >= 6;
    }

    public void loginToFile(CustomersLogin customersLogin) {
        File file = new File(getFilesDir(), FILE_NAME);

        FileOutputStream outputStream;

        try {
            outputStream = openFileOutput(FILE_NAME, LoginActivity.this.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(outputStream);
            oos.writeObject(customersLogin);
            outputStream.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public CustomersLogin loginRetrive() {
        try {
            ObjectInputStream ois = new ObjectInputStream(openFileInput(FILE_NAME));
            CustomersLogin r = (CustomersLogin) ois.readObject();
            return r;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}