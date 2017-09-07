package com.ventruxinformatics.prodcast;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import businessObjects.SessionInformations;
import businessObjects.domain.Category;
import businessObjects.domain.OrderDetails;
import businessObjects.domain.Product;

/**
 * An activity representing a single Product detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link ProductListActivity}.
 */
public class ProductDetailActivity extends ProdcastCBaseActivity {
    public static final String ARG_ITEM_ID = "item_id";
    private Category selectedCategory;


    public Category getSelectedCategory() {
        return selectedCategory;
    }

    public void setSelectedCategory(Category selectedCategory) {
        this.selectedCategory = selectedCategory;
    }

    @Override
    public String getProdcastTitle(){

        return "Product";
    }

    @Override
    public boolean getCompanyName() {
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

       /* ImageButton addToCart = (ImageButton) findViewById(R.id.addToCart);
        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(ProductDetailActivity.this,EntryActivity.class);
                startActivity(i);

            }
        });*/

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            //Category selectedCategory=intent.getExtra(ARG_ITEM_ID,0);
            ProductDetailFragment fragment = new ProductDetailFragment();

            Intent i=getIntent();
            long selectedCategoryId=i.getLongExtra("selectedCategoryId",0);
            String selectedCategoryName=i.getStringExtra("selectedCategoryName");
            Category category=new Category();
            category.setCategoryId(selectedCategoryId);
            category.setCategoryName(selectedCategoryName);

            fragment.setSelectedCategory(category);
            fragment.setProductDetailActivity(ProductDetailActivity.this);


            //fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.product_detail_container, fragment)
                    .commit();
        }
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            navigateUpTo(new Intent(this, ProductListActivity.class));
            return true;
        }
        else
        {
            Intent intent=new Intent(this,EntryActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    MenuItem menuItem;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        menuItem = menu.findItem(R.id.testAction);

        setOrderTotal();

        return true;
    }

    public void setOrderTotal(){
        int count=0;
        List<OrderDetails> entries=SessionInformations.getInstance().getEntry();
        for(OrderDetails orderDetails:entries)
        {
            count=count+orderDetails.getQuantity();
        }
        menuItem.setIcon(buildCounterDrawable(count));

    }

    private Drawable buildCounterDrawable(int count) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.content_dd_to_cart, null);
        //view.setBackgroundResource(backgroundImageId);
        Bitmap bitmap = null;


        if (count == 0) {
            View counterTextPanel = view.findViewById(R.id.count);
            counterTextPanel.setVisibility(View.GONE);
        } else {
            TextView textView = (TextView) view.findViewById(R.id.count);
            textView.setText("" + count);



        }
        view.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        view.setDrawingCacheEnabled(true);
        bitmap=Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        view.setDrawingCacheEnabled(false);



        return new BitmapDrawable(getResources(), bitmap);


    }






}
