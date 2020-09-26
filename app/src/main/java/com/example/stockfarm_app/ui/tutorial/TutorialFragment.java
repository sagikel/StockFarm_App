package com.example.stockfarm_app.ui.tutorial;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.stockfarm_app.R;

public class TutorialFragment extends Fragment {

    TextView textView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tutorial, container, false);
        textView = view.findViewById(R.id.text_tutorial);
        textView.setText("This is 'Tutorial' fragment");
        return view;
    }
}
