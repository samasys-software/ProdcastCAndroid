package com.example.fgs.stockquotes;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.preference.PreferenceActivity;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.fgs.stockquotes.bussinessobjects.AverageForStockQuotes;
import com.example.fgs.stockquotes.bussinessobjects.ListData;

import org.json.JSONArray;
import org.json.JSONObject;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {
    private String ticker;
    private ListView averageStocks;
    ListView lvDetails;
    Context context=MainActivity.this;
    ArrayList<ListData> myList=new ArrayList<ListData>();
    ArrayList<AverageForStockQuotes> averageList=new ArrayList<AverageForStockQuotes>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent data = getIntent();
        ticker = data.getStringExtra("STOCKSYMBOL");
        lvDetails=(ListView) findViewById(R.id.stockQuote);
        averageStocks = (ListView) findViewById(R.id.avergeStockQuotes);
        StockQuotes taskquotes=new StockQuotes(ticker);
        taskquotes.execute();
    }
    public class StockQuotes extends AsyncTask<Void, Void, JSONObject> {
        private final String stockSymbol;

        JSONObject jsonArray;

        StockQuotes(String ticker) {
            stockSymbol = ticker;
        }


        protected JSONObject doInBackground(Void... params) {
            try {
                SyncHttpClient asyncHttpClient = new SyncHttpClient();
                RequestParams requestParams = new RequestParams();
                requestParams.put("stockSymbol", stockSymbol);

                String url = "http://192.168.1.9:8080/getStockQuotes";
                asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            System.out.println(response);
                            jsonArray = response;
                        } catch (Exception er) {
                            er.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable e) {
                        super.onFailure(statusCode, headers, responseString, e);
                        e.printStackTrace();
                    }

                });
            } catch (Exception e) {
                e.printStackTrace();

            }
            return jsonArray;
        }

        @Override
        protected void onPostExecute(JSONObject stocks) {

            System.out.println(stocks);
            try {

                JSONArray symbol=stocks.getJSONArray("stockQuotes");

                // ArrayList<String> line=new ArrayList<>();
                for(int i=0;i<symbol.length();i++){
                    com.example.fgs.stockquotes.bussinessobjects.ListData lv=new com.example.fgs.stockquotes.bussinessobjects.ListData();


                    lv.setQuoteId(symbol.getJSONObject(i).getString("quoteId"));
                    lv.setStockSymbol(symbol.getJSONObject(i).getString("stockSymbol"));
                    lv.setClose(symbol.getJSONObject(i).getString("close"));
                    lv.setDate(symbol.getJSONObject(i).getString("date"));
                    //add this object into the ArrayList myList
                    myList.add(lv);

                }
                lvDetails.setAdapter(new com.example.fgs.stockquotes.MyBaseAdapter(context,myList));
                ListUtils.setDynamicHeight(lvDetails);

                AvgStockQuotes task=new AvgStockQuotes(ticker);
                task.execute();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    public class AvgStockQuotes extends AsyncTask<Void, Void, JSONObject> {
        private final String stockSymbol;

        JSONObject jsonObject;

        AvgStockQuotes(String ticker) {
            stockSymbol = ticker;
        }


        protected JSONObject doInBackground(Void... params) {
            try {
                SyncHttpClient asyncHttpClient = new SyncHttpClient();
                RequestParams requestParams = new RequestParams();
                requestParams.put("stockSymbol", stockSymbol);

                String url = "http://192.168.1.9:8080/getAverageForStockSymbol";
                asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            System.out.println(response);
                            jsonObject = response;
                        } catch (Exception er) {
                            er.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable e) {
                        super.onFailure(statusCode, headers, responseString, e);
                        e.printStackTrace();
                    }

                });
            } catch (Exception e) {
                e.printStackTrace();

            }
            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject stocks) {

            System.out.println(stocks);

            try {

                JSONArray symbol=stocks.getJSONArray("averages");
                for(int i=0;i<symbol.length();i++){
                    com.example.fgs.stockquotes.bussinessobjects.AverageForStockQuotes avearge=new com.example.fgs.stockquotes.bussinessobjects.AverageForStockQuotes();
                    avearge.setAvgId(symbol.getJSONObject(i).getString("avgId"));
                    avearge.setAvgStockSymbol(symbol.getJSONObject(i).getString("stockSymbol"));
                    avearge.setAverageValues(symbol.getJSONObject(i).getString("averagevalue"));
                    avearge.setAvgdate(symbol.getJSONObject(i).getString("date"));
                    averageList.add(avearge);

                }

                AverageAdapter adapter=new AverageAdapter(getApplicationContext(),R.layout.average_stockquotes,averageList);
                averageStocks.setAdapter(adapter);
                ListUtils.setDynamicHeight(averageStocks);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public class AverageAdapter extends ArrayAdapter {
        private List<com.example.fgs.stockquotes.bussinessobjects.AverageForStockQuotes> AvgStockQuotes;
        private int resource;
        private LayoutInflater inflator;
        public AverageAdapter(Context context, int resource, List<com.example.fgs.stockquotes.bussinessobjects.AverageForStockQuotes> objects) {
            super(context, resource, objects);
            AvgStockQuotes=objects;
            this.resource=resource;
            inflator= (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView==null){
                convertView=inflator.inflate(R.layout.average_stockquotes,null);

            }
            TextView avgId;
            TextView ticker;
            TextView date;
            TextView avgValues;
            avgId=(TextView)convertView.findViewById(R.id.avgId);
            ticker=(TextView)convertView.findViewById(R.id.ticker);
            date=(TextView)convertView.findViewById(R.id.date);
            avgValues=(TextView)convertView.findViewById(R.id.avgValues);
            avgId.setText(AvgStockQuotes.get(position).getAvgId());
            ticker.setText(AvgStockQuotes.get(position).getAvgStockSymbol());
            date.setText(AvgStockQuotes.get(position).getAvgdate());
            avgValues.setText(AvgStockQuotes.get(position).getAverageValues());
            return convertView;
        }

    }

    public static class ListUtils{
        public static void setDynamicHeight(ListView listView){
            ListAdapter listAdapter=listView.getAdapter();
            if(listAdapter==null){
                return;
            }
            int height=0;
            int desiredWidth= View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
            for(int i=0;i<listAdapter.getCount();i++){
                View listItem=listAdapter.getView(i,null,listView);
                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                height=height+listItem.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = height + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
            listView.setLayoutParams(params);
            listView.requestLayout();

        }

    }

}
