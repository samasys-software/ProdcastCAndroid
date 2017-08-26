package com.ventruxinformatics.prodcast;

import android.content.Context;
import android.content.Intent;
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

import businessObjects.connect.ProdcastServiceManager;
import businessObjects.domain.CustomersLogin;

import businessObjects.SessionInformations;
import businessObjects.dto.AdminDTO;
import businessObjects.dto.CustomerLoginDTO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    //private UserLoginTask mAuthTask = null;
    String[] country = { "IN", "USA"  };
    public static final String PREFS_NAME = "MyPrefsFile";


    EditText mobileNumber,pinNumber;

    Button signInButton,clearButton;
    TextView forgotPin,register;
    Spinner spin;
    boolean cancel = false;
    View focusView = null;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        mobileNumber = (EditText)findViewById(R.id.loginMobileNumber);
        context=this;
        pinNumber = (EditText)findViewById(R.id.loginPinNumber);
        signInButton=(Button) findViewById(R.id.logIn);
        clearButton=(Button) findViewById(R.id.logClear);
        forgotPin=(TextView) findViewById(R.id.forgotPin);
        register=(TextView) findViewById(R.id.register);


        //Getting the instance of Spinner and applying OnItemSelectedListener on it
        spin = (Spinner) findViewById(R.id.spinner2);

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

                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);

            }
        });


        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,country);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);
    }


    public void checkValue(String username,String password,String country){
           // Reset errors.
        cancel=false;
        mobileNumber.setError(null);
        pinNumber.setError(null);

        // Store values at the time of the login attempt.


        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)){
            pinNumber.setError(getString(R.string.error_field_required));
            focusView = pinNumber;
            cancel = true;
        }
        else if(!isPasswordValid(password)) {
            pinNumber.setError(getString(R.string.error_invalid_password));
            focusView = pinNumber;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(username)) {
            mobileNumber.setError(getString(R.string.error_field_required));
            focusView = mobileNumber;
            cancel = true;
        }


    }

    private void attemptLogin() {
        String username = mobileNumber.getText().toString();
        String password = pinNumber.getText().toString();
        String country=spin.getSelectedItem().toString();
        checkValue(username,password,country);

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {

            Call<CustomerLoginDTO<CustomersLogin>> loginDTO = new ProdcastServiceManager().getClient().login( username,password,country );

            loginDTO.enqueue(new Callback<CustomerLoginDTO<CustomersLogin>>() {
                @Override
                public void onResponse(Call<CustomerLoginDTO<CustomersLogin>> call, Response<CustomerLoginDTO<CustomersLogin>> response) {
                    String responseString = null;
                    CustomerLoginDTO<CustomersLogin> dto = response.body();
                    if(dto.isError()) {
                        mobileNumber.setError(getString(R.string.error_incorrect_password));
                        focusView=mobileNumber;
                    }
                    else {
                        if(dto.isVerified()){
                            CustomersLogin cust1 = dto.getResult();
                            SessionInformations.getInstance().setCustomerDetails(cust1);
                            Intent intent=new Intent(LoginActivity.this, StoreActivity.class);
                            startActivity(intent);
                        }
                        else{
                            Intent intent=new Intent(LoginActivity.this, VerifyPinActivity.class);
                            startActivity(intent);
                        }
                    }

                }

                @Override
                public void onFailure(Call<CustomerLoginDTO<CustomersLogin>> call, Throwable t) {
                    t.printStackTrace();

                }
            });


            System.out.println("successfully Login");
        }
    }

    private void attemptRetrive() {
        String username = mobileNumber.getText().toString();
        String country=spin.getSelectedItem().toString();
        if (TextUtils.isEmpty(username)) {
            mobileNumber.setError(getString(R.string.error_field_required));
            focusView = mobileNumber;
            cancel = true;
        }
        else{
            Call<AdminDTO> retriveDTO = new ProdcastServiceManager().getClient().retrieve( username, country );

            retriveDTO.enqueue(new Callback<AdminDTO>() {
                @Override
                public void onResponse(Call<AdminDTO> call, Response<AdminDTO> response) {
                    String responseString = null;
                    AdminDTO dto = response.body();
                    if(dto.isError())
                    {
                        mobileNumber.setError(dto.getErrorMessage());
                        focusView=mobileNumber;

                    }
                    else{
                        Toast.makeText(context,"Message Has Been Sent To The Mobile Number with the pin",Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onFailure(Call<AdminDTO> call, Throwable t) {
                    t.printStackTrace();

                }
            });
        }


    }
    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your ow
        // logic
        return password.length() >= 6;
    }
}

