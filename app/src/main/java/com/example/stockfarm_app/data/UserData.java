package com.example.stockfarm_app.data;

import java.util.HashMap;
import java.util.LinkedList;

public class UserData
{
    private String name;
    private String email;
    private String password;
    private double funds;

    public HashMap<String, UserStockData> stocks;

    /**
     * this c'tor is for initializing users who authenticate via google and therefore no password.
     */
    public UserData(String name, String email, double initialFunds)
    {
        this(name, email, "", initialFunds);
    }



    public UserData(String name, String email, String password, double initialFunds)
    {
        this.name = name;
        this.email = email;
        this.funds = initialFunds;
        this.password = password;
        stocks = new HashMap<>();
        String[] list = {"AAPL","AMGN","AXP","BA","CAT","CRM","CSCO","CVX","DIS","DOW","GS",
                "HD","HON","IBM","INTC","JNJ","JPM","KO","MCD","MMM","MRK","MSFT","NKE","PG",
                "TRV","UNH","V","VZ","WBA","WMT"};
        for (String symbol : list) {
            stocks.put(symbol, new UserStockData(symbol, 0));
        }
    }

    public String getName()
    {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public double getFunds() {
        return funds;
    }

    public HashMap<String, UserStockData> getStocks() {
        return stocks;
    }

    public LinkedList<UserStockData> getActiveStocks() // used for farm crops creation
    {
        LinkedList<UserStockData> activeStocks = new LinkedList<>();
        String[] list = {"AAPL","AMGN","AXP","BA","CAT","CRM","CSCO","CVX","DIS","DOW","GS",
                "HD","HON","IBM","INTC","JNJ","JPM","KO","MCD","MMM","MRK","MSFT","NKE","PG",
                "TRV","UNH","V","VZ","WBA","WMT"};
        for (String stock : list)
        {
            if (stocks.get(stock).getCurrAmount() > 0)
            {
                activeStocks.add(stocks.get(stock));
            }
        }
        return activeStocks;
    }
    public void setFunds(double funds) {
    this.funds += funds;
    }
}
