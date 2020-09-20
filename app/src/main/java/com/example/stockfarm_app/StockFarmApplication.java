package com.example.stockfarm_app;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class StockFarmApplication extends Application
{
    StockFarmApplication app;
    SharedPreferences sp;
    boolean signedIn;
    String username;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        sp = PreferenceManager.getDefaultSharedPreferences(app);
        username = sp.getString("userName", "");
        signedIn = (!username.equals(""));


    }

}
