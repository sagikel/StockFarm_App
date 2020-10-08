package com.example.stockfarm_app.ui.myFarm;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.anychart.graphics.math.Coordinate;
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
                R.layout.trees_layout1, container, false);

        leftTopX = view.findViewById(R.id.lowLeftGuide).getX();
        leftTopY = view.findViewById(R.id.topGuide).getY();
        rightBottomX = view.findViewById(R.id.lowRightGuide).getX();
        rightBottomY = view.findViewById(R.id.bottomGuide).getY();
        rowSize = (rightBottomY - leftTopY) / 10;
        colSize = (rightBottomX - leftTopX) / 10;
        tree1 = view.findViewById(R.id.tree1);
        setTree(tree1, 9, 9);
        return view;
    }

    void setTree(LottieAnimationView treeAnimation, int row, int col)
    {
//        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
////        params.leftMargin = 40; //XCOORD
////        params.topMargin = 300; //YCOORD
//        tree1.setLayoutParams(params);
//            treeAnimation.setX(leftTopX + (row + 0.5f) * rowSize);
//        treeAnimation.setY(leftTopY + (col + 0.5f) * colSize);
    }
}
