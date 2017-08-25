package com.example.fgs.stockquotes.bussinessobjects;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by fgs on 8/10/2017.
 */

public class ResultInformation {
    public JSONObject getStockSymbols() {
        return StockSymbols;
    }

    public void setStockSymbols(JSONObject stockSymbols) {
        StockSymbols = stockSymbols;
    }

    private JSONObject StockSymbols;
    private List<JSONObject> stockSymbol;
    private static final ResultInformation Instance=new ResultInformation();
    public static ResultInformation getInstance(){
        return Instance;
    }

}
