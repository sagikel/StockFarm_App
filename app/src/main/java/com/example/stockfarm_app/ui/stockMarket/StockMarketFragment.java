package com.example.stockfarm_app.ui.stockMarket;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.stockfarm_app.ChartActivity;
import com.example.stockfarm_app.R;
import com.example.stockfarm_app.TradeActivity;
import com.example.stockfarm_app.VolleyApiKeyUrl;
import com.example.stockfarm_app.data.StocksFixedData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import de.hdodenhof.circleimageview.CircleImageView;

public class StockMarketFragment extends Fragment implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {

    TextView stockNameText;
    TextView ceoText;
    TextView sectorText;
    TextView industryText;
    TextView descriptionText;
    TextView RealTimeText;
    TextView RealTimeHeadlineText;
    Button tradeButton;
    Button moreInfoButton;
    Spinner spinner;
    ProgressBar progressBar;
    boolean first = false;
    boolean info = true;
    boolean realTimeInfo = false;
    String symbol;
    StocksFixedData stocksFixedData;
    CircleImageView circleImageView;
    VolleyApiKeyUrl volleyApiKeyUrl;
    RequestQueue queue;
    ListView listView;
    String[] options = {"Press Releases", "Stock News", "Analyst Stock Grades",
            "Analyst Stock Recommendations", "Company Enterprise Value", "Historical Price Data",
            "Company Annual Reports", "Company Quarter Reports", "Earning Call Transcript"};
    ScrollView scrollView;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stock_market, container, false);
        stockNameText = view.findViewById(R.id.stock_info_text2);
        tradeButton = view.findViewById(R.id.trade_button);
        moreInfoButton = view.findViewById(R.id.more_info_button);
        spinner = (Spinner) view.findViewById(R.id.stock_options);
        ceoText = view.findViewById(R.id.textView2);
        sectorText = view.findViewById(R.id.textView3);
        industryText = view.findViewById(R.id.textView4);
        descriptionText = view.findViewById(R.id.textView5);
        circleImageView = view.findViewById(R.id.icon);
        stocksFixedData = new StocksFixedData();
        queue = Volley.newRequestQueue(getContext());
        volleyApiKeyUrl = new VolleyApiKeyUrl();
        progressBar = view.findViewById(R.id.progress_bar);
        listView = view.findViewById(R.id.list_view);
        RealTimeText = view.findViewById(R.id.textView8);
        RealTimeHeadlineText = view.findViewById(R.id.textView7);
        scrollView = view.findViewById(R.id.scroll_view);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.Stock_Options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        descriptionText.setMovementMethod(new ScrollingMovementMethod());

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, options);
        listView.setAdapter(arrayAdapter);

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
                        .setIcon(R.drawable.internet_icon)
                        .show();
            }
        });

        moreInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sectorText.setText("Real Time Stock Data");
                industryText.setVisibility(View.INVISIBLE);
                ceoText.setVisibility(View.INVISIBLE);
                descriptionText.setVisibility(View.INVISIBLE);
                moreInfoButton.setVisibility(View.INVISIBLE);
                spinner.setVisibility(View.INVISIBLE);

                tradeButton.setText("back");
                listView.setVisibility(View.VISIBLE);
                info = false;
            }
        });

        tradeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (info)
                {
                    Intent intent = new Intent(getActivity(), TradeActivity.class);
                    intent.putExtra("symbol", symbol);
                    startActivity(intent);
                }
                else
                {
                    if (realTimeInfo) {
                        queue.cancelAll(symbol);
                        ceoText.setVisibility(View.INVISIBLE);
                        ceoText.setText("CEO: " + stocksFixedData.getMap().get(symbol).get(1));
                        industryText.setVisibility(View.INVISIBLE);
                        industryText.setText(stocksFixedData.getMap().get(symbol).get(3));
                        progressBar.setVisibility(View.INVISIBLE);
                        RealTimeHeadlineText.setVisibility(View.INVISIBLE);
                        RealTimeText.setVisibility(View.INVISIBLE);
                        scrollView.setVisibility(View.INVISIBLE);

                        sectorText.setVisibility(View.VISIBLE);
                        listView.setVisibility(View.VISIBLE);
                        stockNameText.setVisibility(View.VISIBLE);
                        circleImageView.setVisibility(View.VISIBLE);

                        realTimeInfo = false;
                    }
                    else {
                        tradeButton.setText("Trade");
                        moreInfoButton.setText("More info");
                        sectorText.setText(stocksFixedData.getMap().get(symbol).get(2));

                        listView.setVisibility(View.INVISIBLE);

                        ceoText.setVisibility(View.VISIBLE);
                        sectorText.setVisibility(View.VISIBLE);
                        industryText.setVisibility(View.VISIBLE);
                        descriptionText.setVisibility(View.VISIBLE);
                        tradeButton.setVisibility(View.VISIBLE);
                        moreInfoButton.setVisibility(View.VISIBLE);
                        spinner.setVisibility(View.VISIBLE);

                        info = true;
                    }
                }
            }
        });

        listView.setOnItemClickListener(this);

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
            tradeButton.setVisibility(View.INVISIBLE);
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
        tradeButton.setVisibility(View.VISIBLE);
        moreInfoButton.setVisibility(View.VISIBLE);
        circleImageView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        progressBar.setVisibility(View.VISIBLE);
        listView.setVisibility(View.INVISIBLE);
        stockNameText.setVisibility(View.INVISIBLE);
        circleImageView.setVisibility(View.INVISIBLE);
        sectorText.setVisibility(View.INVISIBLE);
        realTimeInfo = true;
        switch (position){
            case 0:
                getDataFromServer("press-releases", RealTimeText, false, false, false, false);
                RealTimeHeadlineText.setText("Press-Releases");
                RealTimeHeadlineText.setVisibility(View.VISIBLE);
                break;
            case 1:
                getDataFromServer("stock_news", RealTimeText, false, false, false, true);
                RealTimeHeadlineText.setText("Stock News");
                RealTimeHeadlineText.setVisibility(View.VISIBLE);
                break;
            case 2:
                RealTimeHeadlineText.setText("Analyst Stock Grade");
                getDataFromServer("grade", RealTimeText, false, false, false, false);
                RealTimeHeadlineText.setVisibility(View.VISIBLE);
                break;
            case 3:
                RealTimeHeadlineText.setText("Analyst Stock Recommendations");
                getDataFromServer("analyst-stock-recommendations", RealTimeText, false, false, false, false);
                RealTimeHeadlineText.setVisibility(View.VISIBLE);
                break;
            case 4:
                RealTimeHeadlineText.setText("Company Enterprise Value");
                getDataFromServer("enterprise-values", RealTimeText, false, false, false, false);
                RealTimeHeadlineText.setVisibility(View.VISIBLE);
                break;
            case 5:
                progressBar.setVisibility(View.INVISIBLE);
                listView.setVisibility(View.VISIBLE);
                stockNameText.setVisibility(View.VISIBLE);
                circleImageView.setVisibility(View.VISIBLE);
                sectorText.setVisibility(View.VISIBLE);
                realTimeInfo = false;
                Intent intent = new Intent(getActivity(), ChartActivity.class);
                intent.putExtra("symbol", symbol);
                startActivity(intent);
                break;
            case 6:
                RealTimeHeadlineText.setText("Company Annual Reports");
                getDataFromServer("income-statement", RealTimeText, false, false, false, false);
                RealTimeHeadlineText.setVisibility(View.VISIBLE);
                break;
            case 7:
                RealTimeHeadlineText.setText("Company Quarter Reports");
                getDataFromServer("income-statement", RealTimeText, false, true, false, false);
                RealTimeHeadlineText.setVisibility(View.VISIBLE);
                break;
            case 8:
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                final EditText input = new EditText(getContext());
                input.setText("");
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                input.setRawInputType(Configuration.KEYBOARD_12KEY);
                dialog.setTitle("Enter Earning Call Date")
                        .setView(input)
                        .setMessage("Enter Quarter (1-4) and Year, for Quarter number 4 and Year 2019 enter: 42019")
                        .setIcon(R.drawable.calendar_icon)
                        .setPositiveButton("Enter", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String s = input.getText().toString();
                                if (s.length() == 5 && (s.substring(0, 1).equals("1") ||
                                        s.substring(0, 1).equals("2") ||
                                        s.substring(0, 1).equals("3") ||
                                        s.substring(0, 1).equals("4"))) {
                                    progressBar.setVisibility(View.VISIBLE);
                                    getDataFromServer(s, RealTimeText, false, false, true, false);
                                    RealTimeHeadlineText.setVisibility(View.VISIBLE);
                                }
                                else {
                                    Toast.makeText(getContext(),"Wrong Input",Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.INVISIBLE);
                                    listView.setVisibility(View.VISIBLE);
                                    stockNameText.setVisibility(View.VISIBLE);
                                    circleImageView.setVisibility(View.VISIBLE);
                                    sectorText.setVisibility(View.VISIBLE);
                                    realTimeInfo = false;
                                }
                            }
                        })
                        .setNegativeButton("Back", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                progressBar.setVisibility(View.INVISIBLE);
                                listView.setVisibility(View.VISIBLE);
                                stockNameText.setVisibility(View.VISIBLE);
                                circleImageView.setVisibility(View.VISIBLE);
                                sectorText.setVisibility(View.VISIBLE);
                                realTimeInfo = false;
                            } })
                        .create()
                        .show();
                RealTimeHeadlineText.setText("Earning Call Transcript");
                progressBar.setVisibility(View.INVISIBLE);
                break;
        }
        Log.d("list - item clicked", String.valueOf(position));
    }

    private void getDataFromServer(final String type, final TextView textViewForData, boolean one, boolean quarter, boolean call, boolean news) {
        String url = "";
        if (one){
            url = volleyApiKeyUrl.getCorrectUrlForOne(symbol, type);
        }
        else if (quarter) {
            url = volleyApiKeyUrl.getCorrectUrlQ(symbol, type);
        }
        else if (call) {
            url = volleyApiKeyUrl.getCorrectUrlC(symbol, type);
        }
        else if (news) {
            url = volleyApiKeyUrl.getCorrectUrlN(symbol, type);
        }
        else {
            url = volleyApiKeyUrl.getCorrectUrlForMany(symbol, type);
        }
        final String finalUrl = url;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                switch (type)
                {
                    case "press-releases":
                        parseForPR(response, textViewForData);
                        break;
                    case "grade":
                        parseForG(response, textViewForData);
                        break;
                    case "analyst-stock-recommendations":
                        parseForR(response, textViewForData);
                        break;
                    case "enterprise-values":
                        parseForV(response, textViewForData);
                        break;
                    case "income-statement":
                        parseForA(response, textViewForData);
                        break;
                    case "stock_news":
                        parseForN(response, textViewForData);
                        break;
                    default:
                        parseForC(response, textViewForData);

                }
                Log.d("server","information was pass to the app from: " + finalUrl);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.e("server","error in respond");
                progressBar.setVisibility(View.INVISIBLE);
                ceoText.setText("Error response from our server.. Please try again!");
                ceoText.setVisibility(View.VISIBLE);
                industryText.setText(new String(Character.toChars(0x1F635)));
                industryText.setVisibility(View.VISIBLE);
            }
        });
        jsonArrayRequest.setTag(symbol);
        queue.add(jsonArrayRequest);
    }

    private void parseForN(JSONArray jsonArray, TextView textViewForData) {
        StringBuilder toShow = new StringBuilder();
        try {
            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String publishedDate = jsonObject.getString("publishedDate");
                String title = jsonObject.getString("title");
                String site = jsonObject.getString("site");
                String text = jsonObject.getString("text");
                String url = jsonObject.getString("url");

                toShow.append("<b>").append(publishedDate).append("</b><br>Site: ")
                        .append(site).append("<br><br>")
                        .append(title).append("<br><br>").append(text).append("<br>")
                        .append(url).append("<br><br><br>");
            }
            textViewForData.setText(Html.fromHtml(toShow.toString()));
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }
        progressBar.setVisibility(View.INVISIBLE);
        scrollView.setVisibility(View.VISIBLE);
        textViewForData.setVisibility(View.VISIBLE);
    }

    private void parseForC(JSONArray jsonArray, TextView textViewForData) {
        StringBuilder toShow = new StringBuilder();
        try {
            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String date = jsonObject.getString("date");
                String content = jsonObject.getString("content");

                toShow.append("<b>").append(date).append("</b><br>").append("<br>Call Transcript:<br><br>")
                        .append(content);
            }
            if (jsonArray.length() == 0) {
                toShow.append("<br><b>No data to Show for selected Date..");
            }
            textViewForData.setText(Html.fromHtml(toShow.toString()));
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }
        progressBar.setVisibility(View.INVISIBLE);
        scrollView.setVisibility(View.VISIBLE);
        textViewForData.setVisibility(View.VISIBLE);
    }

    private void parseForA(JSONArray jsonArray, TextView textViewForData) {
        StringBuilder toShow = new StringBuilder();
        try {
            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String date = jsonObject.getString("date");
                String finalLink = jsonObject.getString("finalLink");

                if (finalLink == "null"){
                    finalLink = "Sorry, not exist in our DataBase.";
                }

                toShow.append("<b>").append(date).append("</b><br>").append("<br>~U.S. Securities and Exchange Commission~")
                        .append("<br>K-10 Form (Annual/Quarter report): <br>").append(finalLink).append("<br><br><br>");
            }
            if (toShow.length() > 12) {
                toShow.setLength(toShow.length()-12);
            }
            textViewForData.setText(Html.fromHtml(toShow.toString()));
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }
        progressBar.setVisibility(View.INVISIBLE);
        scrollView.setVisibility(View.VISIBLE);
        textViewForData.setVisibility(View.VISIBLE);
    }

    private void parseForV(JSONArray jsonArray, TextView textViewForData) {
        StringBuilder toShow = new StringBuilder();
        try {
            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String date = jsonObject.getString("date");
                String stockPrice = jsonObject.getString("stockPrice");
                String numberOfShares = jsonObject.getString("numberOfShares");
                String marketCapitalization = jsonObject.getString("marketCapitalization");
                String minusCashAndCashEquivalents = jsonObject.getString("minusCashAndCashEquivalents");
                String addTotalDebt = jsonObject.getString("addTotalDebt");
                String enterpriseValue = jsonObject.getString("enterpriseValue");

                toShow.append("<b>").append(date).append("</b><br>").append("<br>Stock Price: ")
                        .append(stockPrice).append("<br>Number Of Shares: ").append(numberOfShares)
                        .append("<br>Market Capitalization: ").append(marketCapitalization).append("<br>Minus Cash And Cash Equivalents: ").append(minusCashAndCashEquivalents)
                        .append("<br>Add Total Debt: ").append(addTotalDebt).append("<br>Enterprise Value: ").append(enterpriseValue).append("<br><br><br>");
            }
            if (toShow.length() > 12) {
                toShow.setLength(toShow.length()-12);
            }
            textViewForData.setText(Html.fromHtml(toShow.toString()));
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }
        progressBar.setVisibility(View.INVISIBLE);
        scrollView.setVisibility(View.VISIBLE);
        textViewForData.setVisibility(View.VISIBLE);
    }

    private void parseForR(JSONArray jsonArray, TextView textViewForData) {
        StringBuilder toShow = new StringBuilder();
        try {
            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String date = jsonObject.getString("date");
                String analystRatingsbuy = jsonObject.getString("analystRatingsbuy");
                String analystRatingsHold = jsonObject.getString("analystRatingsHold");
                String analystRatingsSell = jsonObject.getString("analystRatingsSell");
                String analystRatingsStrongSell = jsonObject.getString("analystRatingsStrongSell");
                String analystRatingsStrongBuy = jsonObject.getString("analystRatingsStrongBuy");

                toShow.append("<b>").append(date).append("</b><br>").append("Amount of Analyst Ratings for:").append("<br>Strong Buy: ")
                        .append(analystRatingsStrongBuy).append("<br>Buy: ").append(analystRatingsbuy)
                        .append("<br>Hold: ").append(analystRatingsHold).append("<br>Sell: ").append(analystRatingsSell)
                        .append("<br>Strong Sell: ").append(analystRatingsStrongSell).append("<br><br><br>");
            }
            if (toShow.length() > 12) {
                toShow.setLength(toShow.length()-12);
            }
            textViewForData.setText(Html.fromHtml(toShow.toString()));
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }
        progressBar.setVisibility(View.INVISIBLE);
        scrollView.setVisibility(View.VISIBLE);
        textViewForData.setVisibility(View.VISIBLE);
    }

    private void parseForG(JSONArray jsonArray, TextView textViewForData) {
        StringBuilder toShow = new StringBuilder();
        try {
            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String date = jsonObject.getString("date");
                String gradingCompany = jsonObject.getString("gradingCompany");
                String previousGrade = jsonObject.getString("previousGrade");
                String newGrade = jsonObject.getString("newGrade");

                toShow.append("<b>").append(date).append("</b><br>").append("Grading Company: <font color='black'>")
                        .append(gradingCompany).append("</font><br>Previous Grade: ").append(previousGrade)
                        .append("<br>New Grade: ").append(newGrade)
                        .append("<br><br><br>");
            }
            if (toShow.length() > 12) {
                toShow.setLength(toShow.length()-12);
            }
            textViewForData.setText(Html.fromHtml(toShow.toString()));
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }
        progressBar.setVisibility(View.INVISIBLE);
        scrollView.setVisibility(View.VISIBLE);
        textViewForData.setVisibility(View.VISIBLE);
    }

    private void parseForPR(JSONArray jsonArray, TextView textViewForData) {
        StringBuilder toShow = new StringBuilder();
        try {
            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String date = jsonObject.getString("date");
                String title = jsonObject.getString("title");
                String text = jsonObject.getString("text");

                toShow.append("<b>").append(date).append("</b><br>").append(title)
                        .append("<br><br>").append("Original Releases:<br>").append(text)
                        .append("<br><br><br>");
            }
            if (toShow.length() > 12) {
                toShow.setLength(toShow.length()-12);
            }
            textViewForData.setText(Html.fromHtml(toShow.toString()));
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }
        progressBar.setVisibility(View.INVISIBLE);
        scrollView.setVisibility(View.VISIBLE);
        textViewForData.setVisibility(View.VISIBLE);
    }




    private void getPriceData(String type, final TextView textView) {
        String url = volleyApiKeyUrl.getCorrectUrlForOne(symbol, type);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    JSONObject jsonObject = response.getJSONObject(0);
                    double price = Double.parseDouble(jsonObject.getString("price"));
                    textView.setText(String.valueOf(price) + " USD");
                    Log.d("server",symbol + " price was pass to the app");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.e("server","error in respond");
            }
        });
        queue.add(jsonArrayRequest);
    }
}
