package com.example.stockfarm_app.data;


import java.util.LinkedList;

public class UserData
{
    private String name;
    private String email;
    private String password;
    private double funds;
    LinkedList<UserStockData> stocks;

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
    }

    public String getName() {
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

    public LinkedList<UserStockData> getStocks() {
        return stocks;
    }

    public LinkedList<UserStockData> getActiveStocks() // used for farm crops creation
    {
        LinkedList<UserStockData> activeStocks = new LinkedList<>();
        for (UserStockData stock : stocks)
        {
            if (stock.getCurrAmount() > 0)
            {
                activeStocks.add(stock);
            }
        }
        return activeStocks;
    }
}
