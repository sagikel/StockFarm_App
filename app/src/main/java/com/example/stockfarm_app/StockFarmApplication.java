package com.example.stockfarm_app;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.stockfarm_app.data.UserData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.*;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;


public class StockFarmApplication extends Application
{
    StockFarmApplication app;
    SharedPreferences sp;
    FirebaseFirestore db;
    public UserData userData;
    String currId;

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

    private void updateUserDataToServer(Activity activity)
    {
        String json = new Gson().toJson(userData);
        db.collection("users").document(currId).update(getString(R.string.firestore_fieldname_userdata), json);
    }





}
