package postpc.y2020.stockfarm_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.HighLowDataEntry;
import com.anychart.charts.Stock;
import com.anychart.core.stock.Plot;
import com.anychart.data.Table;
import com.anychart.data.TableMapping;
import postpc.y2020.stockfarm_app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChartActivity extends AppCompatActivity {

    AnyChartView anyChartView;
    TextView textView1;
    TextView textView2;
    Button button1min;
    Button button5min;
    Button button15min;
    Button button30min;
    Button button1hour;
    Button button1day;
    RequestQueue queue;
    String symbol;
    VolleyApiKeyUrl volleyApiKeyUrl;
    List<DataEntry> data;
    ProgressBar progressBar;
    ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_chart);
        anyChartView = findViewById(R.id.any_chart_view);
        progressBar = findViewById(R.id.progress_bar);
        textView1 = findViewById(R.id.textViewChart1);
        textView2 = findViewById(R.id.textViewChart2);
        button1min = findViewById(R.id.buttonChart1);
        button5min = findViewById(R.id.buttonChart2);
        button15min = findViewById(R.id.buttonChart3);
        button30min = findViewById(R.id.buttonChart4);
        button1hour = findViewById(R.id.buttonChart5);
        button1day = findViewById(R.id.buttonChart6);
        queue = Volley.newRequestQueue(this);
        volleyApiKeyUrl = new VolleyApiKeyUrl();
        data = new ArrayList<>();
        scrollView = findViewById(R.id.scroll_view_char);

        Intent intent = getIntent();
        symbol = intent.getExtras().getString("symbol");

        button1min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visible();
                getDataFromServer(1);
            }
        });
        button5min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visible();
                getDataFromServer(2);
            }
        });
        button15min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visible();
                getDataFromServer(3);
            }
        });
        button30min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visible();
                getDataFromServer(4);
            }
        });
        button1hour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visible();
                getDataFromServer(5);
            }
        });
        button1day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visible();
                getDataFromServer(6);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        queue.cancelAll(symbol);
    }

    private void getDataFromServer(int choice) {
        String url = "";
        switch (choice){
            case 1:
                url = volleyApiKeyUrl.getCorrectUrlForOne(symbol, "historical-chart/1min");
                break;
            case 2:
                url = volleyApiKeyUrl.getCorrectUrlForOne(symbol, "historical-chart/5min");
                break;
            case 3:
                url = volleyApiKeyUrl.getCorrectUrlForOne(symbol, "historical-chart/15min");
                break;
            case 4:
                url = volleyApiKeyUrl.getCorrectUrlForOne(symbol, "historical-chart/30min");
                break;
            case 5:
                url = volleyApiKeyUrl.getCorrectUrlForOne(symbol, "historical-chart/1hour");
                break;
            case 6:
                getDataFromServerObjectJson();
                return;
        }
        final String finalUrl = url;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    data.clear();
                    for (int i = 0; i < response.length(); i++){
                        JSONObject jsonObject = response.getJSONObject(i);
                        String date = jsonObject.getString("date");
                        String open = jsonObject.getString("open");
                        String low = jsonObject.getString("low");
                        String high = jsonObject.getString("high");
                        String close = jsonObject.getString("close");
                        data.add(new OHCLDataEntry(date, Double.valueOf(open),
                                Double.valueOf(high), Double.valueOf(low), Double.valueOf(close)));
                    }
                    chartCreate();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                textView1.setText(new String(Character.toChars(0x1F635)));
                textView2.setText("Error response from our server.. Please try again!");
                scrollView.setVisibility(View.VISIBLE);
                textView1.setVisibility(View.VISIBLE);
                textView2.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                error.printStackTrace();
                Log.e("server","error in respond");
            }
        });
        jsonArrayRequest.setTag(symbol);
        queue.add(jsonArrayRequest);
    }

    private void getDataFromServerObjectJson() {
        String url = volleyApiKeyUrl.getCorrectUrlForOne(symbol, "historical-price-full");
        final String finalUrl = url;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    data.clear();
                    JSONArray jsonArray = response.getJSONArray("historical");
                    for (int i = 0; i < jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String date = jsonObject.getString("date");
                        String open = jsonObject.getString("open");
                        String low = jsonObject.getString("low");
                        String high = jsonObject.getString("high");
                        String close = jsonObject.getString("close");
                        data.add(new OHCLDataEntry(date, Double.valueOf(open),
                                Double.valueOf(high), Double.valueOf(low), Double.valueOf(close)));
                    }
                    chartCreate();
                } catch (JSONException e) {
                    e.printStackTrace();
                    textView1.setText(new String(Character.toChars(0x1F635)));
                    textView2.setText("Error response from our server.. Please try again!");
                    scrollView.setVisibility(View.VISIBLE);
                    textView1.setVisibility(View.VISIBLE);
                    textView2.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                textView1.setText(new String(Character.toChars(0x1F635)));
                textView2.setText("Error response from our server.. Please try again!");
                scrollView.setVisibility(View.VISIBLE);
                textView1.setVisibility(View.VISIBLE);
                textView2.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                error.printStackTrace();
                Log.e("server","error in respond");
            }
        });
        jsonObjectRequest.setTag(symbol);
        queue.add(jsonObjectRequest);
    }

    private void chartCreate() {
        Table table = Table.instantiate("x");
        table.addData(data);
        TableMapping mapping = table.mapAs("{open: 'open', high: 'high', low: 'low', close: 'close'}");
        Stock stock = AnyChart.stock();
        Plot plot = stock.plot(0);
        plot.yGrid(true)
                .xGrid(true)
                .yMinorGrid(true)
                .xMinorGrid(true);
        //plot.ema(table.mapAs("{value: 'close'}"), 1, StockSeriesType.LINE);
        plot.ohlc(mapping).fallingStroke("red").risingStroke("green").allowPointSettings(true)
                .name(symbol)
                .legendItem("{\n" +
                        "        iconType: 'rising-falling'\n" +
                        "      }");

        stock.scroller().ohlc(mapping);
        anyChartView.setChart(stock);
        anyChartView.setVisibility(View.VISIBLE);
    }

    private class OHCLDataEntry extends HighLowDataEntry {
        OHCLDataEntry(String x, Double open, Double high, Double low, Double close) {
            super(x, high, low);
            setValue("open", open);
            setValue("close", close);
        }
    }

    private void visible(){
        anyChartView.setProgressBar(findViewById(R.id.progress_bar));
        scrollView.setVisibility(View.INVISIBLE);
        textView1.setVisibility(View.INVISIBLE);
        textView2.setVisibility(View.INVISIBLE);
        button1min.setVisibility(View.INVISIBLE);
        button5min.setVisibility(View.INVISIBLE);
        button15min.setVisibility(View.INVISIBLE);
        button30min.setVisibility(View.INVISIBLE);
        button1hour.setVisibility(View.INVISIBLE);
        button1day.setVisibility(View.INVISIBLE);
    }
}
