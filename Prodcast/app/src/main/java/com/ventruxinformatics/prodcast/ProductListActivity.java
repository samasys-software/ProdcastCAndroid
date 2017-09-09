package com.ventruxinformatics.prodcast;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import businessObjects.connect.ProdcastServiceManager;

import java.util.ArrayList;
import java.util.List;

import businessObjects.SessionInformations;
import businessObjects.domain.Category;
import businessObjects.domain.OrderDetails;
import businessObjects.domain.Product;
import businessObjects.dto.AdminDTO;
import businessObjects.font_design.NewTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * An activity representing a list of Products. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ProductDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */

public class ProductListActivity extends ProdcastCBaseActivity {




    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    ProgressDialog progressDialog;
    //List<Category> categories=new ArrayList<Category>();
    View recyclerView;
    Context context;

    @Override
    public String getProdcastTitle(){

            return "Categories";
    }

    @Override
    public boolean getCompanyName() {
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        context=this;
        progressDialog=getProgressDialog(this);
        recyclerView= findViewById(R.id.product_list);

        long employeeId=SessionInformations.getInstance().getEmployee().getEmployeeId();
        SessionInformations.getInstance().setEntry(new ArrayList<OrderDetails>());
        progressDialog.show();

        Call<AdminDTO<List<Category>>> categoryDTO = new ProdcastServiceManager().getClient().getCategory( employeeId );

        categoryDTO.enqueue(new Callback<AdminDTO<List<Category>>>() {
            @Override
            public void onResponse(Call<AdminDTO<List<Category>>> call, Response<AdminDTO<List<Category>>> response) {
                String responseString = null;
                AdminDTO<List<Category>> dto = response.body();
                if(dto.isError()) {
                    progressDialog.dismiss();

                }
                else {

                    //List<Category> categories = dto.getResult();
                    SessionInformations.getInstance().setCategoryDetails(dto.getResult());
                    // System.out.println(categories.get(0).getCategoryId());
                    setupRecyclerView((RecyclerView) recyclerView);

                    progressDialog.dismiss();


                }

            }

            @Override
            public void onFailure(Call<AdminDTO<List<Category>>> call, Throwable t) {
                t.printStackTrace();
                progressDialog.dismiss();
                getAlertBox(context).show();

            }
        });

        progressDialog.show();

        Call<AdminDTO<List<Product>>> productDTO = new ProdcastServiceManager().getClient().getProducts( employeeId );

        productDTO.enqueue(new Callback<AdminDTO<List<Product>>>() {
            @Override
            public void onResponse(Call<AdminDTO<List<Product>>> call, Response<AdminDTO<List<Product>>> response) {
                String responseString = null;
                AdminDTO<List<Product>> dto = response.body();
                if(dto.isError()) {
                    progressDialog.dismiss();
                }
                else {

                    //List<Product> products = dto.getResult();
                    SessionInformations.getInstance().setProductDetails(dto.getResult());
                    progressDialog.dismiss();



                }

            }

            @Override
            public void onFailure(Call<AdminDTO<List<Product>>> call, Throwable t) {
                t.printStackTrace();
                progressDialog.dismiss();
                getAlertBox(context).show();

            }
        });



        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/


        assert recyclerView != null;

        if (findViewById(R.id.product_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        setOrderTotal();
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {

        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(SessionInformations.getInstance().getCategoryDetails()));
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<Category> categories;

        public SimpleItemRecyclerViewAdapter(List<Category> items) {
            categories = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.product_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = categories.get(position);
            holder.mIdView.setText(String.valueOf(categories.get(position).getCategoryName()));
           // holder.mContentView.setText(categories.get(position).getCategory());

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle bundle=new Bundle();

                        ProductDetailFragment fragment = new ProductDetailFragment();
                        fragment.setSelectedCategory(holder.mItem);
                        //fragment.setSelected
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.product_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, ProductDetailActivity.class);
                        intent.putExtra("selectedCategoryId",holder.mItem.getCategoryId());
                        intent.putExtra("selectedCategoryName",holder.mItem.getCategoryName());
                        //ProductDetailActivity productDetail=new ProductDetailActivity();
                        //productDetail.setSelectedCategory(holder.mItem);
                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return categories.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final NewTextView mIdView;
            public Category mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = (NewTextView) view.findViewById(R.id.categoryName);
               // mContentView = (TextView) view.findViewById(R.id.categoryDescription);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mIdView.getText() + "'";
            }
        }
    }


    MenuItem menuItem;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        menuItem = menu.findItem(R.id.testAction);

        setOrderTotal();

        return true;
    }

    public void setOrderTotal()  {
        int count=0;
        List<OrderDetails> entries=SessionInformations.getInstance().getEntry();

        if(menuItem != null)
            menuItem.setIcon(buildCounterDrawable(entries.size()));

/*
        if( menuItem != null ) {
            View icon = menuItem.getActionView();

            if( icon == null ) return;
            final ScaleAnimation growAnim = new ScaleAnimation(1.0f, 1.5f, 1.0f, 1.5f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            final ScaleAnimation shrinkAnim = new ScaleAnimation(1.5f, 1.0f, 1.5f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

            growAnim.setDuration(1000);
            growAnim.start();

            icon.setAnimation(growAnim);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            icon.setAnimation(shrinkAnim);
            shrinkAnim.start();
        }
*/

    }

    public static Bitmap createImage(int width, int height) {

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);

        Paint paint = new Paint();


        canvas.drawRect(0F, 0F, (float) width, (float) height, paint);

        return bitmap;

    }


    private Drawable buildCounterDrawable(int count) {
        if(count ==0)
            return new BitmapDrawable(getResources(), createImage(1,1)) ;

        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.content_dd_to_cart, null);
        //view.setBackgroundResource(backgroundImageId);
        Bitmap bitmap = null;


        if (count == 0) {
            View counterTextPanel = view.findViewById(R.id.count);
            counterTextPanel.setVisibility(View.GONE);
            view.setVisibility(View.GONE);
        } else {

            NewTextView textView = (NewTextView) view.findViewById(R.id.count);
            textView.setText(" " + count+" ");
        }


        view.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        int measureWidth = view.getMeasuredWidth();
        int measureHeight = view.getMeasuredHeight();
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());


        view.setDrawingCacheEnabled(true);
        bitmap=Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        view.setDrawingCacheEnabled(false);



        return new BitmapDrawable(getResources(), bitmap);

        //return view;
    }
}

