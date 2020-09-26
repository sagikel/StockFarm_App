package com.example.stockfarm_app.ui.myFarm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.stockfarm_app.R;

public class MyFarmFragment extends Fragment {

    TextView textView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_farm, container, false);
        textView = view.findViewById(R.id.text_home);
        textView.setText("This is 'My Farm' fragment");
        return view;
    }
}
