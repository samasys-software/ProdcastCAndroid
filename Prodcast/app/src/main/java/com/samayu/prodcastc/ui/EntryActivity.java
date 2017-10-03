package com.samayu.prodcastc.ui;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.samayu.prodcastc.R;
import com.samayu.prodcastc.businessObjects.GlobalUsage;
import com.samayu.prodcastc.businessObjects.SessionInfo;
import com.samayu.prodcastc.businessObjects.connect.ProdcastServiceManager;
import com.samayu.prodcastc.businessObjects.domain.Customer;
import com.samayu.prodcastc.businessObjects.domain.EmployeeDetails;
import com.samayu.prodcastc.businessObjects.domain.OrderDetails;
import com.samayu.prodcastc.businessObjects.dto.CustomerDTO;
import com.samayu.prodcastc.businessObjects.dto.OrderDetailDTO;
import com.samayu.prodcastc.businessObjects.dto.OrderEntryDTO;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EntryActivity extends ProdcastCBaseActivity {

    Inflater inflater;
    Button order,backButton;
    ProgressDialog progressDialog;
    Context context;
    String currencySymbol= SessionInfo.getInstance().getEmployee().getDistributor().getCurrencySymbol();

    NumberFormat numberFormat= GlobalUsage.getNumberFormat();
    float minimumDeliveryAmount = SessionInfo.getInstance().getEmployee().getDistributor().getMinimumDeliveryAmount();

    double total_value=0.0;

    TextView message;
    EditText address,city,state,zipCode;
    Spinner shippingMethod;
    String shippingType;
    TextView confirmationMessage;
    RelativeLayout addressLayout;

    TextView unitPriceCurrency;


    @Override
    public String getProdcastTitle() {
        return "Checkout Order";
    }

    @Override
    public boolean getCompanyName() {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        context=this;
        progressDialog=getProgressDialog(this);

        unitPriceCurrency = (TextView) findViewById(R.id.unitPriceCurrency);
        unitPriceCurrency.setText("("+currencySymbol+")");



         final List<OrderDetails> entries = SessionInfo.getInstance().getEntry();

        if (entries.size() != 0) {



            //View convertView = (View) inflater.inflate(R.layout.activity_all_products, null);
            //alertDialog.setView(convertView);
            // alertDialog.setTitle("List");
            final SwipeMenuListView swipeMenuListView = (SwipeMenuListView) findViewById(R.id.listofentries);

/*
            TextView txtView = (TextView) findViewById(R.id.subTotal);
            txtView.setText("(" + currencySymbol + ")");*/

            /*TextView txtView=(TextView) findViewById(R.id.subTotal);
            txtView.setText("Sub Tot"+"("+currencySymbol+")");
*/

            final SwipeMenuCreator creator = new SwipeMenuCreator() {

                @Override
                public void create(SwipeMenu menu) {

                    SwipeMenuItem deleteItem = new SwipeMenuItem(getApplicationContext());
                    //deleteItem.setBackground(getDrawable(R.drawable.yellow_background));
                    deleteItem.setWidth(300);
                    deleteItem.setIcon(R.drawable.ic_delete_button);
                    menu.addMenuItem(deleteItem);

                }
            };


            swipeMenuListView.setMenuCreator(creator);

            swipeMenuListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {

                @Override
                public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {

                    final OrderDetails orderDetails = entries.get(position);

                    AlertDialog.Builder alert = new AlertDialog.Builder(context, R.style.AlertTheme);
                    alert.setTitle("Prodcast Notification");

                    LayoutInflater inflater = EntryActivity.this.getLayoutInflater();
                    View layoutView = inflater.inflate(R.layout.alert_dialog, null);
                    alert.setView(layoutView);
                    TextView message = (TextView) layoutView.findViewById(R.id.alertName);
                    message.setText("The item " + orderDetails.getProduct().getProductName() + " " +
                            "will be removed from the cart. Would you like to Continue ?");

                    alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    alert.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            //alert.show();


                            SessionInfo.getInstance().getEntry().remove(position);
                            if (SessionInfo.getInstance().getEntry().size() > 0) {
                                EntriesCustomAdapter entriesCustomAdapter = new EntriesCustomAdapter(EntryActivity.this, SessionInfo.getInstance().getEntry());
                                swipeMenuListView.setAdapter(entriesCustomAdapter);
                                entriesCustomAdapter.notifyDataSetChanged();
                                getValueForTotal(SessionInfo.getInstance().getEntry());
                            } else {
                                Toast.makeText(context, "Cart is Empty", Toast.LENGTH_LONG);
                                dialog.dismiss();
                                finish();
                            }


                            // Toast.makeText(context,"Your Order has Been Deleted Successfully",Toast.LENGTH_LONG);
                            //SessionInfo.getInstance().setEntry(orderEntries);

                        }
                    });
                    alert.show();


                    return false;
                }
            });


            order = (Button) findViewById(R.id.order);
            backButton = (Button) findViewById(R.id.backButton);
            final EntriesCustomAdapter adapter = new EntriesCustomAdapter(EntryActivity.this, entries);
            progressDialog.show();
            swipeMenuListView.setAdapter(adapter);
            getValueForTotal(entries);

            progressDialog.dismiss();


            //total.setText("Total:"+getOrdersEntry());
            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    finish();


                }
            });


            order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*order.setVisibility(View.INVISIBLE);*/


                    String distType=SessionInfo.getInstance().getEmployee().getDistributor().getFulfillmentType();
                    if(distType.equals("0") || distType.equals("2")){
                        placeOrder(distType,null);

                    }
                    else if(distType.equals("1")){
                        showDialogBox(false);
                    }
                    else{
                        showDialogBox(true);
                    }

                }
            });
        }

    }

    public void getValueForTotal(List<OrderDetails> entries)
    {
        final TextView total=(TextView) findViewById(R.id.total);
        final TextView subTotal=(TextView) findViewById(R.id.totalSubTotal);
        final TextView tax=(TextView) findViewById(R.id.totalTax);

        float sub_total=0;
        float total_tax=0;
        for(OrderDetails entry:entries)
        {
            total_value=total_value+ProductDetailFragment.calculateTotal(entry.getProduct(),entry.getQuantity());
            sub_total=sub_total+ProductDetailFragment.calculateSubTotal(entry.getProduct(),entry.getQuantity());
            total_tax=total_tax+ProductDetailFragment.calculateTax(entry.getProduct(),entry.getQuantity());

        }
        total.setText(currencySymbol+""+numberFormat.format(total_value));
        subTotal.setText(currencySymbol+""+numberFormat.format(sub_total));
        tax.setText(currencySymbol+""+numberFormat.format(total_tax));
    }

    public void showDialogBox(final boolean showDialog){


        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(EntryActivity.this, R.style.AlertTheme);
        alertDialog.setTitle("Shipping Details");
        alertDialog.setCancelable(true);
        LayoutInflater inflater = EntryActivity.this.getLayoutInflater();
        View diaView = inflater.inflate(R.layout.address_verification, null);
        alertDialog.setView(diaView);
        shippingMethod = (Spinner) diaView.findViewById(R.id.shippingType);
        addressLayout=(RelativeLayout) diaView.findViewById(R.id.addressLayout);
        message = (TextView) diaView.findViewById(R.id.confirmationText);
        address = (EditText) diaView.findViewById(R.id.address);
        city = (EditText) diaView.findViewById(R.id.city);
        state = (EditText) diaView.findViewById(R.id.state);
        zipCode = (EditText) diaView.findViewById(R.id.zipCode);
        confirmationMessage = (TextView)  diaView.findViewById(R.id.confirmationMessage);
        List<String> categories = new ArrayList<String>();
        categories.add("Delivery");
        categories.add("Pickup");


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.drop_down_list, categories);

        // attaching data adapter to spinner
        shippingMethod.setAdapter(dataAdapter);

        Customer customer=SessionInfo.getInstance().getEmployee().getCustomer();
        address.setText(customer.getBillingAddress1()+" "+customer.getBillingAddress2()+" "+customer.getBillingAddress3());
        city.setText(customer.getCity());

        state.setText(customer.getState());
        zipCode.setText(customer.getPostalCode());

        if(showDialog){
            checkVisiblity();

        }
        else{
            addressLayout.setVisibility(View.VISIBLE);
            shippingMethod.setVisibility(View.GONE);
            shippingType="1";

        }
        alertDialog.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        dialog.cancel();

                    }
                });
       /* alertDialog.setPositiveButton("Order", null);
        final AlertDialog theDialog = alertDialog.show();
        final TextView confirmationMessage = (TextView)  theDialog.findViewById(R.id.confirmationMessage);

        theDialog.getButton(
                DialogInterface.BUTTON_POSITIVE)
                .setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                float minimumDeliveryAmount = SessionInfo.getInstance().getEmployee().getDistributor().getMinimumDeliveryAmount();
                                if(shippingType =="2"){
                                    placeOrder(shippingType,null);
                                }

                                else if(total_value < minimumDeliveryAmount){
                                    confirmationMessage.setText( "Sorry . Minimum Order for delivery should be grater than minimum delivey");
                                    confirmationMessage.setVisibility(View.VISIBLE);
                                }
                                else{
                                    placeOrder(shippingType,address.getText().toString()+","+city.getText().toString()+","+state.getText().toString());
                                    Toast.makeText(context,"Your order for has been placed successfully.",Toast.LENGTH_LONG).show();
                                }

                            }
                        }
                );
*/

        alertDialog.setPositiveButton("Order",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        ((AlertDialog)dialog).getButton(which).setVisibility(View.INVISIBLE);

                     if(shippingType =="2"){
                            placeOrder(shippingType,null);
                         Toast.makeText(context,"Your order for has been placed successfully.",Toast.LENGTH_LONG).show();

                        }

                        else if(total_value < minimumDeliveryAmount){
                         showDialogBox(true);

                         confirmationMessage.setVisibility(View.VISIBLE);
                         confirmationMessage.setText("Sorry . Minimum Order for delivery should be grater than "+minimumDeliveryAmount+currencySymbol);


                            /*String msg ="Sorry . Minimum Order for delivery should be grater than "+minimumDeliveryAmount+currencySymbol;
                            Toast.makeText(context,msg,Toast.LENGTH_LONG).show();*/

                           }
                        else {
                            placeOrder(shippingType,address.getText().toString()+","+city.getText().toString()+","+state.getText().toString());
                            Toast.makeText(context,"Your order for has been placed successfully.",Toast.LENGTH_LONG).show();

                           dialog.cancel();
                        }


                    }
                });



        shippingMethod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                checkVisiblity();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
        alertDialog.show();

    }


    public void placeOrder(String ShippingType,String deliveryAddress){

        final EmployeeDetails emp = SessionInfo.getInstance().getEmployee();


        OrderDetailDTO dto = new OrderDetailDTO();
        List<OrderEntryDTO> orderEntries=new ArrayList<OrderEntryDTO>();

        List<OrderDetails> orderDetails = SessionInfo.getInstance().getEntry();
        //  System.out.println("Size:" + orderDetails.size());
        for (OrderDetails d1 : orderDetails) {
            OrderEntryDTO orderEntry=new OrderEntryDTO();
            orderEntry.setProductId(String.valueOf(d1.getProduct().getId()));
            orderEntry.setQuantity(String.valueOf(d1.getQuantity()));
            //subtotal=subtotal+d1.getSubTotal();
            orderEntries.add(orderEntry);


        }
        //return subtotal;

        dto.setEntries(orderEntries);
        dto.setCustomerId(String.valueOf(emp.getCustomer().getId()));
        dto.setDiscountType(null);
        dto.setDiscountValue("0");
        dto.setEmployeeId(String.valueOf(emp.getEmployeeId()));
        dto.setOrderStatus("S");
        dto.setPaymentAmount("0");
        dto.setPaymentType(null);
        dto.setRefDetail(null);
        dto.setRefNO(null);
        dto.setShippingType(ShippingType);
        dto.setDeliveryAddress(deliveryAddress);
        progressDialog.show();

        Call<CustomerDTO> saveOrderDTO = new ProdcastServiceManager().getClient().saveOrder(dto);

        saveOrderDTO.enqueue(new Callback<CustomerDTO>() {
            @Override
            public void onResponse(Call<CustomerDTO> call, Response<CustomerDTO> response) {
                if(response.isSuccessful()) {
                    CustomerDTO dto = response.body();
                    if (dto.isError()) {
                        getErrorBox(context,dto.getErrorMessage()).show();

                        progressDialog.dismiss();


                    } else {
                        //  System.out.println("success");
                        SessionInfo.getInstance().setEntry(null);
                        Customer customer = dto.getCustomer();

                        SessionInfo.getInstance().setBillsForCustomer(customer);

                        Toast.makeText(context,"Your Order Has Been Placed Successfully",Toast.LENGTH_LONG);
                        Intent i = new Intent(EntryActivity.this, OutstandingBillsActivity.class);
                        i.putExtra("useCache", true);
                        startActivity(i);
                        /*AlertDialog.Builder alert = new AlertDialog.Builder(context,R.style.AlertTheme);
                        alert.setTitle("Prodcast Notification");
                        LayoutInflater inflater = EntryActivity.this.getLayoutInflater();
                        View layoutView = inflater.inflate(R.layout.alert_dialog, null);
                        alert.setView(layoutView);
                        TextView message = (TextView) layoutView.findViewById(R.id.alertName);*/
                        /*alert.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        alert.show();*/

                        //Toast.makeText(EntryActivity.this,"",Toast.LENGTH_LONG);

                        progressDialog.dismiss();


                    }
                }

            }

            @Override
            public void onFailure(Call<CustomerDTO> call, Throwable t) {
                t.printStackTrace();
                getAlertBox(context).show();
                progressDialog.dismiss();

            }
        });

    }

    public void checkVisiblity(){
        String type=shippingMethod.getSelectedItem().toString();
        if(type.equalsIgnoreCase("Delivery") ){
            shippingType="1";

            shippingMethod.setVisibility(View.VISIBLE);
            addressLayout.setVisibility(View.VISIBLE);



        }
        else if(type.equalsIgnoreCase("Pickup")){
            shippingType="2";
            addressLayout.setVisibility(View.GONE);
            shippingMethod.setVisibility(View.VISIBLE);

        }
        else{
            TextView errorText = (TextView)shippingMethod.getSelectedView();
            errorText.setError(getString(R.string.error_field_required));
            Toast.makeText(this, "This field is Reqiured", Toast.LENGTH_SHORT).show();
            //errorText.setText(getString(R.string.error_field_required));
            View focusView=errorText;
            focusView.requestFocus();
            return;
        }
    }
}
