package com.example.fgs.stockquotes;


import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class StockQuotesForTicker extends AppCompatActivity {
    private String ticker;

  private ListView StockQuote;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_quotes_for_ticker);
       // setContentView(R.layout.stockquote_testview);

        Intent data = getIntent();
        ticker = data.getStringExtra("STOCKSYMBOL");
        StockQuote = (ListView) findViewById(R.id.stockQuote);

        StockQuotes taskvalue = new StockQuotes(ticker);
        taskvalue.execute();

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

                String url = "http://192.168.1.2:8080/getStockQuotes";
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

    ArrayList<String> line=new ArrayList<>();
                for(int i=0;i<symbol.length();i++){
        String  values=symbol.getJSONObject(i).getString("quoteId");

        line.add(values);

        }
                for(int i=0;i<symbol.length();i++){
                    String value=symbol.getJSONObject(i).getString("date");
                    line.add(value);
                }
                for(int i=0;i<symbol.length();i++){
                    String price=symbol.getJSONObject(i).getString("close");
                    line.add(price);
                }
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(StockQuotesForTicker.this,android.R.layout.simple_list_item_1,line);
        StockQuote.setAdapter(adapter);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}


 /*
                        System.out.println(adapter);*/

