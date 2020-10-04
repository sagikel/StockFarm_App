package com.example.stockfarm_app.ui.myFarm;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.stockfarm_app.R;
import com.example.stockfarm_app.StockFarmApplication;
import com.example.stockfarm_app.data.UserStockData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class SignFragment extends Fragment
{
    FloatingActionButton floatingActionButton;
    ScrollView scrollView;
    TextView history;
    boolean open;
    StockFarmApplication app;
    DateFormat df;
    TextView playerName;
    TextView money;
    TextView assets;
    TextView portfolioReturn;
    TextView Level;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final ViewGroup view = (ViewGroup) inflater.inflate(
                R.layout.sign_layout, container, false);
        floatingActionButton = view.findViewById(R.id.floatingActionButton2);
        scrollView = view.findViewById(R.id.scroll_view_history);
        history = view.findViewById(R.id.history);
        playerName = view.findViewById(R.id.player_name);
        money = view.findViewById(R.id.coin);
        assets = view.findViewById(R.id.assets);
        portfolioReturn = view.findViewById(R.id.return2);
        Level = view.findViewById(R.id.level);

        app = (StockFarmApplication) getActivity().getApplication();
        playerName.setText(app.userData.getName());

        open = false;
        df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss ':' ");
        df.setTimeZone(TimeZone.getTimeZone("America/New_York"));

        getData();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                historyData();
                if (open) {
                    scrollView.setVisibility(View.INVISIBLE);
                    open = false;
                } else {
                    scrollView.setVisibility(View.VISIBLE);
                    open = true;
                }
            }
        });
        return view;
    }

    public void getData() {
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        double funds = app.userData.getFunds();
        money.setText(formatter.format(funds));
        double sum = 0.0;
        for (UserStockData userStockData : app.userData.getStocks().values())
        {
            sum += userStockData.getCurrAmount()*userStockData.getLastPrice();
        }
        sum += funds;
        assets.setText(formatter.format(sum));
        double change = (sum/app.userData.getFundsFix()-1)*100;
        portfolioReturn.setText(String.format("%.2f", change) + "%");
        if (change > 0) {
            portfolioReturn.setTextColor(getContext().getColor(R.color.colorPrimary));
        } else if (change < 0) {
            portfolioReturn.setTextColor(Color.RED);
        } else {
            portfolioReturn.setTextColor(Color.GRAY);
        }
        level(change);
    }

    private void level(double change) {
        String level = "Dynamic Level: ";
        String rank = "Beginner";
        if (change < 2) {
            rank = "Beginner";
        } else if (change < 5) {
            rank = "Trainee";
        } else if (change < 10) {
            rank = "Amateur";
        } else if (change < 15) {
            rank = "Hotshot";
        } else if (change < 20) {
            rank = "Virtuoso";
        } else if (change < 25) {
            rank = "Expert";
        } else if (change < 30) {
            rank = "Veteran";
        } else if (change < 35) {
            rank = "Semi-Pro";
        } else if (change < 40) {
            rank = "Professional";
        } else if (change < 45) {
            rank = "Master";
        } else if (change < 50) {
            rank = "Champ";
        } else if (change < 75) {
            rank = "Superstar";
        } else if (change < 100) {
            rank = "Hero";
        } else if (change < 150) {
            rank = "Legend";
        } else if (change < 200) {
            rank = "Immortal";
        } else if (change >= 200) {
            rank = "God";
        }
        Level.setText(level + rank);
    }

    @Override
    public void onPause() {
        super.onPause();
        scrollView.setVisibility(View.INVISIBLE);
        open = false;
    }

    private void historyData() {
        StringBuilder toShow = new StringBuilder();
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        String color = "";
        for (UserStockData userStockData : app.userData.getStocks().values()) {
            toShow.append("<big><b>").append(userStockData.getStockName()).append("</b><br></big>");
            for (UserStockData.UserStockEvent userStockEvent : userStockData.getStockTradeHistory()) {
                long amount = userStockEvent.getTradeAmount();
                if (amount > 0){
                    color = "<font color='#00802b'>Buy: </font>";
                } else {
                    color = "<font color='#990000'>Sell: </font>";
                }
                toShow.append("<small>").append(df.format(userStockEvent.getDate())).append("</small><br>")
                        .append(color).append(Math.abs(userStockEvent.getTradeAmount()))
                        .append(" stocks at ")
                        .append(formatter.format(userStockEvent.getTradeValue()))
                        .append(" worth ")
                        .append(formatter.format(Math.abs(userStockEvent.getTradeValue()*userStockEvent.getTradeAmount()))).append(".<br>");
            }
            toShow.append("<small> ~End history~ </small><br><br>");
        }
        history.setText(Html.fromHtml(toShow.toString()));
    }
}
