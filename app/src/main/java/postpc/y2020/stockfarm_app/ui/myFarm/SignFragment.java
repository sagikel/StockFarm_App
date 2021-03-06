package postpc.y2020.stockfarm_app.ui.myFarm;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import postpc.y2020.stockfarm_app.LoginActivity;

import postpc.y2020.stockfarm_app.R;
import postpc.y2020.stockfarm_app.StockFarmApplication;
import postpc.y2020.stockfarm_app.data.UserStockData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

public class SignFragment extends Fragment
{
    FloatingActionButton floatingActionButtonH;
    FloatingActionButton floatingActionButtonS;
    ScrollView scrollView;
    TextView history;
    boolean historyBol;
    boolean settingBol;
    StockFarmApplication app;
    DateFormat df;
    TextView playerName;
    TextView money;
    TextView assets;
    TextView portfolioReturn;
    TextView Level;
    LinearLayout setting;
    Button logOut;
    Button delete;
    SwitchCompat switchCompat;
    LinearLayout linearLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final ViewGroup view = (ViewGroup) inflater.inflate(
                R.layout.sign_layout, container, false);
        floatingActionButtonH = view.findViewById(R.id.floatingActionButton2);
        floatingActionButtonS = view.findViewById(R.id.floatingActionButton3);
        scrollView = view.findViewById(R.id.scroll_view_history);
        history = view.findViewById(R.id.history);
        playerName = view.findViewById(R.id.player_name);
        money = view.findViewById(R.id.coin);
        assets = view.findViewById(R.id.assets);
        portfolioReturn = view.findViewById(R.id.return2);
        Level = view.findViewById(R.id.level);
        setting = view.findViewById(R.id.setting);
        logOut = view.findViewById(R.id.log_out);
        delete = view.findViewById(R.id.delete);
        switchCompat = view.findViewById(R.id.switch1);

        linearLayout = view.findViewById(R.id.backkk);

        app = (StockFarmApplication) getActivity().getApplication();
        playerName.setText(app.userData.getName());

        historyBol = false;
        settingBol = false;
        df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss ':' ");
        df.setTimeZone(TimeZone.getTimeZone("America/New_York"));

        boolean switchBool = app.sp.getBoolean(app.userData.getName()+"N", true);
        if (switchBool) {
            app.startAlarm();
        }
        switchCompat.setChecked(switchBool);
        getData();

