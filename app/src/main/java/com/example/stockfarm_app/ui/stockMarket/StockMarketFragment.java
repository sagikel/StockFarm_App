package com.example.stockfarm_app.ui.stockMarket;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
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
import com.example.stockfarm_app.data.StocksFixedData;

import de.hdodenhof.circleimageview.CircleImageView;

public class StockMarketFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    TextView stockNameText;
    TextView ceoText;
    TextView sectorText;
    TextView industryText;
    TextView descriptionText;
    Button buyOrSellButton;
    Button moreInfoButton;
    Spinner spinner;
    boolean first = false;
    boolean info = true;
    boolean buyOrSell = true;
    String symbol;
    StocksFixedData stocksFixedData;
    CircleImageView circleImageView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stock_market, container, false);
        stockNameText = view.findViewById(R.id.stock_info_text);
        buyOrSellButton = view.findViewById(R.id.buy_Sell_button);
        moreInfoButton = view.findViewById(R.id.more_info_button);
        spinner = (Spinner) view.findViewById(R.id.stock_options);
        ceoText = view.findViewById(R.id.textView2);
        sectorText = view.findViewById(R.id.textView3);
        industryText = view.findViewById(R.id.textView4);
        descriptionText = view.findViewById(R.id.textView5);
        circleImageView = view.findViewById(R.id.icon);
        stocksFixedData = new StocksFixedData();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.Stock_Options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        descriptionText.setMovementMethod(new ScrollingMovementMethod());

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(getActivity())
                        .setTitle("URL")
                        .setMessage("Continue to " + stocksFixedData.getMap().get(symbol).get(0) + " website?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(stocksFixedData.getMap().get(symbol).get(4)));
                                startActivity(browserIntent);
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(R.drawable.internet)
                        .show();
            }
        });

        moreInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (info)
                {
                    stockNameText.setText(String.format("%s More Info", symbol));
                    moreInfoButton.setText("back to info");
                    info = false;
                }
                else
                {
                    stockNameText.setText(String.format("%s Info", symbol));
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
                    stockNameText.setText(String.format("%s buy or sell", symbol));
                    moreInfoButton.setText("back to info");
                    buyOrSellButton.setVisibility(View.INVISIBLE);
                    buyOrSell = false;
                }
                else
                {
                    stockNameText.setText(String.format("%s Info", symbol));
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

    @SuppressLint("ResourceType")
    private void stockInfo() {
        if (symbol.equals("Choose Stock Symbol"))
        {
            stockNameText.setVisibility(View.INVISIBLE);
            ceoText.setVisibility(View.INVISIBLE);
            sectorText.setVisibility(View.INVISIBLE);
            industryText.setVisibility(View.INVISIBLE);
            descriptionText.setVisibility(View.INVISIBLE);
            buyOrSellButton.setVisibility(View.INVISIBLE);
            moreInfoButton.setVisibility(View.INVISIBLE);
            circleImageView.setVisibility(View.INVISIBLE);
            return;
        }
        stockNameText.setText(stocksFixedData.getMap().get(symbol).get(0));
        ceoText.setText("CEO: " + stocksFixedData.getMap().get(symbol).get(1));
        sectorText.setText(stocksFixedData.getMap().get(symbol).get(2));
        industryText.setText(stocksFixedData.getMap().get(symbol).get(3));
        descriptionText.setText(stocksFixedData.getMap().get(symbol).get(6));
        String mDrawableName = symbol.toLowerCase();
        int resID = getResources().getIdentifier(mDrawableName , "drawable", getContext().getPackageName());
        circleImageView.setImageResource(resID);

        stockNameText.setVisibility(View.VISIBLE);
        ceoText.setVisibility(View.VISIBLE);
        sectorText.setVisibility(View.VISIBLE);
        industryText.setVisibility(View.VISIBLE);
        descriptionText.setVisibility(View.VISIBLE);
        buyOrSellButton.setVisibility(View.VISIBLE);
        moreInfoButton.setVisibility(View.VISIBLE);
        circleImageView.setVisibility(View.VISIBLE);
    }

}
