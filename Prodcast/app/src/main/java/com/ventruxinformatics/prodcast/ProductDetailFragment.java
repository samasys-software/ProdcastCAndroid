package com.ventruxinformatics.prodcast;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

//import com.ventruxinformatics.prodcast.dummy.DummyContent;

import org.w3c.dom.Text;

import java.text.NumberFormat;
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

    TextView productName;
    TextView unitPrice;
    EditText qty;
    TextView subTotal;
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    ProductDetailActivity productDetailActivity;


    public ProductDetailActivity getProductDetailActivity() {
        return productDetailActivity;
    }

    public void setProductDetailActivity(ProductDetailActivity productDetailActivity) {
        this.productDetailActivity = productDetailActivity;
    }

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

    public  ProductDetailFragment(){

    }


    List<Product> productDetails=new ArrayList<Product>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
               //    System.out.println(mItem.size());

            Activity activity = this.getActivity();
            Toolbar appBarLayout = (Toolbar) activity.findViewById(R.id.detail_toolbar);

                appBarLayout.setSubtitle(getSelectedCategory().getCategoryName());


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.product_detail, container, false);


        List<Category> categories = SessionInformations.getInstance().getCategoryDetails();
        List<Product> products = SessionInformations.getInstance().getProductDetails();
        int count = 0;
        for (Category category : categories)
            if (category.getCategoryId() == getSelectedCategory().getCategoryId()) {
                for (Product product : products) {
                    System.out.println("Selected Category Id" + category.getCategoryId());
                    System.out.println("Product Category Id" + product.getCategoryId());
                    if (product.getCategoryId() == category.getCategoryId()) {
                        productDetails.add(count, product);
                        count++;
                    }

                }
                break;
            }


        // Show the dummy content as text in a TextView.
        if (productDetails != null) {
            ListView listView = (ListView) rootView.findViewById(R.id.product_detail);
            listView.setAdapter(new AllProductsAdapter(getActivity(), productDetails));


            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    final Product product = SessionInformations.getInstance().getProductDetails().get(position);

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity(), R.style.AlertTheme);
                    alertDialog.setTitle("Prodcast Notification");

                    alertDialog.setCancelable(true);
                    LayoutInflater inflater = getActivity().getLayoutInflater();
                     View diaView = inflater.inflate(R.layout.qty_dialog, null);
                    alertDialog.setView(diaView);


                    productName = (TextView) diaView.findViewById(R.id.productName);
                    unitPrice = (TextView) diaView.findViewById(R.id.unitPrice);
                    qty = (EditText) diaView.findViewById(R.id.qty);
                    subTotal = (TextView) diaView.findViewById(R.id.subTotal);

                    productName.setText("Product Name :" + product.getProductName());
                    unitPrice.setText("Unit Price : " + product.getUnitPrice());

                   subTotal.setText("Sub Total : 0.0");

                    final float unitPrice;
                    if (SessionInformations.getInstance().getEmployee().getCustomerType().equals("R")) {
                        unitPrice = product.getRetailPrice();
                    } else {
                        unitPrice = product.getUnitPrice();
                    }

                    qty.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        NumberFormat nf1 = NumberFormat.getInstance();


                        int quantity = 0;
                        qty.setError(null);
                        try{
                            quantity = Integer.parseInt(s.toString());


                        }
                        catch (Exception e){

                            qty.setError("Please Enter valid number");

                        }
                        subTotal.setText("Sub Total : " +nf1.format(quantity*unitPrice));

                    }
                });

                    alertDialog.setIcon(R.drawable.customer_icon);


                    alertDialog.setPositiveButton("SUBMIT",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Write your code here to execute after dialog
                                    String quantity = qty.getText().toString();
                                    if (TextUtils.isEmpty(quantity)) {
                                        qty.setError(getString(R.string.required_quantity));
                                        qty.requestFocus();
                                    }
                                    if (!TextUtils.isEmpty(quantity)) {
                                        final OrderDetails orderDetails = new OrderDetails();
                                        orderDetails.setProduct(product);
                                        orderDetails.setQuantity(Integer.parseInt(quantity));

                                        SessionInformations.getInstance().getEntry().add(orderDetails);
                                        if(productDetailActivity!=null) {
                                            productDetailActivity.setOrderTotal();
                                        }
                                        //SessionInformations.getInstance().setEntry(null);
                                        // SessionInformations.getInstance().setEntry(entries);
                                    }


                                }
                            });
                    // Setting Negative "NO" Button
                    alertDialog.setNegativeButton("CANCEL",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Write your code here to execute after dialog
                                    dialog.cancel();

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








