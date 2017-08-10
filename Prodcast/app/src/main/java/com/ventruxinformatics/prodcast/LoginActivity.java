package com.ventruxinformatics.prodcast;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInstaller;
import android.os.AsyncTask;
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

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.ventruxinformatics.prodcast.connect.ProdcastServiceManager;
import com.ventruxinformatics.prodcast.domain.CustomerLoginDTO;
import com.ventruxinformatics.prodcast.domain.LoginDTO;

import businessObjects.FormDataLogin;
import businessObjects.SessionInformations;
import cz.msebera.android.httpclient.Header;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import org.json.JSONObject;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {
    //private UserLoginTask mAuthTask = null;
    String[] country = { "IN", "USA"  };
    public static final String PREFS_NAME = "MyPrefsFile";


    EditText mobileNumber,pinNumber;

    Button signInButton,clearButton;
    TextView forgotPin;
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


        //Getting the instance of Spinner and applying OnItemSelectedListener on it
        spin = (Spinner) findViewById(R.id.spinner2);
        //  spin.setOnItemSelectedListener(this);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
                /*Intent i = new Intent(LoginActivity.this,StoreActivity.class);
                startActivity(i);*/
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


        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,country);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);
    }

    /* //Performing action onItemSelected and onNothing selected
     @Override
     public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
         Toast.makeText(getApplicationContext(),country[position] , Toast.LENGTH_LONG).show();
     }

     @Override
     public void onNothingSelected(AdapterView<?> arg0) {
         // TODO Auto-generated method stub

     }*/
    public void checkValue(String username,String password,String country){
        /*if (mAuthTask != null) {
            return;
        }*/

        // Reset errors.
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
        FormDataLogin login = new FormDataLogin();
        login.setUserid(username);
        login.setPassword(password);
        login.setCountry(country);
        //checkValue(username,password,country);
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
          /* Intent i = new Intent(LoginActivity.this,StoreActivity.class);
           startActivity(i);*/              //prev. code
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
        //   showProgress(true);              //prev. code

            Call<com.ventruxinformatics.prodcast.domain.CustomerLoginDTO> loginDTO = new ProdcastServiceManager().getClient().login( login.getUserid(), login.getPassword(), login.getCountry() );

            loginDTO.enqueue(new Callback<CustomerLoginDTO>() {
                @Override
                public void onResponse(Call<CustomerLoginDTO> call, Response<CustomerLoginDTO> response) {
                    String responseString = null;
                    CustomerLoginDTO dto = response.body();
                    showMainMenuScreen(dto);
                    SessionInformations.getInstance().setCustomerDetails(null);
                }

                @Override
                public void onFailure(Call<CustomerLoginDTO> call, Throwable t) {

                }
            });

            //mAuthTask = new UserLoginTask(username, password,country);
            //mAuthTask.execute((Void) null);
            System.out.println("successfully Login");
        }
    }

    private void attemptRetrive() {
        String username = mobileNumber.getText().toString();
        String password = pinNumber.getText().toString();
        String country=spin.getSelectedItem().toString();
        if (TextUtils.isEmpty(username)) {
            mobileNumber.setError(getString(R.string.error_field_required));
            focusView = mobileNumber;
            cancel = true;
        }

           UserRetriveTask task = new UserRetriveTask(username, country);
            task.execute((Void) null);
        Toast.makeText(context,"huigyufyuf",Toast.LENGTH_LONG).show();


    }
    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your ow
        // logic
        return password.length() >= 6;
    }
    private void showMainMenuScreen(CustomerLoginDTO employee){


        try {
            Intent intent=new Intent(this, StoreActivity.class);
            startActivity(intent);






        }
        catch(Exception e){
            e.printStackTrace();
        }

    }


  /*  public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;
        private final String mCountry;
        JSONObject jsonObject;

        UserLoginTask(String username, String password,String country) {
            mEmail = username;
            mPassword = password;
            mCountry=country;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                SyncHttpClient asyncHttpClient = new SyncHttpClient();
                RequestParams requestParams = new RequestParams();
                requestParams.put("userid", mEmail);
                requestParams.put("password", mPassword);
                requestParams.put("country", mCountry);
                String url = "http://ec2-52-91-5-22.compute-1.amazonaws.com:8080/prodcast/customer/login";

                asyncHttpClient.post(url, requestParams,new JsonHttpResponseHandler(){

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try{
                            System.out.println(response);
                            Boolean error=response.getBoolean("error");
                            System.out.println(error);
                            if(error)
                            {

                                pinNumber.setError("Error Message:"+response.getString("errorMessage"));
                                pinNumber.requestFocus();
                                jsonObject=null;


                            }
                            else {

                                jsonObject = (JSONObject) response.get("result");


                            }
                        }
                        catch(Exception er)
                        {
                            er.printStackTrace();
                        }

                    }
                    public void onFailure(int statusCode,Header[] headers,String responseString,Throwable e){
                        e.printStackTrace();
                    }

                });
            }


            catch (Exception e) {
                e.printStackTrace();
                return false;
            }



            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {



            mAuthTask = null;

            if (jsonObject!=null) {


                SessionInformations.getInstance().setCustomerDetails(jsonObject);

                SharedPreferences sett = getSharedPreferences(PREFS_NAME, 0);
                String localStorage = sett.getString("CustomerDetails","value");
                System.out.println("Sliehvghjfyk="+localStorage);


                // Commit the edits!
                showMainMenuScreen(jsonObject);

            }
            else {


                pinNumber.setError(getString(R.string.error_incorrect_password));
                pinNumber.requestFocus();
            }
        }


    }
*/
    public class UserRetriveTask extends AsyncTask<Void, Void, Boolean> {

        private final String mobilePhone;
        //private final String mPassword;
        private final String mCountry;
        JSONObject jsonObject;

        UserRetriveTask(String username, String country) {
            mobilePhone = username;
            mCountry= country;

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                SyncHttpClient asyncHttpClient = new SyncHttpClient();
                RequestParams requestParams = new RequestParams();
                requestParams.put("mobilePhone", mobilePhone);
                requestParams.put("country", mCountry);
              //  requestParams.put("country", mCountry);
                String url = "http://ec2-52-91-5-22.compute-1.amazonaws.com:8080/prodcast/customer/retrievePin";

                asyncHttpClient.post(url, requestParams,new JsonHttpResponseHandler(){

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try{
                            System.out.println(response);
                            Boolean error=response.getBoolean("error");
                            System.out.println(error);
                            if(error) {
                                System.out.print("fdsvgsffe");
                            }
                            else {
                                System.out.print("false");


                            }
                        }
                        catch(Exception er)
                        {
                            er.printStackTrace();
                        }

                    }
                    public void onFailure(int statusCode,Header[] headers,String responseString,Throwable e){
                        e.printStackTrace();
                    }

                });
            }


            catch (Exception e) {
                e.printStackTrace();
                return false;
            }



            // TODO: register the new account here.
            return true;
        }





    }
}
