package com.example.stockfarm_app.ui.myFarm;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.stockfarm_app.R;
import com.example.stockfarm_app.data.UserStockData;

public class CropFragment extends Fragment {
    private UserStockData stock;

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
        TextView stockName = view.findViewById(R.id.stock_name_header);
        stockName.setText(stock.getStockName());
        return view;
    }
}