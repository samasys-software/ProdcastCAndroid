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

import com.example.fgs.stockquotes.bussinessobjects.AverageForStockQuotes;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class Main2Activity extends AppCompatActivity {
    private String ticker;
    private ListView averageStocks;
    ArrayList<AverageForStockQuotes> myList=new ArrayList<AverageForStockQuotes>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Intent data = getIntent();
        ticker = data.getStringExtra("STOCKSYMBOL");
        averageStocks = (ListView) findViewById(R.id.avergeStockQuote);
        AvgStockQuotes task=new AvgStockQuotes(ticker);

        task.execute();
    }

    public class AvgStockQuotes extends AsyncTask<Void, Void, JSONObject> {
        private final String stockSymbol;

        JSONObject jsonArray;

        AvgStockQuotes(String ticker) {
            stockSymbol = ticker;
        }


        protected JSONObject doInBackground(Void... params) {
            try {
                SyncHttpClient asyncHttpClient = new SyncHttpClient();
                RequestParams requestParams = new RequestParams();
                requestParams.put("stockSymbol", stockSymbol);

                String url = "http://192.168.1.2:8080/getAverageForStockSymbol";
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

               JSONArray symbol=stocks.getJSONArray("averages");

                // ArrayList<String> line=new ArrayList<>();
                for(int i=0;i<symbol.length();i++){
                    AverageForStockQuotes lv=new AverageForStockQuotes();


                    lv.setAvgId(symbol.getJSONObject(i).getString("avgId"));
                    lv.setAvgStockSymbol(symbol.getJSONObject(i).getString("stockSymbol"));
                    lv.setAverageValues(symbol.getJSONObject(i).getString("averagevalue"));
                    lv.setAvgdate(symbol.getJSONObject(i).getString("date"));
                    //add this object into the ArrayList myList
                    myList.add(lv);

                }

               AverageAdapter adapter=new AverageAdapter(getApplicationContext(),R.layout.average_stockquotes,myList);
               averageStocks.setAdapter(adapter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public class AverageAdapter extends ArrayAdapter {
        private List<AverageForStockQuotes> AvgStockQuotes;
        private int resource;
        private LayoutInflater inflator;

        public AverageAdapter(Context context, int resource, List<AverageForStockQuotes> objects) {
            super(context, resource, objects);
            AvgStockQuotes = objects;
            this.resource = resource;
            inflator = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflator.inflate(R.layout.average_stockquotes, null);

            }
            TextView avgId;
            TextView ticker;
            TextView date;
            TextView avgValues;
            avgId = (TextView) convertView.findViewById(R.id.avgId);
            ticker = (TextView) convertView.findViewById(R.id.ticker);
            date = (TextView) convertView.findViewById(R.id.date);
            avgValues = (TextView) convertView.findViewById(R.id.avgValues);
            avgId.setText(AvgStockQuotes.get(position).getAvgId());
            ticker.setText(AvgStockQuotes.get(position).getAvgStockSymbol());
            date.setText(AvgStockQuotes.get(position).getAvgdate());
            avgValues.setText(AvgStockQuotes.get(position).getAverageValues());
            return convertView;
        }
    }
}
