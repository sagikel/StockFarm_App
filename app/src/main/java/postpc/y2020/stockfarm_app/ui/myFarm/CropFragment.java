package postpc.y2020.stockfarm_app.ui.myFarm;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.stockfarm_app.R;
import postpc.y2020.stockfarm_app.TradeActivity;
import postpc.y2020.stockfarm_app.data.UserStockData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.NumberFormat;
import java.util.Locale;

public class CropFragment extends Fragment {
    ViewGroup view;
    private UserStockData stock;
    TextView stockName;
    String color1;
    String color2;
    FloatingActionButton floatingActionButton;
    Double sum;
    LayoutInflater inflater;
    View treeView;

    public CropFragment(UserStockData stock)
    {
        super();
        this.stock = stock;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.inflater = inflater;
        view = (ViewGroup) inflater.inflate(
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
        setTrees();
        return view;
    }

    public void updateTrees()
    {
        view.removeView(treeView);
        setTrees();
    }

    public void setTrees()
    {
        int treeConditionLayoutId;
        if (sum >= 8) treeConditionLayoutId = R.layout.trees_layout4;
        else if (sum >= 4 && sum < 8) treeConditionLayoutId = R.layout.trees_layout3;
        else if (sum >= 2 && sum < 4) treeConditionLayoutId = R.layout.trees_layout2;
        else if (sum > -3 && sum < 2) treeConditionLayoutId = R.layout.trees_layout1;
        else treeConditionLayoutId = R.layout.trees_layout_bad;
        treeView = inflater.inflate(treeConditionLayoutId, null);
        int treeNum = Math.toIntExact(stock.getCurrAmount());
        if (treeNum < 5)
        {
            treeView.findViewById(R.id.tree5).setVisibility(View.INVISIBLE);
            if (treeNum < 4) {
                treeView.findViewById(R.id.tree4).setVisibility(View.INVISIBLE);
                if (treeNum < 3) {
                    treeView.findViewById(R.id.tree3).setVisibility(View.INVISIBLE);
                    if (treeNum < 2) {
                        treeView.findViewById(R.id.tree2).setVisibility(View.INVISIBLE);
                    }
                }
            }
        }
        ConstraintLayout.LayoutParams p = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT);
        treeView.setLayoutParams(p);
        view.addView(treeView);
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

    public UserStockData getStock()
    {
        return stock;
    }
}