package postpc.y2020.stockfarm_app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import java.util.Calendar;


public class StockMarketBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Calendar rightNow = Calendar.getInstance();
        if (rightNow.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || rightNow.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
            //stock market closed
            return;
        }
        NotificationHelper notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification();
        notificationHelper.getManager().notify(1, nb.build());
    }
}