        floatingActionButtonH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                historyData();
                if (settingBol) {
                    setting.setVisibility(View.INVISIBLE);
                    linearLayout.setVisibility(View.INVISIBLE);
                    settingBol = false;
                    return;
                }
                if (historyBol) {
                    scrollView.setVisibility(View.INVISIBLE);
                    linearLayout.setVisibility(View.INVISIBLE);
                    scrollView.fullScroll(scrollView.FOCUS_UP);
                    historyBol = false;
                } else {
                    linearLayout.setVisibility(View.VISIBLE);
                    scrollView.setVisibility(View.VISIBLE);
                    historyBol = true;
                }
            }
        });

        floatingActionButtonS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                historyData();
                if (historyBol) {
                    scrollView.setVisibility(View.INVISIBLE);
                    linearLayout.setVisibility(View.INVISIBLE);
                    scrollView.fullScroll(scrollView.FOCUS_UP);
                    historyBol = false;
                    return;
                }
                if (settingBol) {
                    setting.setVisibility(View.INVISIBLE);
                    linearLayout.setVisibility(View.INVISIBLE);
                    settingBol = false;
                } else {
                    linearLayout.setVisibility(View.VISIBLE);
                    setting.setVisibility(View.VISIBLE);
                    settingBol = true;
                }
            }
        });


        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout.setVisibility(View.INVISIBLE);
                setting.setVisibility(View.INVISIBLE);
                scrollView.setVisibility(View.INVISIBLE);
                scrollView.fullScroll(scrollView.FOCUS_UP);
                settingBol = false;
                historyBol = false;
            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Need to be empty
            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Log Out")
                        .setMessage("\nAre you sure?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                app.cancelAlarm();
                                app.updateUserDataToServer();
                                SignFragment.this.signOut();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(R.drawable.exit)
                        .show();
            }
        });


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Delete Account")
                        .setMessage("\nAre you sure?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                app.cancelAlarm();
                                app.db.collection("users").document(app.currId).delete();
                                SignFragment.this.signOut();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(R.drawable.delete_account)
                        .show();
            }
        });

        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    app.startAlarm();
                } else {
                    app.cancelAlarm();
                }
            }
        });

        return view;
    }

    public void signOut()
    {
        app.userData = null;
        app.sp.edit().putString(getString(R.string.last_user_id), "").apply();
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getActivity(), LoginActivity.class));
        getActivity().finish();
    }

    public void getData() {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.US);
        double funds = app.userData.getFunds();
        money.setText(formatter.format(funds));
        double sum = 0.0;
        for (UserStockData userStockData : app.userData.getStocks().values())
        {
            sum += userStockData.getCurrAmount()*userStockData.getLastPrice();
        }
        sum += funds;
        assets.setText(formatter.format(sum));
        double change = (sum/app.userData.getFundsFix()-1)*100;
        portfolioReturn.setText(String.format("%.2f", change) + "%");
        if (change > 0) {
            portfolioReturn.setTextColor(0xFF00802b);
        } else if (change < 0) {
            portfolioReturn.setTextColor(0xFF990000);
        } else {
            portfolioReturn.setTextColor(Color.GRAY);
        }
        level(change);
    }

    private void level(double change) {
        String level = "Dynamic Level: ";
        String rank = "Beginner";
        if (change < 1) {
            rank = "Beginner";
        } else if (change < 3) {
            rank = "Trainee";
        } else if (change < 5) {
            rank = "Amateur";
        } else if (change < 10) {
            rank = "Hotshot";
        } else if (change < 15) {
            rank = "Virtuoso";
        } else if (change < 20) {
            rank = "Expert";
        } else if (change < 25) {
            rank = "Veteran";
        } else if (change < 30) {
            rank = "Semi-Pro";
        } else if (change < 35) {
            rank = "Professional";
        } else if (change < 40) {
            rank = "Master";
        } else if (change < 45) {
            rank = "Champ";
        } else if (change < 50) {
            rank = "Superstar";
        } else if (change < 60) {
            rank = "Hero";
        } else if (change < 75) {
            rank = "Legend";
        } else if (change < 100) {
            rank = "Immortal";
        } else if (change >= 100) {
            rank = "God";
        }
        Level.setText(level + rank);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (historyBol){
            linearLayout.setVisibility(View.INVISIBLE);
            scrollView.setVisibility(View.INVISIBLE);
            scrollView.fullScroll(scrollView.FOCUS_UP);
            historyBol = false;
        }
        if (settingBol) {
            linearLayout.setVisibility(View.INVISIBLE);
            setting.setVisibility(View.INVISIBLE);
            settingBol = false;
        }
    }

    private void historyData() {
        StringBuilder toShow = new StringBuilder();
        NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.US);
        String color = "";
        for (UserStockData userStockData : app.userData.getStocks().values()) {
            toShow.append("<big><b>").append(userStockData.getStockName()).append("</b><br></big>");
            for (UserStockData.UserStockEvent userStockEvent : userStockData.getStockTradeHistory()) {
                long amount = userStockEvent.getTradeAmount();
                if (amount > 0){
                    color = "<font color='#00802b'>Buy: </font>";
                } else {
                    color = "<font color='#990000'>Sell: </font>";
                }
                toShow.append("<small>").append(df.format(userStockEvent.getDate())).append("</small><br>")
                        .append(color).append(Math.abs(userStockEvent.getTradeAmount()))
                        .append(" stocks at ")
                        .append(formatter.format(userStockEvent.getTradeValue()))
                        .append(" worth ")
                        .append(formatter.format(Math.abs(userStockEvent.getTradeValue()*userStockEvent.getTradeAmount()))).append(".<br>");
            }
            toShow.append("<small> ~End history~ </small><br><br>");
        }
        history.setText(Html.fromHtml(toShow.toString()));
    }
}
