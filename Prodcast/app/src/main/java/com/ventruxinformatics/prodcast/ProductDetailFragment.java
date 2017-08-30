package com.ventruxinformatics.prodcast;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

//import com.ventruxinformatics.prodcast.dummy.DummyContent;

import java.util.ArrayList;
import java.util.List;

import businessObjects.SessionInformations;
import businessObjects.domain.Category;
import businessObjects.domain.OrderDetails;
import businessObjects.domain.Product;

/**
 * A fragment representing a single Product detail screen.
 * This fragment is either contained in a {@link ProductListActivity}
 * in two-pane mode (on tablets) or a {@link ProductDetailActivity}
 * on handsets.
 */
public class ProductDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";





    /**
     * The dummy content this fragment is presenting.
     */
   private Category selectedCategory;

    public Category getSelectedCategory() {
        return selectedCategory;
    }

    public void setSelectedCategory(Category selectedCategory) {
        this.selectedCategory = selectedCategory;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ProductDetailFragment() {
    }

    List<Product> productDetails;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
               //    System.out.println(mItem.size());

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(getSelectedCategory().getCategoryName());
            }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.product_detail, container, false);


        List<Category> categories=SessionInformations.getInstance().getCategoryDetails();
        List<Product> products=SessionInformations.getInstance().getProductDetails();
        int count=0;
        for(Category category:categories)
            if(category.getCategoryId()==getSelectedCategory().getCategoryId())
            {
                for(Product product:products) {
                    System.out.println("Selected Category Id"+category.getCategoryId());
                    System.out.println("Product Category Id"+product.getCategoryId());
                    if(product.getCategoryId()==category.getCategoryId())
                    {
                        productDetails.add(count,product);
                        count++;
                    }

                }
                break;
            }


        // Show the dummy content as text in a TextView.
        if (productDetails != null) {
            ListView listView=(ListView) rootView.findViewById(R.id.product_detail);
            listView.setAdapter(new AllProductsAdapter(getActivity(),productDetails));



            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                     final Product product= SessionInformations.getInstance().getProductDetails().get(position);


                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                    alertDialog.setTitle("Prodcast Notification");


                    LinearLayout layout = new LinearLayout(getActivity());
                    LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    layout.setOrientation(LinearLayout.VERTICAL);
                    layout.setLayoutParams(parms);

                    layout.setGravity(Gravity.CLIP_HORIZONTAL);

                    RelativeLayout reltivelayout=new RelativeLayout(getActivity());
                    RelativeLayout.LayoutParams parameters = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                   reltivelayout.setLayoutParams(parameters);


                    final EditText qty = new EditText(getActivity());
                    qty.setHint("Enter Quantity");

                    final TextView subTotal = new TextView(getActivity());
                    subTotal.setText("Subtotal : 0.00");
                    subTotal.setTextSize(25);

                    RelativeLayout.LayoutParams tv1Params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    tv1Params.topMargin = 150;




                    reltivelayout.addView(qty, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    reltivelayout.addView(subTotal,tv1Params);
                    layout.addView(reltivelayout);

                    alertDialog.setView(layout);


                    qty.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count,
                                                      int after) {

                            //subTotal.setText("SubTotal:0.00");
                        }
                        @Override
                        public void onTextChanged(final CharSequence s, int start, int before,
                                                  int count) {

                            //subTotal.setText("SubTotal : "+calculateTotal(product,Integer.valueOf(qty.getText().toString())));

                        }
                        @Override
                        public void afterTextChanged(final Editable s) {
                            //avoid triggering event when text is too short
                            subTotal.setText("SubTotal : "+calculateTotal(product,Integer.valueOf(qty.getText().toString())));

                        }
                    });





                    alertDialog.setIcon(R.drawable.customer_icon);


                    alertDialog.setPositiveButton("YES",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int which) {
                                    // Write your code here to execute after dialog
                                    String quantity=qty.getText().toString();
                                    if(TextUtils.isEmpty(quantity))
                                    {
                                        qty.setError(getString(R.string.required_quantity));
                                        qty.requestFocus();
                                    }
                                    if(!TextUtils.isEmpty(quantity))
                                    {
                                        final OrderDetails orderDetails=new OrderDetails();

                                        System.out.println(qty.getText() + "success");
                                        orderDetails.setProduct(product);
                                        orderDetails.setQuantity(Integer.parseInt(quantity));

                                        SessionInformations.getInstance().getEntry().add(orderDetails);
                                        //SessionInformations.getInstance().setEntry(null);
                                       // SessionInformations.getInstance().setEntry(entries);
                                    }



                                }
                            });
                    // Setting Negative "NO" Button
                    alertDialog.setNegativeButton("NO",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Write your code here to execute after dialog
                                    dialog.cancel();
                                    System.out.println("Closed");
                                }
                            });

                    // closed

                    // Showing Alert Message
                    alertDialog.show();


                }
            });





    }

        return rootView;


    }
    public static final String calculateTotal(Product pro,int quantity){
        float unitPrice = pro.getUnitPrice();
        float retailPrice=pro.getRetailPrice();
        float salesTax = Float.valueOf(pro.getSalesTax());
        float otherTax = Float.valueOf(pro.getOtherTax());
        double subtotal=0.0;


        //var subtotal = (Number(unitPrice) * Number(quantity)*( 1+(Number(salesTax)+Number(otherTax))/100  )).toFixed(2);

       if(SessionInformations.getInstance().getEmployee().getCustomerType().equals("R"))
            subtotal=(retailPrice * quantity*( 1+(salesTax+otherTax)/100  ));
        else
            subtotal=(unitPrice * quantity*( 1+(salesTax+otherTax)/100  ));
        return String.valueOf(subtotal);

    }


}
