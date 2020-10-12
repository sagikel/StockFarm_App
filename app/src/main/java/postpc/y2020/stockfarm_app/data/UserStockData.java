package postpc.y2020.stockfarm_app.data;


import java.util.ArrayList;
import java.util.Date;

public class UserStockData
{
    private String stockName;
    private long currAmount;
    private ArrayList<UserStockEvent> stockTradeHistory;
    private double lastPrice;

    public UserStockData(String name, long amount)
    {
        this.stockName = name;
        currAmount = amount;
        stockTradeHistory = new ArrayList<UserStockEvent>();
        lastPrice = 0.0;
    }

    // class getters:
    public String getStockName() {return stockName;}
    public long getCurrAmount() {return currAmount;}
    public ArrayList<UserStockEvent> getStockTradeHistory() {return stockTradeHistory;}
    public void addEvent(Date date, int amount, double value)
    {
        stockTradeHistory.add(new UserStockEvent(date, amount, value));
        currAmount += amount;
    }

    public void setLastPrice(double lastPrice) {
        this.lastPrice = lastPrice;
    }

    public double getLastPrice() {
        return lastPrice;
    }

    public static class UserStockEvent
            /**
             * this class represents a buy/sell event of a certain stock by the user, as part of
             * its trade history.
             */
    {
        private Date date;
        private long tradeAmount; // negative value = sale
        private double tradeValue; // value of stock at time of trade

        public UserStockEvent(Date date, long amount, double value)
        {
           this.date = date;
           this.tradeAmount = amount;
           this.tradeValue = value;
        }

        // Data getters:
        public Date getDate() {return date;}
        public long getTradeAmount() {return tradeAmount;}
        public double getTradeValue() {return tradeValue;}

    }




}

