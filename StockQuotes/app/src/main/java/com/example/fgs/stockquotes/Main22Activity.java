package com.example.fgs.stockquotes;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.fgs.stockquotes.bussinessobjects.ListData;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class Main22Activity extends AppCompatActivity {
    private String ticker;
    private ListView stocks;

    ArrayList<ListData> myLists=new ArrayList<ListData>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main22);
        Intent data = getIntent();
        ticker = data.getStringExtra("STOCKSYMBOL");
        stocks = (ListView) findViewById(R.id.avergeStockQuotess);
       StockQuotess task=new StockQuotess(ticker);
        task.execute();
    }
    public class StockQuotess extends AsyncTask<Void, Void, JSONObject> {
        private final String stockSymbol;

        JSONObject jsonArray;

        StockQuotess(String ticker) {
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
                    ListData lv=new ListData();


                    lv.setQuoteId(symbol.getJSONObject(i).getString("quoteId"));
                    lv.setStockSymbol(symbol.getJSONObject(i).getString("stockSymbol"));
                    lv.setClose(symbol.getJSONObject(i).getString("close"));
                    lv.setDate(symbol.getJSONObject(i).getString("date"));
                    //add this object into the ArrayList myList
                    myLists.add(lv);

                }
                StockQuotesArrayAdapter adapter=new StockQuotesArrayAdapter(getApplicationContext(),R.layout.stockquotes_ticker,myLists);
                //stocks.setAdapter(adapter);
              //  lvDetails.setAdapter(new MyBaseAdapter(context,myList));
              //  MainActivity.ListUtils.setDynamicHeight(lvDetails);

              //  MainActivity.AvgStockQuotes task=new MainActivity.AvgStockQuotes(ticker);
              //  task.execute();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    public class StockQuotesArrayAdapter extends ArrayAdapter {
        private List<ListData> listData;
        private int resource;
        private LayoutInflater inflator;
        public StockQuotesArrayAdapter(Context context, int resource, List<ListData> objects) {
            super(context, resource, objects);
            listData=objects;
            this.resource=resource;
            inflator= (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView==null){
                convertView=inflator.inflate(R.layout.stockquotes_ticker,null);

            }
            TextView quoteId;
            TextView ticker;
            TextView date;
            TextView price;
            quoteId=(TextView)convertView.findViewById(R.id.quoteId);
            ticker=(TextView)convertView.findViewById(R.id.ticker);
            date=(TextView)convertView.findViewById(R.id.date);
            price=(TextView)convertView.findViewById(R.id.close);
            quoteId.setText(listData.get(position).getQuoteId());
            ticker.setText(listData.get(position).getStockSymbol());
            date.setText(listData.get(position).getDate());
            price.setText(listData.get(position).getClose());
            return convertView;
        }

    }
}
