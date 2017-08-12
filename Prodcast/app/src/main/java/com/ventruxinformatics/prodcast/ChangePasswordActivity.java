package com.ventruxinformatics.prodcast;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import com.ventruxinformatics.prodcast.domain.ProdcastDTO;

import org.json.JSONObject;

import businessObjects.SessionInformations;
import cz.msebera.android.httpclient.Header;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {

    //private UserChangePasswordTask mAuthTask = null;
    EditText oldPinNumber,newPinNumber,confirmPinNumber;

    Button submitButton,resetButton;
    boolean cancel = false;
    View focusView = null;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_change_password);

        oldPinNumber = (EditText)findViewById(R.id.oldPinNumber);
        context=this;
        newPinNumber = (EditText)findViewById(R.id.newPinNumber);
        confirmPinNumber = (EditText)findViewById(R.id.confirmPinNumber);
        submitButton=(Button) findViewById(R.id.changePasswordSubmit);
        resetButton=(Button) findViewById(R.id.changePasswordReset);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptChangePassword();

            }
        });


        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               reset();
            }
        });
    }

    public void reset(){
        oldPinNumber.setText("");
        newPinNumber.setText("");
        confirmPinNumber.setText("");

    }
    public void checkValue(String oldPassword,String newPassword,String confirmPassword){


        // Reset errors.
        oldPinNumber.setError(null);
        newPinNumber.setError(null);
        confirmPinNumber.setError(null);

        // Store values at the time of the login attempt.


        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(oldPassword)){
            oldPinNumber.setError(getString(R.string.error_field_required));
            focusView = oldPinNumber;
            cancel = true;
        }
                // Check for a valid email address.
       if (TextUtils.isEmpty(newPassword)) {
            newPinNumber.setError(getString(R.string.error_field_required));
            focusView = newPinNumber;
            cancel = true;
        }
        if(oldPassword.equals(newPassword)){
                newPinNumber.setError(getString(R.string.error_oldpassword_and_newpassword_is_same));
                focusView = newPinNumber;
                cancel = true;
            }
      if(newPassword.length()<6)
            {
                newPinNumber.setError(getString(R.string.error_newpassword_is_minimum));
                focusView = newPinNumber;
                cancel = true;
            }

         if (TextUtils.isEmpty(confirmPassword)) {
            confirmPinNumber.setError(getString(R.string.error_field_required));
            focusView = confirmPinNumber;
            cancel = true;
        }
         if(!confirmPassword.equals(newPassword)){
                confirmPinNumber.setError(getString(R.string.error_oldpassword_and_newpassword_is_same));
                focusView = confirmPinNumber;
                cancel = true;
            }
         else{
             cancel=false;
         }

    }

    private void attemptChangePassword() {
        String oldPassword = oldPinNumber.getText().toString();
        String newPassword = newPinNumber.getText().toString();
        String confirmPassword=confirmPinNumber.getText().toString();
        checkValue(oldPassword,newPassword,confirmPassword);
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {

            long accessId=SessionInformations.getInstance().getCustomerDetails().getAccessId();

            Call<ProdcastDTO> changePinDTO = new ProdcastServiceManager().getClient().changePinNumber( accessId,oldPassword,newPassword );

            changePinDTO.enqueue(new Callback<ProdcastDTO>() {
                @Override
                public void onResponse(Call<ProdcastDTO> call, Response<ProdcastDTO> response) {
                    String responseString = null;
                    ProdcastDTO dto = response.body();
                    if(dto.isError()) {
                        oldPinNumber.setError(dto.getErrorMessage());
                        focusView=oldPinNumber;
                        focusView.requestFocus();
                    }
                    else {
                        Toast.makeText(context,"Your Pin Number Has Been Changed Successfully",Toast.LENGTH_LONG).show();
                        reset();
                    }
                }

                @Override
                public void onFailure(Call<ProdcastDTO> call, Throwable t) {
                    t.printStackTrace();

                }
            });


        }
    }
}
