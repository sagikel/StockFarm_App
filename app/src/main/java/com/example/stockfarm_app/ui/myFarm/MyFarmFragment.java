package com.example.stockfarm_app.ui.myFarm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.stockfarm_app.R;

public class MyFarmFragment extends Fragment {

    private MyFarmViewModel myFarmViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myFarmViewModel =
                ViewModelProviders.of(this).get(MyFarmViewModel.class);
        View root = inflater.inflate(R.layout.fragment_my_farm, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        myFarmViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}
