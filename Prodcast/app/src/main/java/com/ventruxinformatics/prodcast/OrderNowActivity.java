package com.ventruxinformatics.prodcast;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class OrderNowActivity extends AppCompatActivity{

    String[] country = { "Abc", "XYX", "Cab", "Jba", "Abc",  };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_now);




    }
}
