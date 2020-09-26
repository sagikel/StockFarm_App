package com.example.stockfarm_app.ui.myFarm;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyFarmViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MyFarmViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is 'My Farm' fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}