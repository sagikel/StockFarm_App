package com.example.stockfarm_app.ui.myFarm;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.stockfarm_app.R;
import com.example.stockfarm_app.StockFarmApplication;
import com.example.stockfarm_app.data.UserStockData;
import java.util.LinkedList;

public class MyFarmFragment extends Fragment {
    private StockFarmApplication app;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ViewPager2 farmPager;
    private ImageView farmReel;
    int pageNum;


    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_my_farm, container, false);
        app = (StockFarmApplication) getActivity().getApplication();
        farmReel = view.findViewById(R.id.farm_reel);
        swipeRefreshLayout = view.findViewById(R.id.swiperefresh);
        farmPager = view.findViewById(R.id.farm_pager);
        LinkedList<UserStockData> activeStocks = app.userData.getActiveStocks();
        farmPager.setAdapter(new ViewPagerAdapter(getActivity(), activeStocks));
        farmPager.registerOnPageChangeCallback(new PagerAnimationCallback());

        pageNum = 3; // TODO update dynamically


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

    protected int getReelMovementStep()
    {
        int reelWidth = farmReel.getMeasuredWidth();
        int screenWidth = this.getResources().getDisplayMetrics().widthPixels;
        Log.d("TAG", "reel: " + String.valueOf(reelWidth) + ", screen: " + String.valueOf(screenWidth));
        return (int) (reelWidth - screenWidth) / (pageNum - 1);
    }

    // === Utility nested Classes: === //

    private class PagerAnimationCallback extends ViewPager2.OnPageChangeCallback
    {
        int prevPage;

        public PagerAnimationCallback()
        {
            super();
            prevPage = 0;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
        {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            if (positionOffset == 0.0 && prevPage != position)
            {
                int changeInPixels = (prevPage - position) * getReelMovementStep();
                TranslateAnimation animation = new TranslateAnimation(0, changeInPixels, 0, 0);
                animation.setDuration(200);
                animation.setFillAfter(false);
                animation.setAnimationListener(new MyAnimationListener(changeInPixels));
                farmReel.startAnimation(animation);
                prevPage = position;
            }
        }
    }

    public class ViewPagerAdapter extends FragmentStateAdapter {

        private LinkedList<Fragment> mFragments;
//        = new Fragment[] {//Initialize fragments views
////Fragment views are initialized like any other fragment (Extending Fragment)
//                new SignFragment(),//First fragment to be displayed within the pager tab number 1
//                new CropFragment(),
//                new CropFragment(),
//        };

        public ViewPagerAdapter(FragmentActivity fa, LinkedList<UserStockData> activeStocks){//Pager constructor receives Activity instance
            super(fa);
            mFragments.add(new SignFragment());
            for (UserStockData stock : activeStocks)
                {
                    mFragments.add(new CropFragment()); // TODO add stock data to fragment c'tor
                }
        }

        @Override
        public int getItemCount() {
            return mFragments.size();//Number of fragments displayed
        }

        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return mFragments.get(position);
        }
    }

    private class MyAnimationListener implements Animation.AnimationListener {
        int movement;

        public MyAnimationListener(int movement)
        {
            super();
            this.movement = movement;
        }

        @Override
        public void onAnimationEnd(Animation animation)
        {
            farmReel.clearAnimation();
            farmReel.setX(farmReel.getX() + movement);
        }

        @Override
        public void onAnimationStart(Animation animation) {}

        @Override
        public void onAnimationRepeat(Animation animation) {}


    }


}


