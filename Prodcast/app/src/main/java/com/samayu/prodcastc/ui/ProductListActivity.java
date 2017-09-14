package com.samayu.prodcastc.ui;

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
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.samayu.prodcastc.businessObjects.SessionInfo;
import com.samayu.prodcastc.businessObjects.connect.ProdcastServiceManager;
import com.samayu.prodcastc.businessObjects.domain.Category;
import com.samayu.prodcastc.businessObjects.domain.OrderDetails;
import com.samayu.prodcastc.businessObjects.domain.Product;
import com.samayu.prodcastc.businessObjects.dto.AdminDTO;
import com.ventruxinformatics.prodcast.R;

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
    FloatingActionButton newOrderPin;
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

        long employeeId= SessionInfo.getInstance().getEmployee().getEmployeeId();
        newOrderPin=(FloatingActionButton) findViewById(R.id.newOrderPin);
        SessionInfo.getInstance().setEntry(new ArrayList<OrderDetails>());
        progressDialog.show();

        Call<AdminDTO<List<Category>>> categoryDTO = new ProdcastServiceManager().getClient().getCategory( employeeId );

        categoryDTO.enqueue(new Callback<AdminDTO<List<Category>>>() {
            @Override
            public void onResponse(Call<AdminDTO<List<Category>>> call, Response<AdminDTO<List<Category>>> response) {
                if(response.isSuccessful()) {
                    AdminDTO<List<Category>> dto = response.body();
                    if (dto.isError()) {
                        progressDialog.dismiss();
                        getErrorBox(context,dto.getErrorMessage()).show();

                    } else {

                        //List<Category> categories = dto.getResult();
                        SessionInfo.getInstance().setCategoryDetails(dto.getResult());
                        // System.out.println(categories.get(0).getCategoryId());
                        setupRecyclerView((RecyclerView) recyclerView);

                        progressDialog.dismiss();


                    }
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
                if(response.isSuccessful()) {


                    AdminDTO<List<Product>> dto = response.body();
                    if (dto.isError()) {

                        progressDialog.dismiss();
                        getErrorBox(context,dto.getErrorMessage()).show();
                    } else {

                        //List<Product> products = dto.getResult();
                        SessionInfo.getInstance().setProductDetails(dto.getResult());
                        progressDialog.dismiss();


                    }
                }

            }

            @Override
            public void onFailure(Call<AdminDTO<List<Product>>> call, Throwable t) {
                t.printStackTrace();
                progressDialog.dismiss();
                getAlertBox(context).show();

            }
        });



        newOrderPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SessionInfo.getInstance().getEntry().size()>0) {
                    Intent intent = new Intent(ProductListActivity.this, EntryActivity.class);
                    startActivity(intent);
                }
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

        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(SessionInfo.getInstance().getCategoryDetails()));
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
            public final TextView mIdView;
            public Category mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = (TextView) view.findViewById(R.id.categoryName);
               // mContentView = (TextView) view.findViewById(R.id.categoryDescription);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mIdView.getText() + "'";
            }
        }
    }


   /* MenuItem menuItem;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        menuItem = menu.findItem(R.id.testAction);

        setOrderTotal();

        return true;
    }*/

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item){
        return super.onOptionsItemSelected(item);
    }*/

    public void setOrderTotal()  {
        int count=0;
        List<OrderDetails> entries= SessionInfo.getInstance().getEntry();
        newOrderPin.setImageDrawable(buildCounterDrawable(entries.size()));
    }

    public static Bitmap createImage(int width, int height) {

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);

        Paint paint = new Paint();


        canvas.drawRect(0F, 0F, (float) width, (float) height, paint);

        return bitmap;

    }


    private Drawable buildCounterDrawable(int count) {
        /*if(count ==0)
            return new BitmapDrawable(getResources(), createImage(1,1)) ;

         */

        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.content_dd_to_cart, null);
        //view.setBackgroundResource(backgroundImageId);
        Bitmap bitmap = null;


        /*if (count == 0) {
            View counterTextPanel = view.findViewById(R.id.count);
            counterTextPanel.setVisibility(View.GONE);
           view.setVisibility(View.GONE);
        } else {*/

            TextView textView = (TextView) view.findViewById(R.id.count);
            textView.setText(" " + count+" ");
        //}


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

