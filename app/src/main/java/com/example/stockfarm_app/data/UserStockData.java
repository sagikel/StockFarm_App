package com.example.stockfarm_app.data;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserStockData
{
    private String stockName;
    private double currAmount;
    private ArrayList<UserStockEvent> stockTradeHistory;

    public UserStockData(String name, double amount)
    {
        this.stockName = name;
        currAmount = amount;
        stockTradeHistory = new ArrayList<UserStockEvent>();
    }

    // class getters:
    public String getStockName() {return stockName;}
    public double getCurrAmount() {return currAmount;}
    public ArrayList<UserStockEvent> getStockTradeHistory() {return stockTradeHistory;}

    public void addEvent(Date date, double amount, double value)
    {
        stockTradeHistory.add(new UserStockEvent(date, amount, value));
        currAmount += amount;
    }

    public class UserStockEvent
            /**
             * this class represents a buy/sell event of a certain stock by the user, as part of
             * its trade history.
             */
    {
        private Date date;
        private double tradeAmount; // negative value = sale
        private double tradeValue; // value of stock at time of trade

        public UserStockEvent(Date date, double amount, double value)
        {
           this.date = date;
           this.tradeAmount = amount;
           this.tradeValue = value;
        }

        // Data getters:
        public Date getDate() {return date;}
        public double getTradeAmount() {return tradeAmount;}
        public double getTradeValue() {return tradeValue;}
    }




}

