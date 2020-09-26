package com.example.stockfarm_app.ui.stockMarket;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class StockMarketViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public StockMarketViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is 'Stock Market' fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}