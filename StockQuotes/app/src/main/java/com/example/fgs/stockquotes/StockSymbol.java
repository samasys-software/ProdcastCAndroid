package com.example.fgs.stockquotes;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;



public class StockSymbol extends AppCompatActivity {
    private ListView ticker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_symbol);
        ticker=(ListView) findViewById(R.id.stockSymbol);

        StockQuoteValuesTask task=new StockQuoteValuesTask();
        task.execute() ;


    }

    public class StockQuoteValuesTask extends AsyncTask<Void,Void,JSONObject> {
        JSONObject jsonObject;


        protected JSONObject doInBackground(Void... voids) {
            try {
                SyncHttpClient asyncHttpClient = new SyncHttpClient();
                RequestParams requestParams = new RequestParams();

                String url = "http://192.168.1.9:8080/getStockSymbols";

                asyncHttpClient.get(url,requestParams,new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try{
                            System.out.println(response);

                            jsonObject=response;


                        }
                        catch(Exception er)
                        {
                            er.printStackTrace();
                        }

                    }
                    public void onFailure(int statusCode,Header[] headers,String responseString,Throwable e){
                        super.onFailure(statusCode, headers, responseString,e);
                        e.printStackTrace();
                    }

                });


            } catch (Exception e) {
                e.printStackTrace();

            }
            return jsonObject;

        }

        protected void onPostExecute(JSONObject result ){


            System.out.println(result);
            try {
                JSONArray symbol=result.getJSONArray("stockSymbols");

                final ArrayList<String> list=new ArrayList<>();

                for(int i=0;i<symbol.length();i++){
                 String val= symbol.getJSONObject(i).getString("stockSymbol");
                    System.out.println(val);
                    list.add(val);
                    System.out.println(symbol);
                  }
               ArrayAdapter adapter=new ArrayAdapter(StockSymbol.this,android.R.layout.simple_list_item_1,list);

                ticker.setAdapter(adapter);
                System.out.println(adapter.toString());
                ticker.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapter, View view, int i, long l) {
                         Object obj=ticker.getAdapter().getItem(i);
                        String value=obj.toString();
                        System.out.println(value);
                        Intent intent = new Intent(StockSymbol.this, MainActivity.class);
                        intent.putExtra("STOCKSYMBOL",value);
                        startActivity(intent);
                    }


                });

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
