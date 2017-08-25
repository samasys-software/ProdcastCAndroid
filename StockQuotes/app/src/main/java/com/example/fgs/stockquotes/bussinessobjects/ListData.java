package com.example.fgs.stockquotes.bussinessobjects;

import java.util.Date;

/**
 * Created by fgs on 8/23/2017.
 */

public class ListData {
    String stockSymbol;



    public String getStockSymbol() {
        return stockSymbol;
    }

    public void setStockSymbol(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    String date;


    public String getClose() {
        return close;
    }

    public void setClose(String close) {
        this.close = close;
    }

    String close;




    public void setQuoteId(String quoteId) {
        this.quoteId = quoteId;
    }

    String quoteId;

    public String getQuoteId() {
        return quoteId;
    }
}
