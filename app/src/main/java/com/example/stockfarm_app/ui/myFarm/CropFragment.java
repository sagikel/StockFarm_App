package com.example.stockfarm_app.ui.myFarm;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.stockfarm_app.R;
import com.example.stockfarm_app.TradeActivity;
import com.example.stockfarm_app.data.UserStockData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.NumberFormat;
import java.util.Locale;

public class CropFragment extends Fragment {
    private UserStockData stock;
    TextView stockName;
    String color1;
    String color2;
    FloatingActionButton floatingActionButton;
    Double sum;

    public CropFragment(UserStockData stock)
    {
        super();
        this.stock = stock;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final ViewGroup view = (ViewGroup) inflater.inflate(
                R.layout.crop_layout, container, false);
        stockName = view.findViewById(R.id.stock_name_header);
        floatingActionButton = view.findViewById(R.id.floatingActionButton);
        color1 = "";
        color2 = "";
        sum = 0.0;
        setWindowText();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TradeActivity.class);
                intent.putExtra("symbol", stock.getStockName());
                startActivity(intent);
            }
        });

//        LayoutInflater inf = (LayoutInflater)   getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View treeView = inflater.inflate(R.layout.trees_layout1, null);
        ConstraintLayout.LayoutParams p = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT);
        treeView.setLayoutParams(p);
        view.addView(treeView);
        return view;
    }

    public void setWindowText() {
        StringBuilder toShow = new StringBuilder();
        NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.US);
        toShow.append("<big><b>").append(stock.getStockName())
                .append("</b><br></big><small>Amount: <b>").append(stock.getCurrAmount())
                .append("</b><br>Last Price: <b>").append(formatter.format(stock.getLastPrice()))
                .append("</b><br>Average Return: <b>");
        String string = gainCalculate();
        toShow.append(color1).append(string).append(color2).append("</b>");
        stockName.setText(Html.fromHtml(toShow.toString()));
    }

    private String gainCalculate() {
        double sum = 0.0;
        long amountNow = stock.getCurrAmount();
        long amount = 0;
        for (int i = stock.getStockTradeHistory().size()-1; i >=0 ; i--) {
            long amountTrade = stock.getStockTradeHistory().get(i).getTradeAmount();
            if (amountTrade > 0){
                long temp = amountNow - amount;
                if (amountTrade >= temp){
                    sum += temp*stock.getStockTradeHistory().get(i).getTradeValue();
                    break;
                } else {
                    sum += amountTrade*stock.getStockTradeHistory().get(i).getTradeValue();
                    amount += amountTrade;
                }
            }
        }
        sum = ((amountNow*stock.getLastPrice())/sum-1)*100;
        this.sum = sum;
        if (sum > 0){
            color1 = "<font color='#00802b'>";
            color2 = "</font>";
        } else if (sum < 0) {
            color1 = "<font color='#990000'>";
            color2 = "</font>";
        } else {
            color1 = "";
            color2 = "";
        }
        return (String.format("%.2f", sum) + "%");
    }
}