package postpc.y2020.stockfarm_app;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;

import com.example.stockfarm_app.R;

import postpc.y2020.stockfarm_app.data.UserData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.*;
import com.google.gson.Gson;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;


public class StockFarmApplication extends Application
{
    StockFarmApplication app;
    public SharedPreferences sp;
    public FirebaseFirestore db;
    public UserData userData;
    public String currId;
    boolean notification;
    public String autoTransition;

    // updating values from the sp/server:
    double initialFunds;

    // default values for offline use:
    static final double DEFAULT_INITIAL_FUNDS = 10000;


    @Override
    public void onCreate()
    {
        super.onCreate();
        app = this;
        db = FirebaseFirestore.getInstance();
        sp = PreferenceManager.getDefaultSharedPreferences(app);
        updateValues();
    }


    public void getUserById(final String Id, Activity activity)
    {
        DocumentReference userDocRef = db.collection("users").document(Id);
        userDocRef.get().addOnCompleteListener((OnCompleteListener<DocumentSnapshot>) activity);
    }

    public String setUserDataFromServer(String id, String json)
    {
        currId = id;
        app.userData = new Gson().fromJson(json, UserData.class);
        app.sp.edit().putString(getString(R.string.firestore_fieldname_userdata), json)
                .apply();
        return userData.getName();
    }

    public void generateAccountRegularUser(String id, final String regEmail, final String regName, final String regPassword, final LoginActivity activity)
    {
        UserData newUser = new UserData(regName,
                regEmail,
                regPassword,
                initialFunds);
        userData = newUser;
        currId = id;
        final String json = new Gson().toJson(newUser);
        sp.edit().putString(getString(R.string.firestore_fieldname_userdata), json).apply();
        Map<String, String> data = new HashMap<String, String>()
        {{  put("email", regEmail);
            put("name", regName);
            put("password", regPassword);
            put(getString(R.string.firestore_fieldname_userdata), json);}};
        db.collection("users").document(id).set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {activity.registerResult(task.isSuccessful());}
        });
    }

    public void saveUserForAutoLogIn(String id)
    {
        sp.edit().putString(getString(R.string.last_user_id), id).apply();
    }

    public void generateAccountGoogleUser(final FirebaseUser user, LoginActivity activity)
    {
        generateAccountRegularUser(user.getUid(), user.getEmail(),
                user.getDisplayName(), "", activity);
    }

    /**
     * handles updating default values that may change over time (such as: iniitial funds amount)
     * from the server to the application and sp.
     */
    private void updateValues()
    {
        DocumentReference valuesDocRef = db.collection("values")
                .document(getString(R.string.firestore_docID_values));
        valuesDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task)
            {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists())
                    {
                        // here set received values
                        Double funds = document.getDouble(getString(R.string.firestore_fieldname_initialfunds));
                        if (funds != null)
                        {
                            initialFunds = funds;
                            sp.edit().putLong(getString(R.string.firestore_fieldname_initialfunds),
                                    Double.doubleToLongBits(initialFunds))
                                    .apply();
                        }
                    }
                }
                else // offline case, use saved/default values
                {
                    double spFunds = Double.longBitsToDouble(sp.getLong(
                            getString(R.string.firestore_fieldname_initialfunds), 0));
                    if (spFunds != 0) initialFunds = spFunds;
                    else initialFunds = DEFAULT_INITIAL_FUNDS;
                }
            }
        });
    }

    public void updateUserDataToServer()
    {
        String json = new Gson().toJson(userData);
        db.collection("users").document(currId).update(getString(R.string.firestore_fieldname_userdata), json);
    }

    public void startAlarm() {
        sp.edit().putBoolean(app.userData.getName()+"N", true).apply();
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
        c.set(Calendar.HOUR_OF_DAY, 9);
        c.set(Calendar.MINUTE, 30);
        c.set(Calendar.SECOND, 0);
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, StockMarketBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
        }
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    public void cancelAlarm() {
        app.sp.edit().putBoolean(app.userData.getName()+"N", false).apply();
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, StockMarketBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        alarmManager.cancel(pendingIntent);
    }
}
