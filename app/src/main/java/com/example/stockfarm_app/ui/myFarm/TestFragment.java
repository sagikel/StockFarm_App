package com.example.stockfarm_app.ui.myFarm;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.example.stockfarm_app.R;

public class TestFragment extends Fragment {
    float leftTopX;
    float leftTopY;
    float rightBottomX;
    float rightBottomY;
    float rowSize;
    float colSize;
    LottieAnimationView tree1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final ViewGroup view = (ViewGroup) inflater.inflate(
                R.layout.trees_layout_bad, container, false);

        leftTopX = view.findViewById(R.id.lowLeftGuide).getX();
        leftTopY = view.findViewById(R.id.topGuide).getY();
        rightBottomX = view.findViewById(R.id.lowRightGuide).getX();
        rightBottomY = view.findViewById(R.id.bottomGuide).getY();
        rowSize = (rightBottomY - leftTopY) / 10;
        colSize = (rightBottomX - leftTopX) / 10;
        return view;
    }

}
