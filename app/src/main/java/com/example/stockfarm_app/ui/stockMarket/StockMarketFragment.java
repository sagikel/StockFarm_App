package com.example.stockfarm_app.ui.stockMarket;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.stockfarm_app.R;

public class StockMarketFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    TextView spinnerText;
    TextView stockInfoText;
    Button buyOrSellButton;
    Button moreInfoButton;
    Spinner spinner;
    boolean first = false;
    boolean info = true;
    boolean buyOrSell = true;
    String symbol;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stock_market, container, false);
        spinnerText = view.findViewById(R.id.text_stock_market);
        stockInfoText = view.findViewById(R.id.stock_info_text);
        buyOrSellButton = view.findViewById(R.id.buy_Sell_button);
        moreInfoButton = view.findViewById(R.id.more_info_button);
        spinner = (Spinner) view.findViewById(R.id.stock_options);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.Stock_Options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);


        moreInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (info)
                {
                    stockInfoText.setText(String.format("%s More Info", symbol));
                    moreInfoButton.setText("back to info");
                    info = false;
                }
                else
                {
                    stockInfoText.setText(String.format("%s Info", symbol));
                    moreInfoButton.setText("More info");
                    info = true;
                }

            }
        });

        buyOrSellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buyOrSell)
                {
                    stockInfoText.setText(String.format("%s buy or sell", symbol));
                    moreInfoButton.setText("back to info");
                    buyOrSellButton.setVisibility(View.INVISIBLE);
                    buyOrSell = false;
                }
                else
                {
                    stockInfoText.setText(String.format("%s Info", symbol));
                    moreInfoButton.setText("More info");
                    buyOrSellButton.setVisibility(View.VISIBLE);
                    buyOrSell = true;
                }

            }
        });

        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        symbol = parent.getItemAtPosition(position).toString();
        if (!first)
        {
            first = true;
            return;
        }
        Log.d("Spinner","A stock was selected");
        stockInfo();
    }



    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void stockInfo() {
        stockInfoText.setText(String.format("%s Info", symbol));
        stockInfoText.setVisibility(View.VISIBLE);
        buyOrSellButton.setVisibility(View.VISIBLE);
        moreInfoButton.setVisibility(View.VISIBLE);
    }

}
