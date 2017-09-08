package com.ventruxinformatics.prodcast;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import businessObjects.connect.ProdcastServiceManager;
import businessObjects.domain.Country;
import businessObjects.domain.Customer;
import businessObjects.domain.CustomersLogin;

import businessObjects.SessionInformations;
import businessObjects.dto.AdminDTO;
import businessObjects.dto.CountryDTO;
import businessObjects.dto.CustomerLoginDTO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    //private UserLoginTask mAuthTask = null;
    String[] countries= {"IN", "USA"};
    public static final String PREFS_NAME = "MyPrefsFile";


    EditText mobileNumber, pinNumber;

    Button signInButton, clearButton;
    TextView forgotPin, register;
    Spinner country;
    boolean cancel = false;
    View focusView = null;
    Context context;
    public static final String FILE_NAME = "prodcastCustomerLogin.txt";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        mobileNumber = (EditText) findViewById(R.id.loginMobileNumber);
        context = this;
        pinNumber = (EditText) findViewById(R.id.loginPinNumber);
        signInButton = (Button) findViewById(R.id.logIn);
        clearButton = (Button) findViewById(R.id.logClear);
        forgotPin = (TextView) findViewById(R.id.forgotPin);
        register = (TextView) findViewById(R.id.register);

        //Getting the instance of Spinner and applying OnItemSelectedListener on it
        country = (Spinner) findViewById(R.id.country);



        Call<CountryDTO> countryDTOCall = new ProdcastServiceManager().getClient().getCountries();
        countryDTOCall.enqueue(new Callback<CountryDTO>() {
            @Override
            public void onResponse(Call<CountryDTO> call, Response<CountryDTO> response) {
                if (response.isSuccessful()){
                    CountryDTO countryDTO = response.body();
                    List<Country> countryList= countryDTO.getResult();
                    SessionInformations.getInstance().setCountries(countryList);
                    Country defaultCountry = new Country();
                    defaultCountry.setCountryId("");
                    defaultCountry.setCountryName("Select Country");
                    countryList.add(0, defaultCountry  );
                    ArrayAdapter<Country> adapter = new ArrayAdapter<Country>(LoginActivity.this, android.R.layout.simple_list_item_1, countryList);
                    country.setAdapter(adapter);

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
            SessionInformations.getInstance().setCustomerDetails(custLogin);
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
                mobileNumber.setText("");
                pinNumber.setText("");
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


/*
        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, countries);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        country.setAdapter(aa);
 */
    }


    public boolean checkValue(String username, String password, int ctry) {
        // Reset errors.
        cancel = false;
        mobileNumber.setError(null);
        pinNumber.setError(null);

        // Store values at the time of the login attempt.
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

        // Check for a valid username
        if (TextUtils.isEmpty(username)) {
            mobileNumber.setError(getString(R.string.error_field_required));
            focusView = mobileNumber;
            cancel = true;
            return  cancel;
        }

        //check for a valid country
        if (ctry <= 0) {
            focusView=country;
            cancel = true;
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
                    CustomerLoginDTO<CustomersLogin> dto = response.body();
                    if (dto.isError()) {
                        mobileNumber.setError(getString(R.string.error_incorrect_password));
                        focusView = mobileNumber;
                    } else {
                        if (dto.isVerified()) {
                            CustomersLogin cust1 = dto.getResult();
                            SessionInformations.getInstance().setCustomerDetails(cust1);
                            loginToFile(cust1);
                            Intent intent = new Intent(LoginActivity.this, StoreActivity.class);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(LoginActivity.this, VerifyPinActivity.class);
                            startActivity(intent);
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
                    String responseString = null;
                    AdminDTO dto = response.body();
                    if (dto.isError()) {
                        mobileNumber.setError(dto.getErrorMessage());
                        focusView = mobileNumber;

                    } else {
                      //  Toast.makeText(context, "Message Has Been Sent To The Mobile Number with the pin", Toast.LENGTH_LONG).show();
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

