package com.ventruxinformatics.prodcast;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
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
    //List<Category> categories=new ArrayList<Category>();
    View recyclerView;

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
        recyclerView= findViewById(R.id.product_list);

        long employeeId=SessionInformations.getInstance().getEmployee().getEmployeeId();
        SessionInformations.getInstance().setEntry(new ArrayList<OrderDetails>());

        Call<AdminDTO<List<Category>>> categoryDTO = new ProdcastServiceManager().getClient().getCategory( employeeId );

        categoryDTO.enqueue(new Callback<AdminDTO<List<Category>>>() {
            @Override
            public void onResponse(Call<AdminDTO<List<Category>>> call, Response<AdminDTO<List<Category>>> response) {
                String responseString = null;
                AdminDTO<List<Category>> dto = response.body();
                if(dto.isError()) {

                }
                else {

                    //List<Category> categories = dto.getResult();
                    SessionInformations.getInstance().setCategoryDetails(dto.getResult());
                    // System.out.println(categories.get(0).getCategoryId());
                    setupRecyclerView((RecyclerView) recyclerView);


                }

            }

            @Override
            public void onFailure(Call<AdminDTO<List<Category>>> call, Throwable t) {
                t.printStackTrace();

            }
        });



        Call<AdminDTO<List<Product>>> productDTO = new ProdcastServiceManager().getClient().getProducts( employeeId );

        productDTO.enqueue(new Callback<AdminDTO<List<Product>>>() {
            @Override
            public void onResponse(Call<AdminDTO<List<Product>>> call, Response<AdminDTO<List<Product>>> response) {
                String responseString = null;
                AdminDTO<List<Product>> dto = response.body();
                if(dto.isError()) {

                }
                else {

                    //List<Product> products = dto.getResult();
                    SessionInformations.getInstance().setProductDetails(dto.getResult());



                }

            }

            @Override
            public void onFailure(Call<AdminDTO<List<Product>>> call, Throwable t) {
                t.printStackTrace();

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
            holder.mIdView.setText(String.valueOf(categories.get(position).getCategoryId()));
            holder.mContentView.setText(categories.get(position).getCategoryName());

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
            public final TextView mContentView;
            public Category mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = (TextView) view.findViewById(R.id.id);
                mContentView = (TextView) view.findViewById(R.id.content);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }
}

