package com.example.stockfarm_app.ui.myFarm;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final ViewGroup view = (ViewGroup) inflater.inflate(
                R.layout.sign_layout, container, false);
        floatingActionButton = view.findViewById(R.id.floatingActionButton2);
        scrollView = view.findViewById(R.id.scroll_view_history);
        history = view.findViewById(R.id.history);
        app = (StockFarmApplication) getActivity().getApplication();
        open = false;
        df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss ':' ");
        df.setTimeZone(TimeZone.getTimeZone("America/New_York"));

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

    @Override
    public void onPause() {
        super.onPause();
        scrollView.setVisibility(View.INVISIBLE);
        open = false;
    }

    private void historyData() {
        StringBuilder toShow = new StringBuilder();
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
                        .append(color).append(Math.abs(userStockEvent.getTradeAmount())).append(" stocks worth ")
                .append(Math.abs(userStockEvent.getTradeValue()*userStockEvent.getTradeAmount())).append(" USD.<br>");
            }
            toShow.append("<small> ~End history~ </small><br><br>");
        }
        history.setText(Html.fromHtml(toShow.toString()));
    }
}
