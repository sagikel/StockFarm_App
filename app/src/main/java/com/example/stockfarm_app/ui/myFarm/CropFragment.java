package com.example.stockfarm_app.ui.myFarm;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.stockfarm_app.R;
import com.example.stockfarm_app.TradeActivity;
import com.example.stockfarm_app.data.UserStockData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CropFragment extends Fragment {
    private UserStockData stock;
    TextView stockName;
    String color1;
    String color2;
    FloatingActionButton floatingActionButton;

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
        setWindowText();floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TradeActivity.class);
                intent.putExtra("symbol", stock.getStockName());
                startActivity(intent);
            }
        });

        return view;
    }

    private void setWindowText() {
        StringBuilder toShow = new StringBuilder();
        toShow.append("<big><b>").append(stock.getStockName())
                .append("</b><br></big><small>Amount: <b>").append(stock.getCurrAmount())
                .append("</b><br>Last Price: <b>").append(String.format("%.2f", stock.getLastPrice()))
                .append("</b><br>Average Total Gain: <b>");
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
            long temp = amountNow - amount;
            if (amountTrade > 0){
                if (amountTrade >= temp){
                    sum += temp*stock.getStockTradeHistory().get(i).getTradeValue();
                    break;
                } else {
                    sum += amountTrade*stock.getStockTradeHistory().get(i).getTradeValue();
                }
            }
        }
        sum = ((amountNow*stock.getLastPrice())/sum-1)*100;
        if (sum > 0){
            color1 = "<font color='#00802b'>";
            color2 = "</font>";
        } else {
            color1 = "<font color='#990000'>";
            color2 = "</font>";
        }
        return (String.format("%.2f", sum) + "%");
    }

    @Override
    public void onResume() {
        super.onResume();
        setWindowText();
    }

    /**
     *
     *
     */

}