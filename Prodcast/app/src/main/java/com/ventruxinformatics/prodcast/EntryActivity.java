package com.ventruxinformatics.prodcast;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import businessObjects.GlobalUsage;
import businessObjects.SessionInformations;
import businessObjects.connect.ProdcastServiceManager;
import businessObjects.domain.Customer;
import businessObjects.domain.EmployeeDetails;
import businessObjects.domain.OrderDetails;
import businessObjects.dto.CustomerDTO;
import businessObjects.dto.OrderDetailDTO;
import businessObjects.dto.OrderEntryDTO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EntryActivity extends ProdcastCBaseActivity {

    Inflater inflater;
    Button order,backButton;
    ProgressDialog progressDialog;
    Context context;
    String currencySymbol=SessionInformations.getInstance().getEmployee().getDistributor().getCurrencySymbol();
    NumberFormat numberFormat= GlobalUsage.getNumberFormat();



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



         List<OrderDetails> entries = SessionInformations.getInstance().getEntry();
        if (entries.size() != 0) {
            final EmployeeDetails emp = SessionInformations.getInstance().getEmployee();


            //View convertView = (View) inflater.inflate(R.layout.activity_all_products, null);
            //alertDialog.setView(convertView);
            // alertDialog.setTitle("List");
            final SwipeMenuListView swipeMenuListView = (SwipeMenuListView) findViewById(R.id.listofentries);

            TextView txtView=(TextView) findViewById(R.id.subTotal);
            txtView.setText("Sub Total("+currencySymbol+")");

            final SwipeMenuCreator creator = new SwipeMenuCreator() {

                @Override
                public void create(SwipeMenu menu) {

                    SwipeMenuItem deleteItem = new SwipeMenuItem(getApplicationContext());
                    //deleteItem.setBackground(getDrawable(R.drawable.yellow_background));
                    deleteItem.setWidth(300);
                    deleteItem.setIcon(R.drawable.ic_delete);
                    menu.addMenuItem(deleteItem);

                }
            };


                swipeMenuListView.setMenuCreator(creator);

                swipeMenuListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick( final int position, SwipeMenu menu, int index) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(context);
                        alert.setTitle("Prodcast Notification");
                        alert.setMessage("This Order Has Been Deleted Permanently...Do You Want To Continue?");
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


                                SessionInformations.getInstance().getEntry().remove(position);
                                if(SessionInformations.getInstance().getEntry().size() >0) {
                                    EntriesCustomAdapter entriesCustomAdapter = new EntriesCustomAdapter(EntryActivity.this, SessionInformations.getInstance().getEntry());
                                    swipeMenuListView.setAdapter(entriesCustomAdapter);
                                    entriesCustomAdapter.notifyDataSetChanged();
                                    getValueForTotal(SessionInformations.getInstance().getEntry());
                                }
                                else{
                                    Toast.makeText(context,"Cart is Empty",Toast.LENGTH_LONG);
                                    dialog.dismiss();
                                    finish();
                                }




                                // Toast.makeText(context,"Your Order has Been Deleted Successfully",Toast.LENGTH_LONG);
                                //SessionInformations.getInstance().setEntry(orderEntries);

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
                    order.setVisibility(View.INVISIBLE);
                    OrderDetailDTO dto = new OrderDetailDTO();
                    List<OrderEntryDTO> orderEntries=new ArrayList<OrderEntryDTO>();

                    List<OrderDetails> orderDetails = SessionInformations.getInstance().getEntry();
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
                    dto.setCustomerId(String.valueOf(emp.getCustomerId()));
                    dto.setDiscountType(null);
                    dto.setDiscountValue("0");
                    dto.setEmployeeId(String.valueOf(emp.getEmployeeId()));
                    dto.setOrderStatus("S");
                    dto.setPaymentAmount("0");
                    dto.setPaymentType(null);
                    dto.setRefDetail(null);
                    dto.setRefNO(null);
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
                                    SessionInformations.getInstance().setEntry(null);
                                    Customer customer = dto.getCustomer();

                                    SessionInformations.getInstance().setBillsForCustomer(customer);
                                    AlertDialog.Builder alert = new AlertDialog.Builder(context,R.style.AlertTheme);
                                    alert.setTitle("Prodcast Notification");
                                    LayoutInflater inflater = EntryActivity.this.getLayoutInflater();
                                    View layoutView = inflater.inflate(R.layout.alert_dialog, null);
                                    alert.setView(layoutView);
                                    TextView message = (TextView) layoutView.findViewById(R.id.alertName);
                                    message.setText("Your Order Has Been Placed Successfully");
                                    alert.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent i = new Intent(EntryActivity.this, OutstandingBillsActivity.class);
                                            i.putExtra("useCache", true);
                                            startActivity(i);
                                        }
                                    });
                                    alert.show();

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
            });


        }
       
    }

    public void getValueForTotal(List<OrderDetails> entries)
    {
        final TextView total=(TextView) findViewById(R.id.total);
        final TextView subTotal=(TextView) findViewById(R.id.totalSubTotal);
        final TextView tax=(TextView) findViewById(R.id.totalTax);
        double total_value=0.0;
        float sub_total=0;
        float total_tax=0;
        for(OrderDetails entry:entries)
        {
            total_value=total_value+ProductDetailFragment.calculateTotal(entry.getProduct(),entry.getQuantity());
            sub_total=sub_total+ProductDetailFragment.calculateSubTotal(entry.getProduct(),entry.getQuantity());
            total_tax=total_tax+ProductDetailFragment.calculateTax(entry.getProduct());

        }
        total.setText("("+currencySymbol+")"+""+numberFormat.format(total_value));
        subTotal.setText("("+currencySymbol+")"+""+numberFormat.format(sub_total));
        tax.setText("("+currencySymbol+")"+""+numberFormat.format(total_tax));
    }
}
