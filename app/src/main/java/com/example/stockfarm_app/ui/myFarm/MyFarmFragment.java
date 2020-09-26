package com.example.stockfarm_app.ui.myFarm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.stockfarm_app.R;

public class MyFarmFragment extends Fragment {

    TextView textView;
    SwipeRefreshLayout swipeRefreshLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_my_farm, container, false);

        textView = view.findViewById(R.id.text_home);
        swipeRefreshLayout = view.findViewById(R.id.swiperefresh);

        textView.setText("This is 'My Farm' fragment");
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(getContext(), "Not connected yet..", Toast.LENGTH_LONG).show();

                // מתודה שמורידה מידע על המניות, מעדכנת, ואז קוראת למתודה swipeRefreshLayout.setRefreshing(false); בסופה

                swipeRefreshLayout.setRefreshing(false);
            }
        });
        return view;
    }
}
