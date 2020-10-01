package com.example.stockfarm_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


public class TradeActivity extends AppCompatActivity {

    String symbol;
    TextView stockName;
    TextView companyName;
    TextView price;
    TextView usd;
    TextView percent;
    TextView change;
    TextView time;
    TextView open;
    TextView openD;
    TextView high;
    TextView highD;
    TextView low;
    TextView lowD;
    TextView marketCap;
    TextView marketCapD;
    TextView volume;
    TextView volumeD;
    TextView pClose;
    TextView pCloseD;
    TextView yearH;
    TextView yearHD;
    TextView yearL;
    TextView yearLD;
    TextView exchange;
    TextView exchangeD;
    ProgressBar progressBar;
    Button charts;
    Button buyOrSell;
    RequestQueue queue;
    VolleyApiKeyUrl volleyApiKeyUrl;
    DateFormat df;
    SwipeRefreshLayout swipeRefreshLayout;
    boolean swipe;
    Context context;
    ImageView imageView;
    boolean trade;
    TextView tradeText;
    boolean action;
    RadioGroup radioGroup;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade);
        getSupportActionBar().hide();

        stockName = findViewById(R.id.stock_info_text2);
        companyName = findViewById(R.id.company_name);
        price = findViewById(R.id.price);
        usd = findViewById(R.id.usd);
        percent = findViewById(R.id.percent);
        change = findViewById(R.id.change);
        time = findViewById(R.id.time);
        open = findViewById(R.id.o1);
        openD = findViewById(R.id.o2);
        high = findViewById(R.id.h1);
        highD = findViewById(R.id.h2);
        low = findViewById(R.id.l1);
        lowD = findViewById(R.id.l2);
        marketCap = findViewById(R.id.mc1);
        marketCapD = findViewById(R.id.mc2);
        volume = findViewById(R.id.v1);
        volumeD = findViewById(R.id.v2);
        pClose = findViewById(R.id.pc1);
        pCloseD = findViewById(R.id.pc2);
        yearH = findViewById(R.id.yh1);
        yearHD = findViewById(R.id.yh2);
        yearL = findViewById(R.id.yl1);
        yearLD = findViewById(R.id.yl2);
        exchange = findViewById(R.id.ex1);
        exchangeD = findViewById(R.id.ex2);
        progressBar = findViewById(R.id.progress_bar_trade);
        charts = findViewById(R.id.button_trade1);
        buyOrSell = findViewById(R.id.button_trade2);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        imageView = findViewById(R.id.imageViewArrow);
        tradeText = findViewById(R.id.open_close);
        radioGroup = findViewById(R.id.radio);
        editText = findViewById(R.id.editTextNumber);
        trade = false;
        action = false;
        queue = Volley.newRequestQueue(this);
        volleyApiKeyUrl = new VolleyApiKeyUrl();
        swipe = false;
        context = this;

        Intent intent = getIntent();
        symbol = intent.getExtras().getString("symbol");
        stockName.setText(symbol);
        df = new SimpleDateFormat("'New York TimeZone:' MM/dd/yyyy HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("America/New_York"));
        getDataFromServer();
        getDataFromServer2();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                swipe = true;
                getDataFromServer();
                if (!action) {
                    getDataFromServer2();
                }
            }
        });

        charts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (action) {
                    action = false;
                    charts.setText("Charts");
                    visible();
                    time.setVisibility(View.VISIBLE);
                    radioGroup.setVisibility(View.INVISIBLE);
                    editText.setVisibility(View.INVISIBLE);
                }
                else {
                    Intent intent = new Intent(context, ChartActivity.class);
                    intent.putExtra("symbol", symbol);
                    startActivity(intent);
                }
            }
        });

        buyOrSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (action) {
                    calculate();
                } else {
                    if (!trade) { // להוציא את הסימן שאלה ברגע שעוברים לזמן אמת
                        action = true;
                        charts.setText("Back");
                        invisible();
                        time.setVisibility(View.INVISIBLE);
                        imageView.setVisibility(View.VISIBLE);
                        charts.setVisibility(View.VISIBLE);
                        radioGroup.setVisibility(View.VISIBLE);
                        editText.setVisibility(View.VISIBLE);
                    }
                    else {
                        new AlertDialog.Builder(context)
                                .setTitle("Stock Market Hours")
                                .setMessage("New York trading hours are Monday to Friday:\n\n" +
                                        "Opening 09:30 a.m.\n\n" +
                                        "Closing 04:00 p.m.")
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                                .setIcon(R.drawable.trade)
                                .show();
                    }
                }

            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() == 0)
                {
                    buyOrSell.setVisibility(View.INVISIBLE);
                }else {
                    buyOrSell.setText("Make trade");
                    buyOrSell.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        queue.cancelAll(symbol);
    }

    private void getDataFromServer() {
        String url = volleyApiKeyUrl.getCorrectUrlForOne(symbol, "quote");
        final String finalUrl = url;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    JSONObject jsonObject = response.getJSONObject(0);

                    time.setText(df.format(new Date(Long.parseLong(jsonObject.getString("timestamp")) * 1000)));
                    companyName.setText(jsonObject.getString("name"));
                    price.setText(String.valueOf(Double.parseDouble(jsonObject.getString("price"))));
                    percent.setText(Double.parseDouble(jsonObject.getString("changesPercentage")) + "%");
                    double changeValue = Double.parseDouble(jsonObject.getString("change"));
                    change.setText(String.valueOf(changeValue));
                    openD.setText(String.valueOf(Double.parseDouble(jsonObject.getString("open"))));
                    highD.setText(String.valueOf(Double.parseDouble(jsonObject.getString("dayHigh"))));
                    lowD.setText(String.valueOf(Double.parseDouble(jsonObject.getString("dayLow"))));
                    marketCapD.setText(String.valueOf(Double.parseDouble(jsonObject.getString("marketCap"))));
                    volumeD.setText(String.valueOf(Long.parseLong(jsonObject.getString("volume"))));
                    pCloseD.setText(String.valueOf(Double.parseDouble(jsonObject.getString("previousClose"))));
                    yearHD.setText(String.valueOf(Double.parseDouble(jsonObject.getString("yearHigh"))));
                    yearLD.setText(String.valueOf(Double.parseDouble(jsonObject.getString("yearLow"))));
                    exchangeD.setText(jsonObject.getString("exchange"));

                    if ( changeValue > 0){
                        change.setTextColor(getColor(R.color.colorPrimary));
                        percent.setTextColor(getColor(R.color.colorPrimary));
                        imageView.setBackgroundResource(R.drawable.up);
                    } else if (changeValue < 0) {
                        change.setTextColor(Color.RED);
                        percent.setTextColor(Color.RED);
                        imageView.setBackgroundResource(R.drawable.down);
                    } else {
                        change.setTextColor(Color.GRAY);
                        percent.setTextColor(Color.GRAY);
                        imageView.setBackgroundResource(R.drawable.arrow);
                    }
                    if (swipe){
                        swipeRefreshLayout.setRefreshing(false);
                        swipe = false;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    return;
                }
                if (!action) {
                    visible();
                }
                Log.d("server","information was pass to the app from: " + finalUrl);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.e("server","error in respond");
                progressBar.setVisibility(View.INVISIBLE);
                change.setText("Something went wrong..");
                change.setVisibility(View.VISIBLE);
                percent.setText(new String(Character.toChars(0x1F635)));
                percent.setVisibility(View.VISIBLE);
                time.setText("Please try again!");

                if (swipe){
                    swipeRefreshLayout.setRefreshing(false);
                    swipe = false;
                }
                invisible();
            }
        });
        jsonArrayRequest.setTag(symbol);
        queue.add(jsonArrayRequest);
    }

    private void getDataFromServer2() {
        String url = volleyApiKeyUrl.getCorrectUrlH("market-hours");
        final String finalUrl = url;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    JSONObject jsonObject = response.getJSONObject(0);
                    trade = jsonObject.getBoolean("isTheStockMarketOpen");
                    if (trade) {
                        tradeText.setText("Open");
                        tradeText.setTextColor(getColor(R.color.colorPrimary));

                    } else {
                        tradeText.setText("Close");
                        tradeText.setTextColor(Color.RED);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    return;
                }
                Log.d("server","information was pass to the app from: " + finalUrl);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.e("server","error in respond");
            }
        });
        jsonArrayRequest.setTag(symbol);
        queue.add(jsonArrayRequest);
    }

    private void invisible() {
        companyName.setVisibility(View.INVISIBLE);
        open.setVisibility(View.INVISIBLE);
        openD.setVisibility(View.INVISIBLE);
        high.setVisibility(View.INVISIBLE);
        highD.setVisibility(View.INVISIBLE);
        low.setVisibility(View.INVISIBLE);
        lowD.setVisibility(View.INVISIBLE);
        marketCap.setVisibility(View.INVISIBLE);
        marketCapD.setVisibility(View.INVISIBLE);
        volume.setVisibility(View.INVISIBLE);
        volumeD.setVisibility(View.INVISIBLE);
        pClose.setVisibility(View.INVISIBLE);
        pCloseD.setVisibility(View.INVISIBLE);
        yearH.setVisibility(View.INVISIBLE);
        yearHD.setVisibility(View.INVISIBLE);
        yearL.setVisibility(View.INVISIBLE);
        yearLD.setVisibility(View.INVISIBLE);
        exchange.setVisibility(View.INVISIBLE);
        exchangeD.setVisibility(View.INVISIBLE);
        buyOrSell.setVisibility(View.INVISIBLE);
        imageView.setVisibility(View.INVISIBLE);
        tradeText.setVisibility(View.INVISIBLE);

        charts.setVisibility(View.INVISIBLE);

        progressBar.setVisibility(View.INVISIBLE);
    }

    private void visible() {
        companyName.setVisibility(View.VISIBLE);
        percent.setVisibility(View.VISIBLE);
        change.setVisibility(View.VISIBLE);
        open.setVisibility(View.VISIBLE);
        openD.setVisibility(View.VISIBLE);
        high.setVisibility(View.VISIBLE);
        highD.setVisibility(View.VISIBLE);
        low.setVisibility(View.VISIBLE);
        lowD.setVisibility(View.VISIBLE);
        marketCap.setVisibility(View.VISIBLE);
        marketCapD.setVisibility(View.VISIBLE);
        volume.setVisibility(View.VISIBLE);
        volumeD.setVisibility(View.VISIBLE);
        pClose.setVisibility(View.VISIBLE);
        pCloseD.setVisibility(View.VISIBLE);
        yearH.setVisibility(View.VISIBLE);
        yearHD.setVisibility(View.VISIBLE);
        yearL.setVisibility(View.VISIBLE);
        yearLD.setVisibility(View.VISIBLE);
        exchange.setVisibility(View.VISIBLE);
        exchangeD.setVisibility(View.VISIBLE);
        buyOrSell.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.VISIBLE);
        tradeText.setVisibility(View.VISIBLE);

        charts.setVisibility(View.VISIBLE);

        progressBar.setVisibility(View.INVISIBLE);
    }

    private void calculate(){
        Toast.makeText(context, "Not ready yet..", Toast.LENGTH_LONG).show();
    }
}