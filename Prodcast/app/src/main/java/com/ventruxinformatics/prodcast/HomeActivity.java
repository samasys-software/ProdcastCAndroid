package com.ventruxinformatics.prodcast;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;


public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ImageView orderIcon,changePasswordIcon,changeStoreIcon,editRegitrationIcon,orderHistroyIcon;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        orderIcon=(ImageView) findViewById(R.id.orderIcon);
        orderIcon.setClickable(true);

        orderIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(HomeActivity.this,BillActivity.class);
                startActivity(i);

            }
        });

        changePasswordIcon=(ImageView) findViewById(R.id.changePasswordIcon);
        changePasswordIcon.setClickable(true);

        changePasswordIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(HomeActivity.this,ChangePasswordActivity.class);
                startActivity(i);
            }
        });

        changeStoreIcon=(ImageView) findViewById(R.id.changeStoreIcon);
        changeStoreIcon.setClickable(true);

        changeStoreIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(HomeActivity.this,StoreActivity.class);
                startActivity(i);
            }
        });


        editRegitrationIcon=(ImageView) findViewById(R.id.editRegistrationIcon);
        editRegitrationIcon.setClickable(true);

        editRegitrationIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(HomeActivity.this,StoreActivity.class);
                startActivity(i);
            }
        });

        orderHistroyIcon=(ImageView) findViewById(R.id.orderHistroyIcon);
        orderHistroyIcon.setClickable(true);

        orderHistroyIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(HomeActivity.this,OrderHistroyActivity.class);
                startActivity(i);
            }
        });

    }
}
