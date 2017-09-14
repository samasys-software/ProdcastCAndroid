package com.samayu.prodcastc.ui;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.samayu.prodcastc.businessObjects.GlobalUsage;
import com.samayu.prodcastc.businessObjects.SessionInfo;
import com.samayu.prodcastc.businessObjects.domain.Category;
import com.samayu.prodcastc.businessObjects.domain.OrderDetails;
import com.samayu.prodcastc.businessObjects.domain.Product;
import com.ventruxinformatics.prodcast.R;

//import com.ventruxinformatics.prodcast.dummy.DummyContent;

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
    ImageView img;
    String currencySymbol= SessionInfo.getInstance().getEmployee().getDistributor().getCurrencySymbol();
    //NumberFormat numberFormat= GlobalUsage.getNumberFormat();
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

    public ProductDetailFragment() {

    }


    List<Product> productDetails = new ArrayList<Product>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Load the dummy content specified by the fragment
        // arguments. In a real-world scenario, use a Loader
        // to load content from a content provider.
        //    System.out.println(mItem.size());

        Activity activity = this.getActivity();
        Toolbar appBarLayout = (Toolbar) activity.findViewById(R.id.detail_toolbar);
        setHasOptionsMenu(true);

       // appBarLayout.setTitle(getSelectedCategory().getCategoryName());
        activity.setTitle(getSelectedCategory().getCategoryName());
        appBarLayout.setSubtitle("Items");


    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.product_detail, container, false);


        List<Category> categories = SessionInfo.getInstance().getCategoryDetails();
        List<Product> products = SessionInfo.getInstance().getProductDetails();
        int count = 0;
        for (Category category : categories)
            if (category.getCategoryId() == getSelectedCategory().getCategoryId()) {
                for (Product product : products) {
                  //  System.out.println("Selected Category Id" + category.getCategoryId());
                   // System.out.println("Product Category Id" + product.getCategoryId());
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

                    final Product product = productDetails.get(position);

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity(), R.style.AlertTheme);
                    alertDialog.setTitle("Please Enter A Quantity");
                    alertDialog.setCancelable(true);
                    LayoutInflater inflater = getActivity().getLayoutInflater();
                    View diaView = inflater.inflate(R.layout.qty_dialog, null);
                    alertDialog.setView(diaView);

                    productName = (TextView) diaView.findViewById(R.id.productName);
                    unitPrice = (TextView) diaView.findViewById(R.id.unitPrice);
                    qty = (EditText) diaView.findViewById(R.id.qty);
                    subTotal = (TextView) diaView.findViewById(R.id.subTotal);
                    img = (ImageView) diaView.findViewById(R.id.img);
                    productName.setText("Item :" + product.getProductName());
                    float price;
                    if (SessionInfo.getInstance().getEmployee().getCustomerType().equals("R")) {
                        price = product.getRetailPrice();
                    } else {
                        price = product.getUnitPrice();
                    }
                    unitPrice.setText("Unit Price : "+currencySymbol+"" +GlobalUsage.getNumberFormat().format( price));

                    subTotal.setText("Sub Total :"+currencySymbol+" 0.00");



                    qty.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                           // NumberFormat nf1 = NumberFormat.getInstance();


                            int quantity = 0;
                            qty.setError(null);
                            try {
                                quantity = Integer.parseInt(s.toString());


                            } catch (Exception e) {

                                qty.setError("Please Enter valid number");

                            }
                            subTotal.setText("Sub Total : " +currencySymbol+""+GlobalUsage.getNumberFormat().format(calculateTotal(product,quantity)));

                        }
                    });

                    //alertDialog.setIcon(R.drawable.customer_icon);
                    alertDialog.setNegativeButton("CANCEL",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Write your code here to execute after dialog
                                    dialog.cancel();

                                }
                            });

                    alertDialog.setPositiveButton("ADD TO ORDER", null);
                    TextView textView = new TextView(getActivity());
                    textView.setText("Please Enter Quantity");
                    textView.setTextColor(getResources().getColor(R.color.colorInversePrimary));
                    textView.setTextSize(20);
                    textView.setPadding(10,10,10,10);

                    alertDialog.setCustomTitle(textView);
                    final AlertDialog theDialog = alertDialog.show();
                    final TextView confirmationMessage = (TextView)  theDialog.findViewById(R.id.confirmationMessage);

                    theDialog.getButton(
                            DialogInterface.BUTTON_POSITIVE)
                            .setOnClickListener(
                                    new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Button button = (Button) v;
                                            boolean add=false;

                                            if( button.getText().equals("CONTINUE")){
                                                add = true;
                                            }

                                                final String quantity = qty.getText().toString();
                                                if (TextUtils.isEmpty(quantity)) {
                                                    qty.setError(getString(R.string.required_quantity));
                                                    qty.requestFocus();
                                                    return;
                                                }

                                                button.setEnabled(false);

                                                List<OrderDetails> orderEntries = SessionInfo.getInstance().getEntry();
                                                boolean productActive = false;
                                                OrderDetails existingProduct = null;
                                                for (OrderDetails orderEntry : orderEntries) {
                                                    if (orderEntry.getProduct().getId() == product.getId()) {
                                                        productActive = true;
                                                        existingProduct = orderEntry;
                                                        break;

                                                    }
                                                }

                                            final OrderDetails selectedProduct = existingProduct;

                                                if( productActive && !add ){
                                                    button.setText("CONTINUE");
                                                    button.setEnabled(true);
                                                    confirmationMessage.setText( "Your order already has " + selectedProduct.getQuantity() + " of the Item " + selectedProduct.getProduct().getProductName() + " Would you like to add more to it? " );
                                                    confirmationMessage.setVisibility(View.VISIBLE);
                                                    return;
                                                }

                                                if (productActive && add) {
                                                    selectedProduct.setQuantity(selectedProduct.getQuantity() + Integer.parseInt(quantity));

                                                } else {
                                                    addProduct(product, Integer.parseInt(quantity));
                                                }

                                                final long animationDuration = 1000;
                                                ObjectAnimator animX = ObjectAnimator.ofFloat(img, "x", 850);
                                                ObjectAnimator animY = ObjectAnimator.ofFloat(img, "y", 0);
                                                AnimatorSet animSetXY = new AnimatorSet();
                                                animSetXY.playTogether(animX, animY);
                                                animSetXY.setDuration(animationDuration);
                                                animSetXY.addListener(new Animator.AnimatorListener() {
                                                    @Override
                                                    public void onAnimationStart(Animator animation) {

                                                        img.setVisibility(View.VISIBLE);
                                                    }

                                                    @Override
                                                    public void onAnimationEnd(Animator animation) {
                                                        if (productDetailActivity != null) {
                                                            productDetailActivity.setOrderTotal();
                                                        }

                                                        theDialog.dismiss();
                                                    }

                                                    @Override
                                                    public void onAnimationCancel(Animator animation) {

                                                    }

                                                    @Override
                                                    public void onAnimationRepeat(Animator animation) {

                                                    }
                                                });
                                                animSetXY.start();


                                            }

                                    });
                }
            });
        }
        return rootView;
    }


    public static final double calculateTotal(Product pro, int quantity) {


        float subTotal=calculateSubTotal(pro,quantity);
        float tax=calculateTax(pro,quantity);
        double total=subTotal+tax;


        return total;

    }
    public static final float calculateTax(Product pro,int quantity) {

        float salesTax = Float.valueOf(pro.getSalesTax());
         float otherTax = Float.valueOf(pro.getOtherTax());
        float tax = calculateSubTotal(pro,quantity)*(salesTax + otherTax)/100 ;


        return tax;

    }

    public static final float calculateSubTotal(Product pro, int quantity) {
        float unitPrice = pro.getUnitPrice();
        float retailPrice = pro.getRetailPrice();
        //float salesTax = Float.valueOf(pro.getSalesTax());
        // float otherTax = Float.valueOf(pro.getOtherTax());
        float subtotal = 0;



        //var subtotal = (Number(unitPrice) * Number(quantity)*( 1+(Number(salesTax)+Number(otherTax))/100  )).toFixed(2);
        if (SessionInfo.getInstance().getEmployee().getCustomerType().equals("R"))
            subtotal = (retailPrice * quantity);
        else
            subtotal = (unitPrice * quantity);
        return subtotal;

    }


    private void addProduct(Product product,int quantity) {

        final OrderDetails orderDetails = new OrderDetails();
        orderDetails.setProduct(product);
        orderDetails.setQuantity(quantity);
//        orderDetails.setSubTotal(Float.parseFloat(calculateTotal(product,quantity)));
        SessionInfo.getInstance().getEntry().add(orderDetails);
        if(productDetailActivity!=null)
            productDetailActivity.setOrderTotal();
    }



}








