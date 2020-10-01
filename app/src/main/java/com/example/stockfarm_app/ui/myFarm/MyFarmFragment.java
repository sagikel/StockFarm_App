package com.example.stockfarm_app.ui.myFarm;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;





import com.example.stockfarm_app.R;

public class MyFarmFragment extends Fragment {

    private SwipeRefreshLayout swipeRefreshLayout;
    private ViewPager2 farmPager;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_my_farm, container, false);
        swipeRefreshLayout = view.findViewById(R.id.swiperefresh);
        farmPager = view.findViewById(R.id.farm_pager);
        farmPager.setAdapter(new ViewPagerAdapter(getActivity()));
        farmPager.setCurrentItem(1);

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

    public class ViewPagerAdapter extends FragmentStateAdapter {

        private final Fragment[] mFragments = new Fragment[] {//Initialize fragments views
//Fragment views are initialized like any other fragment (Extending Fragment)
                new SignFragment(),//First fragment to be displayed within the pager tab number 1
                new CropFragment(),
        };
        public final String[] mFragmentNames = new String[] {//Tabs names array
                "First Tab",
                "SecondTab"
        };

        public ViewPagerAdapter(FragmentActivity fa){//Pager constructor receives Activity instance
            super(fa);
        }

        @Override
        public int getItemCount() {
            return mFragments.length;//Number of fragments displayed
        }

        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return mFragments[position];
        }
    }


}


