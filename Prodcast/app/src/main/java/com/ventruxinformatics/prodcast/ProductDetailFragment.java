package com.ventruxinformatics.prodcast;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

//import com.ventruxinformatics.prodcast.dummy.DummyContent;

import java.util.ArrayList;
import java.util.List;

import businessObjects.domain.Category;
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
   private ArrayList<Product> mItem;

    public ArrayList<Product> getmItem() {
        return mItem;
    }

    public void setmItem(ArrayList<Product> mItem) {
        this.mItem = mItem;
    }


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ProductDetailFragment() {
    }

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
                appBarLayout.setTitle(getmItem().get(0).getCategoryName());
            }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.product_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            ListView listView=(ListView) rootView.findViewById(R.id.product_detail);
            listView.setAdapter(new AllProductsList(getActivity(),getmItem()));
        }

        return rootView;


    }


}
